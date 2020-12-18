package com.gfu.ml.calculators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class EntropyCalculator {

    private static final Logger logger = LogManager.getLogger(EntropyCalculator.class);
    private final Map<Integer, Integer> labelsCounter;
    private int total;

    public EntropyCalculator() {
        this.labelsCounter = new HashMap<>();
    }

    public EntropyCalculator digest(final int label) {
        int counter = labelsCounter.getOrDefault(label, 0);
        labelsCounter.put(label, counter+1);
        total++;
        logger.trace(format("digest[%d]: %d", label, labelsCounter.get(label)));
        return this;
    }

    public double getEntropy() {
        double entropy = 0;
        for (final Integer label : labelsCounter.keySet()) {
            final double probability = getProbability(label);
            logger.trace(format("getEntropy: P[Y=%d]=%f", label, probability));
            entropy -= probability * (Math.log(probability) / Math.log(2));
        }
        return entropy;
    }

    private double getProbability(final int label) {
        final int occurrence = labelsCounter.getOrDefault(label, 0);
        return ((double) occurrence) / (double) total;
    }
}
