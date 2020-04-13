package io.pomelo.file.server.core.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import io.pomelo.file.server.core.persistence.entity.File;

public interface FileRepository extends CrudRepository<File, String>, JpaSpecificationExecutor<File> {

}
