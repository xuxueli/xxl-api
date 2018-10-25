package com.xxl.api.admin.controller.interceptor;

import com.xxl.api.admin.controller.annotation.PermessionLimit;
import com.xxl.api.admin.core.model.XxlApiUser;
import com.xxl.api.admin.service.impl.LoginService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截
 * @author xuxueli 2015-12-12 18:09:04
 */
@Component
public class PermissionInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}

		// if need login
		boolean needLogin = true;
		boolean needAdminuser = false;
		HandlerMethod method = (HandlerMethod)handler;
		PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
		if (permission!=null) {
			needLogin = permission.limit();
			needAdminuser = permission.superUser();
		}

		// if pass
		if (needLogin) {
			XxlApiUser loginUser = loginService.ifLogin(request);
			if (loginUser == null) {
				response.sendRedirect(request.getContextPath() + "/toLogin");	//request.getRequestDispatcher("/toLogin").forward(request, response);
				return false;
			}
			if (needAdminuser && loginUser.getType()!=1) {
				throw new RuntimeException("权限拦截");
			}
			request.setAttribute(LoginService.LOGIN_IDENTITY, loginUser);
		}

		return super.preHandle(request, response, handler);
	}
	
}
