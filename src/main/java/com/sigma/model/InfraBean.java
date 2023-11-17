package com.sigma.model;

import java.util.ArrayList;
import java.util.List;

public class InfraBean {
	private String provider;
	private String name;
	private String chainId;
	private String created_at;
	private String updated_at;
	private String paused_at;
	private String resumed_at;
	private Integer noOfNodes;
	private List<NodeBean> nodes = new ArrayList<NodeBean>();
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getPaused_at() {
		return paused_at;
	}
	public void setPaused_at(String paused_at) {
		this.paused_at = paused_at;
	}
	public String getResumed_at() {
		return resumed_at;
	}
	public void setResumed_at(String resumed_at) {
		this.resumed_at = resumed_at;
	}
	public List<NodeBean> getNodes() {
		return nodes;
	}
	public void setNodes(List<NodeBean> nodes) {
		this.nodes = nodes;
	}
	public Integer getNoOfNodes() {
		return noOfNodes;
	}
	public void setNoOfNodes(Integer noOfNodes) {
		this.noOfNodes = noOfNodes;
	}
	
}