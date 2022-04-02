package org.noses.wordl.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class GuessResultsTest {

    @Test
    public void testGuessResults() throws Exception {
        GuessResults guessResults = new GuessResults();
        LetterResult[] letterResults = new LetterResult[5];
        letterResults[0] = new LetterResult();
        letterResults[0].setLetter('a');
        letterResults[0].setResult("orange");

        letterResults[1] = new LetterResult();
        letterResults[1].setLetter('t');
        letterResults[1].setResult("black");

        letterResults[2] = new LetterResult();
        letterResults[2].setLetter('i');
        letterResults[2].setResult("green");

        letterResults[3] = new LetterResult();
        letterResults[3].setLetter('q');
        letterResults[3].setResult("black");

        letterResults[4] = new LetterResult();
        letterResults[4].setLetter('q');
        letterResults[4].setResult("black");

        guessResults.setLetterResults(letterResults);

        ObjectMapper mapper = new ObjectMapper();
        String deser = mapper.writeValueAsString(guessResults);
        GuessResults reser = mapper.readValue(deser, GuessResults.class);
        System.out.println(deser);
        System.out.println(reser);
    }
}
