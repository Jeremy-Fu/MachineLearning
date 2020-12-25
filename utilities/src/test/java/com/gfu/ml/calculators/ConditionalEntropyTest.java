package com.gfu.ml.calculators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class ConditionalEntropyTest {

    @Test
    public void testConditionalEntropy() {
        final ConditionalEntropy tester = new ConditionalEntropy();
        final boolean[][] matrix = {
                {false, false},
                {true, false},
                {false, true},
                {true, false},
                {true, true},
                {false, false},
                {false, true},
                {true, false},
                {false, false},
                {true, false}
        };

        for (int i = 0; i < matrix.length; i++) {
            tester.digest(matrix[i][0], matrix[i][1]);
        }
        assertTrue(Math.abs(0.84643934467 - tester.value()) < 0.00000000001);
    }

}
