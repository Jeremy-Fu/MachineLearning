package com.gfu.ml.serialization;

import com.gfu.ml.model.DTBinaryBranchNode;
import com.gfu.ml.model.DTBinaryLeaf;
import com.gfu.ml.model.DTBinaryNode;
import com.gfu.ml.model.DecisionTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DecisionTreeObjectMapperTest {

    @Test
    public void testDeserializeZeroLayer() {
        final DTBinaryNode root = new DTBinaryLeaf(true);
        final DecisionTree expected = new DecisionTree(root);
        assertEquals(expected, DecisionTreeObjectMapper.deserialize("leaf:true"));
    }

    @Test
    public void testDeserializeOneLayer() {
        final DTBinaryBranchNode root = new DTBinaryBranchNode("attr1");
        final DecisionTree expected = new DecisionTree(root);
        root.setNegative(new DTBinaryLeaf(true));
        final DTBinaryBranchNode pl2 = new DTBinaryBranchNode("attr2");
        root.setPositive(pl2);
        pl2.setNegative(new DTBinaryLeaf(true));
        pl2.setPositive(new DTBinaryLeaf(false));

        assertEquals(expected, DecisionTreeObjectMapper.deserialize("attr1,leaf:true,attr2,leaf:true,leaf:false"));
    }

    @Test
    public void testDeserializeInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            DecisionTreeObjectMapper.deserialize("attr1,leaf:true,attr2,leaf:true,leaf:false,leaf:false");
        });
    }

    @Test
    public void testDeserializeIllegalInputMultiFields() {
        assertThrows(IllegalArgumentException.class, () -> {
            DecisionTreeObjectMapper.deserialize("attr1,leaf:true:abc");
        });
    }

    @Test
    public void testDeserializeIllegalInputNotBoolean() {
        assertThrows(IllegalArgumentException.class, () -> {
            DecisionTreeObjectMapper.deserialize("attr1,leaf:abc");
        });
    }


}
