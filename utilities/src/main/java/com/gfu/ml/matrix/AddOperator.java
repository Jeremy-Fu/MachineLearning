package com.gfu.ml.matrix;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class AddOperator {

    public double[] apply(final double[] X, final double Y) {
        final int N = X.length;
        final double[] ans = new double[N];
        for (int i = 0; i < N; i++) {
            ans[i] = X[i] + Y;
        }
        return ans;
    }

    public double[] apply(final double[] X, final double epsilon, final int i) {
        final int N = X.length;
        final double[] ans = Arrays.copyOf(X, N);
        ans[i] += epsilon;
        return ans;
    }

}
