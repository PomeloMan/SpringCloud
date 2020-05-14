package io.pomelo.user.center.core.view;

import io.pomelo.commons.view.IFile;
import io.pomelo.user.center.core.persistence.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class IUser extends User {

	private static final long serialVersionUID = 1L;

	private String search;
	@ApiModelProperty(hidden = true)
	private IFile file;
}
