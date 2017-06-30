package com.xxl.api.client.response.impl;


import com.xxl.api.client.response.XxlWebResponse;
import com.xxl.api.client.util.JacksonUtil;
import com.xxl.api.client.response.annotation.XxlWebContentType;
import org.codehaus.jackson.map.util.JSONPObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * response msg
 * @author xuxueli 2015-11-16 21:09:14
 */
public class JsonpResponse extends XxlWebResponse {
	private transient static Logger logger = LoggerFactory.getLogger(JsonpResponse.class);
	public transient static final int CODE_SUCCESS = 200;
	public transient static final int CODE_FAIL = 500;

	private String jsonp;
	private JsonResponse jsonResponse;

	public JsonpResponse(){
	}
	public JsonpResponse(String jsonp, JsonResponse jsonResponse) {
		this.jsonp = jsonp;
		this.jsonResponse = jsonResponse;
	}
	@Override
	public XxlWebContentType contentType() {
		return XxlWebContentType.JSONP;
	}

	@Override
	public String content() {
		String content = null;
		content = JacksonUtil.writeValueAsString(new JSONPObject(jsonp, jsonResponse));
		return content;
	}

	public String getJsonp() {
		return jsonp;
	}

	public void setJsonp(String jsonp) {
		this.jsonp = jsonp;
	}

	public JsonResponse getJsonResponse() {
		return jsonResponse;
	}

	public void setJsonResponse(JsonResponse jsonResponse) {
		this.jsonResponse = jsonResponse;
	}
}
