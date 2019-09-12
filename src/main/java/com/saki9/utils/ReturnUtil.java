package com.saki9.utils;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
* @describe 处理返回前端的数据
* @author saki9
* @date 2019年8月15日
* @version 1.0
 */
public class ReturnUtil {
	
	/**
	 * @describe 将对象转换为Json格式数据
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T>String objReturn(T obj) {
		JSONObject jobj = new JSONObject(); 
		if (obj == null) {
			return errorReturn("查询结果为空");
		}
		if (obj instanceof List) {
			if (((List)obj).isEmpty()) {
				return errorReturn("查询结果为空");
			}
			jobj.put("data", JSONArray.parseArray(JSONArray.toJSONString(obj)));
			jobj.put("status", "OK");
			return jobj.toJSONString();
		} else {
			jobj.put("data", JSONObject.parseObject(JSONObject.toJSONString(obj)));
			jobj.put("status", "OK");
			return jobj.toJSONString();
		}
	}
	
	/**
	 * @describe 返回错误信息
	 * @param message
	 * @return
	 */
	public static String errorReturn(String message) {
		JSONObject jobj = new JSONObject();
		jobj.put("status", "ERROR");
		jobj.put("message", message);
		return jobj.toJSONString();
	}
	
	/**
	 * @describe 正确执行返回信息
	 * @param message
	 * @return
	 */
	public static String okReturn(String message) {
		JSONObject jobj = new JSONObject();
		jobj.put("status", "OK");
		jobj.put("message", message);
		return jobj.toJSONString();
	}
	
}
