package com.sigma.model;

/**
 * @author joeus
 *
 */
public class CloudResource2 {
	private String amiId;
	private String machineName;
	private String region;
	private String instanceType;
	public String getAmiId() {
		return amiId;
	}
	public String getMachineName() {
		return machineName;
	}
	public String getRegion() {
		return region;
	}
	public String getInstanceType() {
		return instanceType;
	}
	public void setAmiId(String amiId) {
		this.amiId = amiId;
	}
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
	@Override
	public String toString() {
		return "CloudResource2 [amiId=" + amiId + ", machineName=" + machineName + ", region=" + region
				+ ", instanceType=" + instanceType + "]";
	}
	public CloudResource2(String amiId, String machineName, String region, String instanceType) {
		super();
		this.amiId = amiId;
		this.machineName = machineName;
		this.region = region;
		this.instanceType = instanceType;
	}
	
}
