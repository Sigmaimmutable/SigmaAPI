package com.sigma.model;

public class ImmutableRec {
	private String uuid;	
	private String tenantId;
	private String idata;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getIdata() {
		return idata;
	}
	public void setIdata(String idata) {
		this.idata = idata;
	}
	@Override
	public String toString() {
		return "ImmutableRec [uuid=" + uuid + ", tenantId=" + tenantId + ", idata=" + idata + "]";
	}	
}
