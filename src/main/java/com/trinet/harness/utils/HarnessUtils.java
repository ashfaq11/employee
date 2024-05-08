package com.trinet.harness.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class HarnessUtils {
	 public static  String getFeatureFlagValues() {
	        var httpClient = HttpClient.newBuilder().build();

	        HashMap<String, String> params = new HashMap<>();
	        params.put("accountIdentifier", FeatureFlagConstants.ACCOUNT_IDENTIFIER);
	        params.put("orgIdentifier", FeatureFlagConstants.ORG_IDENTIFIER);
	        params.put("projectIdentifier", FeatureFlagConstants.PROJECT_IDENTIFIER);
	        params.put("environmentIdentifier", FeatureFlagConstants.ENVIRONMENT_IDENTIFIER);
//	        params.put("pageNumber", "0");
//	        params.put("pageSize", "0");
//	        params.put("sortOrder", "ASCENDING");
//	        params.put("sortByField", "name");
//	        params.put("name", "string");
//	        params.put("identifier", "string");
//          params.put("archived", "true");
//	        params.put("kind", "json");
//	        params.put("targetIdentifier", "string");
//	        params.put("targetIdentifierFilter", "string");
//	        params.put("metrics", "true");
//	        params.put("featureIdentifiers", "string");
//	        params.put("excludedFeatures", "string");
//	        params.put("status", "string");
//	        params.put("lifetime", "string");
//	        params.put("enabled", "true");
//	        params.put("flagCounts", "true");
//	        params.put("summary", "true");
//	        params.put("tags", "string");

	        var query = params.keySet().stream()
	          .map(key -> key + "=" + URLEncoder.encode(params.get(key), StandardCharsets.UTF_8))
	          .collect(Collectors.joining("&"));

	        var host = "https://app.harness.io";
	        var pathname = "/cf/admin/features";
	        var request = HttpRequest.newBuilder()
	          .GET()
	          .uri(URI.create(host + pathname + '?' + query))
	          .header("x-api-key", FeatureFlagConstants.DEV_X_API_KEY)
	          .build();

	        HttpResponse<String> response;
			try {
				response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			} catch (IOException e) {
				e.printStackTrace();
				return "Exception Occurred".concat(e.getMessage());
			} catch (InterruptedException e) {
				e.printStackTrace();
				return "Exception Occurred".concat(e.getMessage());
			}

	        return response.body();
	      }

}
