package com.sigma.model;

import java.util.Date;

public class DocumentO2Job {
	private	Long id;					
	private	String jobName;
	private	String	status;				
	private	String	errorSummary;
	private String companyCode;
	private Date runStartTime;
	private Date runCompletionTime;
	private Integer noOfRecordsProcessed;
	private String jobRunByUser;
	private String latestDocumentDate;
	private String tenantId;
	private String jobType;
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getLatestDocumentDate() {
		return latestDocumentDate;
	}
	public void setLatestDocumentDate(String latestDocumentDate) {
		this.latestDocumentDate = latestDocumentDate;
	}
	public String getJobRunByUser() {
		return jobRunByUser;
	}
	public void setJobRunByUser(String jobRunByUser) {
		this.jobRunByUser = jobRunByUser;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorSummary() {
		return errorSummary;
	}
	public void setErrorSummary(String errorSummary) {
		this.errorSummary = errorSummary;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public Date getRunStartTime() {
		return runStartTime;
	}
	public void setRunStartTime(Date runStartTime) {
		this.runStartTime = runStartTime;
	}
	public Date getRunCompletionTime() {
		return runCompletionTime;
	}
	public void setRunCompletionTime(Date runCompletionTime) {
		this.runCompletionTime = runCompletionTime;
	}
	public Integer getNoOfRecordsProcessed() {
		return noOfRecordsProcessed;
	}
	public void setNoOfRecordsProcessed(Integer noOfRecordsProcessed) {
		this.noOfRecordsProcessed = noOfRecordsProcessed;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}	
}