package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiUser;
import com.xxl.api.admin.dao.IXxlApiUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/user")
public class XxlApiUserController {

	@Resource
	private IXxlApiUser iXxlApiUser;

	@RequestMapping
	public String index(Model model) {
		model.addAttribute("JobGroupList", null);
		return "user/user.list";
	}

	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(XxlApiUser xxlApiUser) {
		int ret = iXxlApiUser.add(xxlApiUser);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
