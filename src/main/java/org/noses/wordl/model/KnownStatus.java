package org.noses.wordl.model;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
public class KnownStatus {

    List<List<Character>> wrongPlace;
    List<Character> notExists;
    Character[] correct;
    int numGuesses;

    public KnownStatus() {
        wrongPlace = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            wrongPlace.add(new ArrayList<>());
        }

        notExists = new ArrayList<>();
        correct = new Character[5];

        numGuesses = 0;
    }

    public void addCorrect(int place, Character letter) {
        correct[place]=letter;
    }

    public void addNotExists(Character letter) {
        notExists.add(letter);
    }

    public void addWrongPlace(int place, Character letter) {
        List<Character> letters = wrongPlace.get(place);
        if (letters.indexOf(letter)<0) {
            letters.add(letter);
        }
    }

    public List<Character> getAllLettersExists() {
        List<Character> letters = new ArrayList<>();

        for (List<Character> misplaced: wrongPlace) {
            for (Character letter: misplaced) {
                if (letter == null) {
                    continue;
                }
                if (!letters.contains(letter)) {
                    letters.add(letter);
                }
            }
        }

        for (Character letter: correct) {
            if (letter == null) {
                continue;
            }
            if (!letters.contains(letter)) {
                letters.add(letter);
            }
        }

        return letters;
    }

    public List<List<Character>> getWrongPlace() {
        return wrongPlace;
    }

    public List<Character> getNotExists() {
        return notExists;
    }

    public Character[] getCorrect() {
        return correct;
    }

    public int getNumGuesses() {
        return numGuesses;
    }

    public void setNumGuesses(int numGuesses) {
        this.numGuesses = numGuesses;
    }
}
