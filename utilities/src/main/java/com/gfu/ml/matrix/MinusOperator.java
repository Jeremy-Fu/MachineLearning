package com.gfu.ml.matrix;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class MinusOperator {

    public double[] apply(final double[] subtrahend, final double[] minuend) {
        checkArgument(subtrahend.length == minuend.length, "subtrahend and minuend vector must be the same");
        final int N = subtrahend.length;
        final double[] ans = new double[N];
        for (int i = 0; i < N; i++) {
            ans[i] = subtrahend[i] - minuend[i];
        }
        return ans;
    }
}
