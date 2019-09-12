package com.saki9.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saki9.api.BilibiliApiClient;
import com.saki9.pojo.VideoView;
import com.saki9.service.VideoViewService;
import com.saki9.service.impl.VideoViewServiceImpl;

/**
* @describe 测试接口
* @author saki9
* @date 2019年8月19日
* @version 1.0
 */
@Controller
public class TestController {
	@Autowired
	private VideoViewService videoViewServiceImpl;

	/**
	 * @describe MVC Request test
	 * @return
	 */
	@RequestMapping("/bif/printWord.htm")
	@ResponseBody
	public String printWord() {
		return "success";
	}
	
	/**
	 * @describe 测试查询性能
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/bif/jpatest.htm")
	@ResponseBody
	public void jpatest() throws Exception {
		String htmlByAid = BilibiliApiClient.getHtmlByAid(63760606L, null);
		VideoView jsonToVideoView = VideoViewServiceImpl.jsonToVideoView(htmlByAid);
		videoViewServiceImpl.saveVideoView(jsonToVideoView);
	}
	
}
