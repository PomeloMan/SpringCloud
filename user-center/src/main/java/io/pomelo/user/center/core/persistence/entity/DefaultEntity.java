package io.pomelo.user.center.core.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import io.pomelo.commons.enums.Status;
import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
public abstract class DefaultEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(hidden = true)
	protected Status status = Status.Init;
	protected String creator;
	protected String modifier;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	@org.hibernate.annotations.CreationTimestamp
	@ApiModelProperty(hidden = true)
	protected Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@org.hibernate.annotations.UpdateTimestamp
	@ApiModelProperty(hidden = true)
	protected Date modifiedDate;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	// 状态信息
	@ApiModelProperty(hidden = true)
	public String getStatusDesc() {
		return this.status.getDescription();
	}

	@ApiModelProperty(hidden = true)
	public int getStatusCode() {
		return this.status.getCode();
	}
}
