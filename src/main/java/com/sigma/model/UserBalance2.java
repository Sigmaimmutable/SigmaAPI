package com.sigma.model;

public class UserBalance2 {
	private String userId;
	private Double userCredits;
	@Override
	public String toString() {
		return "UserBalance2 [userId=" + userId + ", userCredits=" + userCredits + "]";
	}
	public UserBalance2(String userId, Double userCredits) {
		super();
		this.userId = userId;
		this.userCredits = userCredits;
	}
	public UserBalance2() {
		super();
	}
	public String getUserId() {
		return userId;
	}
	public Double getUserCredits() {
		return userCredits;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserCredits(Double userCredits) {
		this.userCredits = userCredits;
	}	
}
