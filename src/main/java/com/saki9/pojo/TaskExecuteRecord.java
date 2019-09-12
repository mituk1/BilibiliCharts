package com.saki9.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
* @describe 拉取执行记录
* @author saki9
* @date 2019年8月14日
* @version 1.0
 */
@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name = "biv_taskexecute_record")
public class TaskExecuteRecord extends IdEntity {
	/** 拉取类型 */
	@Column(columnDefinition = "int default 0")
	private Integer type;
	/** 执行开始时间 */
	private Date startTime;
	/** 执行结束时间 */
	private Date endTime;
	/** 添加条数 */
	@Column(columnDefinition = "bigint default 0")
	private Long addSize;
	/** 拉取数据起始id */
	private Long firstId;
	/** 拉取数据结束id */
	@Column(columnDefinition = "bigint default 0")
	private Long lastId;
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getAddSize() {
		return addSize;
	}
	public void setAddSize(Long addSize) {
		this.addSize = addSize;
	}
	public Long getFirstId() {
		return firstId;
	}
	public void setFirstId(Long firstId) {
		this.firstId = firstId;
	}
	public Long getLastId() {
		return lastId;
	}
	public void setLastId(Long lastId) {
		this.lastId = lastId;
	}
	
	public static enum TYPE {
		/** 定时器拉取 */
		TIMERGET(0),
		/** 定时器更新 */
		TIMEUPDATE(1),
		/** 手动拉取 */
		MANUALGET(2);
		public int VALUE;
		private TYPE(int VALUE) {
			this.VALUE = VALUE;
		}
	}
}
