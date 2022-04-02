package org.noses.wordl.service;

import lombok.extern.slf4j.Slf4j;
import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.KnownStatus;
import org.noses.wordl.model.LetterResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WordlService {

    Dictionary dictionary;
    WordFrequency wordFrequency;

    public WordlService(@Autowired Dictionary dictionary, @Autowired WordFrequency wordFrequency) {
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

    public List<String> getSortedByBestGuess(List<String> allPossibleWords) {
        return allPossibleWords.stream()
                        .sorted((a,b)->{
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

    public void populateKnownStatus(KnownStatus knownStatus, GuessResults guessResults) {
        log.info("Guess Results={}", guessResults);
        for (int i = 0; i < guessResults.getLetterResults().length; i++) {
            LetterResult letterResult = guessResults.getLetterResults()[i];

            if (letterResult.getResult().equalsIgnoreCase("black")) {
                knownStatus.addNotExists(letterResult.getLetter());
            } else if (letterResult.getResult().equalsIgnoreCase("orange")) {
                knownStatus.addWrongPlace(i, letterResult.getLetter());
            } else if (letterResult.getResult().equalsIgnoreCase("green")) {
                knownStatus.addCorrect(i, letterResult.getLetter());
            } else {
                log.error("Unknown letter result {}", letterResult.getResult());
            }
        }
    }

}
