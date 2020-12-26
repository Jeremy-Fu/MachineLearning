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

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class DecisionTreeMain {

    private static final Logger logger = LogManager.getLogger(DecisionTreeMain.class);

    public static void main(final String[] args) {
        final String trainInput = args[0];
        final int maxDepth = Integer.parseInt(args[1]);
        final Parser parser = new Parser();
        try {
            final BufferedReader in = new BufferedReader(new FileReader(trainInput));
            final String header = in.readLine();
            final String[] attributes = parser.parseHeader(header);
            final Builder builder = DataSet.builder(attributes);
            String line;
            while ((line = in.readLine()) != null) {
                builder.addRow(
                        line,
                        parser.nyParser(),
                        parser.politicianParser());
            }
            final DataSet dataSet = builder.build();
            final Trainer trainer = new Trainer(new MutualInformation(new Entropy(), new ConditionalEntropy()), maxDepth);
            final DecisionTree dt = trainer.train(dataSet);
            logger.info("The decision tree is: " + DecisionTreeObjectMapper.serialize(dt));
        } catch (IOException e) {
            logger.error("IOException", e);
        } catch (Error e) {
            logger.error("Error",e);
        } catch (Exception e) {
            logger.error("Exception",e);
        }
    }

}
