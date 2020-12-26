package com.gfu.ml.calculators;

import com.gfu.ml.data.DataSet;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class MutualInformation {

    private final Entropy e;
    private final ConditionalEntropy ce;

    public MutualInformation(
            final Entropy e,
            final ConditionalEntropy ce
    ) {
        this.e = e;
        this.ce = ce;
    }

    public double calculate(final DataSet dataSet, final int attrIdx) {
        for (int i = 0; i < dataSet.getRowsCount(); i++) {
            digest(dataSet.attrValue(i, attrIdx), dataSet.outValue(i));
        }
        return value();
    }

    public MutualInformation digest(final boolean attrValue, final boolean outValue) {
        e.digest(outValue);
        ce.digest(attrValue, outValue);
        return this;
    }

    public double value() {
        return e.value() - ce.value();
    }
}
