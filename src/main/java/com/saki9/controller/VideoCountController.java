package com.saki9.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saki9.service.VideoCountService;
import com.saki9.utils.ReturnUtil;

@Controller
public class VideoCountController {
	@Autowired
	private VideoCountService videoCountServiceImpl;
	
	/**
	 * @describe 查询并获取当前b站视频总数
	 * @return
	 */
	@RequestMapping("/bif/videoCount.htm")
	@ResponseBody
	public String quaryAndSaveVideoCount() {
		return ReturnUtil.objReturn(videoCountServiceImpl.saveVideoCount());
	}
	
	/**
	 * 列出最近n条视频总数记录
	 * @param size 条数
	 * @return
	 */
	@RequestMapping("/bif/listVideoCount.htm")
	@ResponseBody
	public String ListVideoCount(@RequestParam(defaultValue = "10") Integer size) {
		return ReturnUtil.objReturn(videoCountServiceImpl.ListVideoCount(size));
	}
}
