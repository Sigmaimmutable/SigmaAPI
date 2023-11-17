package com.sigma.model;

public class RawInvoice {
	private String id;
	private String userId;
	private String resourceId;
	private Double fee;
	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public Double getFee() {
		return fee;
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
	public void setFee(Double fee) {
		this.fee = fee;
	}
	@Override
	public String toString() {
		return "RawInvoice [id=" + id + ", userId=" + userId + ", resourceId=" + resourceId + ", fee=" + fee + "]";
	}
	public RawInvoice(String id, String userId, String resourceId, Double fee) {
		super();
		this.id = id;
		this.userId = userId;
		this.resourceId = resourceId;
		this.fee = fee;
	}
	public RawInvoice() {
		super();
	}	
}
