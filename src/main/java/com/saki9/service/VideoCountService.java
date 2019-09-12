package com.saki9.service;

import java.util.List;

import com.saki9.pojo.VideoCount;

/**
* @describe 视频总数service 
* @author renJunBo
* @date 2019年8月15日
* @version 1.0
 */
public interface VideoCountService {
	
	/**
	 * @describe 通过请求获取当前b站视频总数并保存
	 * @return
	 */
	VideoCount saveVideoCount();
	
	/**
	 * @describe 列出最近n条视频总数记录
	 * @return
	 */
	List<VideoCount> ListVideoCount(Integer size);
	
}
