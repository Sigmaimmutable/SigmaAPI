package com.sigma.model;

import java.sql.Date;

public class JobTrigger {
	private Long id;
	private String jobType;
	private int jobstatus ;
	private String tennatId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	

	
	public int getJobstatus() {
		return jobstatus;
	}
	public void setJobstatus(int jobstatus) {
		this.jobstatus = jobstatus;
	}
	public String getTennatId() {
		return tennatId;
	}
	public void setTennatId(String tennatId) {
		this.tennatId = tennatId;
	}



	
}