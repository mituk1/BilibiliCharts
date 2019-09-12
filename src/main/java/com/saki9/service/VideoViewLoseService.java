package com.saki9.service;

import com.saki9.pojo.VideoViewLose;

public interface VideoViewLoseService {
	
	/**
	 * @describe 存入未正常存入数据库中的视频信息
	 */
	public void saveVideoViewLose(VideoViewLose videoViewLose);
	
}
