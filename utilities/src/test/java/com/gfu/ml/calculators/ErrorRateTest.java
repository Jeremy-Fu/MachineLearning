package com.gfu.ml.calculators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class ErrorRateTest {

    @Test
    public void testGetErrorRate() {
        final int accurate = 15;
        final int errors = 13;
        final ErrorRate errorRate = new ErrorRate(1);
        for (int i = 0; i < accurate; i++) {
            errorRate.digest(1);
        }
        for (int i = 0; i < errors; i++) {
            errorRate.digest(0);
        }

        assertTrue(Math.abs(errorRate.getErrorRate() - 0.464285714286) < 0.000000000001);
    }
}
