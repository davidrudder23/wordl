package org.noses.wordl.solvers;

import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.KnownStatus;

import java.util.List;
import java.util.stream.Collectors;

public class BadSolver extends Solver {

    public BadSolver(Dictionary dictionary, WordFrequency wordFrequency) {
        super (dictionary, wordFrequency);
    }

    @Override
    public List<String> getSortedAnswers(KnownStatus knownStatus, GuessResults guessResults) {

        // Just like DaveSort, but choosing the least frequently used words that match
        return getSortedByMostFrequent(getAllPossibleWords(knownStatus)).stream().
                sorted((a,b)->b.compareTo(a))
                .collect(Collectors.toList());

    }
}
