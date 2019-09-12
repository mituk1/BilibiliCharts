package com.saki9.service;

import java.util.List;
import java.util.Map;

import com.saki9.pojo.VideoView;

/**
* @describe 视频信息service
* @author saki9
* @date 2019年8月13日
* @version 1.0
 */
public interface VideoViewService {
	
	/**
	 * @describe 保存视频信息
	 * @param videoViewJson
	 * @param oid 外部id
	 */
	int saveVideoView(VideoView entity);
	
	/**
	 * @describe 批量保存
	 * @param entityList
	 * @param continueAid
	 * @return 去重后的数据数
	 */
	int batchSave(List<VideoView> entityList, List<Long> continueAid);
	
	/**
	 * @describe 获取指定时间范围内视频信息
	 * @param start 起始时间
	 * @param end 结束时间
	 * @param orderBy 排序方式, 默认按照播放数
	 * @param tname 分区, 不指定则查询所有
	 * @param limit
	 * @return
	 */
	List<VideoView> listTopView(String start, String end, String orderBy, String tname, int limit);
	
	/**
	 * @describe 根据aid获取视频信息
	 * @param aid
	 * @return
	 */
	VideoView findByAid(Long aid);
	
	/**
	 * @describe 获取指定时间范围内所有分区及子分区播放数量
	 * @param start
	 * @param end
	 * @return
	 */
	List<Map<String, Object>> tnameView(String start, String end);
	
	/**
	 * @describe 获得时间范围内的投稿up,按投稿数排序
	 * @param start
	 * @param end
	 * @param pageable
	 * @return
	 */
	List<Map<String, Object>> upReleaseNum(String start, String end, int limit);
}
