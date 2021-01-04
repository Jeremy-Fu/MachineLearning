package com.gfu.ml.linear.app.train;

import com.gfu.ml.calculators.ErrorRate;
import com.gfu.ml.data.MatrixDataSet;
import com.gfu.ml.linear.learning.StochasticGradientDescent;
import com.gfu.ml.linear.prediction.LogisticRegressionPrediction;
import com.gfu.ml.matrix.MultiplyOperator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

import static java.lang.String.format;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class TrainMain {

    private static final Logger logger = LogManager.getLogger(TrainMain.class);
    private static final HelpFormatter formatter = new HelpFormatter();

    public static void main(final String[] args) {
        final Options opts = getOptions();
        try {
            final CommandLine cli = getCli(args, opts);
            final Properties props = getProperties(cli);

            final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            final ConfigurableEnvironment environment = context.getEnvironment();
            final MutablePropertySources propertySources = environment.getPropertySources();

            final PropertiesPropertySource source = new PropertiesPropertySource("cmd-arguments", props);
            propertySources.addFirst(source);

            context.register(LogisticRegressionConfiguration.class);
            context.refresh();

            final StochasticGradientDescent gradientDescent = context.getBean(StochasticGradientDescent.class);
            final double[] theta = gradientDescent.learn();
            final LogisticRegressionPrediction prediction = new LogisticRegressionPrediction(theta, context.getBean(MultiplyOperator.class));
            final String[] dataSetNames = {"train", "test"};
            for (final String dataSetName : dataSetNames) {
                final MatrixDataSet dataset = (MatrixDataSet)context.getBean(dataSetName);
                final int[] trainYhat = prediction.predict(dataset);
                final ErrorRate err = new ErrorRate();
                for (int i = 0; i < trainYhat.length; i++) {
                    err.digest(dataset.label(i), trainYhat[i]);
                }
                logger.info(format("error(%s): %.6f", dataSetName, err.getErrorRate()));
            }

        } catch (final ParseException ex) {
            formatter.printHelp("gradle run TrainMain", opts, true);
            System.exit(-1);
        }


    }

    private static Options getOptions() {
        final Options options = new Options();
        options.addOption(
                Option.builder()
                        .longOpt("dictionaryInput")
                        .hasArg()
                        .desc("The file path of dict data")
                        .required()
                        .build());
        options.addOption(
                Option.builder()
                        .longOpt("formattedTrainInput")
                        .hasArg()
                        .desc("The file path of formatted train data")
                        .required()
                        .build());

        options.addOption(
                Option.builder()
                        .longOpt("formattedTestInput")
                        .hasArg()
                        .desc("The file path of formatted test data")
                        .required()
                        .build());

        options.addOption(
                Option.builder()
                        .longOpt("numEpoch")
                        .hasArg()
                        .desc("The number of times SGD loops through all training data")
                        .required()
                        .build()
        );
        return options;
    }

    private static CommandLine getCli(final String[] args, final Options options) throws ParseException {
        final CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

    private static Properties getProperties(final CommandLine cli) {
        final Properties properties = new Properties();
        properties.put("dictionaryInput", cli.getOptionValue("dictionaryInput"));
        properties.put("formattedTrainInput", cli.getOptionValue("formattedTrainInput"));
        properties.put("formattedTestInput", cli.getOptionValue("formattedTestInput"));
        properties.put("numEpoch", cli.getOptionValue("numEpoch"));
        return properties;
    }
}
