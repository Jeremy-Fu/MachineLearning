package com.gfu.ml.linear.features;

import com.gfu.ml.data.Pair;
import com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class ExtractFeature {

    private final Map<String, Integer> dictionary;
    private final boolean useModel1;


    public ExtractFeature(
            final Map<String, Integer> dictionary,
            final boolean useModel1
    ) {
        this.dictionary = dictionary;
        this.useModel1 = useModel1;
    }

    public String extract(final String example) {
        final String[] parsed = example.split("\t");
        final int ystari = Integer.parseInt(parsed[0]);
        final String[] words = parsed[1].split(" ");
        final Map<Integer, Integer> frequencyCount = Arrays.stream(words)
                .filter(dictionary::containsKey)
                .map(dictionary::get)
                .collect(HashMap::new,
                        (accumulative, wordIdx) -> {
                            final int count = accumulative.getOrDefault(wordIdx, 0);
                            accumulative.put(wordIdx, count+1);
                        },
                        HashMap::putAll);

        final List<Pair<Integer, Integer>> features = frequencyCount.entrySet()
                .stream()
                .filter(selectPredicate())
                .map(entry -> new Pair<>(entry.getKey(), 1))
                .collect(Collectors.toList());

        final StringBuilder outcome = new StringBuilder();
        outcome.append(ystari);
        outcome.append("\t");
        outcome.append(Joiner.on("\t").join(features));
        return outcome.toString();
    }

    private Predicate<Entry<Integer, Integer>> selectPredicate() {
        if (useModel1) {
            return entry -> true;
        } else {
            return entry -> entry.getValue() < 4;
        }
    }

}
