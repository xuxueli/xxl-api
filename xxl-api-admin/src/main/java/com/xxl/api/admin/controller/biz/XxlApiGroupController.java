package com.xxl.api.admin.controller.biz;

import com.xxl.api.admin.mapper.XxlApiDocumentMapper;
import com.xxl.api.admin.mapper.XxlApiGroupMapper;
import com.xxl.api.admin.mapper.XxlApiProjectMapper;
import com.xxl.api.admin.model.*;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.Response;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import static com.xxl.api.admin.controller.biz.XxlApiDataTypeController.hasBizPermission;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/group")
public class XxlApiGroupController {

	@Resource
	private XxlApiProjectMapper xxlApiProjectDao;
	@Resource
	private XxlApiGroupMapper xxlApiGroupDao;
	@Resource
	private XxlApiDocumentMapper xxlApiDocumentDao;

	@RequestMapping
	public String index(HttpServletRequest request,
						HttpServletResponse response,
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
		model.addAttribute("hasBizPermission", hasBizPermission(request, response, xxlApiProject.getBizId()));

		return "group/group.list";
	}

	@RequestMapping("/add")
	@ResponseBody
	public Response<String> add(HttpServletRequest request, HttpServletResponse response, XxlApiGroup xxlApiGroup) {
		// valid
		if (StringTool.isBlank(xxlApiGroup.getName())) {
			return Response.ofFail( "请输入“分组名称”");
		}

		// 权限校验
		XxlApiProject xxlApiProject = xxlApiProjectDao.load(xxlApiGroup.getProjectId());
		if (!hasBizPermission(request, response, xxlApiProject.getBizId())) {
			return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
		}

		int ret = xxlApiGroupDao.add(xxlApiGroup);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping("/update")
	@ResponseBody
	public Response<String> update(HttpServletRequest request, HttpServletResponse response, XxlApiGroup xxlApiGroup) {
		// exist
		XxlApiGroup existGroup = xxlApiGroupDao.load(xxlApiGroup.getId());
		if (existGroup == null) {
			return Response.ofFail( "更新失败，分组ID非法");
		}

		// 权限校验
		XxlApiProject xxlApiProject = xxlApiProjectDao.load(existGroup.getProjectId());
		if (!hasBizPermission(request, response, xxlApiProject.getBizId())) {
			return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
		}

		// valid
		if (StringTool.isBlank(xxlApiGroup.getName())) {
			return Response.ofFail( "请输入“分组名称”");
		}

		int ret = xxlApiGroupDao.update(xxlApiGroup);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Response<String> delete(HttpServletRequest request, HttpServletResponse response, int id) {

		// exist
		XxlApiGroup existGroup = xxlApiGroupDao.load(id);
		if (existGroup == null) {
			return Response.ofFail( "更新失败，分组ID非法");
		}

		// 权限校验
		XxlApiProject xxlApiProject = xxlApiProjectDao.load(existGroup.getProjectId());
		if (!hasBizPermission(request, response, xxlApiProject.getBizId())) {
			return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
		}

		// 分组下是否存在接口
		List<XxlApiDocument> documentList = xxlApiDocumentDao.loadByGroupId(id);
		if (documentList!=null && documentList.size()>0) {
			return Response.ofFail( "拒绝删除，分组下存在接口，不允许强制删除");
		}

		int ret = xxlApiGroupDao.delete(id);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

}
