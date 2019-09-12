package com.saki9.service;

import java.util.List;

import com.saki9.pojo.TaskExecuteRecord;

/**
* @describe 拉取执行记录Service 
* @author saki9
* @date 2019年8月14日
* @version 1.0
 */
public interface TaskExecuteRecordService {
	
	/**
	 * @describe 根据拉取执行记录类型获取最新的记录信息
	 * @param type
	 * @param pageRequest
	 * @return
	 */
	TaskExecuteRecord getFirstTaskExecuteRecord(Integer type);
	
	/**
	 * @describe 保存拉取执行记录
	 * @param ter
	 * @return
	 */
	TaskExecuteRecord saveTaskExecuteRecord(TaskExecuteRecord ter);
	
	/**
	 * @describe 根据拉取类型获取指定aid区间的拉取执行记录，并按aid升序排序
	 * @param type 拉取执行记录类型
	 * @param pageable
	 * @return
	 */
	List<Long> getFirstTaskExecuteRecordOrderByAid(Integer type, Long firstId, Long lastId);
	
}
