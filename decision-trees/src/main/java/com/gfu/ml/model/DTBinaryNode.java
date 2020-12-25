package com.gfu.ml.model;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public interface DTBinaryNode {

    DTBinaryNode getPositive();

    DTBinaryNode getNegative();

    String getAttribute();

    boolean getPrediction();
}
