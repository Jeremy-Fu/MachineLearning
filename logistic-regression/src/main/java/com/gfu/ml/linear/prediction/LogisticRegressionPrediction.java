package com.gfu.ml.linear.prediction;

import com.gfu.ml.data.MatrixDataSet;
import com.gfu.ml.matrix.MultiplyOperator;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class LogisticRegressionPrediction {

    private double[] theta;
    private MultiplyOperator multiply;

    public LogisticRegressionPrediction(
            final double[] theta,
            final MultiplyOperator multiply
    ) {
        this.theta = theta;
        this.multiply = multiply;
    }

    public int[] predict(final MatrixDataSet data) {
        final int N = data.N();
        final int[] labels = new int[N];
        for (int i = 0; i < N; i++) {
            final double phat0 = 1 / (1 + Math.exp(multiply.apply(theta, data.example(i))));
            labels[i] = phat0 >= 0.5 ? 0 : 1;
        }
        return labels;
    }
}
