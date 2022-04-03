package org.noses.wordl.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.noses.wordl.model.GuessResults;
import static org.springframework.util.Assert.*;

@Slf4j
public class CheckerServiceTest {
    @Test
    public void testChecker() {
        String answer = "antic";
        String guess = "audit";

        CheckerService checkerService = new CheckerService(null, null, null);

        GuessResults guessResults = checkerService.checkGuess(answer, guess);
        log.info("Guess Results={}", guessResults);

        notNull(guessResults, "Guess results are null");
        isTrue(guessResults.getLetterResults().length==answer.length(), "Wrong length of guess results");
        isTrue(guessResults.getLetterResults()[0].getResult().equalsIgnoreCase("green"), "Bad check");
        isTrue(guessResults.getLetterResults()[1].getResult().equalsIgnoreCase("black"), "Bad check");
        isTrue(guessResults.getLetterResults()[2].getResult().equalsIgnoreCase("black"), "Bad check");
        isTrue(guessResults.getLetterResults()[3].getResult().equalsIgnoreCase("green"), "Bad check");
        isTrue(guessResults.getLetterResults()[4].getResult().equalsIgnoreCase("orange"), "Bad check");
    }
}
