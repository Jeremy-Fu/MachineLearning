package com.gfu.ml.linear.app.feagures;

import com.gfu.ml.linear.features.ExtractFeature;
import com.google.common.collect.ImmutableMap;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class FeatureMain {

    private static final Logger logger = LogManager.getLogger(FeatureMain.class);

    private static final HelpFormatter formatter = new HelpFormatter();

    public static void main(final String[] args) {

        final Options options = getOptions();

        try {
            final CommandLine cli = getCli(args, options);
            final Properties properties = getProperties(cli, options);

            final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            final ConfigurableEnvironment environment = context.getEnvironment();
            final MutablePropertySources propertySources = environment.getPropertySources();

            final PropertiesPropertySource source = new PropertiesPropertySource("cmd-arguments", properties);
            propertySources.addFirst(source);

            context.register(FeatureMainConfiguration.class);
            context.refresh();

            final ExtractFeature extractFeature = context.getBean(ExtractFeature.class);
            final Map<String, String> ioMap = ImmutableMap.of("trainInput", "formattedTrainOut", "validationInput", "formattedValidationOut", "testInput", "formattedTestOut");
            for (final Entry<String, String> entry : ioMap.entrySet()) {
                final BufferedReader in = new BufferedReader(new FileReader(cli.getOptionValue(entry.getKey())));
                logger.info("Loading from " +  cli.getOptionValue(entry.getKey()));
                logger.info("Writing to " + cli.getOptionValue(entry.getValue()));
                final BufferedWriter out = new BufferedWriter(new FileWriter(cli.getOptionValue(entry.getValue())));
                String line = null;
                int counter = 0;
                while ((line = in.readLine()) != null) {
                    final String extracted = extractFeature.extract(line);
                    out.append(extracted);
                    out.newLine();
                    counter++;
                }
                in.close();
                out.flush();
                out.close();
            }

        } catch (final ParseException ex) {
            formatter.printHelp("gradle run FeatureMain", options, true);
            System.exit(-1);
        } catch (final IOException ex) {
            logger.error("Encountered IOException while processing train/test/validation data", ex);
            System.exit(-1);
        }

    }

    private static CommandLine getCli(final String[] args, final Options options) throws ParseException{
        final CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args);
    }

    private static Properties getProperties(final CommandLine cli, final Options options) {
        final Properties properties = new Properties();
        properties.put("useModel1", !cli.hasOption("useModel2"));
        if (!cli.hasOption("dictionaryInput")) {

            return null;
        } else {
            properties.put("dictionaryInput", cli.getOptionValue("dictionaryInput"));
        }
        return properties;
    }

    private static Options getOptions() {
        final Options options = new Options();

        options.addOption(
                Option.builder()
                        .longOpt("trainInput")
                        .hasArg()
                        .desc("The file path of training data")
                        .required()
                        .build());

        options.addOption(
                Option.builder()
                        .longOpt("formattedTrainOut")
                        .hasArg()
                        .desc("The file path of formatted training data (output)")
                        .required()
                        .build());


        options.addOption(
                Option.builder()
                        .longOpt("validationInput")
                        .hasArg()
                        .desc("The file path of validation data")
                        .required()
                        .build());

        options.addOption(
                Option.builder()
                        .longOpt("formattedValidationOut")
                        .hasArg()
                        .desc("The file path of formatted validation data (output)")
                        .required()
                        .build());

        options.addOption(
                Option.builder()
                        .longOpt("testInput")
                        .hasArg()
                        .desc("The file path of testing data")
                        .required()
                        .build());

        options.addOption(
                Option.builder()
                        .longOpt("formattedTestOut")
                        .hasArg()
                        .desc("The file path of formatted testing data (output)")
                        .required()
                        .build());

        options.addOption(
                Option.builder()
                        .longOpt("dictionaryInput")
                        .hasArg()
                        .desc("The file path of dictionary file")
                        .required()
                        .build());

        options.addOption(
                Option.builder()
                .longOpt("useModel2")
                .desc("Use model 2 to extract features. " +
                "If this is unset, default to model 1. " +
                "Model 2 removes noise of highly repetitive words")
                .build());
        return options;
    }

}
