package com.gfu.ml.calculators;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class ConditionalEntropy {

    private final Counter counter = new Counter();

    public ConditionalEntropy digest(
            final boolean attributeValue,
            final boolean outputValue
    ) {
        counter.digest(attributeValue, outputValue);
        return this;
    }

    public double value() {
        final int cx0 = counter.getCount(false, false) + counter.getCount(false, true);
        final int cx1 = counter.getCount(true, false) + counter.getCount(true, true);

        final double px0 = (double)cx0 / (double)(cx0 + cx1);
        final double px1 = (double)cx1 / (double)(cx0 + cx1);

        return px0 * scEntropy(false) + px1 * scEntropy(true);
    }

    /**
     * Special conditional entropy
     * @param attributeValue
     * @return
     */
    private double scEntropy(boolean attributeValue) {
        final int c0 = counter.getCount(attributeValue, false); // C[Y=0|X=x]
        final int c1 = counter.getCount(attributeValue, true); // C[Y=0|X=x]

        final double p0 = (double)c0 / (double)(c0 + c1); // C[Y=0|X=x]
        final double p1 = (double)c1 / (double)(c0 + c1); // C[Y=1|X=x]

        double scEntropy = 0;
        scEntropy -= p0 * Math.log(p0) / Math.log(2);
        scEntropy -= p1 * Math.log(p1) / Math.log(2);
        return scEntropy;
    }

    private static class Counter {

        private final Pair nAttr = new Pair();
        private final Pair pAttr = new Pair();

        public void digest(final boolean attrValue, final boolean outputValue) {
            final Pair pair;
            if (attrValue) {
                pair = pAttr;
            } else {
                pair = nAttr;
            }
            if (outputValue) {
                pair.pOutput++;
            } else {
                pair.nOutput++;
            }
        }

        public int getCount(final boolean attrValue, final boolean outputValue) {
            final Pair pair;
            if (attrValue) {
                pair = pAttr;
            } else {
                pair = nAttr;
            }
            if (outputValue) {
                return pair.pOutput;
            } else {
                return pair.nOutput;
            }
        }
    }

    private static class Pair {
        private int nOutput;
        private int pOutput;
    }
}
