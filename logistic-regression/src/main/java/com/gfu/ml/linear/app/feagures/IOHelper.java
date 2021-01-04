package com.gfu.ml.linear.app.feagures;

import com.gfu.ml.data.MatrixDataSet;
import com.gfu.ml.data.Pair;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class IOHelper {

    private static final Logger logger = LogManager.getLogger(IOHelper.class);

    public static Map<String, Integer> loadDictionary(final String input) throws IOException {
        final ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
        final BufferedReader in = new BufferedReader(new FileReader(input));
        String line;
        while((line = in.readLine()) != null) {
            final String[] fields = line.split("\\s");
            builder.put(fields[0], Integer.valueOf(fields[1]));
        }
        in.close();
        final Map<String, Integer> map = builder.build();
        logger.info(format("Loaded the dictionary of size %d.", map.size()));
        return map;
    }

    public static MatrixDataSet loadFormattedFeatures(final String formattedFeaturesFileName, final int M) throws IOException {
        final BufferedReader in = new BufferedReader(new FileReader(formattedFeaturesFileName));
        final MatrixDataSet.Builder builder = MatrixDataSet.builder(M+1); // add an intercept term
        String line = null;
        while ((line = in.readLine()) != null) {
            final String[] fields = line.split("\t");
            final int ystari = Integer.parseInt(fields[0]);
            builder.addExample(ystari, parseFeatures(Arrays.copyOfRange(fields, 1, fields.length)));
        }
        return builder.build();
    }

    public static int featuresCount(final String dictInput) throws IOException {
        final BufferedReader in = new BufferedReader(new FileReader(dictInput));
        String line = null;
        int max = -1, count = 0;
        while ((line = in.readLine()) != null) {
            max = Math.max(max, Integer.parseInt(line.split("\\s")[1]));
            count++;
        }
        logger.info(format("Loaded the dictionary file. Total lines = %d and there are %d features.", count, max+1));
        in.close();
        return count;
    }

    public static List<Pair<Integer, Integer>> parseFeatures(final String[] features) {

        final List<Pair<Integer, Integer>> list = Arrays.stream(features)
                .map(feature -> {
                    final String[] fields = feature.split(":");
                    return new Pair<>(Integer.parseInt(fields[0]) + 1, Integer.parseInt(fields[1])); // offset by one;
                })
                .sorted(Comparator.comparingInt(Pair::getFirst))
                .collect(Collectors.toList());
        list.add(0, new Pair<>(0, 1)); // intercept term;
        return list;
    }
}
