package com.saki9.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.saki9.dao.VideoCountDAO;
import com.saki9.pojo.VideoCount;

@Repository
public class VideoCountDAOImpl implements VideoCountDAO{
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List<VideoCount> findNowVideoCount(Pageable pageable) {
		String sql = "select v from VideoCount v order by v.count desc";
		Query query = entityManager.createQuery(sql);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()); 
		query.setMaxResults(pageable.getPageSize());
		List<VideoCount> resultList = query.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VideoCount> findListByAddtime(Pageable pageable) {
		String sql = "select v from VideoCount v order by v.addTime desc";
		Query query = entityManager.createQuery(sql);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()); 
		query.setMaxResults(pageable.getPageSize());
		List<VideoCount> resultList = query.getResultList();
		return resultList;
	}
}
