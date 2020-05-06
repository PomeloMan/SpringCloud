package io.pomelo.user.center.core.persistence.repo;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import io.pomelo.user.center.core.persistence.entity.TodoStep;

public interface TodoStepRepository extends CrudRepository<TodoStep, Long>, JpaSpecificationExecutor<TodoStep> {

}
