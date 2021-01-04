package com.gfu.ml.matrix;

import com.gfu.ml.data.Pair;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class MultiplyOperator {

    public double[] apply(final double[] X, final double C) {
        final int N = X.length;
        final double[] ans = new double[N];
        for (int i = 0; i < N; i++) {
            ans[i] = X[i] * C;
        }
        return ans;
    }

    public double apply(final double[] X, final List<Pair<Integer, Integer>> Y) {
        double ans = 0;
        for (final Pair<Integer, Integer> pair : Y) {
            ans += X[pair.getFirst()] * pair.getSecond();
        }
        return ans;
    }

    public double apply(final double[] X, final double[] Y) {
        checkArgument(X.length == Y.length, "theta and vector does not have the same length");
        final int N = X.length;
        double ans = 0;
        for (int i = 0; i < N; i++) {
            ans += X[i] * Y[i];
        }
        return ans;
    }

}
