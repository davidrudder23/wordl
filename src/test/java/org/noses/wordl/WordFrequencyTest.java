package org.noses.wordl;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import static org.springframework.util.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordFrequencyTest {

    @Test
    public void testLoading() {
        WordFrequency wordFrequency = new WordFrequency();

        String csv = "raise,100\n" +
                "audit,101\n" +
                "prize,200\n" +
                "slick,102";

        wordFrequency.loadWords(new BufferedReader(new StringReader(csv)));

        assert (wordFrequency.getWordFrequency("raise") == 100);

    }

    @Test
    public void testGetTopWords() {
        Dictionary dictionary = mock(Dictionary.class);

        List<String> dictionaryWords = new ArrayList<>();
        dictionaryWords.add("abode");
        dictionaryWords.add("abort");
        dictionaryWords.add("about");
        dictionaryWords.add("above");
        dictionaryWords.add("abuse");
        dictionaryWords.add("abuts");
        dictionaryWords.add("gifts");
        dictionaryWords.add("gilds");
        dictionaryWords.add("gills");
        dictionaryWords.add("gilts");
        dictionaryWords.add("gimme");
        dictionaryWords.add("gimpy");

        WordFrequency wordFrequency = new WordFrequency();
        wordFrequency.table = new HashMap<>();

        int count = 0;
        for (String word: dictionaryWords) {
            wordFrequency.table.put(word, (long)(1000-count++));
        }

        when(dictionary.getWords(anyInt())).thenReturn(dictionaryWords);

        List<String> top = wordFrequency.getMostFrequent(dictionary, 5, 100);
        notEmpty(top, "word list shouldn't be empty");
        isTrue(top.get(0).equalsIgnoreCase(dictionaryWords.get(0)));
    }

}
