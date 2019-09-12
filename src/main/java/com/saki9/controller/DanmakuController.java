package com.saki9.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saki9.service.DanmakuService;
import com.saki9.utils.ReturnUtil;

@Controller
public class DanmakuController {
	@Autowired
	private DanmakuService danmakuServiceImpl;
	
	/**
	 * @describe 生成视频ass弹幕文件
	 * @param aid
	 * @param p
	 * @param resp
	 * @return
	 */
	@RequestMapping("/bif/createAssFile.htm")
	@ResponseBody
	public String createAssFile(Long aid, @RequestParam(defaultValue = "1") Integer p, HttpServletResponse resp) {
		String createAssFile = danmakuServiceImpl.createAssFile(aid, p, resp);
		if (StringUtils.isBlank(createAssFile)) {
			return ReturnUtil.errorReturn("弹幕文件生成失败");
		}
		return null;
	}
}
