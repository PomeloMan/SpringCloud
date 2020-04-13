package io.pomelo.user.center.core.persistence.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import io.pomelo.user.center.core.enums.RoleGroup;

@Entity
@Table(name = "sys_role")
public class Role extends VersionEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	private String displayName;
	private RoleGroup roleGroup = RoleGroup.SYS_ADMIN;

	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<Authority> authorities;

	public Role(String name) {
		super();
		this.name = name;
	}

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public RoleGroup getRoleGroup() {
		return roleGroup;
	}

	public void setRoleGroup(RoleGroup roleGroup) {
		this.roleGroup = roleGroup;
	}

	public Collection<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<Authority> authorities) {
		this.authorities = authorities;
	}

	// 角色组信息
	public String getRoleGroupDesc() {
		return this.roleGroup.getDescription();
	}

	public int getRoleGroupCode() {
		return this.roleGroup.getCode();
	}
}
