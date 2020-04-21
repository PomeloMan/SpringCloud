package io.pomelo.commons.log.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

import io.pomelo.commons.log.persistence.entity.LogError;

@Transactional(readOnly = true)
public interface ILogErrorService {

	@Transactional(readOnly = false)
	void save(LogError entity);
}
