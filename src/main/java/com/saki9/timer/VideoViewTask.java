package com.saki9.timer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.saki9.api.BilibiliApiClient;
import com.saki9.pojo.TaskExecuteRecord;
import com.saki9.pojo.VideoCount;
import com.saki9.pojo.VideoView;
import com.saki9.pojo.VideoViewLose;
import com.saki9.service.TaskExecuteRecordService;
import com.saki9.service.VideoCountService;
import com.saki9.service.VideoViewLoseService;
import com.saki9.service.VideoViewService;
import com.saki9.service.impl.VideoViewServiceImpl;
import com.saki9.utils.Config;

/**
* @describe 定时器同步视频信息 
* @author saki9
* @date 2019年8月14日
* @version 1.0
 */
@Component
public class VideoViewTask {
	private static Logger logger = Logger.getLogger(VideoViewTask.class);
	@Autowired
	private TaskExecuteRecordService taskExecuteRecordServiceImpl;
	@Autowired
	private VideoCountService videoCountServiceImpl;
	@Autowired
	private VideoViewService videoViewServiceImpl;
	@Autowired
	private VideoViewLoseService videoViewLoseServiceImpl;
	
	@Scheduled(cron="0 0/1 * * * ?")
	public void execute() {
		Long start = null, end = null;
		// 从定时器获取起始查询aid，如果定时器没有记录就以0作为起始值
		TaskExecuteRecord taskExecuteRecord = taskExecuteRecordServiceImpl.getFirstTaskExecuteRecord(TaskExecuteRecord
			.TYPE.TIMERGET.VALUE);
		start = taskExecuteRecord == null ? 1 : taskExecuteRecord.getLastId() + 1;
		taskExecuteRecord = new TaskExecuteRecord();
		taskExecuteRecord.setAddTime(new Date());
		taskExecuteRecord.setType(TaskExecuteRecord.TYPE.TIMERGET.VALUE);
		taskExecuteRecord.setStartTime(new Date());
		// 以b站最新视频aid作为结束值，最大1w
		VideoCount videoCount = videoCountServiceImpl.saveVideoCount();
		if (videoCount != null) {
			Long lastCount = videoCount.getCount();
			end = lastCount - start > Config.LAST_COUNT ? start + Config.LAST_COUNT : lastCount;
		}
		if (start == null || end == null || start >= end) {
			return;
		}
		// 开始线程
		Integer pool = Config.POOL_SIZE;
		logger.debug("定时器开始执行, 线程数 = " + pool + ", 起始aid = " + start + ", 结束aid = " + end);
		Long s1 = System.currentTimeMillis();
		ExecutorService executorService = Executors.newFixedThreadPool(pool);
		List<FutureTask<List<VideoView>>> list = new ArrayList<>();
		// 平均分配每一线程执行所需的参数
		for (int i = 0; i <= pool; i++) {
			Long vccStart = i == 0 ? start : start + ((end - start) / pool) * i + 1;
			Long vccEnd = i == pool ? end : start + ((end - start) / pool) * (i + 1);
			if (vccStart > end) {
				break;
			}
			VideoViewCallable vcc = new VideoViewCallable(vccStart, vccEnd);;
			FutureTask<List<VideoView>> task = new FutureTask<>(vcc);
			executorService.submit(task);
			list.add(task);
		}
		// 等待线程执行完毕
		List<VideoView> saveData = new ArrayList<>();
		list.forEach(x -> {
			List<VideoView> result = null;
			try {
				result = x.get(Config.fUTURETASK_TIMEOUT, TimeUnit.MILLISECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				logger.error("定时器线程出错 " + e.getMessage());
			}
			if (result != null && !result.isEmpty()) {
				saveData.addAll(result);
			}
		});
		executorService.shutdown();
		logger.debug("数据拉取耗时" + (System.currentTimeMillis() - s1) / 1000 + "s");
		Long s2 = System.currentTimeMillis();
		// 调用批量保存
		List<Long> continueAid = taskExecuteRecordServiceImpl.getFirstTaskExecuteRecordOrderByAid(TaskExecuteRecord
			.TYPE.MANUALGET.VALUE, start, end);
		int saveSize = videoViewServiceImpl.batchSave(saveData, continueAid);
		logger.debug("数据存储耗时" + (System.currentTimeMillis() - s2) / 1000 + "s");
		taskExecuteRecord.setEndTime(new Date());
		taskExecuteRecord.setAddSize((long)saveSize);
		taskExecuteRecord.setFirstId(start);
		taskExecuteRecord.setLastId(end);
		taskExecuteRecordServiceImpl.saveTaskExecuteRecord(taskExecuteRecord);
		logger.debug("定时器执行完毕, 拉取信息数量 = " + saveData.size() + ", 实际保存数量=" + saveSize);
	}
	
	class VideoViewCallable implements Callable<List<VideoView>> {
		private Long start;
		private Long end;
		public VideoViewCallable(Long start, Long end) {
			super();
			this.start = start;
			this.end = end;
		}
		@Override
		public List<VideoView> call() {
			List<VideoView> result = new ArrayList<>();
			for (Long i = start; i <= end; i++) {
				String data = null;
				try {
					data = BilibiliApiClient.getHtmlByAid((long)i, null);
				} catch (Exception e) {
					VideoViewLose vvl = new VideoViewLose();
					vvl.setAddTime(new Date());
					vvl.setAid(i);
					vvl.setNote(e.getMessage());
					videoViewLoseServiceImpl.saveVideoViewLose(vvl);
					logger.error("请求视频信息出错, 错误aid = " + i + e.getMessage());
				}
				VideoView vv = VideoViewServiceImpl.jsonToVideoView(data);
				if (vv == null || vv.getTitle() == null || vv.getTitle().isEmpty() || vv.getAid() == null) {
					continue;
				}
				result.add(vv);
			}
			return result;
		}
	}
}
