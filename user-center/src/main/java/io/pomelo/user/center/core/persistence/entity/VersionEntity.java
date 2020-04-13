package io.pomelo.user.center.core.persistence.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class VersionEntity extends DefaultEntity {

	@Version
	protected int version;// spring 乐观锁@*Service.saveOne(T entity)

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
