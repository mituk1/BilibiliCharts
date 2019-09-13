package com.saki9.service.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.saki9.api.BilibiliApiClient;
import com.saki9.pojo.Danmaku;
import com.saki9.pojo.VideoView;
import com.saki9.service.DanmakuService;
import com.saki9.service.VideoViewService;

@Service
public class DanmakuServiceImpl implements DanmakuService{
	private static Logger logger = Logger.getLogger(DanmakuServiceImpl.class);
	@Value("${title}")
	private String title;
	@Value("${originalScript}")
	private String originalScript;
	@Value("${scriptType}")
	private String scriptType;
	@Value("${collisions}")
	private String collisions;
	@Value("${playResX}")
	private Integer playResX;
	@Value("${playResY}")
	private Integer playResY;
	@Value("${timer}")
	private String timer;
	@Value("${stylesFormat}")
	private String stylesFormat;
	@Value("${style1}")
	private String style1;
	@Value("${style2}")
	private String style2;
	@Value("${style1Timer}")
	private Integer style1Timer;
	@Value("${style2Timer}")
	private Integer style2Timer;
	@Autowired
	private VideoViewService videoViewServiceImpl;
	
	@Override
	public String createAssFile(Long aid, Integer p, HttpServletResponse resp) {
		String fileName = aid + "." + p + ".ass";
		String xmlDanmaku = getXmlDanmaku(aid, p);
		if (StringUtils.isBlank(xmlDanmaku)) {
			return null;
		}
		try (PrintWriter writer = resp.getWriter();) {
			List<Danmaku> xml2DanmakuList = xml2DanmakuList(xmlDanmaku);
			String assDanmaku = createAssDanmaku(xml2DanmakuList);
			resp.setContentType("application/octet-stream;charset=UTF-8");  
			resp.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			writer.write(assDanmaku);
		} catch (Exception e) {
			logger.error("生成ass弹幕出错," + fileName + "," + e.getMessage());
		}
		return "success";
	}

	@Override
	public String getXmlDanmakuUrl(Long aid, Integer p) {
		VideoView videoView = videoViewServiceImpl.findByAid(aid);
		if (videoView == null || StringUtils.isBlank(videoView.getPages())) {
			return null;
		}
		JSONArray pagesArr = JSONArray.parseArray(videoView.getPages());
		for (int i = 0; i < pagesArr.size(); i++) {
			JSONObject page = pagesArr.getJSONObject(i);
			if (page.getInteger("page") != p) {
				continue;
			}
			try {
				// 根据cid获取弹幕xml
				return BilibiliApiClient.DANMAKU_URL + page.getLong("cid") + ".xml";
			} catch (Exception e) {
				logger.error("获取xml弹幕链接出错, aid=" + videoView.getAid() + ",p=" + p);
			}
		}
		return null;
	}
	
	/**
	 * 获得弹幕xml
	 * @param aid
	 * @param p
	 * @return
	 */
	private String getXmlDanmaku(Long aid, Integer p) {
		// 获取视频信息
		VideoView videoView = videoViewServiceImpl.findByAid(aid);
		if (videoView == null || StringUtils.isBlank(videoView.getPages())) {
			return null;
		}
		JSONArray pagesArr = JSONArray.parseArray(videoView.getPages());
		for (int i = 0; i < pagesArr.size(); i++) {
			JSONObject page = pagesArr.getJSONObject(i);
			if (page.getInteger("page") != p) {
				continue;
			}
			try {
				// 根据cid获取弹幕xml
				return BilibiliApiClient.getDanmakuXml(page.getLong("cid"));
			} catch (Exception e) {
				logger.error("请求视频弹幕信息出错, aid=" + videoView.getAid() + ",p=" + p);
			}
		}
		return null;
	}
	
