package com.sigma.model.db;

public class SigmaProps {
	private String authUrl;
	private String appUrl;
	private String extUser;
	private String extPwd;
	private String tenantId;
	private Boolean ipfsEnabled;
	private String extFileUrl;
	private String sessionId;
	private String ipfsHash;//PRIV
	
	public SigmaProps() {
		super();
	}

	public String getIpfsHash() {
		return ipfsHash;
	}

	public void setIpfsHash(String ipfsHash) {
		this.ipfsHash = ipfsHash;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public SigmaProps(String authUrl, String appUrl, String extUser, String extPwd, String tenantId) {
		super();
		this.authUrl = authUrl;
		this.appUrl = appUrl;
		this.extUser = extUser;
		this.extPwd = extPwd;
		this.tenantId = tenantId;
	}

	public SigmaProps(String authUrl, String appUrl, String extUser, String extPwd, String tenantId,
			Boolean ipfsEnabled, String extFileUrl) {
		super();
		this.authUrl = authUrl;
		this.appUrl = appUrl;
		this.extUser = extUser;
		this.extPwd = extPwd;
		this.tenantId = tenantId;
		this.ipfsEnabled = ipfsEnabled;
		this.extFileUrl = extFileUrl;
	}

	public String getExtFileUrl() {
		return extFileUrl;
	}

	public void setExtFileUrl(String extFileUrl) {
		this.extFileUrl = extFileUrl;
	}

	public String getExtUser() {
		return extUser;
	}
	public void setExtUser(String extUser) {
		this.extUser = extUser;
	}
	public String getExtPwd() {
		return extPwd;
	}
	public void setExtPwd(String extPwd) {
		this.extPwd = extPwd;
	}
	public String getAuthUrl() {
		return authUrl;
	}
	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}
	public String getAppUrl() {
		return appUrl;
	}
	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Boolean getIpfsEnabled() {
		return ipfsEnabled;
	}

	public void setIpfsEnabled(Boolean ipfsEnabled) {
		this.ipfsEnabled = ipfsEnabled;
	}

	@Override
	public String toString() {
		return "SigmaProps [authUrl=" + authUrl + ", appUrl=" + appUrl + ", extUser=" + extUser + ", extPwd=" + extPwd
				+ ", tenantId=" + tenantId + ", ipfsEnabled=" + ipfsEnabled + ", sessionId=" + extFileUrl + "]";
	}
	
}