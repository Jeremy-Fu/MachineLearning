package com.gfu.ml.loss;

import com.gfu.ml.data.MatrixDataSet;
import com.gfu.ml.matrix.AddOperator;
import com.gfu.ml.matrix.MultiplyOperator;
import com.gfu.ml.data.Pair;

import java.util.List;


/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class LogisticRegressionGradient {


    private final MultiplyOperator multiply;

    private final AddOperator add;

    public LogisticRegressionGradient(
            final MultiplyOperator multiply,
            final AddOperator add) {
        this.multiply = multiply;
        this.add = add;
    }


    public double[] gradientOfTheta(
            final double[] thetaV,
            final MatrixDataSet x,
            final int i
    ) {
        final double[] xi = x.exampleVector(i);
        final double ystari = x.label(i);
        final double yhat = multiply.apply(thetaV, xi);
        final double u = (ystari - Math.exp(yhat) / (1 + Math.exp(yhat)));
        return multiply.apply(xi, -u);
    }


    /**
     * Calculate gradient of theta with finite difference method
     * @param thetaV
     * @param x
     * @param i
     * @return
     */
    public double[] gradientOfTheta (
            final double[] thetaV,
            final MatrixDataSet x,
            final int i,
            final double epsilon
    ) {
        final int M = thetaV.length;
        final double[] gradients = new double[M];
        for (int m = 0; m < M; m++) {
            final double[] thetaV_p = add.apply(thetaV, epsilon, m);
            final double product_p = conditionalLogLikelihood(thetaV_p, x.example(i), x.label(i));

            final double[] thetaV_n = add.apply(thetaV, -epsilon, m);
            final double product_n = conditionalLogLikelihood(thetaV_n, x.example(i), x.label(i));

            gradients[m] = (product_p - product_n) / (2 * epsilon);
        }
        return gradients;
    }

    private double conditionalLogLikelihood(
            final double[] thetaV,
            final List<Pair<Integer, Integer>> xV,
            final Integer ystar
    ) {
        final double product = multiply.apply(thetaV, xV);
        return -1 * ystar * product + Math.log(1 + Math.exp(product));
    }
}
