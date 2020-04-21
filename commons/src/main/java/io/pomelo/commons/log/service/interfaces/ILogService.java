package io.pomelo.commons.log.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

import io.pomelo.commons.log.persistence.entity.Log;

@Transactional(readOnly = true)
public interface ILogService {

	@Transactional(readOnly = false)
	void save(Log log);

}
