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

    public void compute(
            final SimpleMatrix ystar,
            final SimpleMatrix yhat
    ) {
        checkArgument(1 == ystar.numRows());
        checkArgument(1 == yhat.numRows());
        checkArgument(ystar.numCols() == yhat.numCols());
        final SimpleMatrix dYhat = derivativeYhat(ystar, yhat);
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


}
