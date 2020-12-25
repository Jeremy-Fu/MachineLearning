package com.gfu.ml.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DataSetTest {

    @Test
    public void testSplitAttribute0() {
        final String[] attributesNames = {"attr1", "attr2"};
        final boolean[][] matrix = {
                {true, true, true}, {true, true, false}, {true, false, true}, {true, false, false},
                {false, true, true}, {false, true, false}, {false, false, true}, {false, false, false}
        };
        final DataSet dataSet = new DataSet(matrix, attributesNames);

        final DataSet[] subSets = dataSet.split(0);
        final boolean[][] expectedSub = {
                {true, true}, {true, false}, {false, true}, {false, false}
        };
        assertTrue(compareMatrix(expectedSub, subSets[0].getData()));
        assertTrue(compareMatrix(expectedSub, subSets[1].getData()));
    }

    @Test
    public void testSplitAttribute1() {
        final String[] attributesNames = {"attr1", "attr2"};
        final boolean[][] vectors = {
                {true, true, true}, {true, true, false}, {true, false, true}, {true, false, false},
                {false, true, true}, {false, true, false}, {false, false, true}, {false, false, false}
        };
        final int[] rows = new int[] {0, 1, 2, 3, 4, 5, 6, 7};
        final DataSet dataSet = new DataSet(vectors, attributesNames);

        final DataSet[] subSets = dataSet.split(1);
        final boolean[][] expectedSub = new boolean[][] {
                {true, true}, {true, false}, {false, true}, {false, false}
        };
        assertTrue(compareMatrix(expectedSub, subSets[0].getData()));
        assertTrue(compareMatrix(expectedSub, subSets[1].getData()));
    }


    private boolean compareMatrix(final boolean[][] expected, final boolean[][] actual) {
        if (expected.length != actual.length) {
            return false;
        }
        if (expected[0].length != actual[0].length) {
            return false;
        }
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[0].length; j++) {
                if (expected[i][j] != actual[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
