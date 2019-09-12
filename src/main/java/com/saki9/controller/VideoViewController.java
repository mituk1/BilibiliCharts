package com.saki9.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saki9.service.VideoViewService;
import com.saki9.utils.DateUtil;
import com.saki9.utils.ReturnUtil;

@Controller
public class VideoViewController {
	@Autowired
	private VideoViewService videoViewServiceImpl;
	
	/**
	 * @describe 获取指定范围视频排行信息
	 * @param start
	 * @param end
	 * @param orderBy 排序方式
	 * @param tname 分区名
	 * @param limit
	 * @return
	 */
	@RequestMapping("/bif/listTopView.htm")
	@ResponseBody
	public String listTopView(String start, String end, String orderBy, String tname, @RequestParam(defaultValue = "10") int limit) {
		if (start == null || end == null) {
			end = DateUtil.getStringDateShort();
			start = DateUtil.getNextDay(end, "-90");
		}
		if (DateUtil.strToDate(start).after(DateUtil.strToDate(end))) {
			return ReturnUtil.errorReturn("起始时间不应超过结束时间");
		}
		if (start != null && end != null) {
			long days = DateUtil.getDays(end, start);
			if (days > 90) {
				return ReturnUtil.errorReturn("时间范围不能超过90天");
			}
		}
		return ReturnUtil.objReturn(videoViewServiceImpl.listTopView(start, end, orderBy, tname, limit));
	}
	
	/**
	 * @describe 根据aid获取视频信息
	 * @param aid
	 * @return
	 */
	@RequestMapping("/bif/videoView.htm")
	@ResponseBody
	public String videoView(Long aid) {
		if (aid == null) {
			return ReturnUtil.errorReturn("aid不能为空");
		}
		return ReturnUtil.objReturn(videoViewServiceImpl.findByAid(aid));
	}
	
	/**
	 * @describe 获取指定时间范围内所有分区及子分区播放数量
	 * @param start yyyy-MM-dd
	 * @param end yyyy-MM-dd
	 * @return
	 */
	@RequestMapping("/bif/tnameView.htm")
	@ResponseBody
	public String tnameView(String start, String end) {
		if (start == null || end == null) {
			end = DateUtil.getStringDateShort();
			start = DateUtil.getNextDay(end, "-90");
		}
		if (DateUtil.strToDate(start).after(DateUtil.strToDate(end))) {
			return ReturnUtil.errorReturn("起始时间不应超过结束时间");
		}
		if (start != null && end != null) {
			long days = DateUtil.getDays(end, start);
			if (days > 90) {
				return ReturnUtil.errorReturn("时间范围不能超过90天");
			}
		}
		return ReturnUtil.objReturn(videoViewServiceImpl.tnameView(start, end));
	}
	
	/**
	 * @describe 获得时间范围内的投稿up,按投稿数排序
	 * @param start
	 * @param end
	 * @param limit
	 * @return
	 */
	@RequestMapping("/bif/upReleaseNum.htm")
	@ResponseBody
	public String upReleaseNum(String start, String end, @RequestParam(defaultValue = "50") int limit) {
		if (start == null || end == null) {
			end = DateUtil.getStringDateShort();
			start = DateUtil.getNextDay(end, "-90");
		}
		if (DateUtil.strToDate(start).after(DateUtil.strToDate(end))) {
			return ReturnUtil.errorReturn("起始时间不应超过结束时间");
		}
		if (start != null && end != null) {
			long days = DateUtil.getDays(end, start);
			if (days > 90) {
				return ReturnUtil.errorReturn("时间范围不能超过90天");
			}
		}
		return ReturnUtil.objReturn(videoViewServiceImpl.upReleaseNum(start, end, limit));
	}
}
