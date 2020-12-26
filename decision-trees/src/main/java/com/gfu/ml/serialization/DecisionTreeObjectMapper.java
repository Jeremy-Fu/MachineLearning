package com.gfu.ml.serialization;

import com.gfu.ml.model.DTBinaryLeaf;
import com.gfu.ml.model.DTBinaryBranchNode;
import com.gfu.ml.model.DTBinaryNode;
import com.gfu.ml.model.DecisionTree;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.lang.String.format;

/**
 *
 *          "attr1"
 *          |       |
 *       "attr2"  "false"
 *       |     |
 *    "true" "false"
 *
 *  Serialization
 * "attr1", "attr2", "leaf:true", "leaf:false", "leaf:false"
 *
 *
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DecisionTreeObjectMapper {

    private static final Joiner joiner = Joiner.on(",");

    public static DecisionTree deserialize(
            final String serialization
    ) {
        final String[] nodes = serialization.split(",");
        return new DecisionTree(deserialize(nodes));
    }

    public static String serialize(final DecisionTree tree) {
        final List<DTBinaryNode> visited = new ArrayList<>();
        traverse(tree.getRoot(), visited);
        return joiner.join(visited);

    }

    private static DTBinaryNode deserialize(final String[] nodes) {
        final Stack<DTBinaryBranchNode> stack = new Stack<>();
        if (nodes.length < 1) return null;
        final DTBinaryNode root = parse(nodes[0]);
        if ((root instanceof DTBinaryBranchNode)) {
            stack.push((DTBinaryBranchNode)root);
        }
        for (int i = 1; i < nodes.length; i++) {
            final DTBinaryNode dtnode = parse(nodes[i]);
            if (stack.peek().getNegative() == null) {
                stack.peek().setNegative(dtnode);
            } else if (stack.peek().getPositive() == null) {
                stack.peek().setPositive(dtnode);
            } else {
                throw new IllegalArgumentException(
                        format("The parent of %s is %s, " +
                                "and it has set positive and negative branch. " +
                                "The tree is invalid.",
                                nodes[i],
                                nodes[i/2]));
            }
            if (dtnode instanceof DTBinaryLeaf) {
                if (stack.peek().getPositive() != null && stack.peek().getNegative() != null) {
                    stack.pop();
                }
            } else {
                stack.push((DTBinaryBranchNode) dtnode);
            }
        }
        return root;
    }

    private static void traverse(final DTBinaryNode root, List<DTBinaryNode> visited) {
        if (root == null) {
            throw new Error("The root is null");
        }
        visited.add(root);
        if (root instanceof DTBinaryLeaf) {
            return;
        }
        traverse(root.getNegative(), visited);
        traverse(root.getPositive(), visited);
    }

    private static DTBinaryNode parse(final String node) {
        final String[] fields = node.split(":");
        if (fields.length == 2) {
            return new DTBinaryLeaf(Boolean.parseBoolean(fields[1]));
        } else if (fields.length == 1) {
            return new DTBinaryBranchNode(node);
        } else {
            throw new IllegalArgumentException(format("The node should be \"attribute\" or \"leaf:boolean\". But it was %s.", node));
        }
    }

}
