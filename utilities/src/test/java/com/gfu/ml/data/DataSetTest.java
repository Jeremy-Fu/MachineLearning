package com.gfu.ml.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DataSetTest {
    @Test
    public void testSplitAttribute() {
        final String[] attributesNames = {"attr1", "attr2", "attr3", "attr4", "attr5"};
        final boolean[][] vectors = {
                {true,  true,   true,   true,   false,  true},
                {true,  true,   false,  true,   true,   true},
                {true,  false,  true,   false,  false,  true},
                {true,  false,  false,  true,   false,  true},
                {false, true,   true,   false,  false,  true},
                {false, true,   false,  true,   true,   false},
                {false, false,  true,   false,  false,  false},
                {false, false,  false,  true,   false,  true}
        };
        final DataSet dataSet = new DataSet(vectors, attributesNames);

        /**
         *  L1 test
         */
        final DataSet[] subsetsL0 = dataSet.split(2);

        final boolean[][] expectedMatrixL1S0 = {
                {true,  true,  true,   true,   true},
                {true,  false,  true,   false,  true},
                {false, true,  true,   true,   false},
                {false, false,  true,   false,  true}
        };
        final String[] expectedAttributeNamesL1S0 = {"attr1", "attr2", "attr4", "attr5"};
        verify(expectedMatrixL1S0, expectedAttributeNamesL1S0, subsetsL0[0]);

        final boolean[][] expectedMatrixL1S1 = {
                {true,  true,   true,   false,  true},
                {true,  false,  false,  false,  true},
                {false, true,   false,  false,  true},
                {false, false,  false,  false,  false},
        };
        final String[] expectedAttributeNamesL1S1 = {"attr1", "attr2", "attr4", "attr5"};
        verify(expectedMatrixL1S1, expectedAttributeNamesL1S1, subsetsL0[1]);

        /**
         * L2 test
         */
        final DataSet datasetL1S0 = subsetsL0[0];
        final DataSet[] subsetsL2S0 = datasetL1S0.split(0);
        final boolean[][] expectedMatrixL2S0 = {
                {true,  true,   true,   false},
                {false,  true,   false,  true}
        };
        final String[] expectedAttributeNamesL2S0 = {"attr2", "attr4", "attr5"};
        verify(expectedMatrixL2S0, expectedAttributeNamesL2S0, subsetsL2S0[0]);
        assertFalse(subsetsL2S0[0].isConverged());
        assertFalse(subsetsL2S0[0].getMajority());

        final boolean[][] expectedMatrixL2S1 = {
                {true,   true,   true,  true},
                {false,  true,   false, true}
        };
        final String[] expectedAttributeNamesL2S1 = {"attr2", "attr4", "attr5"};
        verify(expectedMatrixL2S1, expectedAttributeNamesL2S1, subsetsL2S0[1]);
        assertTrue(subsetsL2S0[1].isConverged());
        assertTrue(subsetsL2S0[1].getMajority());

        final DataSet datasetL1S1 = subsetsL0[1];
        final DataSet[] subsetsL2S1 = datasetL1S1.split(3);

        final boolean[][] expectedMatrixL2S2 = {
                {true,  true,   true,  true},
                {true,  false,  false,  true},
                {false, true,   false,  true},
                {false, false,  false,  false},
        };
        final String[] expectedAttributeNamesL2S2 = {"attr1", "attr2", "attr4"};
        verify(expectedMatrixL2S2, expectedAttributeNamesL2S2, subsetsL2S1[0]);
        assertFalse(subsetsL2S1[0].isConverged());
        assertTrue(subsetsL2S1[0].getMajority());

        final boolean[][] expectedMatrixL2S3 = {};
        final String[] expectedAttributeNamesL2S3 = {"attr1", "attr2", "attr4"};
        verify(expectedMatrixL2S3, expectedAttributeNamesL2S3, subsetsL2S1[1]);
        assertEquals(0, subsetsL2S1[1].getRowsCount());
        assertEquals(3, subsetsL2S1[1].getAttributesCount());


    }


    private void verify(
            final boolean[][] expectedMatrix,
            final String[] expectedAttributeNames,
            final DataSet actual
    ) {
        for (int i = 0; i < expectedMatrix.length; i++) {
            for (int j = 0; j < expectedMatrix[0].length-1; j++) {
                assertEquals(expectedMatrix[i][j], actual.attrValue(i, j));
            }
        }

        for (int i = 0; i< expectedMatrix.length; i++) {
            final int j = expectedMatrix[0].length-1;
            assertEquals(expectedMatrix[i][j], actual.outValue(i));
        }

        for (int j = 0; j < expectedAttributeNames.length; j++) {
            assertEquals(expectedAttributeNames[j], actual.getAttribute(j));
        }
    }
}
