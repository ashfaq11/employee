package com.trinet.harness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.trinet.harness.utils.FeatureFlagConstants;
import com.trinet.harness.utils.HarnessUtils;

import io.harness.cf.client.api.BaseConfig;
import io.harness.cf.client.api.CfClient;
import io.harness.cf.client.api.Event;
import io.harness.cf.client.api.FeatureFlagInitializeException;
import io.harness.cf.client.connector.HarnessConfig;
import io.harness.cf.client.connector.HarnessConnector;

@Configuration
public class CfClientConfiguration {
	Logger logger = LoggerFactory.getLogger(CfClientConfiguration.class);

	private String apiKey = FeatureFlagConstants.API_KEY;
	CfClient cfClient;
	

	@Bean
	CfClient cfClient() throws FeatureFlagInitializeException, InterruptedException {

		HarnessConfig connectorConfig = HarnessConfig.builder().configUrl("https://config.ff.harness.io/api/1.0")
				.eventUrl("https://events.ff.harness.io/api/1.0").build();

		BaseConfig options = BaseConfig.builder().pollIntervalInSeconds(60).streamEnabled(true).analyticsEnabled(true)
				.build();

		// Create the client
		cfClient = new CfClient(new HarnessConnector(apiKey, connectorConfig), options);
		// CfClient cfClient = new CfClient(apiKey);
		cfClient.waitForInitialization();
		cfClient.on(Event.READY, result -> logger.info("Harness client initialized."));
		cfClient.on(Event.CHANGED, result -> this.getSSevents(result));
		
		// fetch all feature flag values
		String featureFlagJson = HarnessUtils.getFeatureFlagValues();
		logger.info("featureFlagJson :{}" ,featureFlagJson);
		return cfClient;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

	private void getSSevents(String flag) {
		boolean value = cfClient.boolVariation(flag, null, false);
		logger.info(flag + ": " + (value ? "Enabled" : "Disabled"));
	}
}
