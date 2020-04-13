package io.pomelo.user.center.core.view;

import io.pomelo.user.center.core.persistence.entity.Role;

public class IRole extends Role {

	private static final long serialVersionUID = 1L;

	private String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
}
