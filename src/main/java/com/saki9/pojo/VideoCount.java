package com.saki9.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
* @describe 用以记录bilibili视频总量 
* @author saki9
* @date 2019年8月15日
* @version 1.0
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name = "biv_video_count")
public class VideoCount extends IdEntity {
	/** 当前视频总量 */
	private Long count;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
