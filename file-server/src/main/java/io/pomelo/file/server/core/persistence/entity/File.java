package io.pomelo.file.server.core.persistence.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import io.pomelo.commons.enums.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Entity
@Table(name = "sys_file")
@Data
@ApiModel
public class File implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ApiModelProperty(value = "主键")
	private String id;
	@ApiModelProperty(value = "文件名")
	private String originalName;
	@ApiModelProperty(value = "文件类型")
	private String type;
	@ApiModelProperty(value = "文件存储路径")
	private String path;
	@ApiModelProperty(value = "所属模块")
	private String module;
	@ApiModelProperty(value = "文件访问地址")
	private String url;

	@ApiModelProperty(hidden = true)
	protected Status status = Status.Init;
	@ApiModelProperty(value = "创建人")
	protected String creator;
	@ApiModelProperty(value = "修改人")
	protected String modifier;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	@org.hibernate.annotations.CreationTimestamp
	@ApiModelProperty(value = "创建时间")
	protected Date createdDate;
	@Temporal(TemporalType.TIMESTAMP)
	@org.hibernate.annotations.UpdateTimestamp
	@ApiModelProperty(value = "修改时间")
	protected Date modifiedDate;

	@Version
	@ApiModelProperty(hidden = true)
	protected int version;

	public File() {
		super();
		this.id = UUID.randomUUID().toString();
	}

	public File(String originalName, String type, String path, String module) {
		super();
		this.id = UUID.randomUUID().toString();
		this.originalName = originalName;
		this.type = type;
		this.path = path;
		this.module = module;
	}
}
