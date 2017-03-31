package com.xxl.api.admin.controller;

import com.xxl.api.admin.controller.annotation.PermessionLimit;
import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiUser;
import com.xxl.api.admin.core.util.JacksonUtil;
import com.xxl.api.admin.dao.IXxlApiUserDao;
import com.xxl.api.admin.service.IXxlApiUserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.xxl.api.admin.service.impl.XxlApiUserServiceImpl.LOGIN_IDENTITY_KEY;

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
	private IXxlApiUserService xxlApiUserService;

	@RequestMapping
    @PermessionLimit(superUser = true)
	public String index(Model model, HttpServletRequest request) {

		// permission
		XxlApiUser loginUser = (request.getAttribute(LOGIN_IDENTITY_KEY)!=null)? (XxlApiUser) request.getAttribute(LOGIN_IDENTITY_KEY) :null;
		if (loginUser.getType()!=1) {
			throw new RuntimeException("权限拦截.");
		}

		List<XxlApiUser> userList = xxlApiUserDao.loadAll();
		if (CollectionUtils.isEmpty(userList)) {
			userList = new ArrayList<>();
		} else {
			for (XxlApiUser user: userList) {
				user.setPassword("***");
			}
		}
		model.addAttribute("userList", JacksonUtil.writeValueAsString(userList));

		return "user/user.list";
	}

	@RequestMapping("/add")
	@ResponseBody
    @PermessionLimit(superUser = true)
	public ReturnT<String> add(XxlApiUser xxlApiUser) {
		// valid
		if (StringUtils.isBlank(xxlApiUser.getUserName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“登录账号”");
		}
		if (StringUtils.isBlank(xxlApiUser.getPassword())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“登录密码”");
		}

		// valid
		XxlApiUser existUser = xxlApiUserDao.findByUserName(xxlApiUser.getUserName());
		if (existUser != null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "“登录账号”重复，请更换");
		}

		int ret = xxlApiUserDao.add(xxlApiUser);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
    @PermessionLimit(superUser = true)
	public ReturnT<String> update(XxlApiUser xxlApiUser) {

		// exist
		XxlApiUser existUser = xxlApiUserDao.findByUserName(xxlApiUser.getUserName());
		if (existUser == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "更新失败，登录账号非法");
		}

		// update param
		if (StringUtils.isNotBlank(xxlApiUser.getPassword())) {
			existUser.setPassword(xxlApiUser.getPassword());
		}
		existUser.setType(xxlApiUser.getType());
		existUser.setRealName(xxlApiUser.getRealName());

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

		XxlApiUser loginUser = xxlApiUserService.ifLogin(request);
		if (loginUser == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，系统登录异常");
		}

		if (delUser.getUserName().equals(loginUser.getUserName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，不可以删除自己的用户信息");
		}

		// must leave one user
		List<XxlApiUser> allUser = xxlApiUserDao.loadAll();
		if (allUser==null || allUser.size()==1) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，系统至少保留一个登录用户");
		}

		int ret = xxlApiUserDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
