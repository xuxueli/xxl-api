package com.xxl.api.admin.controller;

import com.xxl.api.admin.controller.annotation.PermessionLimit;
import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiBiz;
import com.xxl.api.admin.core.model.XxlApiUser;
import com.xxl.api.admin.core.util.tool.StringTool;
import com.xxl.api.admin.dao.IXxlApiBizDao;
import com.xxl.api.admin.dao.IXxlApiUserDao;
import com.xxl.api.admin.service.impl.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
	private IXxlApiUserDao xxlApiUserDao;
	@Resource
	private IXxlApiBizDao xxlApiBizDao;

	@RequestMapping
    @PermessionLimit(superUser = true)
	public String index(Model model) {

		List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
		model.addAttribute("bizList", bizList);

		return "user/user.list";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	@PermessionLimit(superUser = true)
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
    @PermessionLimit(superUser = true)
	public ReturnT<String> add(XxlApiUser xxlApiUser) {
		// valid
		if (StringTool.isBlank(xxlApiUser.getUserName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入登录账号");
		}
		if (StringTool.isBlank(xxlApiUser.getPassword())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入密码");
		}

		// valid
		XxlApiUser existUser = xxlApiUserDao.findByUserName(xxlApiUser.getUserName());
		if (existUser != null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "“登录账号”重复，请更换");
		}

		// passowrd md5
		String md5Password = DigestUtils.md5DigestAsHex(xxlApiUser.getPassword().getBytes());
		xxlApiUser.setPassword(md5Password);

		int ret = xxlApiUserDao.add(xxlApiUser);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
    @PermessionLimit(superUser = true)
	public ReturnT<String> update(HttpServletRequest request, XxlApiUser xxlApiUser) {

		XxlApiUser loginUser = (XxlApiUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
		if (loginUser.getUserName().equals(xxlApiUser.getUserName())) {
			return new ReturnT<String>(ReturnT.FAIL.getCode(), "禁止操作当前登录账号");
		}

		// exist
		XxlApiUser existUser = xxlApiUserDao.findByUserName(xxlApiUser.getUserName());
		if (existUser == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "更新失败，登录账号非法");
		}

		// update param
		if (StringTool.isNotBlank(xxlApiUser.getPassword())) {
			if (!(xxlApiUser.getPassword().length()>=4 && xxlApiUser.getPassword().length()<=50)) {
				return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码长度限制为4~50");
			}
			// passowrd md5
			String md5Password = DigestUtils.md5DigestAsHex(xxlApiUser.getPassword().getBytes());
			existUser.setPassword(md5Password);
		}
		existUser.setType(xxlApiUser.getType());

		int ret = xxlApiUserDao.update(existUser);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	@PermessionLimit(superUser = true)
	public ReturnT<String> delete(HttpServletRequest request, int id) {

		// valid user
		XxlApiUser delUser = xxlApiUserDao.findById(id);
		if (delUser == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，用户ID非法");
		}

		XxlApiUser loginUser = (XxlApiUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
		if (loginUser.getUserName().equals(delUser.getUserName())) {
			return new ReturnT<String>(ReturnT.FAIL.getCode(), "禁止操作当前登录账号");
		}

		int ret = xxlApiUserDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/updatePwd")
	@ResponseBody
	public ReturnT<String> updatePwd(HttpServletRequest request, String password){

		// new password(md5)
		if (StringTool.isBlank(password)){
			return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码不可为空");
		}
		if (!(password.length()>=4 && password.length()<=100)) {
			return new ReturnT<String>(ReturnT.FAIL.getCode(), "密码长度限制为4~50");
		}
		String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());

		// update pwd
		XxlApiUser loginUser = (XxlApiUser) request.getAttribute(LoginService.LOGIN_IDENTITY);

		XxlApiUser existUser = xxlApiUserDao.findByUserName(loginUser.getUserName());
		existUser.setPassword(md5Password);
		xxlApiUserDao.update(existUser);

		return ReturnT.SUCCESS;
	}

	@RequestMapping("/updatePermissionBiz")
	@ResponseBody
	@PermessionLimit(superUser = true)
	public ReturnT<String> updatePermissionBiz(int id,
													@RequestParam(required = false) String[] permissionBiz){

		String permissionProjectsStr = StringTool.join(permissionBiz, ",");
		XxlApiUser existUser = xxlApiUserDao.findById(id);
		if (existUser == null) {
			return new ReturnT<String>(ReturnT.FAIL.getCode(), "参数非法");
		}
		existUser.setPermissionBiz(permissionProjectsStr);
		xxlApiUserDao.update(existUser);

		return ReturnT.SUCCESS;
	}

}
