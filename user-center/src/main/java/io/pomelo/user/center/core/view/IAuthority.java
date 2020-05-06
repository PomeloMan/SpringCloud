package io.pomelo.user.center.core.view;

import java.util.List;

import io.pomelo.user.center.core.persistence.entity.Authority;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class IAuthority extends Authority {

	private static final long serialVersionUID = 1L;

	private String search;
	private List<Integer> levels;
}
