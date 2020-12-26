package com.gfu.ml.model;

import java.util.Map;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DecisionTree {

    private final DTBinaryNode root;

    public DecisionTree (final DTBinaryNode root) {
        this.root = root;
    }

    public DTBinaryNode getRoot() {
        return root;
    }

    public boolean predict(final Map<String, Boolean> attributes) {
        return search(attributes, root);
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof DecisionTree)) {
            return false;
        }

        final DecisionTree that = (DecisionTree) object;
        return this.root.equals(that.root);
    }

    private boolean search(final Map<String, Boolean> attributes, final DTBinaryNode curr) {
        if (curr instanceof DTBinaryLeaf) {
            return curr.getPrediction();
        }
        final String attribute = curr.getAttribute();
        if (attributes.get(attribute)) {
            return search(attributes, curr.getPositive());
        } else {
            return search(attributes, curr.getNegative());
        }
    }

}
