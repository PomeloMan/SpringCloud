package io.pomelo.file.server.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.pomelo.file.server.core.persistence.entity.File;
import io.pomelo.file.server.core.persistence.repo.FileRepository;
import io.pomelo.file.server.core.service.interfaces.IFileService;

@Service
public class FileService implements IFileService {

	private final Logger logger = LoggerFactory.getLogger(FileService.class);

	@Autowired
	FileRepository fileRep;

	@Override
	public File findOne(String id) {
//		logger.info("find file by id {}", id);
		File file = fileRep.findById(id).orElse(null);
		if (file == null) {
//			logger.debug(String.format("Can't find file by id %s", id));
			logger.debug("Can't find file by id {}", id);
		}
		return file;
	}

	@Override
	public File saveOne(File entity) {
		Assert.notNull(entity, "");
		File _entity = fileRep.findById(entity.getId()).orElse(null);
		if (_entity != null) {
			entity.setVersion(_entity.getVersion());
		}
		return fileRep.save(entity);
	}
}
