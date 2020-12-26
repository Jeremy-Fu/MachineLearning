package com.gfu.ml.calculators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class Entropy {

    private static final Logger logger = LogManager.getLogger(Entropy.class);
    private final Map<Boolean, Integer> labelsCounter;
    private int total;

    public Entropy() {
        this.labelsCounter = new HashMap<>();
    }

    public Entropy digest(final boolean output) {
        int counter = labelsCounter.getOrDefault(output, 0);
        labelsCounter.put(output, counter+1);
        total++;
        logger.trace(format("digest[%s]: %d", output, labelsCounter.get(output)));
        return this;
    }

    public double value() {
        double entropy = 0;
        for (final Boolean output : labelsCounter.keySet()) {
            final double probability = getProbability(output);
            logger.trace(format("getEntropy: P[Y=%s]=%f", output, probability));
            entropy -= probability * (Math.log(probability) / Math.log(2));
        }
        return entropy;
    }

    private double getProbability(final boolean output) {
        final int occurrence = labelsCounter.getOrDefault(output, 0);
        return ((double) occurrence) / (double) total;
    }
}
