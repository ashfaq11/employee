package com.trinet.harness.domain;

public class FeatureFlagDto {

	String key;
	Boolean value;
	
	
	public FeatureFlagDto(String key, Boolean value) {
		super();
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Boolean getValue() {
		return value;
	}
	public void setValue(Boolean value) {
		this.value = value;
	}
	
	
	
}
