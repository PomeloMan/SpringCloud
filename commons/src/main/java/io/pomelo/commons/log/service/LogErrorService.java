package io.pomelo.commons.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.pomelo.commons.log.persistence.entity.LogError;
import io.pomelo.commons.log.persistence.repo.LogErrorRepository;
import io.pomelo.commons.log.service.interfaces.ILogErrorService;

@Service
public class LogErrorService implements ILogErrorService {

	@Autowired
	private LogErrorRepository logErrorRepo;

	@Override
	public void save(LogError entity) {
		Assert.notNull(entity, "");
		logErrorRepo.save(entity);
	}
}
