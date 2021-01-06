package com.gfu.ml.app;

import com.gfu.ml.sgd.SGDOptimization;
import org.apache.commons.cli.CommandLine;
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

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
public class TrainLettersRecognitionMain {

    private static final Logger logger = LogManager.getLogger(TrainLettersRecognitionMain.class);
    private static final HelpFormatter formatter = new HelpFormatter();

    private final AnnotationConfigApplicationContext context;

    public static void main(final String[] args) {
        final int code = new TrainLettersRecognitionMain().run(args);
        System.exit(code);
    }

    public TrainLettersRecognitionMain() {
        this.context = new AnnotationConfigApplicationContext();
    }

    public int run(final String[] args) {
        final Options opts = getOptions();
        try {
            final CommandLine cli = new DefaultParser().parse(opts, args);
            configureProperties(cli);
            context.register(LettersRecognitionConfiguration.class);
            context.refresh();
            final SGDOptimization sgdOptimization = context.getBean(SGDOptimization.class);
            sgdOptimization.train();
        } catch (final ParseException ex) {
            formatter.printHelp("gradle run TrainLettersRecognitionApp", opts);
            return -1;
        }
        return 0;
    }

    private Options getOptions() {
        final Options opts = new Options();
        opts.addOption(Option.builder("input_dir")
                .required()
                .hasArg()
                .build());
        opts.addOption(Option.builder("num_epoch").hasArg().build());
        opts.addOption(Option.builder("hidden_units").hasArg().build());
        opts.addOption(Option.builder("init_flag").hasArg().build());
        opts.addOption(Option.builder("learning_rate").hasArg().build());
        opts.addOption(Option.builder("sanity").hasArg(false).build());

        return opts;
    }

    private void configureProperties(final CommandLine cli) {
        final Properties properties = new Properties();
        properties.put("input_dir", cli.getOptionValue("input_dir"));
        properties.put("num_epoch", cli.hasOption("num_epoch") ? cli.getOptionValue("num_epoch") : 10);
        properties.put("hidden_units", cli.hasOption("hidden_units") ? cli.getOptionValue("hidden_units") : 100);
        properties.put("init_flag", cli.hasOption("init_flag") ? cli.getOptionValue("init_flag") : 1);
        properties.put("learning_rate", cli.hasOption("learning_rate") ? cli.getOptionValue("learning_rate") : 0.1);
        properties.put("sanity", cli.hasOption("sanity"));

        final ConfigurableEnvironment environment = context.getEnvironment();
        final MutablePropertySources propertySources = environment.getPropertySources();
        final PropertiesPropertySource source = new PropertiesPropertySource("cmd-arguments", properties);
        propertySources.addFirst(source);
    }

}
