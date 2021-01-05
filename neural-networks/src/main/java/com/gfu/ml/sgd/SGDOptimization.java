package com.gfu.ml.sgd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ejml.simple.SimpleMatrix;

import static java.lang.String.format;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class SGDOptimization {

    private static final Logger logger = LogManager.getLogger(SGDOptimization.class);

    private final SimpleMatrix trainFeatures;
    private final SimpleMatrix trainLabels;
    private final int hiddenUnits;
    private final int numEpoch;
    private final int initialFlag;

    public SGDOptimization(
            final SimpleMatrix trainFeatures,
            final SimpleMatrix trainLabels,
            final int hiddenUnits,
            final int numEpoch,
            final int initialFlag
    ) {
        this.trainFeatures = trainFeatures;
        this.trainLabels = trainLabels;
        this.hiddenUnits = hiddenUnits;
        this.numEpoch = numEpoch;
        this.initialFlag = initialFlag;
    }

    public SimpleMatrix train() {
        logger.info(format("Start to train the neural network. Hyper-parameters:\n" +
                "\t[# of hidden units]: %d\n" +
                "\t[# of epoch]: %d\n" +
                "\t[initial flag]: %s\n" +
                "\t[total examples]: %d\n" +
                "\t[features]: %d",
                hiddenUnits,
                numEpoch,
                interpret(initialFlag),
                trainFeatures.numRows(),
                trainFeatures.numCols()-1));
        return null;
    }

    private static String interpret(final int initialFlag ) {
        return initialFlag == 1 ? "RANDOM" : "ZERO";
    }

}
