package com.sigma.model;

/**
 * @author joeus
 *
 */
public class Resource {
	private String resourceId;
	private String resourceName;
	private double fee;
	private String feeType;	
	private String status;
	private String creationDate;
	private String createdBy;
	public String getResourceId() {
		return resourceId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public double getFee() {
		return fee;
	}
	public String getFeeType() {
		return feeType;
	}
	public String getStatus() {
		return status;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public void setFee(double fee) {
		this.fee = fee;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Resource(String resourceId, String resourceName, double fee, String feeType, String status,
			String creationDate, String createdBy) {
		super();
		this.resourceId = resourceId;
		this.resourceName = resourceName;
		this.fee = fee;
		this.feeType = feeType;
		this.status = status;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
	}
	public Resource() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Resource [resourceId=" + resourceId + ", resourceName=" + resourceName + ", fee=" + fee + ", feeType="
				+ feeType + ", status=" + status + ", creationDate=" + creationDate + ", createdBy=" + createdBy + "]";
	}
	
}
