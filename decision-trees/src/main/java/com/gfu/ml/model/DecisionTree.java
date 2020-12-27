package com.gfu.ml.model;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

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

    public boolean predict(final String[] attrNames, final boolean[] attrValues) {
        checkArgument(
                attrNames.length == attrValues.length,
                "The length attrNames must equal to the length of attrValues.");
        final Map<String, Boolean>  map = new HashMap<>();
        for (int i = 0; i < attrNames.length; i++) {
            map.put(attrNames[i], attrValues[i]);
        }
        return search(map, root);
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
