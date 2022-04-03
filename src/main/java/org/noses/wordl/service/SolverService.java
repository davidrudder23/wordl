package org.noses.wordl.service;

import lombok.extern.slf4j.Slf4j;
import org.noses.wordl.Dictionary;
import org.noses.wordl.WordFrequency;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.KnownStatus;
import org.noses.wordl.model.LetterResult;
import org.noses.wordl.solvers.DaveSolver;
import org.noses.wordl.solvers.Solver;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SolverService {

    Dictionary dictionary;
    WordFrequency wordFrequency;

    HashMap<String, Solver> solvers;

    public SolverService(@Autowired Dictionary dictionary, @Autowired WordFrequency wordFrequency) {
        this.dictionary = dictionary;
        this.wordFrequency = wordFrequency;
    }

    public void populateKnownStatus(KnownStatus knownStatus, GuessResults guessResults) {
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

    public HashMap<String, Solver> getSolvers() {
        if (solvers != null) {
            return solvers;
        }

        solvers = new HashMap<>();

        Reflections reflections = new Reflections("", new SubTypesScanner(false));
        Set<Class> allClasses = reflections.getSubTypesOf(Solver.class)
                .stream()
                .collect(Collectors.toSet());

        for (Class clazz: allClasses) {
            try {
                Solver solver = (Solver) Class.forName(clazz.getName()).getDeclaredConstructor(Dictionary.class, WordFrequency.class).newInstance(dictionary, wordFrequency);
                solvers.put(clazz.getSimpleName(), solver);
            } catch (Exception exception) {
                log.error("Could not instantiate solver", exception);
            }
        }
        return solvers;
    }

    public Solver getSolver(String name) {
        Solver solver = getSolvers().get(name);
        if (solver == null) {
            log.warn("Could not load solver {}", name);
            solver = new DaveSolver(dictionary, wordFrequency);
        }

        return solver;
    }

    public List<String> getSortedAnswers(Solver solver, KnownStatus knownStatus, GuessResults guessResults) {
        return solver.getSortedAnswers(knownStatus, guessResults);
    }
}