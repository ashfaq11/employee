package com.trinet.harness.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubActionsService {

	private static final Logger log = LoggerFactory.getLogger(GitHubActionsService.class);

	@Value("${github.actions.trigger.url}")
	String workflowUrl;

	@Value("${github.actions.token}")
	String token;

	public void triggerGitHubActionWorkflow() {
		log.info("triggerGitHubActionWorkflow() -- Start");

		RestTemplate restTemplate = new RestTemplate();
//		String workflowUrl = "https://api.github.com/repos/Sridharraoe/employee-harness/dispatches";

		String requestJson = "{\"event_type\":\"trigger-github-actions\"}";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Accept", "application/vnd.github+json");
		headers.set("User-Agent", "BE application");
//		log.info("token "+token);
		headers.set("Authorization", "Bearer "+token);
		headers.set("X-GitHub-Api-Version", "2022-11-28");

		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		ResponseEntity<String> response = null;
		log.info("Calling the dispatcher event ");
		try{
			response = restTemplate.postForEntity(workflowUrl, entity, String.class);
		}catch(Exception e){
			log.info("Exception occured "+ e);
		}
		log.info("completed calling the dispatcher event ");
		HttpStatusCode status = response.getStatusCode();
		log.info("Response Status " + status);
		log.info("triggerGitHubActionWorkflow() -- End");
	}

}
