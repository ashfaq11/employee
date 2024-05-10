package com.trinet.harness.domain;

public class FFRedisDto {
    private String identifier;
    private String name;
    private boolean stale; // True or False
    private String status; // Active or Inactive or Never-requested
    private String state; // On or Off
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isStale() {
		return stale;
	}
	public void setStale(boolean stale) {
		this.stale = stale;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

    
}
