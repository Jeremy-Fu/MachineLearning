package com.gfu.ml.model;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DTBinaryLeaf implements DTBinaryNode {

    private final boolean prediction;

    public DTBinaryLeaf(final boolean prediction) {
        this.prediction = prediction;
    }

    @Override
    public DTBinaryLeaf getNegative() {
        throw new UnsupportedOperationException("There is no negative branch on leaf node.");
    }

    @Override
    public String getAttribute() {
        throw new UnsupportedOperationException("There is no attribute");
    }

    @Override
    public boolean getPrediction() {
        return this.prediction;
    }

    @Override
    public DTBinaryLeaf getPositive() {
        throw new UnsupportedOperationException("There is no negative branch on leaf node.");
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof DTBinaryLeaf)) {
            return false;
        }
        final DTBinaryLeaf that = (DTBinaryLeaf) object;
        return this.prediction == that.prediction;
    }

}
