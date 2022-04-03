package org.noses.wordl.controller;

import lombok.extern.slf4j.Slf4j;
import org.noses.wordl.Dictionary;
import org.noses.wordl.service.CheckerService;
import org.noses.wordl.solvers.Solver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CheckerController {

    Dictionary dictionary;
    CheckerService checkerService;

    public CheckerController(@Autowired CheckerService checkerService, @Autowired Dictionary dictionary) {
        this.dictionary = dictionary;
        this.checkerService = checkerService;
    }

    @GetMapping("/check/{solverName}/{answer}")
    public String checkForSolverAndAnswer(@PathVariable String solverName, @PathVariable String answer) {
        Solver solver = checkerService.getSolver(solverName);

        int numGuesses = checkerService.numGuesses(solver, "fewer", "raise");
        return "Guessed in "+numGuesses+" guesses, startgin with raise";
    }

    @GetMapping("/check/all")
    public String checkAll() {
        return checkerService.checkAll();
    }

}
