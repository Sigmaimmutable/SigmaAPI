package com.sigma.model;

public class UserVisit {
	private String ipAddress;
	private String algoAddress;
	private String networkType;
	private String walletType;
	private String StartDate;

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		this.StartDate = startDate;
	}

	public UserVisit() {
		super();
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getAlgoAddress() {
		return algoAddress;
	}

	public void setAlgoAddress(String algoAddress) {
		this.algoAddress = algoAddress;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}

	public UserVisit(String ipAddress, String algoAddress, String networkType, String walletType,String startDate) {
		super();
		this.ipAddress = ipAddress;
		this.algoAddress = algoAddress;
		this.networkType = networkType;
		this.walletType = walletType;
		this.StartDate = startDate;
	}
}
