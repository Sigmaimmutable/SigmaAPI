package com.sigma.model;

import java.sql.Date;

public class Session {
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private Date loginTime;
	private Date logoutTime;
	private String mailId;
	private String roleType;
	private String tennatId;
	private String activity;
	private String StartDate;
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		this.StartDate = startDate;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public Date getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
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
	public Session() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Session(Long id, Date loginTime, Date logoutTime, String mailId, String roleType, String tennatId,
			String activity, String startDate) {
		super();
		this.id = id;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
		this.mailId = mailId;
		this.roleType = roleType;
		this.tennatId = tennatId;
		this.activity = activity;
		this.StartDate = startDate;
	}
	

	
}