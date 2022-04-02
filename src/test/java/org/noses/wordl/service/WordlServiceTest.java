package org.noses.wordl.service;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.KnownStatus;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class WordlServiceTest {

    @Test
    public void testAllLettersExist() {
        WordlService wordlService = new WordlService(null, null);

        List<Character> letters = new ArrayList<>();
        letters.add('a');
        letters.add('t');
        letters.add('n');

        Assert.isTrue(wordlService.containsAllLetters("narts", letters), "Word should contain all letters");
        Assert.isTrue(!wordlService.containsAllLetters("tyuiu", letters), "Word should not contain all letters");
    }

    @Test
    public void testGetAllPossibleWords() {
        Dictionary dictionary = mock(Dictionary.class);
        ArrayList<String> words = new ArrayList<>();
        words.add("aunts");
        words.add("antic");
        words.add("pants");
        words.add("altar");
        when(dictionary.getWords(anyInt())).thenReturn(words);

        KnownStatus knownStatus = new KnownStatus();
        knownStatus.addCorrect(0, 'a');
        knownStatus.addWrongPlace(2, 'n');
        //knownStatus.addWrongPlace(4, 't');

        WordlService wordlService = new WordlService(dictionary, null);
        List<String> possibleWords = wordlService.getAllPossibleWords(knownStatus);

        Assert.notNull(possibleWords, "Got null possible words");
        Assert.isTrue(possibleWords.size()==2, "Should get 2 possible words, got "+possibleWords.size());
    }

    @Test
    public void testLikelihoodSorting() {

        List<String> words = new ArrayList<>();
        words.add("aunts");
        words.add("pants");

        WordFrequency wordFrequency = mock(WordFrequency.class);
        when (wordFrequency.getWordFrequency("aunts")).thenReturn(100l);
        when (wordFrequency.getWordFrequency("pants")).thenReturn(1000l);

        WordlService wordlService = new WordlService(null, wordFrequency);

        Assert.isTrue(wordlService.getSortedByBestGuess(words).get(0).equals ("pants"), "Pants should be most frequent");
    }
}
