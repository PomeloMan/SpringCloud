package io.pomelo.user.center.core.view;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import io.pomelo.commons.validation.Update;
import io.pomelo.user.center.core.persistence.entity.TodoStep;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ITodoStep extends TodoStep {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("待办项主键")
	@NotNull(groups = { Default.class, Update.class }, message = "{todo.id.notNull}")
	Long todoId;
}
