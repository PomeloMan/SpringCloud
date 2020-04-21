package io.pomelo.commons.log.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import io.pomelo.commons.log.persistence.entity.Log;

public interface LogRepository extends CrudRepository<Log, String>, JpaSpecificationExecutor<Log> {

}
