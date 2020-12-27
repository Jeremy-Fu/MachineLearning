package com.gfu.ml.application;

import com.gfu.ml.calculators.ErrorRate;
import com.gfu.ml.data.DataSet;
import com.gfu.ml.model.DecisionTree;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DecisionTreeTask {

    private DecisionTree decisionTree;

    public boolean[] predictDataSet(final DataSet dataSet, final DecisionTree tree) {

        final List<Boolean> predictedLabels = new ArrayList<>();

        for (int i = 0; i < dataSet.getRowsCount(); i++) {
            predictedLabels.add(tree.predict(dataSet.attrNames(), dataSet.attrValues(i)));
        }
        return cast(predictedLabels);
    }

    public double calculateErrorRate(final boolean[] testLabels, final boolean[] predictions) {
        checkArgument(testLabels.length == predictions.length);
        final ErrorRate err = new ErrorRate();
        for (int i = 0; i < testLabels.length; i++) {
            err.digest(testLabels[i], predictions[i]);
        }
        return err.getErrorRate();
    }

    private boolean[] cast(final List<Boolean> list) {
        final boolean[] array = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }
}
