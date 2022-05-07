package org.noses.wordl.solvers;

import org.junit.jupiter.api.Test;
import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.KnownStatus;
import org.springframework.util.Assert;

import java.util.List;

public class LetterFrequencySolverTest extends BaseSolverTest {

    @Test
    public void testSolver() {
        Dictionary dictionary = getDictionary();
        WordFrequency wordFrequency = getWordFrequency();

        KnownStatus knownStatus = new KnownStatus();
        knownStatus.addCorrect(0, 'a');
        knownStatus.addWrongPlace(2, 'n');

        GuessResults guessResults = getGuessResults();

        LetterFrequencySolver letterFrequencySolver = new LetterFrequencySolver(dictionary, wordFrequency);

        List<String> sortedAnswers = letterFrequencySolver.getSortedAnswers(knownStatus, guessResults);

        Assert.notEmpty(sortedAnswers, "Got empty results");
    }
}
