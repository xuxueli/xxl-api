package com.xxl.api.client.handler;


import com.xxl.api.client.request.XxlWebRequest;
import com.xxl.api.client.response.XxlWebResponse;

/**
 * request handler
 * @author xuxueli 
 * @version 2015-11-28 13:56:05
 */
public abstract class XxlWebHandler<T extends XxlWebRequest> {

	/**
	 * do some validate
	 * @param request
	 * @return
     */
	public abstract XxlWebResponse validate(T request);

	/**
	 * invoke biz handle
	 * @param request
	 * @return
     */
	public abstract XxlWebResponse handle(T request);
	
}
