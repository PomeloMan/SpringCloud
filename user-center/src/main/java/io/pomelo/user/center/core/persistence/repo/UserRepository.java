package io.pomelo.user.center.core.persistence.repo;

import java.util.Collection;
import java.util.Set;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import io.pomelo.enums.Status;
import io.pomelo.user.center.core.persistence.entity.Authority;
import io.pomelo.user.center.core.persistence.entity.Role;
import io.pomelo.user.center.core.persistence.entity.User;

public interface UserRepository extends CrudRepository<User, String>, JpaSpecificationExecutor<User> {

	@Query("select r from User u join u.roles r where u.username = :username")
	Set<Role> findRolesByUsername(@Param("username") String username);

	@Query("select distinct a from User u join u.roles r join r.authorities a where u.username = :username")
	Set<Authority> findAuthoriesByUsername(@Param("username") String username);

	/**
	 * 悲观锁 行锁，查询不会阻塞；主键相同，写操作会阻塞；主键不同，写操作不会阻塞。 场景：在无法避免或者经常发生读写冲突的情况下使用 缺点：
	 * 损失性能换取数据的绝对安全（不可能脏读） 注意：需要以主键作为查询条件（行锁），否则执行的为表锁。 其他：乐观锁 {@link User.version}
	 * 
	 * @param username
	 * @return
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE) // 执行此方法需要 @Transactional(readOnly = false)
	@Query("select u from User u where u.username = :username") // select * from user where username = 'username' for update;
	User findOneForUpdate(@Param("username") String username);

	@Query(value = "select u from User u where u.username = :id")
	User findOneById(@Param("id") String id);

	@Modifying
	@Query(value = "update User u set u.status = :status where u.username in :ids")
	void updateStatusByIds(@Param("ids") Collection<String> ids, @Param("status") Status status);
}
