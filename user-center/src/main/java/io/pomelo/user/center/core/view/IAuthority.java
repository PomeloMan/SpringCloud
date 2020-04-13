package io.pomelo.user.center.core.view;

import java.util.List;

import io.pomelo.user.center.core.persistence.entity.Authority;

public class IAuthority extends Authority {

	private static final long serialVersionUID = 1L;

	private String search;
	private List<Integer> levels;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<Integer> getLevels() {
		return levels;
	}

	public void setLevels(List<Integer> levels) {
		this.levels = levels;
	}
}
