package com.gfu.ml.nns;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ejml.simple.SimpleMatrix;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class ForwardComputation {

    private static final Logger logger = LogManager.getLogger(ForwardComputation.class);

    private final SimpleMatrix interceptZ;
    private final boolean sanity;

    public ForwardComputation(
            final boolean sanity
    ) {
        this.sanity = sanity;
        this.interceptZ = new SimpleMatrix(1,1, true, new double[]{1});
        logger.info(format("ForwardComputation has %s sanity check.", sanity ? "enabled" : "disabled"));
    }

    public ForwardComputationResult compute(
            final SimpleMatrix example, // 1 * (M+1)
            final SimpleMatrix alpha, // (M+1) * D1
            final SimpleMatrix beta // (D1+1) * K
    ) {
        checkArgument(example.numRows() == 1,
                "Under stochastic gradient descent, we should use one example at a time");

        final SimpleMatrix A = example.mult(alpha);
        final SimpleMatrix Z = sigmoid(A);
        final SimpleMatrix ZFold = interceptZ.concatColumns(Z);
        if (sanity) check(Z, ZFold);
        final SimpleMatrix B = ZFold.mult(beta);
        final SimpleMatrix yhat = softmax(B);

        return new ForwardComputationResult(A, Z, B, yhat);
    }

    private SimpleMatrix sigmoid(final SimpleMatrix X) {
        checkArgument(X.numRows() == 1);
        final SimpleMatrix ans = new SimpleMatrix(X.numRows(), X.numCols());
        for (int i = 0; i < X.numRows(); i++) {
            for (int j = 0; j < X.numCols(); j++) {
                ans.set(i, j, sigmoid(X.get(i, j)));
            }
        }
        return ans;
    }

    private void check(final SimpleMatrix Z, final SimpleMatrix ZFold) {
        checkState(Z.numCols()+1 == ZFold.numCols());
        checkState(ZFold.get(0,0) == 1);
        for (int j = 1; j < ZFold.numCols(); j++) {
            checkState(Z.get(0, j-1) == ZFold.get(0, j));
        }
    }

    private double sigmoid(final double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private SimpleMatrix softmax(final SimpleMatrix B) {
        checkArgument(B.numRows() == 1);
        final SimpleMatrix ans = new SimpleMatrix(1, B.numCols());
        double denominator = 0;
        for (int l = 0; l < B.numCols(); l++) {
            denominator += Math.exp(B.get(0, l));
        }
        for (int j = 0; j < B.numCols(); j++) {
            ans.set(0, j, Math.exp(B.get(0, j)) / denominator);
        }
        return ans;
    }

    public static class ForwardComputationResult {
        private final SimpleMatrix A;
        private final SimpleMatrix Z;
        private final SimpleMatrix B;
        private final SimpleMatrix yhat;

        private ForwardComputationResult(
                final SimpleMatrix A,
                final SimpleMatrix Z,
                final SimpleMatrix B,
                final SimpleMatrix yhat
        ) {
            this.A = A;
            this.Z = Z;
            this.B = B;
            this.yhat = yhat;
        }

        public SimpleMatrix getA() {
            return this.A;
        }

        public SimpleMatrix getZ() {
            return this.Z;
        }

        public SimpleMatrix getB() {
            return this.B;
        }

        public SimpleMatrix getYHat() {
            return this.yhat;
        }
    }
}
