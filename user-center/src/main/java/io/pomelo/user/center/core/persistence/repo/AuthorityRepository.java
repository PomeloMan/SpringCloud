package io.pomelo.user.center.core.persistence.repo;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import io.pomelo.commons.enums.Status;
import io.pomelo.user.center.core.persistence.entity.Authority;

public interface AuthorityRepository extends CrudRepository<Authority, String>, JpaSpecificationExecutor<Authority> {

	@Modifying
	@Query(value = "update Authority a set a.status = :status where a.name in :ids")
	void updateStatusByIds(@Param("ids") Collection<String> ids, @Param("status") Status status);

}
