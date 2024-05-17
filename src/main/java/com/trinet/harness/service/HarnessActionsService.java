package com.trinet.harness.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 

public class  HarnessActionsService{
	private static final Logger log = LoggerFactory.getLogger(HarnessActionsService.class);

	private static final String url = "https://app.harness.io/gateway/pipeline/api/webhook/custom/4anBjq7fS2ymHWZS04xxHQ/v3?accountIdentifier=2CxHkmCdRLOIoT4pDdEfjw&orgIdentifier=default&projectIdentifier=ci&pipelineIdentifier=Java_CI1&triggerIdentifier=trigger_CI";
	private static final String body = "{\"sample_key\": \"sample_value\"}";

	public static void main(String[] args) {
		try {
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			// Send post request
			conn.setDoOutput(true);
			conn.getOutputStream().write(body.getBytes());

			int responseCode = conn.getResponseCode();
			log.info("Response Code : " + responseCode);

			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new IOException("POST request failed with code: " + responseCode);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
