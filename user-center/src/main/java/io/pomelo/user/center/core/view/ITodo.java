package io.pomelo.user.center.core.view;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import io.pomelo.commons.validation.Update;
import io.pomelo.user.center.core.persistence.entity.Todo;
import io.pomelo.user.center.core.persistence.entity.TodoStep;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class ITodo extends Todo {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("待办组主键")
	@NotNull(groups = { Default.class, Update.class }, message = "{todo.group.id.notNull}")
	Long groupId;
	@ApiModelProperty(hidden = true)
	List<TodoStep> steps;
}
