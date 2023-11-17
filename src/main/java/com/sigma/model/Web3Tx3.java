package com.sigma.model;

/**
 * @author joeus
 *
 */
public class Web3Tx3 {
	private String txHash;
	private String nonce;
	private String gasPrice;
	private String toAddress;
	private String value;
	private String fromAddress;
	private String blockHash;
	private String blockNumber;
	public String getTxHash() {
		return txHash;
	}
	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getGasPrice() {
		return gasPrice;
	}
	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getBlockHash() {
		return blockHash;
	}
	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}
	public String getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(String blockNumber) {
		this.blockNumber = blockNumber;
	}
	@Override
	public String toString() {
		return "Web3Tx3 [txHash=" + txHash + ", nonce=" + nonce + ", gasPrice=" + gasPrice + ", toAddress=" + toAddress
				+ ", value=" + value + ", fromAddress=" + fromAddress + ", blockHash=" + blockHash + ", blockNumber="
				+ blockNumber + "]";
	}	
}