package org.noses.wordl.solvers;

import lombok.extern.slf4j.Slf4j;
import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.KnownStatus;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class LetterFrequencySolver extends Solver {

    // After the 2nd round of guesses, we'll default back to the DaveSolver
    DaveSolver daveSolver;

    public LetterFrequencySolver(Dictionary dictionary, WordFrequency wordFrequency) {
        super (dictionary, wordFrequency);
        daveSolver = new DaveSolver(dictionary, wordFrequency);
    }

    private int getLetterFrequencyCount(int[] letterFrequency, String word) {
        char[] chars = word.toCharArray();

        int total = 0;
        for (int i = 0; i < chars.length; i++) {
            total += letterFrequency[chars[i]-'a'];
        }
        return total;
    }

    @Override
    public List<String> getSortedAnswers(KnownStatus knownStatus, GuessResults guessResults) {
        if (knownStatus.getNumGuesses()>2) {
            return daveSolver.getSortedAnswers(knownStatus, guessResults);
        }

        // Build a letter frequency chart based on all the words that can possibly match
        List<String> allPossibleWords = getAllPossibleWords(knownStatus);

        int[] letterFrequency = new int[26];
        for (int i = 0; i < letterFrequency.length; i++) {
            letterFrequency[i] = 0;
        }

        for (String word: allPossibleWords) {
            char[] letters = word.toCharArray();
            for (int i = 0; i < letters.length; i++) {
                letterFrequency[letters[i]-'a']++;
            }
        }

        // TODO logging, remove
        /*for (String answer: allPossibleWords) {
            log.info("{}: {}", answer, getLetterFrequencyCount(letterFrequency, answer));
        }*/

        List<String> sorted = allPossibleWords.stream().sorted((a,b)->getLetterFrequencyCount(letterFrequency, b)-getLetterFrequencyCount(letterFrequency, a)).collect(Collectors.toList());

        return sorted;
    }
}
