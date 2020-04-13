package io.pomelo.user.center.core.service.interfaces;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import io.pomelo.commons.view.IPage;
import io.pomelo.user.center.core.persistence.entity.Authority;
import io.pomelo.user.center.core.view.IAuthority;

/**
 * @ClassName IAuthorityService.java
 * @Description TODO
 * @author PomeloMan
 */
@Transactional(readOnly = true)
public interface IAuthorityService {

	/**
	 * @param view
	 * @return
	 */
	Collection<IAuthority> query(IAuthority view);

	/**
	 * @param view
	 * @param pageable
	 * @return
	 */
	Page<IAuthority> query(IPage<IAuthority> pageView, Pageable pageable);

	/**
	 * @param view
	 * @return
	 */
	@Transactional
	Authority saveOne(IAuthority view);

	/**
	 * @param authority
	 * @return
	 */
	@Transactional
	Authority saveOne(Authority entity);

	/**
	 * @param authorities
	 * @return
	 */
	@Transactional
	Collection<Authority> save(Collection<Authority> entities);

	/**
	 * @param ids
	 * @return
	 */
	@Transactional
	void delete(Collection<String> ids);
}
