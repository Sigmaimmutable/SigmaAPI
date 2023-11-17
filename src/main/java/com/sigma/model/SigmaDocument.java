package com.sigma.model;

import java.util.Date;

public class SigmaDocument {
//	private String id;
	private String sigmaId; //  tr v
	private String fVar1;
	private String fVar2;
	private String fVar3;
	private String fVar4;
	private String fVar5;
	private String fVar6;
	private String fVar7;
	private String fVar8;
	private String fVar9;
	private String fVar10;
	private String createdBy;
	private String tenantId;
	private String docChecksum;
	private Integer nftCreationStatus;
	private String uuid;
	private String status;
	private Date createdDate;
	private	Long jobId;
	private String polyProps;
	private String md5Checksum;
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
	public String getfVar1() {
		return fVar1;
	}
	public void setfVar1(String fVar1) {
		this.fVar1 = fVar1;
	}
	public String getfVar2() {
		return fVar2;
	}
	public void setfVar2(String fVar2) {
		this.fVar2 = fVar2;
	}
	public String getfVar3() {
		return fVar3;
	}
	public void setfVar3(String fVar3) {
		this.fVar3 = fVar3;
	}
	public String getfVar4() {
		return fVar4;
	}
	public void setfVar4(String fVar4) {
		this.fVar4 = fVar4;
	}
	public String getfVar5() {
		return fVar5;
	}
	public void setfVar5(String fVar5) {
		this.fVar5 = fVar5;
	}
	public String getfVar6() {
		return fVar6;
	}
	public void setfVar6(String fVar6) {
		this.fVar6 = fVar6;
	}
	public String getfVar7() {
		return fVar7;
	}
	public void setfVar7(String fVar7) {
		this.fVar7 = fVar7;
	}
	public String getfVar8() {
		return fVar8;
	}
	public void setfVar8(String fVar8) {
		this.fVar8 = fVar8;
	}
	public String getfVar9() {
		return fVar9;
	}
	public void setfVar9(String fVar9) {
		this.fVar9 = fVar9;
	}
	public String getfVar10() {
		return fVar10;
	}
	public void setfVar10(String fVar10) {
		this.fVar10 = fVar10;
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
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	@Override
	public String toString() {
		return "SigmaDocument [sigmaId=" + sigmaId + ", fVar1=" + fVar1 + ", fVar2=" + fVar2 + ", fVar3=" + fVar3
				+ ", fVar4=" + fVar4 + ", fVar5=" + fVar5 + ", fVar6=" + fVar6 + ", fVar7=" + fVar7 + ", fVar8=" + fVar8
				+ ", fVar9=" + fVar9 + ", fVar10=" + fVar10 + ", createdBy=" + createdBy + ", tenantId=" + tenantId
				+ ", docChecksum=" + docChecksum + ", nftCreationStatus=" + nftCreationStatus + ", uuid=" + uuid
				+ ", status=" + status + ", createdDate=" + createdDate + ", jobId=" + jobId + ", polyProps="
				+ polyProps + ",md5Checksum=" + md5Checksum +"]";
	}
}
