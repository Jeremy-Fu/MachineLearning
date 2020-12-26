package com.gfu.ml.calculators;

import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.Test;

import static com.gfu.ml.calculators.Constants.DELTA;
import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class MutualInformationTest {

    private EasyMockSupport wrangler = new EasyMockSupport();

    @Test
    public void testMI() {
        final Entropy entropy = wrangler.createMock(Entropy.class);
        final ConditionalEntropy conEntropy = wrangler.createMock(ConditionalEntropy.class);
        final MutualInformation mutualInformation = new MutualInformation(entropy, conEntropy);

        expect(entropy.digest(anyBoolean())).andReturn(entropy).times(3);
        expect(conEntropy.digest(anyBoolean(), anyBoolean())).andReturn(conEntropy).times(3);
        expect(entropy.value()).andReturn(0.8);
        expect(conEntropy.value()).andReturn(0.2);

        wrangler.replayAll();
        mutualInformation.digest(true, true);
        mutualInformation.digest(true, true);
        mutualInformation.digest(true, true);

        assertEquals(0.6, mutualInformation.value(), DELTA);
        wrangler.verifyAll();
    }
}
