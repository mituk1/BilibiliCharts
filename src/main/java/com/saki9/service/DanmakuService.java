package com.saki9.service;

import javax.servlet.http.HttpServletResponse;

public interface DanmakuService {
	
	/**
	 * @describe 生成视频ass弹幕文件
	 * @param aid
	 * @param p
	 * @param resp
	 * @return
	 */
	String createAssFile(Long aid, Integer p, HttpServletResponse resp);
	
	/**
	 * @describe 获取xml弹幕信息
	 * @param aid
	 * @param p
	 * @return
	 */
	String getXmlDanmaku(Long aid, Integer p);
	
}
