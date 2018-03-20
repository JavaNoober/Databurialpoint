package com.noob.databurialpoint;

/**
 * Created by xiaoqi on 2018/3/20.
 */

public class FunctionsTable {

	//功能Id
	private long functionId;
	//操作次数
	private int operateCounts;
	//操作用户Id
	private long operatorId;

	public long getFunctionId() {
		return functionId;
	}

	public void setFunctionId(long functionId) {
		this.functionId = functionId;
	}

	public int getOperateCounts() {
		return operateCounts;
	}

	public void setOperateCounts(int operateCounts) {
		this.operateCounts = operateCounts;
	}

	public long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(long operatorId) {
		this.operatorId = operatorId;
	}
}
