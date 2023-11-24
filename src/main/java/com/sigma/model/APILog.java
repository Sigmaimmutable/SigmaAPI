package com.sigma.model;

public class APILog {
	private String ipAddress;
	private String mailId;
	private String apiName;
	private String callDate;
	private String method;
	private String page;

	

	public APILog() {
		super();
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getCallDate() {
		return callDate;
	}

	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public APILog(String ipAddress, String mailId, String apiName, String callDate, String method, String page) {
		super();
		this.ipAddress = ipAddress;
		this.mailId = mailId;
		this.apiName = apiName;
		this.callDate = callDate;
		this.method = method;
		this.page = page;
		
	}
}
