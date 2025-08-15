package com.xxl.api.admin.web.interceptor;

import com.xxl.api.admin.web.annotation.PermessionLimit;
import com.xxl.api.admin.model.XxlApiUser;
import com.xxl.api.admin.service.impl.LoginService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 权限拦截
 * @author xuxueli 2015-12-12 18:09:04
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

	@Resource
	private LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return true;
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

		return true;
	}
	
}
