package com.xxl.api.admin.controller.biz;

import com.xxl.api.admin.constant.Consts;
import com.xxl.api.admin.model.XxlApiBiz;
import com.xxl.api.admin.model.XxlApiUser;
import com.xxl.api.admin.mapper.XxlApiBizMapper;
import com.xxl.api.admin.mapper.XxlApiUserMapper;
import com.xxl.api.admin.util.I18nUtil;
import com.xxl.api.admin.util.StringTool2;
import com.xxl.sso.core.annotation.XxlSso;
import com.xxl.sso.core.helper.XxlSsoHelper;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.core.CollectionTool;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.crypto.Sha256Tool;
import com.xxl.tool.response.PageModel;
import com.xxl.tool.response.Response;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private XxlApiUserMapper xxlApiUserMapper;
	@Resource
	private XxlApiBizMapper xxlApiBizMapper;

	@RequestMapping
	@XxlSso(role = Consts.ROLE_ADMIN)
	public String index(Model model) {

		List<XxlApiBiz> bizList = xxlApiBizMapper.loadAll();
		model.addAttribute("bizList", bizList);

		return "user/user.list";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	@XxlSso(role = Consts.ROLE_ADMIN)
	public Response<PageModel<XxlApiUser>> pageList(@RequestParam(required = false, defaultValue = "0") int offset,
													@RequestParam(required = false, defaultValue = "10") int pagesize,
													String userName,
													int type) {
		// page list
		List<XxlApiUser> list = xxlApiUserMapper.pageList(offset, pagesize, userName, type);
		int list_count = xxlApiUserMapper.pageListCount(offset, pagesize, userName, type);

		// hide pwd
		if (CollectionTool.isNotEmpty(list)) {
			list.forEach(xxlApiUser -> {xxlApiUser.setPassword("***");});
		}

		// package result
		PageModel<XxlApiUser> pageModel = new PageModel<>();
		pageModel.setData( list);
		pageModel.setTotal(list_count);

		return Response.ofSuccess(pageModel);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
    @XxlSso(role = Consts.ROLE_ADMIN)
	public Response<String> add(XxlApiUser xxlApiUser) {
		// valid
		if (StringTool.isBlank(xxlApiUser.getUserName())) {
			return Response.ofFail( "请输入登录账号");
		}
		if (StringTool.isBlank(xxlApiUser.getPassword())) {
			return Response.ofFail( "请输入密码");
		}

		// valid
		XxlApiUser existUser = xxlApiUserMapper.findByUserName(xxlApiUser.getUserName());
		if (existUser != null) {
			return Response.ofFail( "“登录账号”重复，请更换");
		}

		// passowrd hash
		String passwordHash = Sha256Tool.sha256(xxlApiUser.getPassword());
		xxlApiUser.setPassword(passwordHash);

		int ret = xxlApiUserMapper.add(xxlApiUser);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
    @XxlSso(role = Consts.ROLE_ADMIN)
	public Response<String> update(HttpServletRequest request, HttpServletResponse response, XxlApiUser xxlApiUser) {

		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
		if (loginInfoResponse.getData().getUserName().equals(xxlApiUser.getUserName())) {
			return Response.ofFail("禁止操作当前登录账号");
		}

		// exist
		XxlApiUser existUser = xxlApiUserMapper.findByUserName(xxlApiUser.getUserName());
		if (existUser == null) {
			return Response.ofFail( "更新失败，登录账号非法");
		}

		// update param
		if (StringTool.isNotBlank(xxlApiUser.getPassword())) {
			if (!(xxlApiUser.getPassword().length()>=4 && xxlApiUser.getPassword().length()<=20)) {
				return Response.ofFail( "密码长度限制为4~20");
			}
			// passowrd hash
			String passwordHash = Sha256Tool.sha256(xxlApiUser.getPassword());
			existUser.setPassword(passwordHash);
		}
		existUser.setType(xxlApiUser.getType());

		int ret = xxlApiUserMapper.update(existUser);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	@XxlSso(role = Consts.ROLE_ADMIN)
	public Response<String> delete(HttpServletRequest request, @RequestParam("ids[]") List<Integer> ids) {

		// valid
		if (CollectionTool.isEmpty(ids)) {
			return Response.ofFail(I18nUtil.getString("system_param_empty"));
		}
		if (ids.size() != 1) {
			return Response.ofFail("只允许删除单条数据");
		}
		int id = ids.get(0);

		// valid user
		XxlApiUser delUser = xxlApiUserMapper.findById(id);
		if (delUser == null) {
			return Response.ofFail( "拒绝删除，用户ID非法");
		}

		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
		if (loginInfoResponse.getData().getUserName().equals(delUser.getUserName())) {
			return Response.ofFail( "禁止操作当前登录账号");
		}

		int ret = xxlApiUserMapper.delete(id);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}


	@RequestMapping("/updatePermissionBiz")
	@ResponseBody
	@XxlSso(role = Consts.ROLE_ADMIN)
	public Response<String> updatePermissionBiz(int id, @RequestParam(required = false) String[] permissionBiz){

		String permissionProjectsStr = StringTool2.join(permissionBiz, ",");
		XxlApiUser existUser = xxlApiUserMapper.findById(id);
		if (existUser == null) {
			return Response.ofFail( "参数非法");
		}
		existUser.setPermissionBiz(permissionProjectsStr);
		xxlApiUserMapper.update(existUser);

		return Response.ofSuccess();
	}

}
