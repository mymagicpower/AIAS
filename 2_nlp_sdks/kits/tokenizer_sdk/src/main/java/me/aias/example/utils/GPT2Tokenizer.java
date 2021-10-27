package me.aias.example.utils;

import com.google.common.collect.ImmutableList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.io.Resources;

/**
 * https://github.com/neonbjb/transformers-tokenizer-java
 */
public class GPT2Tokenizer extends Tokenizer {
    Map<Integer, String> byte_encoder = new HashMap<>();
    Map<String, Integer> byte_decoder = new HashMap<>();
    ImmutableList<String[]> mergables;
    Map<String, ImmutableList<String>> bpe_cache = new HashMap<>();

    static final Pattern REGEX_PATTERN =
            Pattern.compile("'s|'t|'re|'ve|'m|'ll|'d| ?\\p{L}+| ?\\p{N}+| ?[^\\s\\p{L}\\p{N}]+|\\s+(?!\\S)|\\s+");

    private GPT2Tokenizer(String modelName) {
        super(modelName, PaddingBehavior.ON_RIGHT, true, true);

        // unknown tokens should be impossible with this tokenizer - fail hard and fast if they are encountered by not specifying the unk token.
        bosToken = Optional.of("<|endoftext|>");
        eosToken = Optional.of("<|endoftext|>");
        padToken = Optional.of("<|endoftext|>");
    }

    public static GPT2Tokenizer fromPretrained(String modelName) throws IOException, org.json.simple.parser.ParseException {
        GPT2Tokenizer result = new GPT2Tokenizer(modelName);
        result.loadRequiredFiles(modelName);

        // The byte encoder is loaded from a JSON file saved as a resource.
        // This JSON file was created directly from Python via the transformers library's tokenization_gpt2.bytes_to_unicode().
        JSONObject encObj = (JSONObject)new JSONParser().parse(Resources.toString(Resources.getResource("gpt_2_byte_encoder.json"), Charset.defaultCharset()));
        for(Object key : encObj.keySet()) {
            int k = Integer.parseInt(key.toString());
            String v = encObj.get(key).toString();
            result.byte_encoder.put(k, v);
            result.byte_decoder.put(v, k);
        }

        // Process the merges file.
        ImmutableList.Builder<String[]> mergablesBuilder = ImmutableList.builder();
        BufferedReader reader = new BufferedReader(new FileReader(result.resolvedVocabFiles.get(MERGES_FILE_KEY)));
        String line = reader.readLine(); // Intentionally skip the first line.
        while((line = reader.readLine()) != null) {
            mergablesBuilder.add(line.split(" "));
        }
        result.mergables = mergablesBuilder.build();

        return result;
    }

    ImmutableList<String> bpe(String word) {
        if(bpe_cache.containsKey(word)) {
            return bpe_cache.get(word);
        }

        LinkedList<String> tokens = word
                .chars()
                .mapToObj(c -> Character.toString((char)c))
                .collect(Collectors.toCollection(LinkedList::new));
        // Use the mergables list, from top to bottom, to merge the tokens list together to get maximal-sized tokens.
        for(String[] mergable : mergables) {
            ListIterator<String> i = tokens.listIterator();
            while(i.hasNext()) {
                String first = i.next();
                if(!i.hasNext()) {
                    // nothing to join here.
                    break;
                }
                if(mergable[0].equals(first)) {
                    String second = i.next();
                    if(mergable[1].equals(second)) {
                        // We want to remove the last two tokens, then replace them with a new token (those two combined).
                        // This is a bit awkward in Java due to the state of list iterators.
                        i.remove();
                        i.previous();
                        i.next();
                        i.remove();
                        i.add(mergable[0] + mergable[1]);
                    } else {
                        i.previous();
                    }
                }
            }
        }

        ImmutableList<String> result = ImmutableList.copyOf(tokens);
        bpe_cache.put(word, result);
        return result;
    }

    @Override
    protected ImmutableList<String> tokenizeDerived(String text) {
        ImmutableList.Builder<String> resBuilder = ImmutableList.builder();
        Matcher matcher = REGEX_PATTERN.matcher(text);
        while(matcher.find()) {
            ByteBuffer tokenUtf8 = StandardCharsets.UTF_8.encode(matcher.group());
            StringBuilder byteEncodedToken = new StringBuilder();
            for(byte b : tokenUtf8.array()) {
                if(b == 0) continue; // Weird java bug, not really sure where this comes from..
                byteEncodedToken.append(byte_encoder.get((int)b));
            }
            resBuilder.addAll(bpe(byteEncodedToken.toString()));
        }

        return resBuilder.build();
    }

    public static void main(String[] args) throws Exception {
        FileUtil.initSsl();
        fromPretrained("gpt2").tokenize("Not his <|endoftext|> best <|endoftext|> album. <|endoftext|>  Some songs are just average.  If you are a completist like me, then purchase it and burn the best songs onto your own disc.");
    }

    @Override
    protected Map<String, Map<String, String>> pretrainedVocabFilesMap() {
        return Map.of(
                VOCAB_FILE_KEY,
                Map.of(
                    "gpt2", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-vocab.json",
                    "gpt2-medium", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-medium-vocab.json",
                    "gpt2-large", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-large-vocab.json",
                    "gpt2-xl","https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-xl-vocab.json",
                    "distilgpt2", "https://s3.amazonaws.com/models.huggingface.co/bert/distilgpt2-vocab.json"
                ),
                MERGES_FILE_KEY,
                Map.of(
                    "gpt2", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-merges.txt",
                    "gpt2-medium", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-medium-merges.txt",
                    "gpt2-large", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-large-merges.txt",
                    "gpt2-xl", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-xl-merges.txt",
                    "distilgpt2", "https://s3.amazonaws.com/models.huggingface.co/bert/distilgpt2-merges.txt"
                ),
                CONFIGS_FILE_KEY,
                Map.of(
                    "gpt2", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-config.json",
                    "gpt2-medium", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-medium-config.json",
                    "gpt2-large", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-large-config.json",
                    "gpt2-xl", "https://s3.amazonaws.com/models.huggingface.co/bert/gpt2-xl-config.json",
                    "distilgpt2", "https://s3.amazonaws.com/models.huggingface.co/bert/distilgpt2-config.json"
                )
        );
    }
}