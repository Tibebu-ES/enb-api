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
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;
	
	@Column(name = "type")
	private String type;
	
	@Column(name = "video_url")
	private String videoURL ;
	
	
	@Column(name = "notice_image")
	private byte[] image;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "notice_id")
	private Notice notice;

	
	
	public NoticeContent() {
		super();
	}
	


	public NoticeContent(long id, String type, String address) {
		super();
		this.id = id;
		this.type = type;
		this.videoURL = address;
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

	

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}
	
	
	
	

}
