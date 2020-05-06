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
import io.pomelo.user.center.core.persistence.entity.TodoGroup;
import io.pomelo.user.center.core.persistence.repo.TodoGroupRepository;
import io.pomelo.user.center.core.service.interfaces.ITodoGroupService;
import io.pomelo.user.center.core.view.ITodoGroup;

@Service
public class TodoGroupService implements ITodoGroupService {

	private final static Logger logger = LoggerFactory.getLogger(TodoGroupService.class);

	@Autowired
	private TodoGroupRepository todoGroupRepo;

	private Specification<TodoGroup> getQueryClause(ITodoGroup _view) {
		return new Specification<TodoGroup>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<TodoGroup> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				ITodoGroup view = _view;
				if (view == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("The query condition is empty!");
					}
					view = new ITodoGroup();
				}

				List<Predicate> restrictions = new ArrayList<Predicate>();
				restrictions.add(builder.conjunction()); // where 1=1

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
	public Page<ITodoGroup> query(IPage<ITodoGroup> pageView, Pageable pageable) {
		if (pageable == null) {
			pageable = pageView.getPageable();
		}
		Page<TodoGroup> page = todoGroupRepo.findAll(getQueryClause(pageView.getSearchable()), pageable);
		List<ITodoGroup> icontent = BeanUtils.transform(page.getContent(), ITodoGroup.class);
		return new IPageImpl<ITodoGroup>(icontent, page.getPageable(), page.getTotalElements());
	}

	@Override
	public Collection<ITodoGroup> query(ITodoGroup view) {
		return BeanUtils.transform((Collection<TodoGroup>) todoGroupRepo.findAll(), ITodoGroup.class);
	}

	@Override
	public ITodoGroup findOne(Long id) {
		TodoGroup entity = todoGroupRepo.findById(id).orElse(null);
		Assert.notNull(entity, "");
		ITodoGroup itodo = BeanUtils.transform(entity, ITodoGroup.class);
		return itodo;
	}

	@Override
	public TodoGroup saveOne(ITodoGroup view) {
		return this.saveOne(BeanUtils.transform(view, TodoGroup.class));
	}

	@Override
	public TodoGroup saveOne(TodoGroup entity) {
		Assert.notNull(entity, "");
		return todoGroupRepo.save(entity);
	}

	@Override
	public Collection<TodoGroup> save(Collection<ITodoGroup> entities) {
		List<TodoGroup> result = new ArrayList<TodoGroup>();
		entities.stream().forEach(entity -> result.add(saveOne(entity)));
		return result;
	}

	@Override
	public void delete(Collection<Long> ids) {
		todoGroupRepo.deleteAll(todoGroupRepo.findAllById(ids));
	}

}
