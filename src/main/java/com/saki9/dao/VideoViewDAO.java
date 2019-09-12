package com.saki9.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.saki9.pojo.VideoView;

public interface VideoViewDAO {
	
	/**
	 * @describe 根据外部id获取视频信息
	 * @param aid
	 * @return
	 */
	VideoView findByAid(Long aid);
	
	/**
	 * @describe 根据外部id和视频发布日期获取视频信息
	 * @param aid
	 * @param pubdate
	 * @return
	 */
	VideoView findByAidAndPubdate(Long aid, Date pubdate);
	
	/**
	 * @describe 获取指定时间范围内视频信息
	 * @param start 起始时间
	 * @param end 结束时间
	 * @param orderBy 排序方式, 默认按照播放数
	 * @param tname 分区, 不指定则查询所有
	 * @param pageable
	 * @return
	 */
	List<VideoView> listTopView( Date start, Date end, String orderBy, String tname, Pageable pageable);
	
	/**
	 * @describe 获取分区及子分区播放信息统计
	 * @param start 起始时间
	 * @param end 结束时间
	 * @return
	 */
	List<Map<String, Object>> toptypeStatistics(Date start, Date end);
	
	/**
	 * @describe 获得时间范围内的投稿up,按投稿数排序
	 * @param start
	 * @param end
	 * @param pageable
	 * @return
	 */
	List<Map<String, Object>> upReleaseNum(Date start, Date end, Pageable pageable);
	
}
