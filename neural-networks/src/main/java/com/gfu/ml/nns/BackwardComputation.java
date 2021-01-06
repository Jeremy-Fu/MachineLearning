package com.gfu.ml.nns;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ejml.simple.SimpleMatrix;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class BackwardComputation {

    private static final Logger logger = LogManager.getLogger(BackwardComputation.class);

    private final boolean sanity;

    public BackwardComputation(final boolean sanity) {
        this.sanity = sanity;
        logger.info(format("BackwardComputation has %s sanity check.", sanity ? "enabled" : "disabled"));
    }

    public BackwardComputationResult compute(
            final SimpleMatrix ystar,
            final SimpleMatrix yhat,
            final SimpleMatrix Z,
            final SimpleMatrix beta,
            final SimpleMatrix X
    ) {
        checkArgument(1 == ystar.numRows());
        checkArgument(1 == yhat.numRows());
        checkArgument(ystar.numCols() == yhat.numCols());
        final int K = ystar.numCols();
        final SimpleMatrix dYhat = derivativeYhat(ystar, yhat);
        final SimpleMatrix dB = derivativeB(dYhat, yhat);
        final SimpleMatrix dBeta = derivativeBeta(dB, Z, K);
        final SimpleMatrix dZ = derivativeZ(dB, beta);
        final SimpleMatrix dA = derivativeA(dZ, Z);
        final SimpleMatrix dAlpha = derivativeAlpha(dA, X);
        return new BackwardComputationResult(dAlpha, dBeta);
    }

    private SimpleMatrix derivativeYhat(
            final SimpleMatrix ystar,
            final SimpleMatrix yhat
    ) {
        final SimpleMatrix ans = new SimpleMatrix(1, ystar.numCols());
        for (int j = 0; j < ystar.numCols(); j++) {
            final double derivative = ystar.get(0, j) / yhat.get(0, j);
            ans.set(0, j, derivative);
        }
        return ans;
    }

    /**
     * @return (1 * K)
     */
    private SimpleMatrix derivativeB(
            final SimpleMatrix dYhat,
            final SimpleMatrix yhat
    ) {
        final SimpleMatrix ans = new SimpleMatrix(1, dYhat.numCols());
        for (int k = 0; k < dYhat.numCols(); k++) {
            double dBk = 0;
            for (int l = 0; l < dYhat.numCols(); l++) {
                dBk += dYhat.get(l) * yhat.get(l) * (k == l ? 1 : 0 - yhat.get(k));
            }
            ans.set(k, dBk);
        }
        return ans;
    }

    private SimpleMatrix derivativeBeta(
            final SimpleMatrix dB,
            final SimpleMatrix Z, // 1 * D1
            final int K
    ) {
        final int D1 = Z.numCols();
        final SimpleMatrix ans = new SimpleMatrix(D1+1, K); // beta is (D1+1) * K
        for (int j = 0; j < K; j++) {
            for (int i = 0; i < Z.numCols(); i++) {
                ans.set(i, j, dB.get(j) * Z.get(i));
            }
        }
        return ans;
    }

    /**
     *
     * \u2202J/\u2202z<sub>j</sub> = \u03A3<sub>k=1</sub><sup>K</sup> (\u2202J/\u2202b<sub>k</sub> * \u03B2<sub>j+1,</sub><sub>k</sub>)
     *
     * @return 1 * (D1+1) matrix
     */
    private SimpleMatrix derivativeZ(
            final SimpleMatrix dB, // 1 * K
            final SimpleMatrix beta // (D1+1) * K
    ) {
        final int D1 = beta.numRows() - 1;
        final int K = dB.numCols();
        final SimpleMatrix ans = new SimpleMatrix(1, D1);
        for (int j = 0; j < D1; j++) {
            double sum = 0;
            for (int k = 0; k < K; k++) {
                sum += dB.get(k) * beta.get(j+1, k);
            }
            ans.set(j, sum);
        }
        return ans;
    }

    private SimpleMatrix derivativeA(
            final SimpleMatrix dZ,
            final SimpleMatrix Z // 1 * D1
    ) {
        checkArgument(dZ.numRows() == 1);
        checkArgument(Z.numRows() == 1);
        checkArgument(dZ.numCols() == Z.numCols());

        final SimpleMatrix ans = new SimpleMatrix(1, Z.numCols());
        for (int j = 0; j < Z.numCols(); j++) {
            ans.set(j, dZ.get(j) * Z.get(j) * (1 - Z.get(j)));
        }
        return ans;
    }

    /**
     * \u2202J/\u2202\u0251<sub>i,j</sub> = \u2202J/\u2202a<sub>j</sub> * x<sub>i</sub>
     *
     * @return
     */
    private SimpleMatrix derivativeAlpha(
            final SimpleMatrix dA, // 1 * D1
            final SimpleMatrix X // 1 * (M+1)
    ) {
        checkArgument(dA.numRows() == 1);
        checkArgument(X.numRows() == 1);
        final int M = X.numCols();
        final int D1 = dA.numCols();
        final SimpleMatrix ans = new SimpleMatrix(M, D1);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < D1; j++) {
                ans.set(i, j, dA.get(j) * X.get(i));
            }
        }
        return ans;
    }

    public static class BackwardComputationResult {
        private final SimpleMatrix dAlpha;
        private final SimpleMatrix dBeta;

        private BackwardComputationResult (
                final SimpleMatrix dAlpha,
                final SimpleMatrix dBeta
        ) {
            this.dAlpha = dAlpha;
            this.dBeta = dBeta;
        }

        public SimpleMatrix getdAlpha() {
            return dAlpha;
        }

        public SimpleMatrix getdBeta() {
            return dBeta;
        }
    }


}
