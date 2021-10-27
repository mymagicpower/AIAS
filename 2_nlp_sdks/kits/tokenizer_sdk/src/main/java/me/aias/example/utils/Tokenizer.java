package me.aias.example.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * https://github.com/neonbjb/transformers-tokenizer-java
 */
public abstract class Tokenizer {
    static final String VOCAB_FILE_KEY = "vocab_file.json";
    static final String MERGES_FILE_KEY = "merges_file";
    static final String CONFIGS_FILE_KEY = "config.json";
    static final String CACHE_BASE = "tokenizer-cache";

    enum PaddingBehavior {
        ON_LEFT,
        ON_RIGHT
    }

    protected Map<String, File> resolvedVocabFiles = new HashMap<>();

    String cacheDir;
    JSONObject vocab;
    Map<String, Integer> encoder = new HashMap<>();
    Map<Integer, String> decoder = new HashMap<>();
    String modelName;
    PaddingBehavior paddingSide;
    int modelMaxSequenceLength;

    // Setting this to false causes the tokenizer to lowercase all strings fed through it.
    protected boolean isCased;
    // Setting this to true causes the tokenizer to append a space to the front of all strings fed through it.
    protected boolean requiresLeadingSpace;

    protected Optional<String> bosToken = Optional.empty();
    protected Optional<String> eosToken = Optional.empty();
    /**
     * Token to replace strings that are not found in the vocab dict with. If this is not specified by the subclass,
     * the tokenizer will fail with an exception when encoding tokens it cannot find.
     */
    protected Optional<String> unkToken = Optional.empty();
    protected Optional<String> sepToken = Optional.empty();
    protected Optional<String> padToken = Optional.empty();
    protected Optional<String> clsToken = Optional.empty();
    protected Optional<String> maskToken = Optional.empty();
    protected List<String> additionalSpecialTokens = new ArrayList<>();

    protected List<String> compiledSpecialTokenList = new ArrayList<>();

    protected Tokenizer(String modelName, PaddingBehavior paddingSide, boolean isCased, boolean requiresLeadingSpace) {
        this.modelName = modelName;
        this.paddingSide = paddingSide;
        this.isCased = isCased;
        this.requiresLeadingSpace = requiresLeadingSpace;
    }

