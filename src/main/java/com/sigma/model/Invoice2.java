package com.sigma.model;

import java.util.Date;

/**
 * @author joeus
 *
 */
public class Invoice2 {
	private String id;
	private String userId;	
	private Double billAmount;
	private Date invoiceDate;
	private Date dueDate;
	private String status;
	private String usageIds;
	
	public String getUsageIds() {
		return usageIds;
	}
	public void setUsageIds(String usageIds) {
		this.usageIds = usageIds;
	}
	
	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}	
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public Date getDueDate() {
		return dueDate;
	}

	public String getStatus() {
		return status;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public Double getBillAmount() {
		return billAmount;
	}
	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}
	public void setStatus(String status) {
		this.status = status;
	}	

	@Override
	public String toString() {
		return "Invoice2 [id=" + id + ", userId=" + userId + ", invoiceDate=" + invoiceDate + ", dueDate=" + dueDate
				+ ", billAmount=" + billAmount + ", status=" + status + ", usageIds=" + usageIds + "]";
	}
	public Invoice2(String id, String userId, Date invoiceDate, Date dueDate, Double billAmount, String status,
			String usageIds) {
		super();
		this.id = id;
		this.userId = userId;
		this.invoiceDate = invoiceDate;
		this.dueDate = dueDate;
		this.billAmount = billAmount;
		this.status = status;
		this.usageIds = usageIds;
	}
	public Invoice2() {
		super();
	}
	
}
