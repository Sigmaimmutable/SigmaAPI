package com.sigma.model;

import java.util.Date;

public class TenantDocSource2 {
	private String id;
	private String name;
	private String createdBy;
	private Date createdDate;
	private String tenantId;
	private String extUrl; // veeva url
	private String extUserName; // veeva user
	private String extPassword; // veeva password
	private Integer status;
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getExtUrl() {
		return extUrl;
	}

	public void setExtUrl(String extUrl) {
		this.extUrl = extUrl;
	}

	public String getExtUserName() {
		return extUserName;
	}

	public void setExtUserName(String extUserName) {
		this.extUserName = extUserName;
	}

	public String getExtPassword() {
		return extPassword;
	}

	public void setExtPassword(String extPassword) {
		this.extPassword = extPassword;
	}
}