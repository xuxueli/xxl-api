package com.xxl.api.admin.controller.base;

import com.xxl.api.admin.mapper.XxlApiUserMapper;
import com.xxl.api.admin.model.XxlApiUser;
import com.xxl.api.admin.util.I18nUtil;
import com.xxl.sso.core.annotation.XxlSso;
import com.xxl.sso.core.helper.XxlSsoHelper;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.crypto.Sha256Tool;
import com.xxl.tool.id.UUIDTool;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/auth")
public class LoginController {


	@Resource
	private XxlApiUserMapper xxlApiUserMapper;


	@RequestMapping("/login")
	@XxlSso(login = false)
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {

		// xxl-sso, logincheck
		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithCookie(request, response);

		if (loginInfoResponse.isSuccess()) {
			modelAndView.setView(new RedirectView("/",true,false));
			return modelAndView;
		}
		return new ModelAndView("base/login");
	}

	@RequestMapping(value="/doLogin", method=RequestMethod.POST)
	@ResponseBody
	@XxlSso(login=false)
	public Response<String> doLogin(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember){

		// param
		boolean ifRem = StringTool.isNotBlank(ifRemember) && "on".equals(ifRemember);
		if (StringTool.isBlank(userName) || StringTool.isBlank(password)){
			return Response.ofFail( "账号或密码为空" );
		}

		// do login
		XxlApiUser xxlApiUser = xxlApiUserMapper.findByUserName(userName);
		if (xxlApiUser == null) {
			return Response.ofFail("账号或密码错误");
		}

		String passwordHash = Sha256Tool.sha256(password);
		if (!passwordHash.equals(xxlApiUser.getPassword())) {
			return Response.ofFail( "账号或密码错误" );
		}

		// xxl-sso, do login
		LoginInfo loginInfo = new LoginInfo(String.valueOf(xxlApiUser.getId()), UUIDTool.getSimpleUUID());
		return XxlSsoHelper.loginWithCookie(loginInfo, response, ifRem);
	}

	@RequestMapping(value="/logout", method=RequestMethod.POST)
	@ResponseBody
	@XxlSso(login=false)
	public Response<String> logout(HttpServletRequest request, HttpServletResponse response){
		// xxl-sso, do logout
		return XxlSsoHelper.logoutWithCookie(request, response);
	}

	@RequestMapping("/updatePwd")
	@ResponseBody
	public Response<String> updatePwd(HttpServletRequest request, String oldPassword, String password){

		// valid password
		if (StringTool.isBlank(oldPassword)){
			Response.ofFail( I18nUtil.getString("system_please_input") + I18nUtil.getString("change_pwd_field_oldpwd") );
		}
		if (StringTool.isBlank(password)){
			Response.ofFail( I18nUtil.getString("system_please_input") + I18nUtil.getString("change_pwd_field_newpwd") );
		}
		password = password.trim();
		if (!(password.length()>=4 && password.length()<=20)) {
			Response.ofFail( I18nUtil.getString("system_lengh_limit")+"[4-20]" );
		}

		// md5 password
		String oldPasswordHash = Sha256Tool.sha256(oldPassword);
		String passwordHash = Sha256Tool.sha256(password);

		// load user
		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);

		// valid old pwd
		XxlApiUser existUser = xxlApiUserMapper.findByUserName(loginInfoResponse.getData().getUserName());
		if (!oldPasswordHash.equals(existUser.getPassword())) {
			return Response.ofFail(I18nUtil.getString("change_pwd_field_oldpwd") + I18nUtil.getString("system_error"));
		}

		// update pwd
		existUser.setPassword(passwordHash);
		xxlApiUserMapper.update(existUser);
		return Response.ofSuccess();
	}

}