    /**
     * This method is the true initializer for Tokenizer. Once called, Tokenizer is ready for tokenize(), encode()
     * and decode() calls. Calling this method "locks in" a lot of the configuration options available above (ex:
     * the token properties). If you change any class members after calling this method, you should call it again.
     */
    protected void loadRequiredFiles(String forModel) throws IOException, ParseException {
        Map<String, String> requiredFiles = pretrainedVocabFilesMap()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(forModel)));

        cacheDir = CACHE_BASE + "/" + modelName + "/" + forModel + "/";
        for(String file : requiredFiles.keySet()) {
            Path cachedPath = Paths.get(cacheDir + file);
            if(Files.notExists(cachedPath)) {
                URL url = new URL(requiredFiles.get(file));
                Logger.getGlobal().info("Downloading " + url.toString());
                InputStream in = ((HttpsURLConnection)url.openConnection()).getInputStream();

                // Make sure the cache exists. Create it if it does not.
                File cacheDirFil = new File(cacheDir);
                if(!cacheDirFil.exists()) {
                    cacheDirFil.mkdirs();
                }

                Files.copy(in, Paths.get(cacheDir + "/" + file), StandardCopyOption.REPLACE_EXISTING);
            }
            resolvedVocabFiles.put(file, cachedPath.toFile());
        }

        vocab = (JSONObject)new JSONParser().parse(new FileReader(resolvedVocabFiles.get(VOCAB_FILE_KEY)));
        // Create encoder and decoder from vocab file.
        for(Object o : vocab.keySet()) {
            String k = o.toString();
            int v = Integer.parseInt(vocab.get(o).toString());
            encoder.put(k, v);
            decoder.put(v, k);
        }

        // Process the config file.
        JSONObject config = (JSONObject)new JSONParser().parse(new FileReader(resolvedVocabFiles.get(CONFIGS_FILE_KEY)));
        modelMaxSequenceLength = Integer.parseInt(config.get("n_positions").toString());
        Logger.getGlobal().info("Done loading files.");

        // "Compile" all of the tokens into a single list.
        if(eosToken.isPresent()) compiledSpecialTokenList.add(eosToken.get());
        else throw new RuntimeException("EOS token is required!");
        if(bosToken.isPresent()) compiledSpecialTokenList.add(bosToken.get());
        else throw new RuntimeException("BOS token is required!");
        if(unkToken.isPresent()) compiledSpecialTokenList.add(unkToken.get());
        if(padToken.isPresent()) compiledSpecialTokenList.add(padToken.get());
        else throw new RuntimeException("Pad token is required!");
        if(sepToken.isPresent()) compiledSpecialTokenList.add(sepToken.get());
        if(clsToken.isPresent()) compiledSpecialTokenList.add(clsToken.get());
        if(maskToken.isPresent()) compiledSpecialTokenList.add(maskToken.get());
        additionalSpecialTokens.stream().forEach(t -> compiledSpecialTokenList.add(t));

        // Add special tokens into the vocab where necessary.
        int nextEncoderToken = encoder.values()
                .stream()
                .reduce(Integer.MIN_VALUE, (max, next) -> Math.max(max, next)) + 1;
        for(String specialToken : additionalSpecialTokens) {
            if(!encoder.containsKey(specialToken)) {
                encoder.put(specialToken, nextEncoderToken);
                decoder.put(nextEncoderToken, specialToken);
                nextEncoderToken++;
            }
        }
    }

    static class TokenOrString {
        Optional<String> str = Optional.empty();
        Optional<String> token = Optional.empty();
        public TokenOrString() {
        }

        public static TokenOrString string(String s){ TokenOrString t = new TokenOrString(); t.str = Optional.of(s); return t; }
        public static TokenOrString token(String s){ TokenOrString t = new TokenOrString(); t.token = Optional.of(s); return t; }
    }

    /**
     * Converts the given string into a list of string tokens which correspond to values which can be encoded directly
     * encoded into integers and then fed into the corresponding model. Most clients will probably want to use encode.
     */
    public ImmutableList<String> tokenize(String text) {
        if(text.length() == 0) return ImmutableList.of();

        if(!isCased) {
            text = text.toLowerCase();
        }

        text = text.strip();
        if(requiresLeadingSpace) {
            text = " " + text;
        }

        // Now split the text up into strings of text (which will be tokenized by the subclass) and processed special
        //  tokens.
        LinkedList<TokenOrString> splitList = new LinkedList<>();
        splitList.add(TokenOrString.string(text));
        for(String specialToken : compiledSpecialTokenList) {
            ListIterator<TokenOrString> iter = splitList.listIterator();
            while(iter.hasNext()) {
                TokenOrString tokenOrString = iter.next();
                if(!tokenOrString.str.isPresent()) continue; // We don't really care about tokens.
                String str = tokenOrString.str.get();
                if (str.contains(specialToken)) {
                    iter.remove(); // Remove the entry we most recently removed - we're going to split it up and re-add it.
                    Iterator<String> splitsIter = Splitter.on(specialToken).split(str).iterator(); // Splitter is a must-use in this case. String.split() has some weird behavior on empty strings which will show up a ton for tokenization.
                    boolean isFirst = true; // Needed because we don't want to strip the leading whitespace on the input text, but we do want to strip everything else.
                    while(splitsIter.hasNext()) {
                        String split = splitsIter.next().stripTrailing();
                        while(split.startsWith("  ")) split = split.substring(1);

                        if(!split.isEmpty()) {
                            iter.add(TokenOrString.string(split.stripTrailing()));
                        }
                        if(splitsIter.hasNext()) {
                            iter.add(TokenOrString.token(specialToken));
                        }
                    }
                }
            }
        }

        // Individually process the un-tokenized strings and form a complete list of tokens to feed into the string->int mapper.
        List<String> finalList = new LinkedList<String>();
        for(TokenOrString tokenOrString : splitList) {
            if(tokenOrString.token.isPresent()) {
                finalList.add(tokenOrString.token.get());
            } else {
                finalList.addAll(tokenizeDerived(tokenOrString.str.get()));
            }
        }

        return ImmutableList.copyOf(finalList);
    }

    public static class EncoderResult {
        /** The raw encoded tokens, padded on the specified side. */
        public ImmutableList<Integer> inputIds;
        /** Set to 1 wherever at each non-pad token in {@code inputIds}. */
        public ImmutableList<Integer> attentionMask;
        /** Set to 1 wherever a "special" token was inserted. */
        public ImmutableList<Integer> specialTokensMask;
        /** The number of tokens that were removed from the input text if it was too large to fit them all. */
        public int truncatedTokensCount;

        public EncoderResult(ImmutableList<Integer> inputIds, ImmutableList<Integer> attentionMask, ImmutableList<Integer> specialTokensMask, int truncatedTokensCount) {
            this.inputIds = inputIds;
            this.attentionMask = attentionMask;
            this.specialTokensMask = specialTokensMask;
            this.truncatedTokensCount = truncatedTokensCount;
        }
    }

    /**
     * Converts the given {@code text} into several lists of integers, which can then be fed into the model.
     *
     * Notes:
     * - This method behaves like encode_plus from transformers.
     * @todo - Enable appending multiple texts together, along with a truncation strategy for doing so.
     *
     * @param maxSeqLen Returned lists will be exactly this length (unlike the transformers lib, where this is just an upper bound.)
     *                  Note that this tokenizer will respect the max sequence length specified by the model configuration, so this
     *                  value cannot be bigger than that.
     */
    public EncoderResult encode(String text, int maxSeqLen) {
        ImmutableList<String> tokens = tokenize(text);

        maxSeqLen = Integer.min(maxSeqLen, modelMaxSequenceLength);
        ImmutableList.Builder<Integer> inputIdsBuilder = ImmutableList.builder();
        ImmutableList.Builder<Integer> attentionMaskBuilder = ImmutableList.builder();
        ImmutableList.Builder<Integer> specialTokensBuilder = ImmutableList.builder();
        int truncated = 0;
        if(tokens.size() < maxSeqLen && paddingSide == PaddingBehavior.ON_LEFT) {
            int pad = encoder.get(padToken.get());
            for (int i = 0; i < maxSeqLen - tokens.size(); i++) {
                inputIdsBuilder.add(pad);
                attentionMaskBuilder.add(0);
                specialTokensBuilder.add(1);
            }
        }
        int i = 0;
        for(String tok : tokens) {
            if(i >= maxSeqLen) {
                truncated = tokens.size() - maxSeqLen;
                break;
            }

            // Replace un-mappable values with UNK.
            if(!encoder.containsKey(tok)) {
                tok = unkToken.get();
            }

            inputIdsBuilder.add(encoder.get(tok));
            attentionMaskBuilder.add(1);
            specialTokensBuilder.add(compiledSpecialTokenList.contains(tok) ? 1 : 0);
            i++;
        }
        if(tokens.size() < maxSeqLen && paddingSide == PaddingBehavior.ON_RIGHT) {
            int pad = encoder.get(padToken.get());
            for (int j = 0; j < maxSeqLen - tokens.size(); j++) {
                inputIdsBuilder.add(pad);
                attentionMaskBuilder.add(0);
                specialTokensBuilder.add(1);
            }
        }

        return new EncoderResult(inputIdsBuilder.build(), attentionMaskBuilder.build(), specialTokensBuilder.build(), truncated);
    }

    /**
     * Should return a map of files which should be downloaded for the tokenizer. The map should follow this format:
     * file_name => { pretrained_tokenizer_name => url }
     */
    protected abstract Map<String, Map<String, String>> pretrainedVocabFilesMap();

    /**
     * This is used to pre-process the given text, should that be needed. For example, if the implemented tokenizer
     * breaks sentences up into simple words, this method would basically return a list of text.split(" ").
     *
     * Note that this should be a reversible operation by simply flatMapping the return value back into a string.
     */
    protected abstract ImmutableList<String> tokenizeDerived(String text);
}