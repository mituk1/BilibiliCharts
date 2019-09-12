package com.saki9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saki9.pojo.TaskExecuteRecord;

@Repository
public interface TaskExecuteRecordRepository extends JpaRepository<TaskExecuteRecord, Long> {

}
