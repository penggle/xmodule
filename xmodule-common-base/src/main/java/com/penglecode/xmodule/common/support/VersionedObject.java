package com.penglecode.xmodule.common.support;

import java.io.Serializable;

public class VersionedObject<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String version;
	
	private T object;

	public VersionedObject() {
		super();
	}

	public VersionedObject(String version, T object) {
		super();
		this.version = version;
		this.object = object;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

}
