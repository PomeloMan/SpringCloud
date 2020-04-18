package io.pomelo.commons.view;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import io.pomelo.commons.enums.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class IFile implements Serializable {

	private static final long serialVersionUID = 1L;

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
	@ApiModelProperty(value = "创建时间")
	protected Date createdDate;
	@ApiModelProperty(value = "修改时间")
	protected Date modifiedDate;

	@ApiModelProperty(hidden = true)
	protected int version;

	public IFile() {
		super();
		this.id = UUID.randomUUID().toString();
	}

	public IFile(String originalName, String type, String path, String module) {
		super();
		this.id = UUID.randomUUID().toString();
		this.originalName = originalName;
		this.type = type;
		this.path = path;
		this.module = module;
	}
}
