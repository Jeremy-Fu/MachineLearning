package com.gfu.ml.calculators;

import org.junit.jupiter.api.Test;

import static com.gfu.ml.calculators.Constants.DELTA;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class EntropyTest {

    @Test
    public void testGetEntropy() {
        final Entropy calculator = new Entropy();
        final int democrats = 15;
        final int republicans = 13;
        for (int i = 0; i < democrats; i++) {
            calculator.digest(false);
        }
        for (int i = 0; i < republicans; i++) {
            calculator.digest(true);
        }
        assertEquals( 0.996316519559, calculator.value(), DELTA);
    }
}
