package com.saki9.service.impl;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.saki9.api.BilibiliApiClient;
import com.saki9.dao.VideoCountDAO;
import com.saki9.pojo.VideoCount;
import com.saki9.repository.VideoCountRepository;
import com.saki9.service.VideoCountService;
import com.saki9.utils.HttpClientUtil;

@Service
public class VideoCountServiceImpl implements VideoCountService{
	private static Logger logger = Logger.getLogger(HttpClientUtil.class);
	@Autowired
	private VideoCountRepository videoCountRepository;
	@Autowired
	private VideoCountDAO videoCountDAOImpl;
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public VideoCount saveVideoCount() {
		VideoCount videoCount = null;
		List<VideoCount> nowCount = videoCountDAOImpl.findNowVideoCount(new PageRequest(0, 1));
		if (nowCount != null && !nowCount.isEmpty()) {
			videoCount = nowCount.get(0);
		}
		// 通过请求获取页面中最新视频信息
		String newList = null;
		try {
			newList = BilibiliApiClient.getNewList();
		} catch (Exception e) {
			logger.error("请求最新视频信息时出错" + e.getMessage());
			return videoCount;
		}
		// 通过正则获取aid最大值
		Pattern p = Pattern.compile("\"aid\":(\\d{8,}),");
		Matcher m = p.matcher(newList);
		Long most = 0L;
		while(m.find()) {
			most = Long.parseLong(m.group(1)) > most ? Long.parseLong(m.group(1)) : most;
		}
		// 如果表中没有大于该值的条目则新增
		if ((most == 0 && videoCount != null) || (videoCount != null && nowCount.get(0).getCount() >= most)) {
			return nowCount.get(0);
		}
		VideoCount count = new VideoCount();
		count.setAddTime(new Date());
		count.setCount(most);
		VideoCount save = videoCountRepository.save(count);
		return save;
	}

	@Override
	public List<VideoCount> ListVideoCount(Integer size) {
		return videoCountDAOImpl.findListByAddtime(new PageRequest(0, size));
	}

}
