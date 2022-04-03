package org.noses.wordl.solvers;

import org.junit.jupiter.api.Test;
import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.KnownStatus;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DaveSolverTest {
    @Test
    public void testAllLettersExist() {
        DaveSolver daveSolver = new DaveSolver(null, null);

        List<Character> letters = new ArrayList<>();
        letters.add('a');
        letters.add('t');
        letters.add('n');

        Assert.isTrue(daveSolver.containsAllLetters("narts", letters), "Word should contain all letters");
        Assert.isTrue(!daveSolver.containsAllLetters("tyuiu", letters), "Word should not contain all letters");
    }


    @Test
    public void testGetAllPossibleWords() {
        Dictionary dictionary = mock(Dictionary.class);
        ArrayList<String> words = new ArrayList<>();
        words.add("aunts");
        words.add("antic");
        words.add("pants");
        words.add("altar");
        words.add("antsy");
        when(dictionary.getWords(anyInt())).thenReturn(words);

        KnownStatus knownStatus = new KnownStatus();
        knownStatus.addCorrect(0, 'a');
        knownStatus.addWrongPlace(2, 'n');
        //knownStatus.addWrongPlace(4, 't');

        DaveSolver daveSolver = new DaveSolver(dictionary, null);
        List<String> possibleWords = daveSolver.getAllPossibleWords(knownStatus);

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

        DaveSolver daveSolver = new DaveSolver(null, wordFrequency);

        Assert.isTrue(daveSolver.getSortedByMostFrequent(words).get(0).equals ("pants"), "Pants should be most frequent");
    }
}
