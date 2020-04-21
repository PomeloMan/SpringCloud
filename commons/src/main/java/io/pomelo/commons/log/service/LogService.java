package io.pomelo.commons.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.pomelo.commons.log.persistence.entity.Log;
import io.pomelo.commons.log.persistence.repo.LogRepository;
import io.pomelo.commons.log.service.interfaces.ILogService;

@Service
public class LogService implements ILogService {

	@Autowired
	private LogRepository logRepo;

	@Override
	public void save(Log entity) {
		Assert.notNull(entity, "");
		logRepo.save(entity);
	}
}
