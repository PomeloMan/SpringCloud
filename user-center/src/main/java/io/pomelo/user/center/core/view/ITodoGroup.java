package io.pomelo.user.center.core.view;

import java.util.List;

import io.pomelo.user.center.core.persistence.entity.Todo;
import io.pomelo.user.center.core.persistence.entity.TodoGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class ITodoGroup extends TodoGroup {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(hidden = true)
	List<Todo> todos;
}
