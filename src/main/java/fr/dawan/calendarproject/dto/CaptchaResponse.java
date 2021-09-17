package fr.dawan.calendarproject.dto;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptchaResponse {
	private Boolean success;
	
	@JsonProperty("challenge_ts")
	private Timestamp challengeTs;
	
	private String hostname;
	
	@JsonProperty("error-codes")
	private List<String> errorCodes;
	
	public CaptchaResponse() {
	}

	public CaptchaResponse(Boolean success, Timestamp challengeTs, String hostname, List<String> errorCodes) {
		this.success = success;
		this.challengeTs = challengeTs;
		this.hostname = hostname;
		this.errorCodes = errorCodes;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Timestamp getChallengeTs() {
		return challengeTs;
	}

	public void setChallengeTs(Timestamp challengeTs) {
		this.challengeTs = challengeTs;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public List<String> getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(List<String> errorCodes) {
		this.errorCodes = errorCodes;
	}	
}
