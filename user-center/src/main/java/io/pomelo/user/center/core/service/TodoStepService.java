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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.pomelo.commons.enums.Status;
import io.pomelo.commons.util.BeanUtils;
import io.pomelo.user.center.core.persistence.entity.Todo;
import io.pomelo.user.center.core.persistence.entity.TodoStep;
import io.pomelo.user.center.core.persistence.repo.TodoRepository;
import io.pomelo.user.center.core.persistence.repo.TodoStepRepository;
import io.pomelo.user.center.core.service.interfaces.ITodoStepService;
import io.pomelo.user.center.core.view.ITodoStep;

@Service
public class TodoStepService implements ITodoStepService {

	private final static Logger logger = LoggerFactory.getLogger(TodoStepService.class);

	@Autowired
	private TodoStepRepository todoStepRepo;
	@Autowired
	private TodoRepository todoRepo;

	private Specification<TodoStep> getQueryClause(ITodoStep _view) {
		return new Specification<TodoStep>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<TodoStep> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				ITodoStep view = _view;
				if (view == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("The query condition is empty!");
					}
					view = new ITodoStep();
				}

				List<Predicate> restrictions = new ArrayList<Predicate>();
				restrictions.add(builder.conjunction()); // where 1=1

				// todoId
				if (view.getTodoId() != null) {
					Predicate todoIdPredicate = builder.equal(root.get("todo"), view.getTodoId());
					restrictions.add(todoIdPredicate);
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
	public Collection<ITodoStep> query(ITodoStep view) {
		return BeanUtils.transform((Collection<TodoStep>) todoStepRepo.findAll(getQueryClause(view)), ITodoStep.class);
	}

	@Override
	public ITodoStep findOne(Long id) {
		TodoStep entity = todoStepRepo.findById(id).orElse(null);
		Assert.notNull(entity, "");
		ITodoStep istep = BeanUtils.transform(entity, ITodoStep.class);
		return istep;
	}

	@Override
	public TodoStep saveOne(ITodoStep view) {
		Todo todo = todoRepo.findById(view.getTodoId()).orElse(null);
		Assert.notNull(todo, "");
		TodoStep step = BeanUtils.transform(view, TodoStep.class);
		step.setTodo(todo);
		return this.saveOne(step);
	}

	@Override
	public TodoStep saveOne(TodoStep entity) {
		Assert.notNull(entity, "");
		return todoStepRepo.save(entity);
	}

	@Override
	public Collection<TodoStep> save(Collection<ITodoStep> entities) {
		List<TodoStep> result = new ArrayList<TodoStep>();
		entities.stream().forEach(entity -> result.add(saveOne(entity)));
		return result;
	}

	@Override
	public void delete(Collection<Long> ids) {
		todoStepRepo.deleteAll(todoStepRepo.findAllById(ids));
	}

}
