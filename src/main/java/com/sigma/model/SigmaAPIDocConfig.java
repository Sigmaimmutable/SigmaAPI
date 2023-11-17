package com.sigma.model;

public class SigmaAPIDocConfig {
	@Override
	public String toString() {
		return "SigmaAPIDocConfig [id=" + id + ", sigmaField=" + sigmaField + ", extField=" + extField + ", tenantId="
				+ tenantId + ", status=" + status + ", createdBy=" + createdBy + "]";
	}

	private String id;
	private String sigmaField;
	private String extField;
	private String tenantId;
	private String status;
	private String createdBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSigmaField() {
		return sigmaField;
	}

	public void setSigmaField(String sigmaField) {
		this.sigmaField = sigmaField;
	}

	public String getExtField() {
		return extField;
	}

	public void setExtField(String extField) {
		this.extField = extField;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}
