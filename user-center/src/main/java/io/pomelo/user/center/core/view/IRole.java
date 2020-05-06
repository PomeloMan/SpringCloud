package io.pomelo.user.center.core.view;

import io.pomelo.user.center.core.persistence.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IRole extends Role {

	private static final long serialVersionUID = 1L;

	private String search;
}
