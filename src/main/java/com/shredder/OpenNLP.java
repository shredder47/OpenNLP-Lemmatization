package com.shredder;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class OpenNLP {

    private POSTaggerME posTagger;
    private DictionaryLemmatizer lemmatizer;
    private POSModel posModel;
    private WhitespaceTokenizer whitespaceTokenizer;

    public OpenNLP() {
        try (
                InputStream inputStreamPOSTagger = OpenNLP.class.getResourceAsStream("/en-pos-maxent.bin");
                InputStream inputStreamDictLemmatizer = OpenNLP.class.getResourceAsStream("/en-lemmatizer.dict")
        ) {

            lemmatizer = new DictionaryLemmatizer(inputStreamDictLemmatizer);
            posModel = new POSModel(inputStreamPOSTagger);
            posTagger = new POSTaggerME(posModel);
            whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function tokenize sentence using whitespace and the creates a map containing key as lowercase'd original-word and value as its lemmatized word
     *
     * @param sentence - The sentence
     * @return Map <LowercaseOriginalWord,LemmatizedWord>
     */
    public Map<String, String> getLemmatizedMap(String sentence) {

        Map<String, String> wordToLemmaMap = new LinkedHashMap<>();

        try {
            //OpenNLP finds more matches if there terms are in lowercase
            String[] tokens = Arrays.stream(whitespaceTokenizer.tokenize(sentence)).map(String::toLowerCase).toArray(String[]::new);
            String[] tags = posTagger.tag(tokens);
            String[] lemmas = lemmatizer.lemmatize(tokens, tags);

            for (int i = 0; i < tokens.length; i++) {

                String lowerCasedWord = tokens[i];
                String lemmatizedWord = lemmas[i];

                if (lemmatizedWord.equals("O")) //When it cant lemmatize or recognize a word it returns 'O'
                    wordToLemmaMap.put(lowerCasedWord, lowerCasedWord);
                else
                    wordToLemmaMap.put(lowerCasedWord, lemmatizedWord);
            }

        } catch (Exception e) {
            return wordToLemmaMap;
        }
        return wordToLemmaMap;
    }


}
