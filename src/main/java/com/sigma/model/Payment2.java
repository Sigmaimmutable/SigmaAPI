package com.sigma.model;

public class Payment2 {
	private String id;
	private String userId;
	private String status;
	private Double amount;
	
	public String getId() {
		return id;
	}
	public String getUserId() {
		return userId;
	}
	public String getStatus() {
		return status;
	}
	public Double getAmount() {
		return amount;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Payment2(String id, String userId, String status, Double amount) {
		super();
		this.id = id;
		this.userId = userId;
		this.status = status;
		this.amount = amount;
	}
	public Payment2() {
		super();
	}
	@Override
	public String toString() {
		return "Payment2 [id=" + id + ", userId=" + userId + ", status=" + status + ", amount=" + amount + "]";
	}
	
}
