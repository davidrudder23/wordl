package org.noses.wordl.solvers;

import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.KnownStatus;

import java.util.List;
import java.util.stream.Collectors;

public abstract class Solver {

    Dictionary dictionary;
    WordFrequency wordFrequency;

    Solver(Dictionary dictionary, WordFrequency wordFrequency) {
        this.dictionary = dictionary;
        this.wordFrequency = wordFrequency;
    }

    boolean containsAllLetters(String word, List<Character> letters) {
        for (Character letter: letters) {
            if (!word.contains(letter+"")) {
                return false;
            }
        }
        return true;
    }

    boolean containsLettersAt(String word, Character[] letters) {
        char[] wordAsArray = word.toCharArray();
        for (int i = 0; i < letters.length; i++) {
            if (word.length()<=i) {
                return false;
            }

            if (letters[i] == null) {
                continue;
            }

            if (wordAsArray[i] != letters[i]) {
                return false;
            }
        }
        return true;
    }

    boolean doesntContainLettersAt(String word, List<List<Character>> letters) {
        char[] wordAsArray = word.toCharArray();
        for (int i = 0; i < 5; i++) { // unhardcode 5
            if (word.length()<=i) {
                return false;
            }

            if (letters.get(i) == null) {
                continue;
            }

            if (letters.get(i).contains(wordAsArray[i])) {
                return false;
            }
        }
        return true;
    }

    boolean doesntHaveBadLetters(String word, List<Character> letters) {
        for (Character letter: letters) {
            if (word.contains(letter+"")) {
                return false;
            }
        }
        return true;
    }

    public List<String> getAllPossibleWords(KnownStatus knownStatus) {
        List<Character> allLetters = knownStatus.getAllLettersExists();
        List<String> words = dictionary.getWords(5).stream()
                .filter(w->doesntHaveBadLetters(w, knownStatus.getNotExists()))
                .filter(w->containsAllLetters(w, allLetters))
                .filter(w->containsLettersAt(w, knownStatus.getCorrect()))
                .filter(w->doesntContainLettersAt(w, knownStatus.getWrongPlace()))
                .collect(Collectors.toList());

        return words;
    }

    public List<String> getSortedByMostFrequent(List<String> allPossibleWords) {
        return allPossibleWords.stream()
                .sorted((a,b)->{
                    // ugh, this is messy b/c the frequency can be > an int
                    long diff = wordFrequency.getWordFrequency(b) - wordFrequency.getWordFrequency(a);
                    if (diff>0) {
                        return 1;
                    } else if (diff < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }).collect(Collectors.toList());
    }

    public abstract List<String> getSortedAnswers(KnownStatus knownStatus, GuessResults guessResults);
}
