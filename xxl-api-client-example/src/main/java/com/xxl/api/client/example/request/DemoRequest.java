package com.xxl.api.client.example.request;


import com.xxl.api.client.request.XxlWebRequest;
import com.xxl.api.client.request.annotation.XxlWebRequestParam;

import java.util.Date;


public class DemoRequest extends XxlWebRequest {

	private String passphrase;

	private int a;
	private int b;

	@XxlWebRequestParam(datePattern = "yyyy-MM-dd")
	private Date date;

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
