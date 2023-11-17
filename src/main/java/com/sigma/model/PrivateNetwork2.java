package com.sigma.model;

/**
 * @author joeus
 *
 */
public class PrivateNetwork2 {
	private Long id;
	private String networkName;
	private Integer chainId;
	private String ipAddress;	
	private String status;
	private String createdByUser;
	private String smartContractAddress;//PRIV
	private String smartContractAccessUrl;
	private String smartContractDefaultWalletAddress;
	private String tenantId;
	private String ipfsUrl;
	private String consortiaId;
	private String envId;
	private String networkAPIUrl;
	
	public String getNetworkAPIUrl() {
		return networkAPIUrl;
	}
	public void setNetworkAPIUrl(String networkAPIUrl) {
		this.networkAPIUrl = networkAPIUrl;
	}
	public String getConsortiaId() {
		return consortiaId;
	}
	public void setConsortiaId(String consortiaId) {
		this.consortiaId = consortiaId;
	}
	public String getEnvId() {
		return envId;
	}
	public void setEnvId(String envId) {
		this.envId = envId;
	}
	public String getIpfsUrl() {
		return ipfsUrl;
	}
	public void setIpfsUrl(String ipfsUrl) {
		this.ipfsUrl = ipfsUrl;
	}
	public String getSmartContractAddress() {
		return smartContractAddress;
	}
	public void setSmartContractAddress(String smartContractAddress) {
		this.smartContractAddress = smartContractAddress;
	}
	public String getSmartContractAccessUrl() {
		return smartContractAccessUrl;
	}
	public void setSmartContractAccessUrl(String smartContractAccessUrl) {
		this.smartContractAccessUrl = smartContractAccessUrl;
	}
	public String getSmartContractDefaultWalletAddress() {
		return smartContractDefaultWalletAddress;
	}
	public void setSmartContractDefaultWalletAddress(String smartContractDefaultWalletAddress) {
		this.smartContractDefaultWalletAddress = smartContractDefaultWalletAddress;
	}
	public String getNetworkName() {
		return networkName;
	}
	public Integer getChainId() {
		return chainId;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public String getStatus() {
		return status;
	}
	public String getCreatedByUser() {
		return createdByUser;
	}
	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}
	public void setChainId(Integer chainId) {
		this.chainId = chainId;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setCreatedByUser(String createdByUser) {
		this.createdByUser = createdByUser;
	}
	
	@Override
	public String toString() {
		return "PrivateNetwork2 [id=" + id + ", networkName=" + networkName + ", chainId=" + chainId + ", ipAddress="
				+ ipAddress + ", status=" + status + ", createdByUser=" + createdByUser + ", smartContractAddress="
				+ smartContractAddress + ", smartContractAccessUrl=" + smartContractAccessUrl
				+ ", smartContractDefaultWalletAddress=" + smartContractDefaultWalletAddress + ", tenantId=" + tenantId
				+ "]";
	}
	public PrivateNetwork2(Long id, String networkName, Integer chainId, String ipAddress, String status,
			String createdByUser, String smartContractAddress, String smartContractAccessUrl,
			String smartContractDefaultWalletAddress) {
		super();
		this.id = id;
		this.networkName = networkName;
		this.chainId = chainId;
		this.ipAddress = ipAddress;
		this.status = status;
		this.createdByUser = createdByUser;
		this.smartContractAddress = smartContractAddress;
		this.smartContractAccessUrl = smartContractAccessUrl;
		this.smartContractDefaultWalletAddress = smartContractDefaultWalletAddress;
	}
	public PrivateNetwork2() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}	
}