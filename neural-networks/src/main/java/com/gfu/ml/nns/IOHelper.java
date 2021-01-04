package com.gfu.ml.nns;

import org.ejml.data.DMatrix;
import org.ejml.ops.ReadMatrixCsv;
import org.ejml.simple.SimpleMatrix;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
@Configuration
public class IOHelper {

    private static final int M = 128;

    public static DataWrapper loadData(final String input) throws IOException {
        final BufferedReader in = new BufferedReader(new FileReader(input));
        final List<Double> labels = new ArrayList<>();
        final List<Double[]> features = new ArrayList<>();
        String line = null;
        while ((line = in.readLine()) != null) {
            final String[] fields = line.split(",");
            // process column[0] - labels
            labels.add(Double.valueOf(fields[0]));

            // process column[1-128] - features (pixels)
            final Double[] example = new Double[M];
            for (int i = 0; i < example.length; i++) {
                example[i] = Double.parseDouble(fields[i+1]);
            }
            features.add(example);
        }
        in.close();

        final SimpleMatrix labelMatrix = new SimpleMatrix(labels.size(), 1);
        for (int j = 0; j < labels.size(); j++) {
            labelMatrix.set(j, 0, labels.get(j));
        }

        final SimpleMatrix featureMatrix = new SimpleMatrix(features.size(), M);
        for (int j = 0; j < features.size(); j++) {
            final Double[] example = features.get(j);
            for (int i = 0; i < M; i++) {
                featureMatrix.set(j, i, example[i]);
            }
        }

        return new DataWrapper(labelMatrix, featureMatrix);
    }

    public static class DataWrapper {
        private final SimpleMatrix labels;
        private final SimpleMatrix features;

        private DataWrapper(final SimpleMatrix labels, final SimpleMatrix features) {
            this.labels = labels;
            this.features = features;
        }

        public SimpleMatrix getLabels() {
            return this.labels;
        }

        public SimpleMatrix getFeatures() {
            return this.features;
        }
    }

}
