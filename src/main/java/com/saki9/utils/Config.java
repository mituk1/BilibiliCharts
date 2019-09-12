package com.saki9.utils;

public class Config {
	/**
	 * @describe HttpClient
	 */
	public final static int httpConnectTimeout = 10000;
	public final static int httpSocketTimeout = 10000;
	public final static int httpMaxPoolSize = 100;
	public final static int httpMonitorInterval = 10000;
	public final static int httpIdelTimeout = 10000;
	
	/**
	 * @describe videoView 定时器
	 * @param LAST_COUNT 定时器器每次总请求次数
	 * @param POOL_SIZE 定时器请求线程数量
	 * @param fUTURETASK_TIMEOUT 定时器请求超时时长
	 */
	public final static int LAST_COUNT = 9999;
	public final static int POOL_SIZE = 100;
	public final static Long fUTURETASK_TIMEOUT = 120000L;
}
