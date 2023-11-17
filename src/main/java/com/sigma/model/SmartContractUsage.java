package com.sigma.model;

public class SmartContractUsage {
	private String id;
	private String userId;
	private String smartContractAddress;
	private String networkType;
	private String tokenName;
	private String depositTokenAddress;
	private String bondTokenAddress;
	private String ownerAddress;
	private String purchaseTokenAddress;
	private Long startDate;
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	private Long endDate;
	private Long appId;
	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	public String getSmartContractAddress() {
		return smartContractAddress;
	}
	public String getNetworkType() {
		return networkType;
	}
	public String getTokenName() {
		return tokenName;
	}
	public String getDepositTokenAddress() {
		return depositTokenAddress;
	}
	public String getBondTokenAddress() {
		return bondTokenAddress;
	}
	public String getOwnerAddress() {
		return ownerAddress;
	}
	public String getPurchaseTokenAddress() {
		return purchaseTokenAddress;
	}
	public Long getStartDate() {
		return startDate;
	}
	public Long getEndDate() {
		return endDate;
	}
	public Long getAppId() {
		return appId;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setSmartContractAddress(String smartContractAddress) {
		this.smartContractAddress = smartContractAddress;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	public void setDepositTokenAddress(String depositTokenAddress) {
		this.depositTokenAddress = depositTokenAddress;
	}
	public void setBondTokenAddress(String bondTokenAddress) {
		this.bondTokenAddress = bondTokenAddress;
	}
	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}
	public void setPurchaseTokenAddress(String purchaseTokenAddress) {
		this.purchaseTokenAddress = purchaseTokenAddress;
	}
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	@Override
	public String toString() {
		return "SmartContractUsage [id=" + id + ", userId=" + userId + ", smartContractAddress=" + smartContractAddress
				+ ", networkType=" + networkType + ", tokenName=" + tokenName + ", depositTokenAddress="
				+ depositTokenAddress + ", bondTokenAddress=" + bondTokenAddress + ", ownerAddress=" + ownerAddress
				+ ", purchaseTokenAddress=" + purchaseTokenAddress + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", appId=" + appId + "]";
	}	
}