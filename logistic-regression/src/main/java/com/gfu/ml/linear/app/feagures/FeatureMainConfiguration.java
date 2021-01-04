package com.gfu.ml.linear.app.feagures;

import com.gfu.ml.linear.features.ExtractFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;

/**
 * @author Geng Fu (fugeng1991@hotmail.com)
 */
@Configuration
public class FeatureMainConfiguration {

    private static final Logger logger = LogManager.getLogger(FeatureMainConfiguration.class);

    @Bean
    public Map<String, Integer> dictionary (
            @Value("${dictionaryInput}") final String inputFile
    ) throws IOException {
        logger.info("dictionaryInput=" + inputFile);
        return IOHelper.loadDictionary(inputFile);
    }

    @Bean
    public ExtractFeature extractFeature(
            @Value("${useModel1}") final boolean useModel1,
            Map<String, Integer> dictionary
    ) {
        logger.info("useModel1=" + useModel1);
        return new ExtractFeature(dictionary, useModel1);
    }

}
