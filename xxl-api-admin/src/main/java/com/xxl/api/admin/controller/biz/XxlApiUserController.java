package com.xxl.api.admin.controller.biz;

import com.xxl.api.admin.model.XxlApiBiz;
import com.xxl.api.admin.model.XxlApiUser;
import com.xxl.api.admin.mapper.XxlApiBizMapper;
import com.xxl.api.admin.mapper.XxlApiUserMapper;
import com.xxl.api.admin.util.StringTool2;
import com.xxl.sso.core.annotation.XxlSso;
import com.xxl.sso.core.helper.XxlSsoHelper;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.Response;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class XxlApiUserController {

	@Resource
	private XxlApiUserMapper xxlApiUserDao;
	@Resource
	private XxlApiBizMapper xxlApiBizDao;

	@RequestMapping
    @XxlSso(role = "admin")
	public String index(Model model) {

		List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
		model.addAttribute("bizList", bizList);

		return "user/user.list";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	@XxlSso(role = "admin")
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
										@RequestParam(required = false, defaultValue = "10") int length,
										String userName, int type) {
		// page list
		List<XxlApiUser> list = xxlApiUserDao.pageList(start, length, userName, type);
		int list_count = xxlApiUserDao.pageListCount(start, length, userName, type);

		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("recordsTotal", list_count);		// 总记录数
		maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
		maps.put("data", list);  					// 分页列表
		return maps;
	}

	@RequestMapping("/add")
	@ResponseBody
    @XxlSso(role = "admin")
	public Response<String> add(XxlApiUser xxlApiUser) {
		// valid
		if (StringTool.isBlank(xxlApiUser.getUserName())) {
			return Response.ofFail( "请输入登录账号");
		}
		if (StringTool.isBlank(xxlApiUser.getPassword())) {
			return Response.ofFail( "请输入密码");
		}

		// valid
		XxlApiUser existUser = xxlApiUserDao.findByUserName(xxlApiUser.getUserName());
		if (existUser != null) {
			return Response.ofFail( "“登录账号”重复，请更换");
		}

		// passowrd md5
		String md5Password = DigestUtils.md5DigestAsHex(xxlApiUser.getPassword().getBytes());
		xxlApiUser.setPassword(md5Password);

		int ret = xxlApiUserDao.add(xxlApiUser);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping("/update")
	@ResponseBody
    @XxlSso(role = "admin")
	public Response<String> update(HttpServletRequest request, HttpServletResponse response, XxlApiUser xxlApiUser) {

		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
		if (loginInfoResponse.getData().getUserName().equals(xxlApiUser.getUserName())) {
			return Response.ofFail("禁止操作当前登录账号");
		}

		// exist
		XxlApiUser existUser = xxlApiUserDao.findByUserName(xxlApiUser.getUserName());
		if (existUser == null) {
			return Response.ofFail( "更新失败，登录账号非法");
		}

		// update param
		if (StringTool.isNotBlank(xxlApiUser.getPassword())) {
			if (!(xxlApiUser.getPassword().length()>=4 && xxlApiUser.getPassword().length()<=50)) {
				return Response.ofFail( "密码长度限制为4~50");
			}
			// passowrd md5
			String md5Password = DigestUtils.md5DigestAsHex(xxlApiUser.getPassword().getBytes());
			existUser.setPassword(md5Password);
		}
		existUser.setType(xxlApiUser.getType());

		int ret = xxlApiUserDao.update(existUser);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping("/delete")
	@ResponseBody
	@XxlSso(role = "admin")
	public Response<String> delete(HttpServletRequest request, HttpServletResponse response, int id) {

		// valid user
		XxlApiUser delUser = xxlApiUserDao.findById(id);
		if (delUser == null) {
			return Response.ofFail( "拒绝删除，用户ID非法");
		}

		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
		if (loginInfoResponse.getData().getUserName().equals(delUser.getUserName())) {
			return Response.ofFail( "禁止操作当前登录账号");
		}

		int ret = xxlApiUserDao.delete(id);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping("/updatePwd")
	@ResponseBody
	public Response<String> updatePwd(HttpServletRequest request, HttpServletResponse response, String password){

		// new password(md5)
		if (StringTool.isBlank(password)){
			return Response.ofFail( "密码不可为空");
		}
		if (!(password.length()>=4 && password.length()<=100)) {
			return Response.ofFail( "密码长度限制为4~50");
		}
		String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

		// update pwd
		Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);

		XxlApiUser existUser = xxlApiUserDao.findByUserName(loginInfoResponse.getData().getUserName());
		existUser.setPassword(md5Password);
		xxlApiUserDao.update(existUser);

		return Response.ofSuccess();
	}

	@RequestMapping("/updatePermissionBiz")
	@ResponseBody
	@XxlSso(role = "admin")
	public Response<String> updatePermissionBiz(int id, @RequestParam(required = false) String[] permissionBiz){

		String permissionProjectsStr = StringTool2.join(permissionBiz, ",");
		XxlApiUser existUser = xxlApiUserDao.findById(id);
		if (existUser == null) {
			return Response.ofFail( "参数非法");
		}
		existUser.setPermissionBiz(permissionProjectsStr);
		xxlApiUserDao.update(existUser);

		return Response.ofSuccess();
	}

}
