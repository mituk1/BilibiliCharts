package com.saki9.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saki9.pojo.VideoViewLose;
import com.saki9.repository.VideoViewLoseRepository;
import com.saki9.service.VideoViewLoseService;

@Service
public class VideoViewLoseServiceImpl implements VideoViewLoseService {
	@Autowired
	private VideoViewLoseRepository videoViewLoseRepository;

	@Transactional(rollbackOn = Exception.class)
	@Override
	public void saveVideoViewLose(VideoViewLose videoViewLose) {
		videoViewLoseRepository.save(videoViewLose);
	}

}
