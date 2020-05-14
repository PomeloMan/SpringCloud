package io.pomelo.user.center.core.persistence.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.pomelo.commons.enums.Status;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "sys_user")
public class User extends VersionEntity implements UserDetails, Serializable {

	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	// private long id;

	private static final long serialVersionUID = 1L;

	@Id
	private String username;
	private String displayName;
	@ApiModelProperty(hidden = true)
	private String password;

	private String avatar;
	private String email;
	private Integer gender;// 0:male / 1:female

	@ApiModelProperty(hidden = true)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_user_role", inverseJoinColumns = @JoinColumn(name = "role_name"), joinColumns = @JoinColumn(name = "user_username"))
	private Collection<Role> roles;

	@ApiModelProperty(hidden = true)
	@Transient
	private Collection<Authority> sysAuthorities;

	public User(String username, Status status) {
		super();
		this.username = username;
		this.status = status;
	}

	public User(String username) {
		super();
		this.username = username;
	}

	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public User() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (sysAuthorities == null) {
			return Collections.emptyList();
		}
		return sysAuthorities.stream().map(auth -> new SimpleGrantedAuthority(auth.getName()))
				.collect(Collectors.toList());
	}

	public Collection<Authority> getSysAuthorities() {
		return sysAuthorities == null ? Collections.emptyList() : sysAuthorities;
	}

	public void setSysAuthorities(Collection<Authority> customAuthorities) {
		this.sysAuthorities = customAuthorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !this.status.equals(Status.Expired);
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.status.equals(Status.Valid);
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !this.status.equals(Status.Expired);
	}

	@Override
	public boolean isEnabled() {
		return this.status.equals(Status.Valid);
	}
}
