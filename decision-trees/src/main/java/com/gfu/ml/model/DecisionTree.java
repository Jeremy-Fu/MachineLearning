package com.gfu.ml.model;

import static java.lang.Math.max;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DecisionTree {

    private final DTBinaryNode root;

    public DecisionTree (final DTBinaryNode root) {
        this.root = root;
    }

    public int getDepth() {
        return getDepth(root);
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof DecisionTree)) {
            return false;
        }

        final DecisionTree that = (DecisionTree) object;
        return this.root.equals(that.root);
    }

    private int getDepth(DTBinaryNode node) {
        if (node instanceof DTBinaryLeaf) {
            return 1;
        }
        return 1 + max(getDepth(node.getNegative()), getDepth(node.getPositive()));
    }

}
