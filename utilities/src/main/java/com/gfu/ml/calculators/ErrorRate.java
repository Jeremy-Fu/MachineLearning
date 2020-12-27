package com.gfu.ml.calculators;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class ErrorRate {

    private int count;
    private int errors;

    public ErrorRate digest(final boolean expectation, final boolean prediction) {
        if (expectation != prediction) errors++;
        count++;
        return this;
    }

    public double getErrorRate() {
        return ((double) errors) / (double)(count);
    }

}
