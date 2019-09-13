package com.saki9.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.saki9.api.BilibiliApiClient;
import com.saki9.dao.VideoViewDAO;
import com.saki9.pojo.TaskExecuteRecord;
import com.saki9.pojo.VideoView;
import com.saki9.repository.VideoViewRepository;
import com.saki9.service.TaskExecuteRecordService;
import com.saki9.service.VideoViewService;
import com.saki9.utils.DateUtil;

@Service
public class VideoViewServiceImpl implements VideoViewService{
	private static Logger logger = Logger.getLogger(VideoViewServiceImpl.class);
	@Autowired
    private VideoViewRepository videoViewRepository;
	@Autowired
	private VideoViewDAO videoViewDAOImpl;
	@Autowired
	private TaskExecuteRecordService taskExecuteRecordServiceImpl;

	@Transactional(rollbackOn = Exception.class)
	@Override
	public int saveVideoView(VideoView entity) {
		VideoView findByOid = videoViewDAOImpl.findByAidAndPubdate(entity.getAid(), entity.getPubdate());
		if (findByOid != null) {
			// 更新操作
			entity.setId(findByOid.getId());
			// 更新播放统计信息
			JSONArray oldstatArr = JSONArray.parseArray(findByOid.getStat());
			JSONArray newstatArr = JSONArray.parseArray(entity.getStat());
			oldstatArr.add(newstatArr.get(0));
			entity.setStat(oldstatArr.toJSONString());
			VideoView save = videoViewRepository.save(entity);
			return save == null ? -1 : 0;
		}
		entity.setAddTime(new Date());
		VideoView save = videoViewRepository.save(entity);
		return save == null ? -1 : 1;
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public int batchSave(List<VideoView> entityList, List<Long> continueAid) {
		// 批量保存不进行查询操作
		List<VideoView> collect = entityList.stream().filter(entity -> {
			entity.setAddTime(new Date());
			boolean contains = continueAid.contains(entity.getAid());
			if (contains) {
				logger.debug("视频已被手动拉取, 本次保存操作跳过, aid=" + entity.getAid());
			}
			return !contains;
		}).collect(Collectors.toList());
		videoViewRepository.save(collect);
		return collect.size();
	}
	
	@Override
	public List<VideoView> listTopView(String start, String end, String orderBy, String tname, int limit) {
		Date startDate = DateUtil.strToDate(start);
		Date endDate = DateUtil.strToDate(end);
		List<VideoView> listTopView = videoViewDAOImpl.listTopView(startDate, endDate, orderBy, tname, new PageRequest(0, limit));
		return listTopView;
	}

	@Transactional(rollbackOn = Exception.class)
	@Override
	public VideoView findByAid(Long aid) {
		VideoView findByOid = videoViewDAOImpl.findByAid(aid);
		if (findByOid != null) {
			return findByOid;
		}
		TaskExecuteRecord ter = new TaskExecuteRecord();
		ter.setAddTime(new Date());
		ter.setType(TaskExecuteRecord.TYPE.MANUALGET.VALUE);
		ter.setStartTime(new Date());
		String htmlByAid = null;
		try {
			htmlByAid = BilibiliApiClient.getHtmlByAid(aid, 1);
		} catch (Exception e) {
			logger.error("请求视频信息出错, 错误aid = " + aid + e.getMessage());
		}
		if (htmlByAid == null) {
			return null;
		}
		VideoView entity = jsonToVideoView(htmlByAid);
		entity.setAddTime(new Date());
		VideoView save = videoViewRepository.save(entity);
		// 更新拉取执行记录
		ter.setEndTime(new Date());
		ter.setAddSize(1L);
		ter.setFirstId(aid);
		ter.setLastId(aid);
		taskExecuteRecordServiceImpl.saveTaskExecuteRecord(ter);
		logger.debug("手动拉取视频信息, aid = " + aid);
		return save;
	}
	
	@Override
	public List<Map<String, Object>> tnameView(String start, String end) {
		Date startDate = DateUtil.strToDate(start), endDate = DateUtil.strToDate(end);
		List<Map<String, Object>> toptypeStatistics = videoViewDAOImpl.toptypeStatistics(startDate, endDate);
		Map<Object, List<Map<String, Object>>> collect = toptypeStatistics.stream().collect(Collectors
			.groupingBy((Map<String, Object> x) -> x.get("toptype") == null ? "其他" : x.get("toptype")));
		List<Map<String, Object>> result = new ArrayList<>();
		for (Entry<Object, List<Map<String, Object>>> entry : collect.entrySet()) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("toptype", entry.getKey());
			resultMap.put("types", entry.getValue());
			result.add(resultMap);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> upReleaseNum(String start, String end, int limit) {
		Date startDate = DateUtil.strToDate(start), endDate = DateUtil.strToDate(end);
		return videoViewDAOImpl.upReleaseNum(startDate, endDate, new PageRequest(0, limit));
	}
	
	/**
	 * @describe 将json转换为videoView
	 * @param json
	 * @return
	 */
	public static VideoView jsonToVideoView(String json) {
		// 判断JSON内容是否正确
		if (json == null || !json.contains("reduxAsyncConnect")) {
			return null;
		}
		VideoView vv = new VideoView();
		JSONObject dataJson = JSONObject.parseObject(json).getJSONObject("reduxAsyncConnect");
		JSONObject videoInfo = dataJson.getJSONObject("videoInfo");
		vv.setAid(videoInfo.getLong("aid"));
		vv.setTid(videoInfo.getLong("tid"));
		vv.setTname(videoInfo.getString("tname"));
		// 观看统计
		JSONObject stat = videoInfo.getJSONObject("stat");
		if (stat != null && !stat.isEmpty()) {
			JSONArray statArr = new JSONArray();
			stat.remove("aid");
			stat.put("updtime", System.currentTimeMillis());
			statArr.add(stat);
			vv.setStat(statArr.toJSONString());
			vv.setView(stat.getLong("view"));
			vv.setDanmaku(stat.getLong("danmaku"));
			vv.setReply(stat.getLong("reply"));
			vv.setFavorite(stat.getLong("favorite"));
			vv.setCoin(stat.getLong("coin"));
			vv.setShare(stat.getLong("share"));
			vv.setLike(stat.getLong("like"));
		}
		vv.setTitle(videoInfo.getString("title"));
		vv.setDesc(videoInfo.getString("desc"));
		// 标签
		JSONArray videoTag = dataJson.getJSONArray("videoTag");
		if (videoTag != null && !videoTag.isEmpty()) {
			vv.setTag(videoTag.toString());
		}
		vv.setPic(videoInfo.getString("pic"));
		// 投搞人信息
		JSONObject owner = videoInfo.getJSONObject("owner");
		if (owner != null && !owner.isEmpty()) {
			vv.setMid(owner.getLong("mid"));
			vv.setName(owner.getString("name"));
			vv.setFace(owner.getString("face"));
		}
		vv.setPubdate(new Date(videoInfo.getLong("pubdate") * 1000));
		vv.setToptype(videoInfo.getString("toptype"));
		// 分P信息
		JSONArray pages = videoInfo.getJSONArray("pages");
		if (pages != null && !pages.isEmpty()) {
			vv.setPageSize(pages.size());
			vv.setPages(pages.toString());
		}
		return vv;
	}
}
