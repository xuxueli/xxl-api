package com.xxl.api.admin.web.interceptor;

import com.xxl.api.admin.util.tool.ArrayTool;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * push cookies to model as cookieMap
 * @author xuxueli 2015-12-12 18:09:04
 */
@Component
public class CookieInterceptor implements HandlerInterceptor {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		if (modelAndView!=null && ArrayTool.isNotEmpty(request.getCookies())) {
			HashMap<String, Cookie> cookieMap = new HashMap<String, Cookie>();
			for (Cookie ck : request.getCookies()) {
				cookieMap.put(ck.getName(), ck);
			}
			modelAndView.addObject("cookieMap", cookieMap);
		}
		
	}
	
}
