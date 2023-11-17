package com.sigma.model;

public class UserProfile {
	private String userKey;
	private String userId;
	private String userName;
	private String password;
	private String creationTime;
	private String validuser;
	private String role;
	private Double userCredits;
	@Override
	public String toString() {
		return "UserProfile [userKey=" + userKey + ", userId=" + userId + ", userName=" + userName + ", password="
				+ password + ", creationTime=" + creationTime + ", validuser=" + validuser + ", role=" + role
				+ ", userCredits=" + userCredits + "]";
	}
	public UserProfile(String userKey, String userId, String userName, String password, String creationTime,
			String validuser, String role, Double userCredits) {
		super();
		this.userKey = userKey;
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.creationTime = creationTime;
		this.validuser = validuser;
		this.role = role;
		this.userCredits = userCredits;
	}
	public UserProfile() {
		super();
	}
	public String getUserKey() {
		return userKey;
	}
	public String getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public String getPassword() {
		return password;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public String getValiduser() {
		return validuser;
	}
	public String getRole() {
		return role;
	}
	public Double getUserCredits() {
		return userCredits;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public void setValiduser(String validuser) {
		this.validuser = validuser;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public void setUserCredits(Double userCredits) {
		this.userCredits = userCredits;
	}
	
}
