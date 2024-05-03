package com.trinet.harness.sdk;

import static java.util.Optional.ofNullable;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

public class SdkCodesHarness extends io.harness.cf.client.common.SdkCodes  {
	
	
	public static void infoStreamEventReceived(String eventJson) {
//	    log.info(sdkErrMsg(5002, ofNullable(eventJson)));
//	    super(eventJson);
		System.out.println("test");
  }
	
	 


}
