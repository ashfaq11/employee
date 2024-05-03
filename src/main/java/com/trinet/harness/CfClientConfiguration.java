package com.trinet.harness;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.trinet.harness.utils.FeatureFlagConstants;

import io.harness.cf.client.api.BaseConfig;
import io.harness.cf.client.api.CfClient;
import io.harness.cf.client.api.FeatureFlagInitializeException;

@Configuration
public class CfClientConfiguration {

     private String apiKey = FeatureFlagConstants.API_KEY;

    @Bean
    CfClient cfClient() throws FeatureFlagInitializeException, InterruptedException {
    	
    	BaseConfig options = BaseConfig.builder()  
    	        .pollIntervalInSeconds(60)  
    	        .streamEnabled(true)  
    	        .analyticsEnabled(true).debug(true) 
    	        .build();  

    	CfClient cfClient = new CfClient(apiKey,options);
        cfClient.waitForInitialization();
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
}
