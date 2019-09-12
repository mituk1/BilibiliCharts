package com.saki9.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @describe 未正常存入数据库中的视频信息
 * @author saki9
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name = "biv_videoview_lose")
public class VideoViewLose extends IdEntity {
	/** avid */
	private Long aid;
	/** 存入失败原因 */
	@Column(columnDefinition = "LongText")
	private String note;
	
	public Long getAid() {
		return aid;
	}
	public void setAid(Long aid) {
		this.aid = aid;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
