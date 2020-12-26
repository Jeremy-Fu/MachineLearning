package com.gfu.ml.training;

import com.gfu.ml.calculators.MutualInformation;
import com.gfu.ml.data.DataSet;
import com.gfu.ml.model.DTBinaryLeaf;
import com.gfu.ml.model.DTBinaryBranchNode;
import com.gfu.ml.model.DTBinaryNode;
import com.gfu.ml.model.DecisionTree;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class Trainer {

    private final MutualInformation calculator;
    private final int maxDepth;

    public Trainer(
            final MutualInformation calculator,
            final int maxDepth
    ) {
        this.calculator = calculator;
        this.maxDepth = maxDepth;
    }

    public DecisionTree train(final DataSet dataSet) {
        final DTBinaryNode root = build(dataSet, 0);
        final DecisionTree tree = new DecisionTree(root);
        return tree;
    }

    private DTBinaryNode build(
            final DataSet dataSet,
            final int depth
    ) {
        final int attributesCount = dataSet.attributesCount();
        if (dataSet.isConverged() || attributesCount == 1 || depth == maxDepth) {
            final DTBinaryLeaf leaf = new DTBinaryLeaf(dataSet.getMajority());
            return leaf;
        }

        double maxInfoGain = 0;
        int maxInfoGainIdx = -1; // The index of attribute of which infoGain is the highest;
        for (int column = 0; column < attributesCount; column++) {
            double infoGain = calculator.calculate(dataSet, column);
            if (infoGain > maxInfoGain) {
                maxInfoGain = infoGain;
                maxInfoGainIdx = column;
            }
        }

        final String attributeName = dataSet.getAttribute(maxInfoGainIdx);
        final DTBinaryBranchNode node = new DTBinaryBranchNode(attributeName);
        final DataSet[] subsets = dataSet.split(maxInfoGainIdx);
        final DTBinaryNode nNode = build(subsets[0], depth+1);
        node.setNegative(nNode);
        final DTBinaryNode pNode = build(subsets[1], depth+1);
        node.setPositive(pNode);
        return node;
    }

}
