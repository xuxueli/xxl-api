package com.xxl.api.admin.controller.interceptor;

import com.xxl.api.admin.controller.annotation.PermessionLimit;
import com.xxl.api.admin.core.model.XxlApiUser;
import com.xxl.api.admin.service.IXxlApiUserService;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.xxl.api.admin.service.impl.XxlApiUserServiceImpl.LOGIN_IDENTITY_KEY;

/**
 * 权限拦截
 * @author xuxueli 2015-12-12 18:09:04
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private IXxlApiUserService xxlApiUserService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}

		// if limit
		HandlerMethod method = (HandlerMethod)handler;
		PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
		boolean limit = (permission != null)?permission.limit():true;
		int type = (permission != null)?permission.type():0;

		// login user
		XxlApiUser loginUser = xxlApiUserService.ifLogin(request);
		request.setAttribute(LOGIN_IDENTITY_KEY, loginUser);

		// if pass
		boolean ifPass = false;
		if (limit) {
			if (loginUser == null) {
				ifPass = false;
			} else {
				if (type == 0) {
					// 普通用户
					ifPass = true;
				} else if (type == 1) {
					if (loginUser.getType() == 1) {
						// 超级管理员
						ifPass = true;
					} else {
						ifPass = false;
					}
				}
			}
		} else {
			ifPass = true;
		}

		if (!ifPass) {
			response.sendRedirect("/toLogin");	//request.getRequestDispatcher("/toLogin").forward(request, response);
			return false;
		}
		
		return super.preHandle(request, response, handler);
	}
	
}
