package io.pomelo.user.center.core.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import io.pomelo.commons.enums.Status;
import io.pomelo.commons.feign.FileServiceClient;
import io.pomelo.commons.properties.FileServerProp;
import io.pomelo.commons.util.BeanUtils;
import io.pomelo.commons.util.base.DateUtil;
import io.pomelo.commons.view.IPage;
import io.pomelo.commons.view.IPageImpl;
import io.pomelo.user.center.core.persistence.entity.Authority;
import io.pomelo.user.center.core.persistence.entity.Role;
import io.pomelo.user.center.core.persistence.entity.User;
import io.pomelo.user.center.core.persistence.repo.UserRepository;
import io.pomelo.user.center.core.service.interfaces.IUserService;
import io.pomelo.user.center.core.view.IUser;

@Service
public class UserService implements IUserService {

	private final Log logger = LogFactory.getLog(UserService.class);

	@Autowired
	ConfigurableApplicationContext context;
	@Autowired
	FileServiceClient fileServiceClient;
	@Autowired
	FileServerProp fileServerProp;

	@Autowired
	UserRepository userRep;

	private Specification<User> getQueryClause(IUser view) {
		return new Specification<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

				if (view == null) {
					if (logger.isDebugEnabled()) {
						logger.debug(view);
					}
					return null;
				}

				String search = view.getSearch();

				String username = null;
				Collection<Role> roles = null;
				User user = BeanUtils.transform(view, User.class);
				if (user != null) {
					username = user.getUsername();
					roles = user.getRoles();
				}

				List<Predicate> restrictions = new ArrayList<Predicate>();
				if (StringUtils.isNotEmpty(search)) {
					Predicate fuzzyPredicate = null;
					try {
						fuzzyPredicate = builder.equal(root.get("createdDate"),
								DateUtils.parseDate(search, DateUtil.YYYY_MM_DD));
					} catch (ParseException e) {
						fuzzyPredicate = builder.or(builder.equal(root.get("username").as(String.class), search),
								builder.equal(root.get("displayName").as(String.class), search));
					}
					restrictions.add(fuzzyPredicate);
				}
				// add username condition
				if (StringUtils.isNotEmpty(username)) {
//					Predicate likePredicate = builder.like(root.get("username"), "%" + username + "%");
//					restrictions.add(likePredicate);
					// 优化模糊查询，使用locate；locate(?, user.username) >= 1
					Predicate likePredicate = builder.ge(builder.locate(root.get("username"), username), 1);
					restrictions.add(likePredicate);
				}
				// add role condition
				if (CollectionUtils.isNotEmpty(roles)) {
					Join<Role, User> roleJoin = root.join("roles", JoinType.LEFT); // left outer join users_roles
					Iterator<Role> iterator = roles.iterator();
					In<String> in = builder.in(roleJoin.get("name").as(String.class));
					while (iterator.hasNext()) {
						in.value(iterator.next().getName());
					}
					restrictions.add(in);
				}
				restrictions.add(builder.notEqual(root.get("status"), Status.Deleted));
				// add where condition
				query.where(builder.and(restrictions.toArray(new Predicate[restrictions.size()])));
				return query.getRestriction();
			}
		};
	}

	@Override
	public IUser findOne(String username) {
		User user = userRep.findOneById(username);
		IUser iuser = BeanUtils.transform(user, IUser.class);
		Set<Role> roles = userRep.findRolesByUsername(username);
		Set<Authority> auths = userRep.findAuthoriesByUsername(username);
		iuser.setRoles(roles);
		iuser.setAuthorities(auths);
		iuser.setAvatar(fileServerProp.getUserAvatarUrl() + iuser.getAvatar());
//		try {
//			ResponseEntity<IFile> response = fileServiceClient.info("user", "avatar", iuser.getAvatar());
//			if (response.getStatusCode() == HttpStatus.OK) {
//				IFile file = response.getBody();
//				iuser.setFile(file);
//				iuser.setAvatar(file.getUrl());
//			}
//		} catch (Exception e) {
//			logger.error(e);
//		}
		return iuser;
	}

	@Override
	public User findOneForUpdate(String username) {
		return userRep.findOneForUpdate(username);
	}

	@Override
	public Collection<User> query(IUser view) {
		return userRep.findAll(getQueryClause(view));
	}

	@Override
	public Page<IUser> query(IPage<IUser> pageView, Pageable pageable) {
		Page<User> page = userRep.findAll(getQueryClause(pageView.getSearchable()), pageable);
		List<IUser> icontent = BeanUtils.transform(page.getContent(), IUser.class);
		icontent.stream().forEach(iuser -> {
			iuser.setAvatar(fileServerProp.getUserAvatarUrl() + iuser.getAvatar());
//			try {
//				ResponseEntity<IFile> response = fileServiceClient.info("user", "avatar", iuser.getAvatar());
//				if (response.getStatusCode() == HttpStatus.OK) {
//					IFile file = response.getBody();
//					iuser.setFile(file);
//					iuser.setAvatar(file.getUrl());
//				}
//			} catch (Exception e) {
//				logger.error(e);
//			}
		});
		return new IPageImpl<IUser>(icontent, page.getPageable(), page.getTotalElements());
	}

	@Override
	public User saveOne(IUser view) {
		return this.saveOne(BeanUtils.transform(view, User.class));
	}

	@Override
	public User saveOne(User entity) {
		Assert.notNull(entity, "");
		Assert.notNull(entity.getUsername().equals("") ? null : entity.getUsername(), "");
		User _entity = userRep.findById(entity.getUsername()).orElse(null);
		if (_entity != null) {
			entity.setVersion(_entity.getVersion());
		}
		return userRep.save(entity);
	}

	@Override
	public Collection<User> save(Collection<User> entities) {
		List<User> result = new ArrayList<User>();
		entities.stream().forEach(entity -> result.add(saveOne(entity)));
		return result;
	}

	@Override
	public void delete(Collection<String> ids) {
		userRep.updateStatusByIds(ids, Status.Deleted);
	}
}
