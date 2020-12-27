package com.gfu.ml.application;

import com.gfu.ml.calculators.ConditionalEntropy;
import com.gfu.ml.calculators.Entropy;
import com.gfu.ml.calculators.MutualInformation;
import com.gfu.ml.data.DataSet;
import com.gfu.ml.data.DataSet.Builder;
import com.gfu.ml.model.DecisionTree;
import com.gfu.ml.serialization.DecisionTreeObjectMapper;
import com.gfu.ml.training.Trainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.String.format;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DecisionTreeMain {

    private static final Logger logger = LogManager.getLogger(DecisionTreeMain.class);

    public static void main(final String[] args) {
        final String trainInput = args[0];
        final String testInput = args[1];
        final int maxDepth = Integer.parseInt(args[2]);

        try {
            final DataSet trainDataSet = load(trainInput);
            final Trainer trainer = new Trainer(new MutualInformation(new Entropy(), new ConditionalEntropy()), maxDepth);
            final DecisionTree dt = trainer.train(trainDataSet);
            logger.info("The decision tree is: " + DecisionTreeObjectMapper.serialize(dt));

            final DecisionTreeTask task = new DecisionTreeTask();
            final boolean[] trainPredictions = task.predictDataSet(trainDataSet, dt);
            final double trainErrRate = task.calculateErrorRate(trainDataSet.outValues(), trainPredictions);
            logger.info(format("error(test): %.6f", trainErrRate));

            final DataSet testDataSet = load(testInput);

            final boolean[] testPredictions = task.predictDataSet(testDataSet, dt);
            final double testErrRate = task.calculateErrorRate(testDataSet.outValues(), testPredictions);
            logger.info(format("error(test): %.6f", testErrRate));
        } catch (IOException e) {
            logger.error("IOException", e);
        } catch (Error e) {
            logger.error("Error",e);
        } catch (Exception e) {
            logger.error("Exception",e);
        }
    }

    private static DataSet load(final String input) throws IOException {
        final Parser parser = new Parser();
        final BufferedReader in = new BufferedReader(new FileReader(input));
        final String header = in.readLine();
        final String[] attributes = parser.parseHeader(header);
        final Builder dataSetBuilder = DataSet.builder(attributes);
        String line;
        while ((line = in.readLine()) != null) {
            dataSetBuilder.addRow(
                    line,
                    parser.nyParser(),
                    parser.politicianParser());
        }
        return dataSetBuilder.build();
    }

}
