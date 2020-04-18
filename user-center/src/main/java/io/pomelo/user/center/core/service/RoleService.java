package io.pomelo.user.center.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.pomelo.commons.enums.Status;
import io.pomelo.commons.util.BeanUtils;
import io.pomelo.commons.util.base.DateUtil;
import io.pomelo.commons.view.IPage;
import io.pomelo.user.center.core.persistence.entity.Role;
import io.pomelo.user.center.core.persistence.repo.RoleRepository;
import io.pomelo.user.center.core.service.interfaces.IRoleService;
import io.pomelo.user.center.core.view.IRole;

@Service
public class RoleService implements IRoleService {

	private final Log logger = LogFactory.getLog(RoleService.class);

	@Autowired
	RoleRepository roleRep;

	private Specification<Role> getQueryClause(IRole _view) {
		return new Specification<Role>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				IRole view = _view;
				if (view == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("The query condition is empty!");
					}
					view = new IRole();
				}

				List<Predicate> restrictions = new ArrayList<Predicate>();
				restrictions.add(builder.conjunction()); // where 1=1

				String search = view.getSearch();
				if (StringUtils.isNotEmpty(search)) {
					Predicate fuzzyPredicate = null;
					try {
						fuzzyPredicate = builder.equal(root.get("createdDate"),
								DateUtils.parseDate(search, DateUtil.YYYY_MM_DD));
					} catch (ParseException e) {
						fuzzyPredicate = builder.like(root.get("name"), "%" + search + "%");
					}
					restrictions.add(fuzzyPredicate);
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
	public IRole findOne(String id) {
		return BeanUtils.transform(roleRep.findById(id).orElse(null), IRole.class);
	}

	@Override
	public Collection<IRole> query(IRole view) {
		Collection<Role> list = roleRep.findAll(getQueryClause(view));
		return BeanUtils.transform(list, IRole.class);
	}

	@Override
	public Page<IRole> query(IPage<IRole> pageView, Pageable pageable) {
		if (pageable == null) {
			pageable = pageView.getPageable();
		}
		Page<Role> page = roleRep.findAll(getQueryClause(pageView.getObject()), pageable);
		List<IRole> icontent = BeanUtils.transform(page.getContent(), IRole.class);
		return new PageImpl<IRole>(icontent, page.getPageable(), page.getTotalElements());
	}

	@Override
	public Role saveOne(IRole view) {
		return this.saveOne(BeanUtils.transform(view, Role.class));
	}

	@Override
	public Role saveOne(Role entity) {
		Assert.notNull(entity, "");
		Assert.notNull(entity.getName(), "");
		Role _entity = roleRep.findById(entity.getName()).orElse(null);
		if (_entity != null) {
			entity.setVersion(_entity.getVersion());
		}
		return roleRep.save(entity);
	}

	@Override
	public Collection<Role> save(Collection<Role> entities) {
		List<Role> result = new ArrayList<Role>();
		entities.stream().forEach(entity -> result.add(saveOne(entity)));
		return result;
	}

	@Override
	public void delete(Collection<String> ids) {
		roleRep.updateStatusByIds(ids, Status.Deleted);
	}
}
