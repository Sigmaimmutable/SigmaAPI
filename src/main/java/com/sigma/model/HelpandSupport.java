package com.sigma.model;

import java.sql.Date;

public class HelpandSupport {
	private Long id;
	
	
	private String ticket;
	private String descriptions;
	private String firstName;
	private String lastName;
	private String mailId;
	private Date ticketRaisetime;
	private String tennatId;
	private Boolean statuses;
	private String assignee;
	
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public Date getTicketRaisetime() {
		return ticketRaisetime;
	}
	public void setTicketRaisetime(Date ticketRaisetime) {
		this.ticketRaisetime = ticketRaisetime;
	}
	public String getTennatId() {
		return tennatId;
	}
	public void setTennatId(String tennatId) {
		this.tennatId = tennatId;
	}
	public Boolean getStatuses() {
		return statuses;
	}
	public void setStatuses(Boolean statuses) {
		this.statuses = statuses;
	}
		
}