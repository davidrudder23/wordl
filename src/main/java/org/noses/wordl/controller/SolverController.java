package org.noses.wordl.controller;

import lombok.extern.slf4j.Slf4j;
import org.noses.wordl.Dictionary;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.KnownStatus;
import org.noses.wordl.service.SolverService;
import org.noses.wordl.solvers.Solver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class SolverController {

    Dictionary dictionary;
    SolverService service;

    public SolverController(@Autowired Dictionary dictionary, @Autowired SolverService solverService) {
        this.dictionary = dictionary;
        this.service = solverService;
    }

    @GetMapping("/")
    public String startGame(@RequestParam(required = false) String startWord) {
        KnownStatus knownStatus = new KnownStatus();
        session().setAttribute("knownStatus", knownStatus);

        if (StringUtils.hasLength(startWord)) {
            return startWord;
        } else {
            return "raise"; // Dave's default, why not
        }
    }

    @GetMapping("/solvers")
    public Set<String> getSolvers() {
        return service.getSolvers().keySet();
    }

    @PostMapping("/submit")
    public String submitResults(@RequestBody GuessResults guessResults) {
        KnownStatus knownStatus = (KnownStatus) session().getAttribute("knownStatus");
        if (knownStatus == null) {
            knownStatus = new KnownStatus();
            session().setAttribute("knownStatus", knownStatus);
        }

        log.info("Guess results={}", guessResults);
        service.populateKnownStatus(knownStatus, guessResults);

        Solver solver = service.getSolver(guessResults.getSolver());

        List<String> bestWords = service.getSortedAnswers(solver, knownStatus, guessResults);
        log.info("best words for {} are {}", knownStatus, bestWords);
        if (bestWords == null) {
            return "null";
        }

        if (bestWords.size() == 0) {
            return "I don't know";
        }

        return bestWords.get(0);
    }

    public List<String> getSortedAnswers(KnownStatus knownStatus, GuessResults guessResults) {
        return null;
    }

    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

}
