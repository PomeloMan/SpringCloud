package io.pomelo.user.center.core.persistence.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import io.pomelo.commons.validation.Update;
import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "app_todo")
public class Todo extends DefaultEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = { Update.class }, message = "{todo.id.notNull}")
	private Long id;
	@NotEmpty(groups = { Default.class, Update.class }, message = "{todo.name.notEmpty}")
	private String name;
	private Date alarmDate;
	private Date deadline;
	private String remark;
	private Integer sequence;
	@ManyToOne
	@ApiModelProperty(hidden = true)
	private TodoGroup group;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getAlarmDate() {
		return alarmDate;
	}

	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public TodoGroup getGroup() {
		return group;
	}

	public void setGroup(TodoGroup group) {
		this.group = group;
	}

}
