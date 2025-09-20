package com.xxl.api.admin.controller.base;

import com.xxl.api.admin.constant.Consts;
import com.xxl.api.admin.model.dto.XxlBootResourceDTO;
import com.xxl.sso.core.annotation.XxlSso;
import com.xxl.sso.core.helper.XxlSsoHelper;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * index controller
 *
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
public class IndexController {

	@RequestMapping("/")
	@XxlSso
	public String index(HttpServletRequest request, Model model) {

		// menu resource
		model.addAttribute("resourceList", getResourceList(request));

		return "base/index";
	}

	private List<XxlBootResourceDTO> getResourceList(HttpServletRequest request) {
		// init menu-list
		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr( request);
		if (XxlSsoHelper.hasRole(loginInfoResponse.getData(), Consts.ROLE_ADMIN).isSuccess()) {
			return Arrays.asList(
					new XxlBootResourceDTO(1, 0, "项目管理",1, "", "/project", "fa fa-circle-o text-red", 1, 0, null),
					new XxlBootResourceDTO(2, 0, "数据类型",1, "", "/datatype", "fa fa-circle-o text-red", 2, 0, null),
					new XxlBootResourceDTO(3, 0, "业务线管理",1, "", "/biz", "fa fa-circle-o text-red", 3, 0, null),
					new XxlBootResourceDTO(4, 0, "用户管理",1, "", "/user", "fa fa-circle-o text-red", 4, 0,null),
					new XxlBootResourceDTO(5, 0, "帮助中心",1, "", "/help", "fa fa-circle-o text-red", 5, 0, null)
			);
		} else {
			return Arrays.asList(
					new XxlBootResourceDTO(1, 0, "项目管理",1, "", "/project", "fa fa-circle-o text-red", 1, 0, null),
					new XxlBootResourceDTO(2, 0, "数据类型",1, "", "/datatype", "fa fa-circle-o text-red", 2, 0, null),
					/*new XxlBootResourceDTO(3, 0, "业务线管理",1, "", "/biz", "fa fa-circle-o text-red", 3, 0, null),
					new XxlBootResourceDTO(4, 0, "用户管理",1, "", "/user", "fa fa-circle-o text-red", 4, 0,null),*/
					new XxlBootResourceDTO(5, 0, "帮助中心",1, "", "/help", "fa fa-circle-o text-red", 5, 0, null)
			);
		}
	}

	/*@RequestMapping("/dashboard")
	@XxlSso
	public String dashboard(HttpServletRequest request, Model model) {
		return "base/dashboard";
	}*/

	@RequestMapping("/help")
	@XxlSso
	public String help() {
		return "base/help";
	}

	@RequestMapping(value = "/errorpage")
	@XxlSso(login = false)
	public ModelAndView errorPage(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) {

		String exceptionMsg = "HTTP Status Code: "+response.getStatus();

		mv.addObject("exceptionMsg", exceptionMsg);
		mv.setViewName("common/common.errorpage");
		return mv;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	
}
