package org.noses.wordl.controller;

import lombok.extern.slf4j.Slf4j;
import org.noses.wordl.Dictionary;
import org.noses.wordl.model.GuessResults;
import org.noses.wordl.model.KnownStatus;
import org.noses.wordl.service.WordlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Slf4j
public class WordlController {

    Dictionary dictionary;
    WordlService service;

    public WordlController(@Autowired Dictionary dictionary, @Autowired WordlService wordlService) {
        this.dictionary = dictionary;
        this.service = wordlService;
    }

    @GetMapping("/")
    public String startGame() {
        KnownStatus knownStatus = new KnownStatus();
        session().setAttribute("knownStatus", knownStatus);
        return "raise";
    }

    @PostMapping("/submit")
    public String submitResults(@RequestBody  GuessResults guessResults) {
        KnownStatus knownStatus = (KnownStatus) session().getAttribute("knownStatus");
        if (knownStatus == null) {
            knownStatus = new KnownStatus();
            session().setAttribute("knownStatus", knownStatus);
            //return "raise";
        }

        log.info("Guess results={}", guessResults);
        service.populateKnownStatus(knownStatus, guessResults);
        List<String> bestWords = service.getSortedByBestGuess(service.getAllPossibleWords(knownStatus));
        log.info("best words for {} are {}", knownStatus, bestWords);
        if (bestWords == null) {
            return "null";
        }

        if (bestWords.size() == 0) {
            return "I don't know";
        }

        return bestWords.get(0);
    }

    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }


}
