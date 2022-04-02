package org.noses.wordl;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

public class WordFrequencyTest {

    @Test
    public void testLoading() {
        WordFrequency wordFrequency = new WordFrequency();

        String csv = "raise,100\n"+
                "audit,101\n"+
                "prize,200\n"+
                "slick,102";

        wordFrequency.loadWords(new BufferedReader(new StringReader(csv)));

        assert(wordFrequency.getWordFrequency("raise")==100);

    }

}
