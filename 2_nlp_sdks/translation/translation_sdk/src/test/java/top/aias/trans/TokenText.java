package top.aias.trans;

import ai.djl.util.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.aias.trans.tokenizer.SpTokenizer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class TokenText {

    private static final Logger logger = LoggerFactory.getLogger(TokenText.class);

    private TokenText() {
    }

    public static void main(String[] args) throws IOException {

        String input = "My name is Wolfgang and I live in Berlin";
        Path sourceTokenizerPath = Paths.get("models/source.spm");

        try (SpTokenizer sourceTokenizer = new SpTokenizer(sourceTokenizerPath)) {
            List<String> tokens = sourceTokenizer.tokenize(input);
            String[] strs = tokens.toArray(new String[]{});
            logger.info("Tokens: " + Arrays.toString(strs));

            List<String> words = Utils.readLines(Paths.get("models/vocab.txt"));
            String jsonStr ="";
            for (String line:words) {
                jsonStr = jsonStr + line;
            }

            ConcurrentHashMap<String, Long> map = new Gson().fromJson(jsonStr, new TypeToken<ConcurrentHashMap<String, Long>>() {
            }.getType());

            Map<Long, String> reverseMap = new HashMap<>();
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Long> next = (Map.Entry<String, Long>) it.next();
                reverseMap.put(next.getValue(),next.getKey());
            }


            int[] sourceIds = new int[tokens.size()+1];
            sourceIds[tokens.size()] = 0;
            for (int i=0;i<tokens.size();i++) {
                sourceIds[i] = map.get(tokens.get(i)).intValue();
            }


             int[] generated_ids = new int[]{65000, 14209, 25600,  3085, 26055,   105,  9315, 18332,     0};

            StringBuffer sb = new StringBuffer();
            for (int i=0;i<generated_ids.length;i++) {
                String text = reverseMap.get(new Long(generated_ids[i]));
                sb.append(text);
            }
            logger.info("result: " + sb);
        }
    }
}