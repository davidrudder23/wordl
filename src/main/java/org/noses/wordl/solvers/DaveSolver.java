package org.noses.wordl.solvers;

import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.KnownStatus;

import java.util.List;

public class DaveSolver extends Solver {

    public DaveSolver(Dictionary dictionary, WordFrequency wordFrequency) {
        super (dictionary, wordFrequency);
    }

    @Override
    public List<String> getSortedAnswers(KnownStatus knownStatus, GuessResults guessResults) {
        return getSortedByMostFrequent(getAllPossibleWords(knownStatus));
    }
}
