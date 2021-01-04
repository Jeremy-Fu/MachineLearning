package com.gfu.ml.linear.learning;

import com.gfu.ml.data.MatrixDataSet;
import com.gfu.ml.matrix.MinusOperator;
import com.gfu.ml.matrix.MultiplyOperator;
import com.gfu.ml.loss.LogisticRegressionGradient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.String.format;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class StochasticGradientDescent {

    private static final Logger logger = LogManager.getLogger(StochasticGradientDescent.class);

    private final MatrixDataSet examples;
    private final double eta;
    private final LogisticRegressionGradient gratitude;
    private final MinusOperator minus;
    private final MultiplyOperator multiply;
    private final int numEpoch;


    public StochasticGradientDescent(
            final MatrixDataSet examples,
            final double eta,
            final LogisticRegressionGradient gratitude,
            final MinusOperator minus,
            final MultiplyOperator multiply,
            final int numEpoch
    ) {
        this.examples = examples;
        this.eta = eta;
        this.gratitude = gratitude;
        this.minus = minus;
        this.multiply = multiply;
        this.numEpoch = numEpoch;
    }


    public double[] learn() {
        final int M = examples.M();
        double[] theta = new double[M];
        final int N = examples.N();
        int epochCnt = 0;
        while (epochCnt < numEpoch) {
            for (int i = 0; i < N; i++) {
                final double[] gratitudeTheta = gratitude.gradientOfTheta(theta, examples, i);
                theta = minus.apply(theta, multiply.apply(gratitudeTheta, eta));
            }
            epochCnt++;
            logger.info(format("Finished %dth epoch", epochCnt));
        }
        return theta;
    }
}
