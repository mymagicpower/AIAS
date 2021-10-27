package me.aias.example;

import ai.djl.ModelException;
import ai.djl.modality.nlp.SimpleVocabulary;
import ai.djl.modality.nlp.bert.BertFullTokenizer;
import ai.djl.modality.nlp.bert.BertToken;
import ai.djl.modality.nlp.bert.BertTokenizer;
import ai.djl.modality.nlp.bert.WordpieceTokenizer;
import ai.djl.modality.nlp.preprocess.SimpleTokenizer;
import me.aias.example.utils.ClipBPETokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Java NLP Tokenizer (for Transformers)
 *
 * @author calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
public class TokenizerExample {

    private static final Logger logger = LoggerFactory.getLogger(TokenizerExample.class);

    @Test
    public void testSimpleTokenizer() {
        logger.info("testSimpleTokenizer:");
        String input = "This tokenizer generates tokens for input sentence";
        String delimiter = " ";
        SimpleTokenizer tokenizer = new SimpleTokenizer(delimiter);
        List<String> tokens = tokenizer.tokenize(input);
        logger.info("Tokens: {}", tokens);

    }

    @Test
    public void testBertTokenizer() {
        logger.info("testBertTokenizer:");
        String question = "When did Radio International start broadcasting?";
        String paragraph = "Radio International was a general entertainment Channel. Which operated between December 1983 and April 2001";

        BertTokenizer tokenizer = new BertTokenizer();
        BertToken bertToken = tokenizer.encode(question, paragraph);
        logger.info("Tokens: {}", bertToken.getTokens());
        logger.info("TokenTypes: {}", bertToken.getTokenTypes());
        logger.info("AttentionMask: {}", bertToken.getAttentionMask());
    }

    @Test
    public void testWordpieceTokenizer() throws IOException {
        logger.info("testWordpieceTokenizer:");
        //replace vocab.txt with your vocabulary
        Path filePath = Paths.get("src/test/resources/vocab.txt");
        SimpleVocabulary vocabulary =
                SimpleVocabulary.builder()
                        .optMinFrequency(1)
                        .addFromTextFile(filePath)
                        .optUnknownToken("[UNK]")
                        .build();
        WordpieceTokenizer wordpieceTokenizer = new WordpieceTokenizer(vocabulary, "[UNK]", 200);
        String input = "This tokenizer generates tokens for input sentence";
        List<String> tokens = wordpieceTokenizer.tokenize(input);
        logger.info("Tokens: {}", tokens);
    }
    
    @Test
    public void testBertFullTokenizer() throws IOException {
        logger.info("testBertFullTokenizer:");
        //replace vocab.txt with your vocabulary
        Path filePath = Paths.get("src/test/resources/vocab.txt");
        SimpleVocabulary vocabulary =
                SimpleVocabulary.builder()
                        .optMinFrequency(1)
                        .addFromTextFile(filePath)
                        .optUnknownToken("[UNK]")
                        .build();
        //subword tokenizer
        BertFullTokenizer tokenizer = new BertFullTokenizer(vocabulary, true);

        String input = "This tokenizer generates tokens for input sentence";
        List<String> tokens = tokenizer.tokenize(input);
        logger.info("Tokens: {}", tokens);

    }

    @Test
    public void testClipBPETokenizer() throws IOException, ModelException {
        logger.info("testClipBPETokenizer:");
        ClipBPETokenizer tokenizer = new ClipBPETokenizer("src/test/resources/bpe_simple_vocab_16e6.txt");
        List<Integer> tokens = tokenizer.encode("we will test a diagram");
        logger.info("Tokens: {}", tokens);
    }

    @Test
    public void testGPT2Tokenizer() throws IOException, ModelException {
        logger.info("testGPT2Tokenizer:");
        ClipBPETokenizer tokenizer = new ClipBPETokenizer("src/test/resources/bpe_simple_vocab_16e6.txt");
        List<Integer> tokens = tokenizer.encode("we will test a diagram");
        logger.info("Tokens: {}", tokens);
    }
}