package com.saki9.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.saki9.pojo.TaskExecuteRecord;

public interface TaskExecuteRecordDAO {
	
	/**
	 * @describe 根据拉取类型获取最新的拉取执行记录
	 * @param type 定时器类型
	 * @param pageable
	 * @return
	 */
	List<TaskExecuteRecord> getFirstTaskExecuteRecord(Integer type, Pageable pageable);
	
	/**
	 * @describe 根据拉取类型获取指定aid区间的拉取执行记录，并按aid升序排序
	 * @param type 定时器类型
	 * @param pageable
	 * @return
	 */
	List<Long> getFirstTaskExecuteRecordOrderByAid(Integer type, Long firstId, Long lastId);
}
