package org.noses.wordl.service;

import lombok.extern.slf4j.Slf4j;
import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.KnownStatus;
import org.noses.wordl.model.LetterResult;
import org.noses.wordl.solvers.Solver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CheckerService {
    SolverService solverService;
    Dictionary dictionary;
    WordFrequency wordFrequency;

    public CheckerService(@Autowired SolverService solverService, @Autowired Dictionary dictionary, @Autowired WordFrequency wordFrequency) {
        this.solverService = solverService;
        this.dictionary = dictionary;
        this.wordFrequency = wordFrequency;
    }

    public GuessResults checkGuess(String answer, String guess) {
        if ((answer == null) || (guess == null) || (answer.length() != guess.length())) {
            log.warn("Could not check guess with answer={} and guess={}", answer, guess);
        }
        LetterResult[] letterResults = new LetterResult[answer.length()];

        for (int i = 0; i < answer.length(); i++) {
            char answerLetter = answer.charAt(i);
            char guessLetter = guess.charAt(i);

            LetterResult letterResult = new LetterResult();
            letterResult.setLetter(guessLetter);

            if (answerLetter == guessLetter) {
                letterResult.setResult("green");
            } else if (answer.contains(guessLetter+"")) {
                letterResult.setResult("orange");
            } else {
                letterResult.setResult("black");
            }
            letterResults[i] = letterResult;
        }

        GuessResults guessResults = new GuessResults();
        guessResults.setLetterResults(letterResults);

        return guessResults;
    }

    public int numGuesses(Solver solver, String answer, String firstGuess) {
        int count = 0;

        GuessResults guessResults;

        String guess = firstGuess;
        KnownStatus knownStatus = new KnownStatus();

        do  {
            guessResults = checkGuess(answer, guess);
            solverService.populateKnownStatus(knownStatus, guessResults);
            List<String> guesses = solver.getSortedAnswers(knownStatus, guessResults);
            if ((guesses == null) || (guesses.size()==0)) {
                return -1;
            }
            guess = guesses.get(0);
            count++;
        } while (!isSolved(guessResults));

        return count;
    }

    private boolean isSolved (GuessResults guessResults) {
        if (guessResults == null) {
            return  false;
        }

        LetterResult[] letterResults = guessResults.getLetterResults();
        for (int i = 0; i < letterResults.length; i++) {
            if (!letterResults[i].getResult().equalsIgnoreCase("green")) {
                return false;
            }
        }

        return true;
    }

    public String checkAll(int listSize, String solverName) {

        // Okay, we can't actually check _all_ words.  It would output a CSV of 230GB big and I don't have the space
        // Besides, how would you understand the output?
        Collection<Solver> solvers = null;

        if ( StringUtils.hasLength(solverName)) {
            solvers = new ArrayList<>();
            solvers.add(getSolver(solverName));
        } else {
            getSolvers().values();
        }

        List<String> words = wordFrequency.getMostFrequent(dictionary, 5, listSize);
        log.info("Starting with {} words", words.size());
        // filter out words that aren't in the word frequency table, they're probably bullshit
        words = words.stream()
                //.filter(w-> dictionary.contains(w))
                .collect(Collectors.toList());
        log.info("Filtered to with {} words", words.size());

        log.info("Abash exists? {}", words.contains("abash"));

        StringBuffer result = new StringBuffer();

        String header = "Solver,first word,avg num guesses";
        log.info("CSV: {}", header);
        result.append(header+"\n");


        for (Solver solver: solvers) {
            String solverSimpleName = solver.getClass().getSimpleName();
            for (String firstWord: words) {
                long totalValue = 0;
                log.info("Flipping through {} words for firstWord {}", words.size(), firstWord);
                totalValue = words.parallelStream().map(answer->numGuesses(solver, answer, firstWord)).reduce(0, Integer::sum);

                float avg = (float)((float)totalValue/(float)words.size());
                String thisResult = solverSimpleName+","+firstWord+","+avg;
                log.info("CSV: {}", thisResult);
                result.append(thisResult+"\n");
            }
        }
        return result.toString();
    }

    public HashMap<String, Solver> getSolvers() {
        return solverService.getSolvers();
    }

    public Solver getSolver(String name) {
        return solverService.getSolver(name);
    }

}
