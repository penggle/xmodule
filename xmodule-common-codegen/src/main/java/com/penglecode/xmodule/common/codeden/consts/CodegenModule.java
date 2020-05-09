package com.penglecode.xmodule.common.codeden.consts;

public enum CodegenModule {

	MYBATIS("Mybatis"), SERVICE("Service"), CONTROLLER("Controller");
	
	private String name;

	private CodegenModule(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
