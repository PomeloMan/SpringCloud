package io.pomelo.user.center.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.pomelo.commons.enums.Status;
import io.pomelo.commons.util.BeanUtils;
import io.pomelo.commons.view.IPage;
import io.pomelo.commons.view.IPageImpl;
import io.pomelo.user.center.core.persistence.entity.Todo;
import io.pomelo.user.center.core.persistence.entity.TodoGroup;
import io.pomelo.user.center.core.persistence.repo.TodoGroupRepository;
import io.pomelo.user.center.core.persistence.repo.TodoRepository;
import io.pomelo.user.center.core.service.interfaces.ITodoService;
import io.pomelo.user.center.core.view.ITodo;

@Service
public class TodoService implements ITodoService {

	private final static Logger logger = LoggerFactory.getLogger(TodoService.class);

	@Autowired
	private TodoRepository todoRepo;
	@Autowired
	private TodoGroupRepository todoGroupRepo;

	private Specification<Todo> getQueryClause(ITodo _view) {
		return new Specification<Todo>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Todo> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				ITodo view = _view;
				if (view == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("The query condition is empty!");
					}
					view = new ITodo();
				}

				List<Predicate> restrictions = new ArrayList<Predicate>();
				restrictions.add(builder.conjunction()); // where 1=1

				// todoGroupId
				if (view.getGroupId() != null) {
					Predicate groupIdPredicate = builder.equal(root.get("group"), view.getGroupId());
					restrictions.add(groupIdPredicate);
				}
				// name
				if (StringUtils.isNotEmpty(view.getName())) {
					Predicate likePredicate = builder.like(root.get("name"), "%" + view.getName() + "%");
					restrictions.add(likePredicate);
				}

				restrictions.add(builder.notEqual(root.get("status"), Status.Deleted));
				query.where(builder.and(restrictions.toArray(new Predicate[restrictions.size()])));
				return query.getRestriction();
			}
		};
	}

	@Override
	public Page<ITodo> query(IPage<ITodo> pageView, Pageable pageable) {
		if (pageable == null) {
			pageable = pageView.getPageable();
		}
		Page<Todo> page = todoRepo.findAll(getQueryClause(pageView.getSearchable()), pageable);
		List<ITodo> icontent = BeanUtils.transform(page.getContent(), ITodo.class);
		return new IPageImpl<ITodo>(icontent, page.getPageable(), page.getTotalElements());
	}

	@Override
	public Collection<ITodo> query(ITodo view) {
		return BeanUtils.transform((Collection<Todo>) todoRepo.findAll(), ITodo.class);
	}

	@Override
	public ITodo findOne(Long id) {
		Todo entity = todoRepo.findById(id).orElse(null);
		Assert.notNull(entity, "");
		ITodo itodo = BeanUtils.transform(entity, ITodo.class);
		return itodo;
	}

	@Override
	public Todo saveOne(ITodo view) {
		TodoGroup group = todoGroupRepo.findById(view.getGroupId()).orElse(null);
		Assert.notNull(group, "");
		Todo todo = BeanUtils.transform(view, Todo.class);
		todo.setGroup(group);
		return this.saveOne(todo);
	}

	@Override
	public Todo saveOne(Todo entity) {
		Assert.notNull(entity, "");
		return todoRepo.save(entity);
	}

	@Override
	public Collection<Todo> save(Collection<ITodo> entities) {
		List<Todo> result = new ArrayList<Todo>();
		entities.stream().forEach(entity -> result.add(saveOne(entity)));
		return result;
	}

	@Override
	public void delete(Collection<Long> ids) {
		todoRepo.deleteAll(todoRepo.findAllById(ids));
	}

}
