package com.gfu.ml.model;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DTBinaryBranchNode implements DTBinaryNode {

    private DTBinaryNode negative;
    private DTBinaryNode positive;
    private final String attribute;

    public DTBinaryBranchNode(final String attribute) {
        this.attribute = attribute;
    }

    public void setNegative(final DTBinaryNode negative) {
        checkState(this.negative == null, "negative branch has been set.");
        this.negative = negative;
    }

    @Override
    public DTBinaryNode getNegative() {
        return negative;
    }

    public void setPositive(final DTBinaryNode positive) {
        checkState(this.positive == null, "positive branch has been set.");
        this.positive = positive;
    }

    @Override
    public DTBinaryNode getPositive() {
        return positive;
    }

    @Override
    public String getAttribute() {
        return this.attribute;
    }

    @Override
    public boolean getPrediction() {
        throw new UnsupportedOperationException("There is no prediction on a branch node.");
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof DTBinaryBranchNode)) {
            return false;
        }
        final DTBinaryBranchNode that = (DTBinaryBranchNode) object;
        if (!this.attribute.equals(that.attribute)) {
            return false;
        }
        return this.negative.equals(that.negative) && this.positive.equals(that.positive);
    }
}
