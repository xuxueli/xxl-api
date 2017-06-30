package com.xxl.api.client.exception;

/**
 * @author xuxueli 2017-05-27 18:48:33
 */
public class XxlWebException extends RuntimeException {
	private static final long serialVersionUID = 42L;

	public XxlWebException(Exception e) {
		super(e);
	}

	public XxlWebException(String message, Exception e) {
		super(message, e);
	}

	public XxlWebException(String message) {
		super(message);
	}

}
