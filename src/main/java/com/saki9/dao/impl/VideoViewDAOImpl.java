package com.saki9.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.StringUtils;
import com.saki9.dao.VideoViewDAO;
import com.saki9.pojo.VideoView;

@Repository
public class VideoViewDAOImpl implements VideoViewDAO{
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public VideoView findByAid(Long aid) {
		String sql = "select v from VideoView v where v.aid = :aid";
		Query query = entityManager.createQuery(sql);
		query.setParameter("aid", aid);
		List<VideoView> resultList = query.getResultList();
		VideoView videoView = resultList.isEmpty() ? null : resultList.get(0);
		return videoView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public VideoView findByAidAndPubdate(Long aid, Date pubdate) {
		String sql = "select v from VideoView v where v.aid = :aid and v.pubdate = :pubdate";
		Query query = entityManager.createQuery(sql);
		query.setParameter("aid", aid);
		query.setParameter("pubdate", pubdate);
		List<VideoView> resultList = query.getResultList();
		VideoView videoView = resultList.isEmpty() ? null : resultList.get(0);
		return videoView;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VideoView> listTopView(Date start, Date end, String orderBy, String tname, Pageable pageable) {
		String sql = "select v from VideoView v where v.pubdate >= :start and v.pubdate <= :end";
		// 指定分区
		if (!StringUtils.isNullOrEmpty(tname)) {
			sql += " and v.tname = :tname";
		}
		// 排序方式
		if (!StringUtils.isNullOrEmpty(orderBy) && orderBy.equals("like")) {
			sql += " order by v.like desc";
		} else if (!StringUtils.isNullOrEmpty(orderBy) && orderBy.equals("danmaku")) {
			sql += " order by v.danmaku desc";
		} else if (!StringUtils.isNullOrEmpty(orderBy) && orderBy.equals("favorite")) {
			sql += " order by v.favorite desc";
		} else if (!StringUtils.isNullOrEmpty(orderBy) && orderBy.equals("coin")) {
			sql += " order by v.coin desc";
		} else {
			sql += " order by v.view desc";
		}
		Query query = entityManager.createQuery(sql);
		if (!StringUtils.isNullOrEmpty(tname)) {
			query.setParameter("tname", tname);
		}
		query.setParameter("start", start);
		query.setParameter("end", end);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()); 
		query.setMaxResults(pageable.getPageSize());
		List<VideoView> resultList = query.getResultList();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> toptypeStatistics(Date start, Date end) {
		String sql = "select new map(v.toptype as toptype, sum(v.view) as view, sum(v.like) as like, sum(v.danmaku) as danmaku, sum(v.favorite) as favorite, sum(v.coin) as coin, count(*) as count, v.tname as tname) from VideoView v where v.pubdate >= :start and v.pubdate <= :end group by v.tname, v.toptype";
		Query query = entityManager.createQuery(sql);
		query.setParameter("start", start);
		query.setParameter("end", end);
		return (List<Map<String, Object>>)query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> upReleaseNum(Date start, Date end, Pageable pageable) {
		String sql = "select new map(v.name as name, min(v.face) as face, count(*) as num) from VideoView v where v.pubdate >= :start and v.pubdate <= :end group by v.name order by num desc";
		Query query = entityManager.createQuery(sql);
		query.setParameter("start", start);
		query.setParameter("end", end);
		query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize()); 
		query.setMaxResults(pageable.getPageSize());
		return (List<Map<String, Object>>)query.getResultList();
	}
}
