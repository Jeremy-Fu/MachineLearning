package com.gfu.ml.app;

import com.gfu.ml.nns.BackwardComputation;
import com.gfu.ml.nns.ForwardComputation;
import com.gfu.ml.utilities.IOHelper;
import com.gfu.ml.utilities.IOHelper.DataWrapper;
import com.gfu.ml.sgd.SGDOptimization;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
@Configuration
public class LettersRecognitionConfiguration {

    @Bean(name = "train")
    public DataWrapper trainData(
            @Value("${input_dir}/train.csv") final String input
    ) throws IOException {
        return IOHelper.loadData(input);
    }

    @Bean(name = "test")
    public DataWrapper testData(
            @Value("${input_dir}/test.csv") final String input
    ) throws IOException {
        return IOHelper.loadData(input);
    }

    @Bean
    public SGDOptimization trainTask(
            @Qualifier("train") final DataWrapper trainData,
            @Value("${num_epoch}") final int numEpoch,
            @Value("${hidden_units}") final int hiddenUnits,
            @Value("${init_flag}") final int initFlag,
            @Value("${learning_rate}") final double eta,
            final ForwardComputation forwardComputation,
            final BackwardComputation backwardComputation
    ) {
        return new SGDOptimization(
                trainData.getFeatures(),
                trainData.getLabels(),
                hiddenUnits,
                numEpoch,
                initFlag,
                eta,
                forwardComputation,
                backwardComputation);
    }

    @Bean
    public ForwardComputation forwardComputation(
            @Value("${sanity}") final boolean sanity
    ) {
        return new ForwardComputation(sanity);
    }

    @Bean
    public BackwardComputation backwardComputation(
            @Value("${sanity}") final boolean sanity
    ) {
        return new BackwardComputation(sanity);
    }

}
