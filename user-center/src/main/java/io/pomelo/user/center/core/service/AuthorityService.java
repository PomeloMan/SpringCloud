package io.pomelo.user.center.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.pomelo.commons.util.BeanUtils;
import io.pomelo.commons.util.PageableUtil;
import io.pomelo.commons.view.IPage;
import io.pomelo.enums.Status;
import io.pomelo.user.center.core.persistence.entity.Authority;
import io.pomelo.user.center.core.persistence.repo.AuthorityRepository;
import io.pomelo.user.center.core.service.interfaces.IAuthorityService;
import io.pomelo.user.center.core.view.IAuthority;
import io.pomelo.util.common.DateUtil;

@Service
public class AuthorityService implements IAuthorityService {

	private final Log logger = LogFactory.getLog(AuthorityService.class);

	@Autowired
	AuthorityRepository authorityRep;

	private Specification<Authority> getQueryClause(final IAuthority _view) {
		return new Specification<Authority>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Authority> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				IAuthority view = _view;
				if (view == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("The query condition is empty!");
					}
					view = new IAuthority();
				}
//				Authority authority = BeanUtils.transform(view, Authority.class);
				List<Predicate> restrictions = new ArrayList<Predicate>();
				restrictions.add(builder.conjunction()); // where 1=1
				// search
				String search = view.getSearch(); // 关键字查询
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
				// parent_name
				if (StringUtils.isNotEmpty(view.getParentName())) {
					Predicate equalPredicate = builder.equal(root.get("parentName"), view.getParentName());
					restrictions.add(equalPredicate);
				}
				// level
				if (CollectionUtils.isNotEmpty(view.getLevels())) {
					CriteriaBuilder.In<Integer> in = builder.in(root.get("level"));
					view.getLevels().stream().forEach(item -> {
						in.value(item);
					});
					restrictions.add(in);
				}

				restrictions.add(builder.notEqual(root.get("status"), Status.Deleted));

				query.where(builder.and(restrictions.toArray(new Predicate[restrictions.size()])));
				query.orderBy(builder.asc(root.get("sequence"))); // order by authority0_.sequence asc
				return query.getRestriction();
			}
		};
	}

	@Override
	public Collection<IAuthority> query(IAuthority view) {
		Collection<Authority> list = authorityRep.findAll(getQueryClause(view));
		return BeanUtils.transform(list, IAuthority.class);
	}

	@Override
	public Page<IAuthority> query(IPage<IAuthority> pageView, Pageable pageable) {
		if (pageable.getSort().isUnsorted()) {
			pageable = PageableUtil.getPageRequest(pageView.getPage(), pageView.getSize(),
					new Sort(Direction.ASC, "sequence"));
		}
		Page<Authority> page = authorityRep.findAll(getQueryClause(pageView.getObject()), pageable);
		List<IAuthority> icontent = BeanUtils.transform(page.getContent(), IAuthority.class);
		return new PageImpl<IAuthority>(icontent, page.getPageable(), page.getTotalElements());
	}

	@Override
	public Authority saveOne(IAuthority view) {
		return this.saveOne(BeanUtils.transform(view, Authority.class));
	}

	@Override
	public Authority saveOne(Authority entity) {
		Assert.notNull(entity, "");
		Assert.notNull(entity.getName(), "");
		Authority _entity = authorityRep.findById(entity.getName()).orElse(null);
		if (_entity != null) {
			entity.setVersion(_entity.getVersion());
		}
		return authorityRep.save(entity);
	}

	@Override
	public Collection<Authority> save(Collection<Authority> entities) {
		List<Authority> result = new ArrayList<Authority>();
		entities.stream().forEach(entity -> result.add(saveOne(entity)));
		return result;
	}

	@Override
	public void delete(Collection<String> ids) {
		authorityRep.updateStatusByIds(ids, Status.Deleted);
	}

}
