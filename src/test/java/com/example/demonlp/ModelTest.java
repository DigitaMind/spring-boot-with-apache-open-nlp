package com.example.demonlp;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ModelTest {

    @Test
    public void givenEnglishModel_whenDetect_thenSentencesAreDetected()
            throws Exception {

        String paragraph = "This is a statement. This is another statement.";

        InputStream is = getClass().getResourceAsStream("/models/en-sent.bin");
//        InputStream is = new FileInputStream("/models/en-sent.bin");
        SentenceModel model = new SentenceModel(is);

        SentenceDetectorME sdetector = new SentenceDetectorME(model);

        String [] sentences = sdetector.sentDetect(paragraph);
        assertThat(sentences).contains("This is a statement.");
    }

    @Test
    public void givenEnglishModel_whenTokenize_thenTokensAreDetected()
            throws Exception {

        InputStream inputStream = getClass().getResourceAsStream("/models/en-token.bin");
        TokenizerModel model = new TokenizerModel(inputStream);
        TokenizerME tokenizer = new TokenizerME(model);
        String[] tokens = tokenizer.tokenize("This sentence will be tokenized.");
        for(String x: tokens){
            System.out.println(x);
        }
        assertThat(tokens).contains(
                "This", "sentence", "will", "be", "tokenized", ".");
    }

    @Test
    public void givenWhitespaceTokenizer_whenTokenize_thenTokensAreDetected()
            throws Exception {

        WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize("White space tokenize.");

        assertThat(tokens)
                .contains("White", "space", "tokenize.");
    }

    @Test
    public void givenSimpleTokenizer_whenTokenize_thenTokensAreDetected()
            throws Exception {

        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer
                .tokenize("WhitespaceTokenizer and splits the sentence into words, numbers, and punctuation marks.");
        for(String x: tokens){
            System.out.println(x);
        }
        assertThat(tokens)
                .contains("WhitespaceTokenizer", "and", "splits", "the", "sentence", "into", "words", ",", "numbers", ",", "and", "punctuation", "marks",  ".");
    }

    @Test
    public void
    givenEnglishPersonModel_whenNER_thenPersonsAreDetected()
            throws Exception {

        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer
                .tokenize("John is 26 years old. His best friend's "
                        + "name is Lekan. He has a sister named Ope.");

        InputStream inputStreamNameFinder = getClass()
                .getResourceAsStream("/models/en-ner-person.bin");
        TokenNameFinderModel model = new TokenNameFinderModel(
                inputStreamNameFinder);
        NameFinderME nameFinderME = new NameFinderME(model);
        List<Span> spans = Arrays.asList(nameFinderME.find(tokens));

        assertThat(spans.toString())
                .isEqualTo("[[0..1) person, [13..14) person, [20..21) person]");
    }

}
