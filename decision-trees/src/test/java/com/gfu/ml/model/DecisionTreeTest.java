package com.gfu.ml.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DecisionTreeTest {

    @Test
    public void testPredictPositiveRoot() {
        final DTBinaryLeaf root = new DTBinaryLeaf(true);
        final DecisionTree decisionTree = new DecisionTree(root);

        assertEquals(true, decisionTree.predict(new HashMap<>()));
    }

    @Test
    public void testPredictNegativeRoot() {
        final DTBinaryLeaf root = new DTBinaryLeaf(false);
        final DecisionTree decisionTree = new DecisionTree(root);

        assertEquals(false, decisionTree.predict(new HashMap<>()));
    }

    @Test
    public void testPredict2Layers() {
        final DTBinaryBranchNode root = new DTBinaryBranchNode("attr1");
        root.setNegative(new DTBinaryLeaf(true));
        final DTBinaryBranchNode attr2 = new DTBinaryBranchNode("attr2");
        root.setPositive(attr2);
        attr2.setPositive(new DTBinaryLeaf(true));
        attr2.setNegative(new DTBinaryLeaf(false));

        /*
         *              "attr1"
         *              |     |
         *             true  "attr2"
         *                   |      |
         *                  false  true
         */

        final DecisionTree decisionTree = new DecisionTree(root);

        final Map<String, Boolean> attributes = new HashMap<>();
        attributes.put("attr1", false);
        assertEquals(true, decisionTree.predict(attributes));

        attributes.put("attr1", true);
        attributes.put("attr2", false);
        assertEquals(false, decisionTree.predict(attributes));

        attributes.put("attr1", true);
        attributes.put("attr2", true);
        assertEquals(true, decisionTree.predict(attributes));
    }
}
