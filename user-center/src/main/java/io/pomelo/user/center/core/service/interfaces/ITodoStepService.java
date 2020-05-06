package io.pomelo.user.center.core.service.interfaces;

import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import io.pomelo.user.center.core.persistence.entity.TodoStep;
import io.pomelo.user.center.core.view.ITodoStep;

@Transactional(readOnly = true)
public interface ITodoStepService {

	/**
	 * 查询所有待办项步骤
	 * 
	 * @param view
	 * @return
	 */
	Collection<ITodoStep> query(ITodoStep view);

	/**
	 * 查询具体待办项步骤
	 * 
	 * @param id
	 * @return
	 */
	ITodoStep findOne(Long id);

	/**
	 * 保存待办项步骤
	 * 
	 * @param view
	 * @return
	 */
	@Transactional
	TodoStep saveOne(ITodoStep view);

	/**
	 * 保存待办项步骤
	 * 
	 * @param view
	 * @return
	 */
	@Transactional
	TodoStep saveOne(TodoStep entity);

	/**
	 * 批量保存待办项步骤
	 * 
	 * @param roles
	 * @return
	 */
	@Transactional
	Collection<TodoStep> save(Collection<ITodoStep> entities);

	/**
	 * 删除待办项步骤
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional
	void delete(Collection<Long> ids);
}
