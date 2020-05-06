package io.pomelo.user.center.core.service.interfaces;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import io.pomelo.commons.view.IPage;
import io.pomelo.user.center.core.persistence.entity.Todo;
import io.pomelo.user.center.core.view.ITodo;

@Transactional(readOnly = true)
public interface ITodoService {

	/**
	 * 分页查询待办事项
	 * @param view
	 * @param pageable
	 * @return
	 */
	Page<ITodo> query(IPage<ITodo> pageView, Pageable pageable);

	/**
	 * 查询所有待办项
	 * 
	 * @param view
	 * @return
	 */
	Collection<ITodo> query(ITodo view);

	/**
	 * 查询具体待办项
	 * 
	 * @param id
	 * @return
	 */
	ITodo findOne(Long id);

	/**
	 * 保存待办项
	 * 
	 * @param view
	 * @return
	 */
	@Transactional
	Todo saveOne(ITodo view);

	/**
	 * 保存待办项
	 * 
	 * @param view
	 * @return
	 */
	@Transactional
	Todo saveOne(Todo entity);

	/**
	 * 批量保存待办项
	 * 
	 * @param roles
	 * @return
	 */
	@Transactional
	Collection<Todo> save(Collection<ITodo> entities);

	/**
	 * 删除待办项
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional
	void delete(Collection<Long> ids);
}
