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
public class BackwardComputation {

    private static final Logger logger = LogManager.getLogger(BackwardComputation.class);

    private final boolean sanity;

    public BackwardComputation(final boolean sanity) {
        this.sanity = sanity;
        logger.info(format("BackwardComputation has %s sanity check.", sanity ? "enabled" : "disabled"));
    }

    public void compute(
            final SimpleMatrix ystar,
            final SimpleMatrix yhat
    ) {
        checkArgument(1 == ystar.numRows());
        checkArgument(1 == yhat.numRows());
        checkArgument(ystar.numCols() == yhat.numCols());
        final SimpleMatrix d_yhat = yhat.invert().transpose().mult(ystar).diag();
        if (sanity) checkDYhat(d_yhat, ystar, yhat);
    }

    private void checkDYhat(
            final SimpleMatrix actual,
            final SimpleMatrix ystar,
            final SimpleMatrix yhat
    ) {
        for (int j = 0; j < ystar.numCols(); j++) {
            final double expected = ystar.get(0, j) / yhat.get(0, j);
            checkState(expected == actual.get(0, j));
        }
    }


}
