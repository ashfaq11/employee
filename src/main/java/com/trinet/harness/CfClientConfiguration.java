package com.trinet.harness;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trinet.harness.domain.FFRedisDto;
import com.trinet.harness.domain.FeatureFlagDto;
import com.trinet.harness.repo.CacheDataRepo;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class CfClientConfiguration {
	Logger logger = LoggerFactory.getLogger(CfClientConfiguration.class);
	private final CacheDataRepo cacheDataRepository;

	private final ObjectMapper objectMapper;
	private String apiKey = FeatureFlagConstants.API_KEY;
	CfClient cfClient;

    public CfClientConfiguration(CacheDataRepo cacheDataRepository, ObjectMapper objectMapper) {
        this.cacheDataRepository = cacheDataRepository;
        this.objectMapper = objectMapper;
    }


    @SneakyThrows
    @Bean
	CfClient cfClient() throws FeatureFlagInitializeException, InterruptedException, JsonProcessingException {

		HarnessConfig connectorConfig = HarnessConfig.builder().configUrl("https://config.ff.harness.io/api/1.0")
				.eventUrl("https://events.ff.harness.io/api/1.0").build();

		BaseConfig options = BaseConfig.builder().pollIntervalInSeconds(60).streamEnabled(true).analyticsEnabled(true)
				.build();

		// Create the client
		cfClient = new CfClient(new HarnessConnector(apiKey, connectorConfig), options);
		// CfClient cfClient = new CfClient(apiKey);
		cfClient.waitForInitialization();
		cfClient.on(Event.READY, result -> logger.info("Harness client initialized."));
		cfClient.on(Event.CHANGED, this::getSSEvents);
		// Cache Data
		Optional<FeatureFlagDto> optionalCacheData = cacheDataRepository.findById("allFlags");
		if (optionalCacheData.isEmpty())
			this.getFFValues();
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

	private void getSSEvents(String flag)  {
        try {
            this.getFFValues();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //boolean value = cfClient.boolVariation(flag, null, false);
		logger.info(flag + ": " + ("Changed"));
	}


	private void getFFValues() throws JsonProcessingException {
		// fetch all feature flag values
		String featureFlagString = HarnessUtils.getFeatureFlagValues();
		JsonParser parser = new JsonParser();
		// Creating JSONObject from String using parser
		JsonObject featureFlagJson = parser.parse(featureFlagString).getAsJsonObject();
		JsonArray featureFlags = featureFlagJson.getAsJsonArray("features");

		// stale , status , name , identifier ,kind , project , env
		List<FFRedisDto> ffList = new ArrayList<>();
		for(JsonElement element: featureFlags) {
			FFRedisDto flag = new FFRedisDto();
			flag.setIdentifier(element.getAsJsonObject().get("identifier").getAsString());
			flag.setState(element.getAsJsonObject().get("envProperties").getAsJsonObject().get("state").getAsString());
			flag.setName(element.getAsJsonObject().get("name").getAsString());
			ffList.add(flag);
		}

		String flagsAsJsonString = objectMapper.writeValueAsString(ffList);
		FeatureFlagDto cacheData = new FeatureFlagDto("allFlags", flagsAsJsonString);

		cacheDataRepository.save(cacheData);
	}
}
