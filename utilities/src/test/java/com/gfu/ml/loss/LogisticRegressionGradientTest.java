package com.gfu.ml.loss;

import com.gfu.ml.data.MatrixDataSet;
import com.gfu.ml.matrix.AddOperator;
import com.gfu.ml.matrix.MultiplyOperator;
import com.gfu.ml.data.Pair;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class LogisticRegressionGradientTest {

    @Test
    public void testGradientOfTheta_finiteDifferenceMethod() {
        final int M = 3;
        final double epsilon = 1e-10;
        final double[] theta1V = {1, 4.234, 0};

        final MatrixDataSet data = createTestData(M);

        final LogisticRegressionGradient lrg = new LogisticRegressionGradient(
                new MultiplyOperator(),
                new AddOperator()
        );

        final double[] xi = new double[M];
        for (int i = 0; i < M; i++) {
            xi[i] = 1;
        }

        for (int i = 0; i < data.N(); i++) {
            final double[] gradientClosedForm = lrg.gradientOfTheta(theta1V, data, 1);
            final double[] gradientComputational = lrg.gradientOfTheta(theta1V, data, 1, epsilon);

            for (int ii = 0; ii < M; ii++) {
                assertEquals(gradientClosedForm[ii], gradientComputational[ii], 1e-5);
            }
        }

    }

    private MatrixDataSet createTestData(final int M) {
        final MatrixDataSet.Builder builder = MatrixDataSet.builder(M);
        final List<Pair<Integer, Integer>> ex1 = ImmutableList.<Pair<Integer, Integer>>builder()
                .add(new Pair(0, 1))
                .add(new Pair(1, 1))
                .add(new Pair(2, 0))
                .build();

        builder.addExample(1, ex1);

        final List<Pair<Integer, Integer>> ex2 = ImmutableList.<Pair<Integer, Integer>>builder()
                .add(new Pair(0, 1))
                .add(new Pair(1, 0))
                .add(new Pair(2, 1))
                .build();

        builder.addExample(0, ex1);

        return builder.build();
    }
}
