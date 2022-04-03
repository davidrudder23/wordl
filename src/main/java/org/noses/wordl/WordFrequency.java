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
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
            //log.warn("Didn't find {} in dictionary", word);
            return 0;
        }
        return amt;
    }

    public List<String> getMostFrequent(Dictionary dictionary, int wordSize, int listSize) {
        if (table == null) loadWords();

        List<String> results = table.keySet()
                .stream()
                .filter(w->w.length()==wordSize)
                .filter(w->dictionary.getWords(wordSize).contains(w))
                .sorted((a,b)->table.get(b).compareTo(table.get(a)))
                .limit(listSize)
                .collect(Collectors.toList());

        log.info("{} most frequent words={}", listSize, results);
        return results;
    }
}
