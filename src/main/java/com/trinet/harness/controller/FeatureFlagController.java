package com.trinet.harness.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.trinet.harness.CfClientConfiguration;
import com.trinet.harness.domain.FFRedisDto;
import com.trinet.harness.service.FeatureFlagsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/feature_flag")
public class FeatureFlagController {
    Logger logger = LoggerFactory.getLogger(FeatureFlagController.class);
    
    @Autowired
    FeatureFlagsService featureFlagsService;
    @Autowired
    CfClientConfiguration client;

    @GetMapping("/fetchAll")
    public ResponseEntity<List<FFRedisDto>> featureFlag() throws JsonProcessingException {

        return ResponseEntity.ok(client.getAllFFValuesFromHrnSdk());
    }

    @GetMapping("/findById")
    public ResponseEntity<FFRedisDto> findById(@RequestParam String flagName) throws JsonProcessingException {
        return ResponseEntity.ok(featureFlagsService.getFlagById(flagName));
    }


}
