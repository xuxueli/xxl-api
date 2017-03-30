package com.xxl.api.admin.service;

import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xuxueli on 17/3/30.
 */
public interface IXxlApiUserService {

    public ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, boolean ifRemember, String userName, String password);

    public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response);

    public XxlApiUser ifLogin(HttpServletRequest request);

}
