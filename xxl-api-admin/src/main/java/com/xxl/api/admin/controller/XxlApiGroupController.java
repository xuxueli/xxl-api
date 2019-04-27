package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.model.*;
import com.xxl.api.admin.core.util.tool.ArrayTool;
import com.xxl.api.admin.core.util.tool.StringTool;
import com.xxl.api.admin.dao.IXxlApiDocumentDao;
import com.xxl.api.admin.dao.IXxlApiGroupDao;
import com.xxl.api.admin.dao.IXxlApiProjectDao;
import com.xxl.api.admin.service.impl.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/group")
public class XxlApiGroupController {

	@Resource
	private IXxlApiProjectDao xxlApiProjectDao;
	@Resource
	private IXxlApiGroupDao xxlApiGroupDao;
	@Resource
	private IXxlApiDocumentDao xxlApiDocumentDao;

	@RequestMapping
	public String index(HttpServletRequest request,
						Model model,
						int projectId,
						@RequestParam(required = false, defaultValue = "-1")  int groupId) {

		// 项目
		XxlApiProject xxlApiProject = xxlApiProjectDao.load(projectId);
		if (xxlApiProject == null) {
			throw new RuntimeException("系统异常，项目ID非法");
		}
		model.addAttribute("projectId", projectId);
		model.addAttribute("project", xxlApiProject);

		// 分组列表
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// 选中分组
		XxlApiGroup groupInfo = null;
		if (groupList!=null && groupList.size()>0) {
			for (XxlApiGroup groupItem: groupList) {
				if (groupId == groupItem.getId()) {
					groupInfo = groupItem;
				}
			}
		}
		if (groupId!=0 && groupInfo==null) {
			groupId = -1;
		}
		model.addAttribute("groupId", groupId);
		model.addAttribute("groupInfo", groupInfo);

		// 分组下的，接口列表
		List<XxlApiDocument> documentList = xxlApiDocumentDao.loadAll(projectId, groupId);
		model.addAttribute("documentList", documentList);

		// 权限
		model.addAttribute("hasBizPermission", hasBizPermission(request, xxlApiProject.getBizId()));

		return "group/group.list";
	}

	private boolean hasBizPermission(HttpServletRequest request, int bizId){
		XxlApiUser loginUser = (XxlApiUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
		if ( loginUser.getType()==1 ||
				ArrayTool.contains(StringTool.split(loginUser.getPermissionBiz(), ","), String.valueOf(bizId))
				) {
			return true;
		} else {
			return false;
		}
	}

	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(HttpServletRequest request, XxlApiGroup xxlApiGroup) {
		// valid
		if (StringTool.isBlank(xxlApiGroup.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“分组名称”");
		}

		// 权限校验
		XxlApiProject xxlApiProject = xxlApiProjectDao.load(xxlApiGroup.getProjectId());
		if (!hasBizPermission(request, xxlApiProject.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		int ret = xxlApiGroupDao.add(xxlApiGroup);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(HttpServletRequest request, XxlApiGroup xxlApiGroup) {
		// exist
		XxlApiGroup existGroup = xxlApiGroupDao.load(xxlApiGroup.getId());
		if (existGroup == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "更新失败，分组ID非法");
		}

		// 权限校验
		XxlApiProject xxlApiProject = xxlApiProjectDao.load(existGroup.getProjectId());
		if (!hasBizPermission(request, xxlApiProject.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		// valid
		if (StringTool.isBlank(xxlApiGroup.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“分组名称”");
		}

		int ret = xxlApiGroupDao.update(xxlApiGroup);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(HttpServletRequest request, int id) {

		// exist
		XxlApiGroup existGroup = xxlApiGroupDao.load(id);
		if (existGroup == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "更新失败，分组ID非法");
		}

		// 权限校验
		XxlApiProject xxlApiProject = xxlApiProjectDao.load(existGroup.getProjectId());
		if (!hasBizPermission(request, xxlApiProject.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		// 分组下是否存在接口
		List<XxlApiDocument> documentList = xxlApiDocumentDao.loadByGroupId(id);
		if (documentList!=null && documentList.size()>0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，分组下存在接口，不允许强制删除");
		}

		int ret = xxlApiGroupDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
