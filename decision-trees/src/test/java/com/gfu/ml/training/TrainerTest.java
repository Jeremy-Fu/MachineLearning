package com.gfu.ml.training;

import com.gfu.ml.criterion.InfoGain;
import com.gfu.ml.data.DataSet;
import com.gfu.ml.model.DecisionTree;
import com.gfu.ml.serialization.DecisionTreeObjectMapper;
import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class TrainerTest {

    private EasyMockSupport wrangler = new EasyMockSupport();

    @Test
    public void testTrainConverge() {
        final InfoGain infoGain = wrangler.createMock(InfoGain.class);
        final DataSet dataSet = wrangler.createMock(DataSet.class);

        expect(dataSet.attributesCount()).andReturn(3);
        expect(dataSet.isConverged()).andReturn(true);
        expect(dataSet.getMajority()).andReturn(true);
        wrangler.replayAll();

        final Trainer trainer = new Trainer(infoGain, 3);
        final DecisionTree dt = trainer.train(dataSet);
        final DecisionTree expected = DecisionTreeObjectMapper.deserialize("leaf:true");
        assertEquals(expected, dt, "The decision tree should only have 0 layer");
        wrangler.verifyAll();
    }

    @Test
    public void testTrain0Layer() {
        final InfoGain infoGain = wrangler.createMock(InfoGain.class);
        final DataSet dataSet = wrangler.createMock(DataSet.class);

        expect(dataSet.isConverged()).andReturn(false);
        expect(dataSet.attributesCount()).andReturn(10);
        expect(dataSet.getMajority()).andReturn(false);
        wrangler.replayAll();

        final Trainer trainer = new Trainer(infoGain, 0);
        final DecisionTree dt = trainer.train(dataSet);
        final DecisionTree expected = DecisionTreeObjectMapper.deserialize("leaf:false");
        assertEquals(expected, dt, "The decision tree should only have 0 layer");
        wrangler.verifyAll();
    }

    @Test
    public void testTrain1Attribute() {
        final InfoGain infoGain = wrangler.createMock(InfoGain.class);
        final DataSet dataSet = wrangler.createMock(DataSet.class);

        expect(dataSet.attributesCount()).andReturn(1);
        expect(dataSet.isConverged()).andReturn(false);

        expect(dataSet.getMajority()).andReturn(false);
        wrangler.replayAll();

        final Trainer trainer = new Trainer(infoGain, 0);
        final DecisionTree dt = trainer.train(dataSet);
        final DecisionTree expected = DecisionTreeObjectMapper.deserialize("leaf:false");
        assertEquals(expected, dt, "The decision tree should only have 0 layer");
        wrangler.verifyAll();
    }

    @Test
    public void testTrainOneLayer() {
        // "attr2,leaf:false,leaf:true"
        final InfoGain infoGain = wrangler.createMock(InfoGain.class);
        final DataSet dataSet = wrangler.createMock(DataSet.class);

        expect(dataSet.isConverged()).andReturn(false);
        expect(dataSet.attributesCount()).andReturn(3);

        expect(infoGain.calculate(anyObject(DataSet.class), anyInt())).andReturn(0.1);
        expect(infoGain.calculate(anyObject(DataSet.class), anyInt())).andReturn(0.3);
        expect(infoGain.calculate(anyObject(DataSet.class), anyInt())).andReturn(0.2);

        expect(dataSet.getAttribute(eq(1))).andReturn("attr2");

        final DataSet nDataSet = wrangler.createMock(DataSet.class);
        final DataSet pDataSet = wrangler.createMock(DataSet.class);
        expect(dataSet.split(eq(1))).andReturn(new DataSet[]{nDataSet, pDataSet});

        expect(nDataSet.attributesCount()).andReturn(2);
        expect(nDataSet.isConverged()).andReturn(true);
        expect(nDataSet.getMajority()).andReturn(false);

        expect(pDataSet.attributesCount()).andReturn(2);
        expect(pDataSet.isConverged()).andReturn(true);
        expect(pDataSet.getMajority()).andReturn(true);

        final Trainer trainer = new Trainer(infoGain, 3);
        wrangler.replayAll();
        final DecisionTree dt = trainer.train(dataSet);
        assertEquals(2, dt.getDepth(), "The depth of decision tree should be 2");
        final DecisionTree expected = DecisionTreeObjectMapper.deserialize("attr2,leaf:false,leaf:true");
        assertEquals(expected, dt);
        wrangler.verifyAll();
    }
}
