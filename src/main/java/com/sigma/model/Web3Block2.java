package com.sigma.model;

/**
 * @author joeus
 *
 */
public class Web3Block2 {
	private Long blockNumber;
	private String blockHash;
	private String miner;
	private String timeStamp;
	private String gasLimit;
	private String gasUsed;
	private String nonce;
	public Long getBlockNumber() {
		return blockNumber;
	}
	public void setBlockNumber(Long blockNumber) {
		this.blockNumber = blockNumber;
	}
	public String getBlockHash() {
		return blockHash;
	}
	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
	}
	public String getMiner() {
		return miner;
	}
	public void setMiner(String miner) {
		this.miner = miner;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getGasLimit() {
		return gasLimit;
	}
	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
	}
	public String getGasUsed() {
		return gasUsed;
	}
	public void setGasUsed(String gasUsed) {
		this.gasUsed = gasUsed;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	@Override
	public String toString() {
		return "Web3Block2 [blockNumber=" + blockNumber + ", blockHash=" + blockHash + ", miner=" + miner
				+ ", timeStamp=" + timeStamp + ", gasLimit=" + gasLimit + ", gasUsed=" + gasUsed + ", nonce=" + nonce
				+ "]";
	}	
}
