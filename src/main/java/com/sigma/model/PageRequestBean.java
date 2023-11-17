package com.sigma.model;

public class PageRequestBean {
	private Integer start;
	private Integer limit;
	private String tenantId;
	private Integer priorDays = 0;
	
	public Integer getPriorDays() {
		return priorDays;
	}
	public void setPriorDays(Integer priorDays) {
		this.priorDays = priorDays;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	@Override
	public String toString() {
		return "PageRequestBean [start=" + start + ", limit=" + limit + ", tenantId=" + tenantId + "]";
	}	
}