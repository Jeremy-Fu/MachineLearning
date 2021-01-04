package com.gfu.ml.app;

import com.gfu.ml.nns.IOHelper;
import com.gfu.ml.nns.IOHelper.DataWrapper;
import org.ejml.simple.SimpleMatrix;
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
    public TrainTask trainTask(
            @Qualifier("train") final DataWrapper trainData,
            @Value("${num_epoch}") final int numEpoch,
            @Value("${hidden_units}") final int hiddenUnits,
            @Value("${init_flag}") final int initFlag
    ) {
        return new TrainTask(trainData.getFeatures(), trainData.getLabels(), hiddenUnits, numEpoch, initFlag);
    }

}
