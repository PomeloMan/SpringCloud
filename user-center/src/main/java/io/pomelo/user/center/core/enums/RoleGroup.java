package io.pomelo.user.center.core.enums;

public enum RoleGroup {

	/** 0 */ SYS_ADMIN(1, "系统管理员"),
	/** 1 */ SYS_USER(10, "系统用户");

	private int code;
	private String description;

	private RoleGroup(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public static RoleGroup valueOf(int value) {
		switch (value) {
		case 1:
			return SYS_ADMIN;
		case 10:
			return SYS_USER;
		default:
			return SYS_USER;
		}
	}
}
