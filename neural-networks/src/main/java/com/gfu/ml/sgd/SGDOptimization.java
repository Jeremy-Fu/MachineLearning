package com.gfu.ml.sgd;

import com.gfu.ml.nns.BackwardComputation;
import com.gfu.ml.nns.ForwardComputation;
import com.gfu.ml.nns.ForwardComputation.ForwardComputationResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class SGDOptimization {

    private static final Logger logger = LogManager.getLogger(SGDOptimization.class);

    private final SimpleMatrix trainFeatures; // N * (M+1)
    private final SimpleMatrix trainLabels;
    private final int hiddenUnits;
    private final int numEpoch;
    private final int initialFlag;

    private final ForwardComputation forwardComputation;
    private final BackwardComputation backwardComputation;

    private final int K = 6; // num of distinct labels;

    public SGDOptimization(
            final SimpleMatrix trainFeatures,
            final SimpleMatrix trainLabels,
            final int hiddenUnits,
            final int numEpoch,
            final int initialFlag,
            final ForwardComputation forwardComputation,
            final BackwardComputation backwardComputation
    ) {
        this.trainFeatures = trainFeatures;
        this.trainLabels = trainLabels;
        this.hiddenUnits = hiddenUnits;
        this.numEpoch = numEpoch;
        this.initialFlag = initialFlag;
        this.forwardComputation = forwardComputation;
        this.backwardComputation = backwardComputation;

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

        SimpleMatrix alpha = initAlpha();
        SimpleMatrix beta = initBeta();
        for (int epoch = 0; epoch < numEpoch; epoch++) {
            for (int i = 0; i < trainFeatures.numRows(); i++) {
                final ForwardComputationResult result = forwardComputation.compute(trainFeatures.rows(i, i+1), alpha, beta);
//                backwardComputation.compute();
            }
        }
        return null;
    }

    private static String interpret(final int initialFlag ) {
        return initialFlag == 1 ? "RANDOM" : "ZERO";
    }

    private SimpleMatrix initAlpha() {
        final int M = trainFeatures.numCols();
        return new SimpleMatrix(M, hiddenUnits);
    }

    private SimpleMatrix initBeta() {
        return new SimpleMatrix(hiddenUnits+1, K);
    }

    private Map<Integer, SimpleMatrix> init() {
        final Map<Integer, SimpleMatrix> memo = new HashMap<>(K);
        for (int i = 0; i < K; i++) {
            final SimpleMatrix value = new SimpleMatrix(1, K);
            value.set(0, i);
        }
        return memo;
    }

    private SimpleMatrix getLabel(final int i) {
        return null;
    }


}
