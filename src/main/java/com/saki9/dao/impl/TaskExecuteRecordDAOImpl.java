package com.saki9.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.saki9.dao.TaskExecuteRecordDAO;
import com.saki9.pojo.TaskExecuteRecord;

@Repository
public class TaskExecuteRecordDAOImpl implements TaskExecuteRecordDAO{
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<TaskExecuteRecord> getFirstTaskExecuteRecord(Integer type, Pageable pageable) {
		String sql = "select t from TaskExecuteRecord t where t.type = :type order by t.endTime desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter("type", type);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()); 
		query.setMaxResults(pageable.getPageSize());
		List<TaskExecuteRecord> resultList = query.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getFirstTaskExecuteRecordOrderByAid(Integer type, Long firstId, Long lastId) {
		String sql = "select firstId from TaskExecuteRecord t where t.type = :type and t.firstId >= :firstId and t.lastId <= :lastId order by t.firstId";
		Query query = entityManager.createQuery(sql);
		query.setParameter("type", type);
		query.setParameter("firstId", firstId);
		query.setParameter("lastId", lastId);
		List<Long> resultList = query.getResultList();
		return resultList;
	}
}
