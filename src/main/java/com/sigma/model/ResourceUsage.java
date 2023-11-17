package com.sigma.model;

import java.util.Date;

/**
 * @author joeus
 *
 */
public class ResourceUsage {
	private String id;
	private String userId;
	private String resourceId;
	private Date startDate;
	private Date endTime;
	private String status;
	public ResourceUsage(String id, String userId, String resourceId, Date startDate, Date endTime, String status) {
		super();
		this.id = id;
		this.userId = userId;
		this.resourceId = resourceId;
		this.startDate = startDate;
		this.endTime = endTime;
		this.status = status;
	}
	public ResourceUsage() {
		super();
	}
	@Override
	public String toString() {
		return "ResourceUsage [id=" + id + ", userId=" + userId + ", resourceId=" + resourceId + ", startDate="
				+ startDate + ", endTime=" + endTime + ", status=" + status + "]";
	}
	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public Date getEndTime() {
		return endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
