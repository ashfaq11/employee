package com.trinet.harness.utils;

import org.springframework.stereotype.Component;

import io.harness.cf.client.api.CfClient;
import io.harness.cf.client.dto.Target;

@Component
public class HarnessProvider {

	CfClient cfClient;
	public HarnessProvider(CfClient cfClient) {
		this.cfClient = cfClient;
	}
 

    public boolean getFlagValues(String flagName) {
        final Target target = Target.builder()
		    .identifier(FeatureFlagConstants.IDENTIFIER)
		    .name(FeatureFlagConstants.NAME)
		    .attribute(FeatureFlagConstants.LOCATION, FeatureFlagConstants.LOCATION_VALUE)
		    .build();
        
		return cfClient.boolVariation(flagName, target, false);
    }
}
