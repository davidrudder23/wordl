package org.noses.wordl;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

@Component
@Slf4j
public class WordFrequency {
    HashMap<String, Long> table;

    public WordFrequency() {
    }

    void loadWords() {
        BufferedReader in = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/word_frequency.csv")));
        loadWords(in);
    }

    void loadWords(BufferedReader in) {
        table = new HashMap<>();

        String line;
        try {
            while ((line = in.readLine()) != null) {
                try {
                    String word = line.substring(0, line.indexOf(","));
                    long times = Long.parseLong(line.substring(line.indexOf(",") + 1));
                    table.put(word, times);
                } catch (NumberFormatException e) {
                    log.info("Couldn't parse word frequency phrase {}", e, line);
                }
            }
        } catch (IOException ioException) {
            log.error("Could not load dictionary", ioException);
        }
    }

    public long getWordFrequency(String word) {
        if (table == null) {
            loadWords();
        }
        Long amt = table.get(word);
        if (amt == null) {
            log.warn("Didn't find {} in dictionary", word);
            return 0;
        }
        return amt;
    }
}
