package com.yalin.uafmessage.msg;

/**
 * Created by YaLin on 2016/1/21.
 */
public class OperationHeader {
	public Version upv;
	public Operation op;
	public String appID;
	public String serverData;
	public Extension[] exts;
}
