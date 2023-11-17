package com.sigma.model;

import java.util.Date;

public class PieBean {
	public PieBean() {
		super();
	}

	private Date start;
	private Date bw;
	private Integer rawDocs=0;
	private Integer iRecDocs=0;
	private String monthYear;
	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public Integer getiRecDocs() {
		return iRecDocs;
	}

	public void setiRecDocs(Integer iRecDocs) {
		this.iRecDocs = iRecDocs;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getBw() {
		return bw;
	}

	public void setBw(Date bw) {
		this.bw = bw;
	}

	public PieBean(Date start, Date bw) {
		super();
		this.start = start;
		this.bw = bw;
	}

	public PieBean(Date start, Date bw, Integer rawDocs, Integer iRecDocs) {
		super();
		this.start = start;
		this.bw = bw;
		this.rawDocs = rawDocs;
		this.iRecDocs = iRecDocs;
	}

	@Override
	public String toString() {
		return "PieBean [start=" + start + ", bw=" + bw + ", rawDocs=" + rawDocs + ", iRecDocs=" + iRecDocs + "]";
	}

	public Integer getRawDocs() {
		return rawDocs;
	}

	public void setRawDocs(Integer rawDocs) {
		this.rawDocs = rawDocs;
	}
}
