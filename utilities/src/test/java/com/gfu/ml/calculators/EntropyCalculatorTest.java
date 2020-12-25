package com.gfu.ml.calculators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class EntropyCalculatorTest {

    @Test
    public void testGetEntropy() {
        final EntropyCalculator calculator = new EntropyCalculator();
        final int democrats = 15;
        final int republicans = 13;
        for (int i = 0; i < democrats; i++) {
            calculator.digest(false);
        }
        for (int i = 0; i < republicans; i++) {
            calculator.digest(true);
        }
        assertTrue(Math.abs(calculator.getEntropy() - 0.996316519559) < 0.0000000000001);
    }
}
