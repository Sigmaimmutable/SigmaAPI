package com.algo.model;

public class MappingList {
	private String type;
	private String algoAddress;
	private String mappingAddress;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlgoAddress() {
		return algoAddress;
	}

	public void setAlgoAddress(String algoAddress) {
		this.algoAddress = algoAddress;
	}

	public String getMappingAddress() {
		return mappingAddress;
	}

	public void setMappingAddress(String mappingAddress) {
		this.mappingAddress = mappingAddress;
	}

	public MappingList(String type, String algoAddress, String mappingAddress) {
		super();
		this.type = type;
		this.algoAddress = algoAddress;
		this.mappingAddress = mappingAddress;
	}

	public MappingList() {
		super();
	}
}
