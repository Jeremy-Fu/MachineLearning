package com.gfu.ml.linear.app.train;

import com.gfu.ml.data.MatrixDataSet;
import com.gfu.ml.linear.app.feagures.IOHelper;
import com.gfu.ml.linear.learning.StochasticGradientDescent;
import com.gfu.ml.loss.LogisticRegressionGradient;
import com.gfu.ml.matrix.AddOperator;
import com.gfu.ml.matrix.MinusOperator;
import com.gfu.ml.matrix.MultiplyOperator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
@Configuration
public class LogisticRegressionConfiguration {

    @Bean(name="train")
    public MatrixDataSet trainMatrixDataSet (
            @Qualifier("featuresCount") final int M,
            @Value("${formattedTrainInput}") final String formattedInput
    ) throws IOException {
        return IOHelper.loadFormattedFeatures(formattedInput, M);
    }

    @Bean(name="test")
    public MatrixDataSet testMatrixDataSet (
            @Qualifier("featuresCount") final int M,
            @Value("${formattedTestInput}") final String formattedInput
    ) throws IOException {
        return IOHelper.loadFormattedFeatures(formattedInput, M);
    }

    @Bean
    public int featuresCount(
            @Value("${dictionaryInput}") final String dictionaryInputFile
    ) throws IOException {
        return IOHelper.featuresCount(dictionaryInputFile);
    }

    @Bean
    public StochasticGradientDescent gradientDescent(
            @Qualifier("train") final MatrixDataSet examples,
            final MultiplyOperator multiply,
            final LogisticRegressionGradient gradient,
            final MinusOperator minus,
            @Value("${numEpoch}") final int numEpoch
    ) {
        return new StochasticGradientDescent(examples, 0.1, gradient, minus, multiply, numEpoch);
    }

    @Bean
    public MultiplyOperator multiply() {
        return new MultiplyOperator();
    }

    @Bean
    public AddOperator add() {
        return new AddOperator();
    }

    @Bean
    public MinusOperator minus() {
        return new MinusOperator();
    }

    @Bean
    public LogisticRegressionGradient gradient(
            final MultiplyOperator multiply,
            final AddOperator add
    ) {
        return new LogisticRegressionGradient(multiply, add);
    }
}
