package org.noses.wordl;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

public class DictionaryTest {

    private ArrayList<String> getTestDictionary() {
        ArrayList<String> words = new ArrayList<>();

        words.add("audit");
        words.add("sewer");
        words.add("tense");
        words.add("found");

        return words;
    }

    @Test
    public void testLoading() {
        String dict = "audit\n"+
                "sewer\n"+
                "tense\n"+
                "found";

        BufferedReader in = new BufferedReader(new StringReader(dict));

        Dictionary dictionary = new Dictionary();
        dictionary.loadWords(in);

        Assert.notEmpty(dictionary.getWords(5), "dictionary has words");

        for (String word: dictionary.getWords(5)) {
            Assert.isTrue(word.length()==5, word+" isn't 5 letters long");
        }

        Assert.hasLength(dictionary.getRandomWord(5), "Random word should have length");
        Assert.isNull(dictionary.getRandomWord(500), "Random word should not have length");
    }
}
