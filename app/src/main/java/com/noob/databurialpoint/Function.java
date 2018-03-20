package com.noob.databurialpoint;

/**
 * Created by xiaoqi on 2018/3/20
 */

public enum Function {

	LOGIN(1, "登陆"),
	REGISTER(2, "注册");

	int functionId;
	String functionName;

	Function(int functionId, String functionName) {
		this.functionId = functionId;
		this.functionName = functionName;
	}
}
