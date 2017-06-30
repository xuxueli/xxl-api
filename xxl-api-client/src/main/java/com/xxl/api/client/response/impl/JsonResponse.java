package com.xxl.api.client.response.impl;

import com.xxl.api.client.response.XxlWebResponse;
import com.xxl.api.client.response.annotation.XxlWebContentType;
import com.xxl.api.client.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * response msg
 * @author xuxueli 2015-11-16 21:09:14
 */
public class JsonResponse<T> extends XxlWebResponse {
	private transient static Logger logger = LoggerFactory.getLogger(JsonResponse.class);
	public transient static final int CODE_SUCCESS = 200;
	public transient static final int CODE_FAIL = 500;

	private int code;
	private String msg;
	private T content;

	public JsonResponse(){
	}
	public JsonResponse(T content) {
		this.code = CODE_SUCCESS;
		this.content = content;
	}
	public JsonResponse(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public XxlWebContentType contentType() {
		return XxlWebContentType.JSON;
	}

	@Override
	public String content() {
		String content = null;
		content = JacksonUtil.writeValueAsString(this);
		return content;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

}
