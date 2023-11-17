package com.sigma.model;

public class Notification {
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	private String title;
	private String descriptions;
	private String mailId;
	private Long epochtime;
	private String tennatId;
	private Boolean statuses;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public Long getEpochtime() {
		return epochtime;
	}
	public void setEpochtime(Long epochtime) {
		this.epochtime = epochtime;
	}
	public String getTennatId() {
		return tennatId;
	}
	public void setTennatId(String tennatId) {
		this.tennatId = tennatId;
	}
	public Boolean getStatuses() {
		return statuses;
	}
	public void setStatuses(Boolean statuses) {
		this.statuses = statuses;
	}
	

	
}