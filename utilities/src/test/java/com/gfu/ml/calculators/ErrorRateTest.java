package com.gfu.ml.calculators;

import org.junit.jupiter.api.Test;

import static com.gfu.ml.calculators.Constants.DELTA;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class ErrorRateTest {

    @Test
    public void testGetErrorRate() {
        final int accurate = 15;
        final int errors = 13;
        final ErrorRate errorRate = new ErrorRate();
        for (int i = 0; i < accurate; i++) {
            errorRate.digest(true, true);
        }
        for (int i = 0; i < errors; i++) {
            errorRate.digest(true, false);
        }

        assertEquals(0.464285714286, errorRate.getErrorRate(), DELTA);
    }
}