	/**
	 * @describe 从xml中获取弹幕数据
	 * @param xmlDanmaku
	 * @return
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	private List<Danmaku> xml2DanmakuList(String xmlDanmaku) throws DocumentException {
		// 解析并生成弹幕对象集合
		Document document = DocumentHelper.parseText(xmlDanmaku);
		List<Danmaku> danmakuList = new ArrayList<>();
		Element rootElement = document.getRootElement();
		List<Element> ds = rootElement.elements("d");
		ds.forEach(d -> {
			Danmaku danmaku = new Danmaku();
			String[] ps = d.attributeValue("p").split(",");
			String[] style1Arr = style1.split(",");
			String[] style2Arr = style2.split(",");
			double start = Double.parseDouble(ps[0]) * 100;
			danmaku.setText(d.getStringValue());
			danmaku.setType(getDanmakuType(ps[1]));
			if (danmaku.getType() < 0) {
				return;
			}
			danmaku.setStyle(danmaku.getType() != 0 ? style1Arr[0] : style2Arr[0]);
			danmaku.setStart((long)start);
			danmaku.setEnd((long)start + (danmaku.getType() != 0 ? style1Timer : style2Timer));
			danmaku.setColor(num2Color(Long.parseLong(ps[3])));
			danmakuList.add(danmaku);
		});
		// 按照弹幕起始时间排序
		danmakuList.sort((x, y) -> {
			return (int) (x.getStart() - y.getStart());
		});
		// 计算弹幕位置
		List<Danmaku> resultList = new ArrayList<>();
		for (Danmaku danmaku : danmakuList) {
			Integer height = countHeight(resultList, danmaku.getStart(), danmaku.getType());
			if (height == 0) {
				continue;
			}
			danmaku.setHeight(height);
			danmaku.setStartPosition(playResX + danmaku.getText().length() * 24);
			danmaku.setEndPosition(0 - danmaku.getText().length() * 24);
			resultList.add(danmaku);
		}
		return resultList;
	}
	
	/**
	 * @describe 10进制转16进制生成bgr格式色码
	 * @param num
	 * @return
	 */
	private String num2Color(Long num) {
		if (num.equals(16777215L) || num.equals(0)) {
			return "";
		}
		String header = "\\c&H";
		// 补0
		String hexString = String.format("%06x", num);
		String rr = hexString.substring(0, 2);
		String gg = hexString.substring(2, 4);
		String bb = hexString.substring(4);
		String color = (bb + gg + rr).toUpperCase();
		return header + color;
	}
	
	/**
	 * @describe 弹幕高度
	 * @param resultList
	 * @param startTime
	 * @param type
	 * @return
	 */
	private Integer countHeight(List<Danmaku> resultList, Long startTime, Integer type) {
		// 底部由下至上
		Integer height = type.equals(2) ? playResY + 25 : 0;
		while (true) {
			if (height > playResY + 25 || playResY < 0) {
				return 0;
			}
			Integer h = type.equals(2) ? (height -= 28) : (height += 28);
			long count = resultList.stream().filter(x -> {
				if (type == 0) {
					return x.getType() == type && startTime - x.getStart() < 120 && x.getHeight().equals(h);
				} else {
					return x.getType() == type && x.getEnd() > startTime && x.getHeight().equals(h);
				}
			}).count();
			if (count == 0) {
				return height;
			}
		}
	}
	
	/**
	 * 将弹幕转为ass格式字符串
	 * @param danmakuList
	 * @return
	 * @throws IOException
	 */
	private String createAssDanmaku(List<Danmaku> danmakuList) throws IOException {
		// Script Info
		StringBuilder ass = new StringBuilder();
		ass.append("[Script Info]\n");
		ass.append("Title: " + title + "\n");
		ass.append("Original Script: " + originalScript + "\n");
		ass.append("ScriptType: " + scriptType + "\n");
		ass.append("Collisions: " + collisions + "\n");
		ass.append("PlayResX: " + playResX + "\n");
		ass.append("PlayResY: " + playResY + "\n");
		ass.append("Timer: " + timer + "\n").append("\n");
		// V4+ Styles
		ass.append("[V4+ Styles]\n");
		ass.append("Title: " + stylesFormat + "\n");
		ass.append("Style: " + style1 + "\n");
		ass.append("Style: " + style2 + "\n").append("\n");
		// Events
		ass.append("[Events]\n");
		ass.append("Format: " + "Layer, Start, End, Style, Name, MarginL, MarginR, MarginV, Effect, Text" + "\n");
		for (Danmaku danmaku : danmakuList) {
			String start = formatTime(danmaku.getStart() * 10);
			String end = formatTime(danmaku.getEnd() * 10);
			ass.append("Dialogue: " + "0," + start + "," + end + "," + danmaku.getStyle() + ",,20,20,2,,{" + display(danmaku) 
				+ danmaku.getColor() + "}" + danmaku.getText() + "\n");
		}
		return ass.toString();
	}
	
	/**
	 * @describe 时间格式化
	 * @param time
	 * @return
	 */
	private String formatTime(Long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("H:mm:ss.SS");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		return formatter.format(time);
	}
	
	/**
	 * @describe 弹幕显示方式
	 * @param danmaku
	 * @return
	 */
	private String display(Danmaku danmaku) {
		if (danmaku.getType() == 0) {
			return "\\move" + "(" + danmaku.getStartPosition() + "," + danmaku.getHeight() + "," + danmaku
				.getEndPosition() + "," + danmaku.getHeight() + ")";
		} else {
			return "\\pos" + "(" + playResX / 2 + "," + danmaku.getHeight() + ")";
		}
	}

	/**
	 * @describe 滚动弹幕、顶部弹幕、底部弹幕
	 * @param t
	 * @return
	 */
	private Integer getDanmakuType(String t) {
		Integer type = -1;
		switch (t) {
		case "1":
			type = 0;
			break;
		case "5":
			type = 1;
			break;
		case "4":
			type = 2;
			break;
		}
		return type;
	}
}
