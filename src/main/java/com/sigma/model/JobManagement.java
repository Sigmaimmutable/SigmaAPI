package com.sigma.model;

import java.sql.Date;

public class JobManagement {
	private Long id;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	public String getTennatId() {
		return tennatId;
	}
	public void setTennatId(String tennatId) {
		this.tennatId = tennatId;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	private String loginTime;
	
	private String mailId;
	private String roleType;
	private String tennatId;
	private String activity;


	
}