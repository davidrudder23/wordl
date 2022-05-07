package org.noses.wordl.solvers;

import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.LetterResult;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseSolverTest {

    public Dictionary getDictionary() {
        Dictionary dictionary = mock(Dictionary.class);
        ArrayList<String> words = new ArrayList<>();
        words.add("aunts");
        words.add("antic");
        words.add("pants");
        words.add("altar");
        words.add("antsy");
        words.add("wrest");
        words.add("wisps");
        words.add("board");
        words.add("cater");
        words.add("crate");
        words.add("ether");
        words.add("entry");
        words.add("forts");
        when(dictionary.getWords(anyInt())).thenReturn(words);

        return dictionary;
    }

    public WordFrequency getWordFrequency() {
        WordFrequency wordFrequency = mock(WordFrequency.class);
        when (wordFrequency.getWordFrequency("aunts")).thenReturn(100l);
        when (wordFrequency.getWordFrequency("pants")).thenReturn(1000l);
        return wordFrequency;
    }

    public GuessResults getGuessResults() {
        GuessResults guessResults = new GuessResults();
        LetterResult[] letterResults = new LetterResult[5];
        letterResults[0] = new LetterResult();
        letterResults[0].setLetter('a');
        letterResults[0].setResult("orange");

        letterResults[1] = new LetterResult();
        letterResults[1].setLetter('t');
        letterResults[1].setResult("black");

        letterResults[2] = new LetterResult();
        letterResults[2].setLetter('i');
        letterResults[2].setResult("green");

        letterResults[3] = new LetterResult();
        letterResults[3].setLetter('q');
        letterResults[3].setResult("black");

        letterResults[4] = new LetterResult();
        letterResults[4].setLetter('q');
        letterResults[4].setResult("black");

        guessResults.setLetterResults(letterResults);

        return guessResults;
    }
}
