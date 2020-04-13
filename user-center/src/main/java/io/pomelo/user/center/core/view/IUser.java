package io.pomelo.user.center.core.view;

import java.util.Collection;

import io.pomelo.commons.view.IFile;
import io.pomelo.user.center.core.persistence.entity.Authority;
import io.pomelo.user.center.core.persistence.entity.Role;
import io.pomelo.user.center.core.persistence.entity.User;
import io.swagger.annotations.ApiModel;

@ApiModel
public class IUser extends User {

	private static final long serialVersionUID = 1L;

	private String search;
	private Collection<Role> roles;
	private Collection<Authority> authorities;
	private IFile file;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public Collection<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<Authority> authorities) {
		this.authorities = authorities;
	}

	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}

}
