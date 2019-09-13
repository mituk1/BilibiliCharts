package com.saki9.api;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.saki9.utils.HttpClientUtil;

public class BilibiliApiClient {
	/**
	 * @describe 移动端User-Agent
	 */
	public static String PHONE_USER_AGENT = "Mozilla/5.0 (Linux; U; Android 8.1.0; zh-cn; BLA-AL00 Build/HUAWEIBLA-AL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/57.0.2987.132 MQQBrowser/8.9 Mobile Safari/537.36";
	/**
	 * @describe chrome浏览器User-Agent
	 */
	public static String CHROME_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36";
	/**
	 * @describe 视频页面
	 */
	public static String HTML_URL = "https://www.bilibili.com/video/av";
	/**
	 * @describe 移动端视频页面
	 */
	public static String PHONE_HTML_URL = "https://m.bilibili.com/video/av";
	/**
	 * @describe 获取最新视频信息
	 */
	public static String NEWLIST_URL = "https://api.bilibili.com/x/web-interface/newlist";
	/**
	 * @describe 弹幕xml
	 */
	public static String DANMAKU_URL = "http://comment.bilibili.cn/";
	
	/**
	 * @describe 使用移动端User-Agent请求视频页面，获取页面HTML中视频JSON信息
	 * @param aid 视频av号
	 * @param p 分p号，不传参或参数中的p号不存在默认返回1p信息
	 * @return JSON格式的视频详细信息
	 * @throws Exception
	 */
	public static String getHtmlByAid(Long aid, Integer p) throws Exception {
		String url = PHONE_HTML_URL + aid + ".html";
		Map<String, String> headers = new HashMap<>();
		headers.put("User-Agent", PHONE_USER_AGENT);
		Map<String, String> param = new HashMap<>();
		if (p != null) {
			param.put("p", p.toString());
		}
		String doGet = HttpClientUtil.doGet(url, param, headers);
		Pattern pattern = Pattern.compile("window.__INITIAL_STATE__=(.+});");
		Matcher m = pattern.matcher(doGet);
		return m.find() ? m.group(1) : null;
	}
	
	/**
	 * @describe 使用浏览器User-Agent请求视频页面，获取播放器信息
	 * @param aid 视频av号
	 * @param p 分p号，不传参默认返回1p信息, 参数对应的分p不存在时返回null
	 * @return JSON格式播放器信息
	 * @throws Exception
	 */
	public static String getPlayinfoByAid(Long aid, Integer p) throws Exception {
		String url = HTML_URL + aid + "/";
		Map<String, String> headers = new HashMap<>();
		headers.put("User-Agent", CHROME_USER_AGENT);
		Map<String, String> param = new HashMap<>();
		if (p != null) {
			param.put("p", p.toString());
		}
		String doGet = HttpClientUtil.doGet(url, param, headers);
		Pattern pattern = Pattern.compile("window.__playinfo__=(.+})</script><script>");
		Matcher m = pattern.matcher(doGet);
		return m.find() ? m.group(1) : null;
	}
	
	/**
	 * @return 包含最新投稿信息的HTML页面
	 * @throws Exception 
	 */
	public static String getNewList() throws Exception {
		return HttpClientUtil.doGet(NEWLIST_URL, null, null);
	}
	
	/**
	 * @describe 获取xml弹幕信息
	 * @param cid
	 * @return
	 * @throws Exception
	 */
	public static String getDanmakuXml(Long cid) throws Exception {
		String url = DANMAKU_URL + cid + ".xml";
		return HttpClientUtil.doGet(url, null, null);
	}
}