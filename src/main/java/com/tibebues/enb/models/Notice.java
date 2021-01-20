package com.tibebues.enb.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notices")
public class Notice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "title")
	private String title;
	@Column(name = "message")
	private String message;
	@Column(name = "opening_date")
	private Date openingDate;
	@Column(name = "closing_date")
	private Date closingDate;
	@Column(name = "created_date")
	private Date createdDate;
	
	
	
	public Notice() {
		super();
	}
	public Notice(long id, String title, String message, Date openingDate, Date closingDate, Date createdDate) {
		super();
		this.id = id;
		this.title = title;
		this.message = message;
		this.openingDate = openingDate;
		this.closingDate = closingDate;
		this.createdDate = createdDate;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getOpeningDate() {
		return openingDate;
	}
	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}
	public Date getClosingDate() {
		return closingDate;
	}
	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
