package io.pomelo.user.center.core.service.interfaces;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import io.pomelo.commons.view.IPage;
import io.pomelo.user.center.core.persistence.entity.TodoGroup;
import io.pomelo.user.center.core.view.ITodoGroup;

@Transactional(readOnly = true)
public interface ITodoGroupService {

	/**
	 * 分页查询待办组
	 * 
	 * @param view
	 * @param pageable
	 * @return
	 */
	Page<ITodoGroup> query(IPage<ITodoGroup> pageView, Pageable pageable);

	/**
	 * 查询所有待办组
	 * 
	 * @param view
	 * @return
	 */
	Collection<ITodoGroup> query(ITodoGroup view);

	/**
	 * 查询具体待办组
	 * 
	 * @param id
	 * @return
	 */
	ITodoGroup findOne(Long id);

	/**
	 * 保存待办组
	 * 
	 * @param view
	 * @return
	 */
	@Transactional
	TodoGroup saveOne(ITodoGroup view);

	/**
	 * 保存待办组
	 * 
	 * @param view
	 * @return
	 */
	@Transactional
	TodoGroup saveOne(TodoGroup entity);

	/**
	 * 批量保存待办组
	 * 
	 * @param roles
	 * @return
	 */
	@Transactional
	Collection<TodoGroup> save(Collection<ITodoGroup> entities);

	/**
	 * 删除待办组
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional
	void delete(Collection<Long> ids);
}
