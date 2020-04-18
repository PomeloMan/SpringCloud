package io.pomelo.user.center.core.persistence.repo;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import io.pomelo.commons.enums.Status;
import io.pomelo.user.center.core.persistence.entity.Role;

public interface RoleRepository extends CrudRepository<Role, String>, JpaSpecificationExecutor<Role> {

	@Modifying
	@Query(value = "update Role r set r.status = :status where r.name in :ids")
	void updateStatusByIds(@Param("ids") Collection<String> ids, @Param("status") Status status);
}
