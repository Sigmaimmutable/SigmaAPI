package com.sigma.model;

import java.util.Date;

public class SigmaDocument2 {
//	private String id;
	private String sigmaId; //  tr v
	private String createdBy;
	private String tenantId;
	private String docChecksum;
	private Integer nftCreationStatus;
	private String uuid;
	private String status;
	private Date createdDate;
	private String polyProps;
	private String md5Checksum;
	private String fileName;
	private String mailId;
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMd5Checksum() {
		return md5Checksum;
	}
	public void setMd5Checksum(String md5Checksum) {
		this.md5Checksum = md5Checksum;
	}
	public String getPolyProps() {
		return polyProps;
	}
	public void setPolyProps(String polyProps) {
		this.polyProps = polyProps;
	}
	public String getSigmaId() {
		return sigmaId;
	}
	public void setSigmaId(String sigmaId) {
		this.sigmaId = sigmaId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getNftCreationStatus() {
		return nftCreationStatus;
	}
	public void setNftCreationStatus(Integer nftCreationStatus) {
		this.nftCreationStatus = nftCreationStatus;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getDocChecksum() {
		return docChecksum;
	}
	public void setDocChecksum(String docChecksum) {
		this.docChecksum = docChecksum;
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
	
	@Override
	public String toString() {
		return "SigmaDocument [sigmaId=" + sigmaId + ", createdBy=" + createdBy + ", tenantId=" + tenantId
				+ ", docChecksum=" + docChecksum + ", nftCreationStatus=" + nftCreationStatus + ", uuid=" + uuid
				+ ", status=" + status + ", createdDate=" + createdDate + ", polyProps="
				+ polyProps + ",md5Checksum=" + md5Checksum +"]";
	}
}
