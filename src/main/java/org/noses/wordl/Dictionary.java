package org.noses.wordl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Component
public class Dictionary {

    // One array list for each word length
    // We only need 200 because we'll just ditch any words over 200 letters long
    // In theory, we could just store 5 letter words, but this is more flexible and
    // might be fun to make a wordle game with 6 letters or 7 letters
    ArrayList<ArrayList<String>> words;

    SecureRandom random;

    SecureRandom getRandom() {
        if (random == null) {
            random = new SecureRandom((System.currentTimeMillis()+"").getBytes(StandardCharsets.UTF_8));
        }

        return random;
    }

    public List<String> getWords(int wordLength) {
        if (words == null) {
            loadWords();
        }

        if (wordLength > words.size()) {
            return null;
        }

        return words.get(wordLength);
    }

    void loadWords() {
        InputStream in = Dictionary.class.getResourceAsStream("/american-english");
        loadWords(
                new BufferedReader(
                new InputStreamReader(in)));
    }

    void loadWords(BufferedReader in) {
        words = new ArrayList<>(200);

        for (int i = 0; i < 200; i++) {
            ArrayList<String> wordsForLength = new ArrayList<>();
            words.add(wordsForLength);
        }
        Pattern pattern = Pattern.compile(".*[^a-z].*");

        try {

            String word = "";
            while ((word = in.readLine()) != null) {
                if (!pattern.matcher(word).matches()) {

                    int length = word.length();
                    ArrayList<String> wordsForLength = words.get(length);
                    if (wordsForLength == null) {
                        wordsForLength = new ArrayList<>();
                    }
                    wordsForLength.add(word);
                    words.set(length, wordsForLength);
                }
            }
        } catch (IOException ioException) {
            log.warn("Could not load dictionary", ioException);
        }
    }

    public String getRandomWord(int wordLength) {
        List<String> words = getWords(wordLength);
        if (words == null) {
            return null;
        }
        log.info("We have {} words", words.size());
        return words.get(Math.abs(getRandom().nextInt()%words.size()));
    }

}
