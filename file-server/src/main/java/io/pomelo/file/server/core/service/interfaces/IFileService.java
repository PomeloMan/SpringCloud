package io.pomelo.file.server.core.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

import io.pomelo.file.server.core.persistence.entity.File;

@Transactional(readOnly = true)
public interface IFileService {

	File findOne(String id);

	@Transactional
	File saveOne(File entity);
}
