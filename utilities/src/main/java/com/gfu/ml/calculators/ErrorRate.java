package com.gfu.ml.calculators;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class ErrorRate {

    private final int expectation;
    private int accurate;
    private int errors;

    public ErrorRate(final int expectation) {
        this.expectation = expectation;
    }

    public ErrorRate digest(final int label) {
        if (label == expectation) {
            accurate++;
        } else {
            errors++;
        }
        return this;
    }

    public double getErrorRate() {
        return ((double) errors) / (double)(accurate + errors);
    }

}
