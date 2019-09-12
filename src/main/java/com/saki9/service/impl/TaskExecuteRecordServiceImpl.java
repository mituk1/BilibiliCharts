package com.saki9.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.saki9.dao.TaskExecuteRecordDAO;
import com.saki9.pojo.TaskExecuteRecord;
import com.saki9.repository.TaskExecuteRecordRepository;
import com.saki9.service.TaskExecuteRecordService;

@Service
public class TaskExecuteRecordServiceImpl implements TaskExecuteRecordService{
	@Autowired
	private TaskExecuteRecordRepository taskExecuteRecordRepository;
	@Autowired
	private TaskExecuteRecordDAO taskExecuteRecordDAOImpl;
	
	@Override
	public TaskExecuteRecord getFirstTaskExecuteRecord(Integer type) {
		List<TaskExecuteRecord> firstTaskExecuteRecord = taskExecuteRecordDAOImpl.getFirstTaskExecuteRecord(type, new PageRequest(0, 1));
		return firstTaskExecuteRecord == null || firstTaskExecuteRecord.isEmpty() ? null : firstTaskExecuteRecord.get(0);
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public TaskExecuteRecord saveTaskExecuteRecord(TaskExecuteRecord ter) {
		return taskExecuteRecordRepository.save(ter);
	}

	@Override
	public List<Long> getFirstTaskExecuteRecordOrderByAid(Integer type, Long firstId, Long lastId) {
		return taskExecuteRecordDAOImpl.getFirstTaskExecuteRecordOrderByAid(type, firstId, lastId);
	}
	
}
