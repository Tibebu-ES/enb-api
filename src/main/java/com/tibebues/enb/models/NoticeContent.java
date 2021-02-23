package com.tibebues.enb.models;

import java.sql.Blob;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="noticeContent")
public class NoticeContent {
	
	public static final String CONTENT_TYPE_IMAGE = "image";
	public static final String CONTENT_TYPE_VIDEO = "video";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "notice_url")
	private String noticeURL ;
	
	
	
	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "notice_id")
	private Notice notice;

	
	
	public NoticeContent() {
		super();
	}
	


	public NoticeContent(long id, String type, String noticeURL) {
		super();
		this.id = id;
		this.type = type;
		this.noticeURL = noticeURL;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	

	public String getNoticeURL() {
		return noticeURL;
	}



	public void setNoticeURL(String noticeURL) {
		this.noticeURL = noticeURL;
	}



	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}
	
	
	
	

}
