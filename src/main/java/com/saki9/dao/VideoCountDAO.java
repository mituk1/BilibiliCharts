package com.saki9.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.saki9.pojo.VideoCount;

public interface VideoCountDAO {
	
	/**
	 * @describe 获取当前视频数量
	 * @return
	 */
	List<VideoCount> findNowVideoCount(Pageable pageable);
	
	/**
	 * @describe 获取最近n条视频总数记录
	 * @return
	 */
	List<VideoCount> findListByAddtime(Pageable pageable);
	
}
