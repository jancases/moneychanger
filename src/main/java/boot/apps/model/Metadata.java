package boot.apps.model;

import java.io.Serializable;
import java.time.Instant;

public class Metadata implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String status;
	private int httpCode;
	private String description;
	private String requestTimestamp;
	private String responseTimestamp;
	private String uri;
	
	public Metadata() {
		this.requestTimestamp = String.valueOf(System.currentTimeMillis());
	}
	
	public Metadata(String description, String uri, int httpCode) {
		this.description = description;
		this.uri = uri;
		this.requestTimestamp = String.valueOf(System.currentTimeMillis());
		this.httpCode = httpCode;
	}

	public Metadata(String description, String uri, int httpCode, Instant requestTime) {
		this.description = description;
		this.uri = uri;
		this.requestTimestamp = String.valueOf(requestTime.toEpochMilli());
		this.httpCode = httpCode;
	}
	
	public Metadata(String description, String requestedTimestamp, String uri) {
		this.description = description;
		if(requestedTimestamp == null || requestedTimestamp.trim().length() == 0) {
			this.requestTimestamp = String.valueOf(System.currentTimeMillis());
		} else {
			this.requestTimestamp = requestedTimestamp;
		}
		this.uri = uri;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getHttpCode() {
		return httpCode;
	}
	
	public void setHttpCode(int httpCode) {
		if (httpCode == 200) {
			this.status = "success";
		} else {
			this.status = "error";
		}
		this.httpCode = httpCode;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getRequestTimestamp() {
		return requestTimestamp;
	}
	
	public void setRequestTimestamp(String requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}
	
	public String getResponseTimestamp() {
		if(responseTimestamp == null) {
			responseTimestamp = String.valueOf(System.currentTimeMillis());
		}
		
		return responseTimestamp;
	}
	
	public void setResponseTimestamp(String responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}
	
	public String getUri() {
		return this.uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
}
