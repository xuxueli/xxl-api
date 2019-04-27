package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.model.*;
import com.xxl.api.admin.core.util.tool.ArrayTool;
import com.xxl.api.admin.core.util.tool.StringTool;
import com.xxl.api.admin.dao.IXxlApiBizDao;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/project")
public class XxlApiProjectController {

	@Resource
	private IXxlApiProjectDao xxlApiProjectDao;
	@Resource
	private IXxlApiGroupDao xxlApiGroupDao;
	@Resource
	private IXxlApiBizDao xxlApiBizDao;
	@Resource
	private IXxlApiDocumentDao xxlApiDocumentDao;

	@RequestMapping
	public String index(Model model, @RequestParam(required = false, defaultValue = "0") int bizId) {

		// 业务线ID
		model.addAttribute("bizId", bizId);

		// 业务线列表
		List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
		model.addAttribute("bizList", bizList);

		return "project/project.list";
	}

	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
										@RequestParam(required = false, defaultValue = "10") int length,
										String name, int bizId) {
		// page list
		List<XxlApiProject> list = xxlApiProjectDao.pageList(start, length, name, bizId);
		int list_count = xxlApiProjectDao.pageListCount(start, length, name, bizId);

		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("recordsTotal", list_count);		// 总记录数
		maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
		maps.put("data", list);  					// 分页列表
		return maps;
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
	public ReturnT<String> add(HttpServletRequest request, XxlApiProject xxlApiProject) {
		// valid
		if (StringTool.isBlank(xxlApiProject.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入项目名称");
		}
		if (StringTool.isBlank(xxlApiProject.getBaseUrlProduct())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入根地址(线上)");
		}

		if (!hasBizPermission(request, xxlApiProject.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		int ret = xxlApiProjectDao.add(xxlApiProject);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(HttpServletRequest request, XxlApiProject xxlApiProject) {
		// exist
		XxlApiProject existProkect = xxlApiProjectDao.load(xxlApiProject.getId());
		if (existProkect == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "更新失败，项目ID非法");
		}

		// valid
		if (StringTool.isBlank(xxlApiProject.getName())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入项目名称");
		}
		if (StringTool.isBlank(xxlApiProject.getBaseUrlProduct())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入根地址(线上)");
		}

		if (!hasBizPermission(request, xxlApiProject.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		int ret = xxlApiProjectDao.update(xxlApiProject);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(HttpServletRequest request, int id) {

		// exist
		XxlApiProject existProkect = xxlApiProjectDao.load(id);
		if (existProkect == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "项目ID非法");
		}

		if (!hasBizPermission(request, existProkect.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		// 项目下是否存在分组
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(id);
		if (groupList!=null && groupList.size()>0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "该项目下存在分组信息，拒绝删除");
		}

		// 项目下是否存在接口
		List<XxlApiDocument> documents = xxlApiDocumentDao.loadAll(id, -1);
		if (documents!=null && documents.size()>0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "该项目下存在接口信息，拒绝删除");
		}

		int ret = xxlApiProjectDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
