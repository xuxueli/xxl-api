package com.xxl.api.admin.controller.biz;

import com.xxl.api.admin.mapper.XxlApiBizMapper;
import com.xxl.api.admin.mapper.XxlApiDocumentMapper;
import com.xxl.api.admin.mapper.XxlApiGroupMapper;
import com.xxl.api.admin.mapper.XxlApiProjectMapper;
import com.xxl.api.admin.model.*;
import com.xxl.api.admin.util.I18nUtil;
import com.xxl.tool.core.CollectionTool;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.PageModel;
import com.xxl.tool.response.Response;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xxl.api.admin.controller.biz.DataTypeController.hasBizPermission;

/**
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/project")
public class ProjectController {

	@Resource
	private XxlApiProjectMapper xxlApiProjectDao;
	@Resource
	private XxlApiGroupMapper xxlApiGroupDao;
	@Resource
	private XxlApiBizMapper xxlApiBizDao;
	@Resource
	private XxlApiDocumentMapper xxlApiDocumentDao;

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
	public Response<PageModel<XxlApiProject>> pageList(@RequestParam(required = false, defaultValue = "0") int start,
										@RequestParam(required = false, defaultValue = "10") int length,
										String name, int bizId) {
		// page list
		List<XxlApiProject> list = xxlApiProjectDao.pageList(start, length, name, bizId);
		int list_count = xxlApiProjectDao.pageListCount(start, length, name, bizId);

		// package result
		PageModel<XxlApiProject> pageModel = new PageModel<>();
		pageModel.setPageData(list);
		pageModel.setTotalCount(list_count);
		return Response.ofSuccess(pageModel);
	}

	@RequestMapping("/add")
	@ResponseBody
	public Response<String> add(HttpServletRequest request, HttpServletResponse response, XxlApiProject xxlApiProject) {
		// valid
		if (StringTool.isBlank(xxlApiProject.getName())) {
			return Response.ofFail( "请输入项目名称");
		}
		if (StringTool.isBlank(xxlApiProject.getBaseUrlProduct())) {
			return Response.ofFail( "请输入根地址(线上)");
		}

		if (!hasBizPermission(request, response, xxlApiProject.getBizId())) {
			return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
		}

		int ret = xxlApiProjectDao.add(xxlApiProject);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping("/update")
	@ResponseBody
	public Response<String> update(HttpServletRequest request, HttpServletResponse response, XxlApiProject xxlApiProject) {
		// exist
		XxlApiProject existProkect = xxlApiProjectDao.load(xxlApiProject.getId());
		if (existProkect == null) {
			return Response.ofFail( "更新失败，项目ID非法");
		}

		// valid
		if (StringTool.isBlank(xxlApiProject.getName())) {
			return Response.ofFail( "请输入项目名称");
		}
		if (StringTool.isBlank(xxlApiProject.getBaseUrlProduct())) {
			return Response.ofFail( "请输入根地址(线上)");
		}

		if (!hasBizPermission(request, response, xxlApiProject.getBizId())) {
			return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
		}

		int ret = xxlApiProjectDao.update(xxlApiProject);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Response<String> delete(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids[]") List<Integer> ids) {

		// valid
		if (CollectionTool.isEmpty(ids)) {
			return Response.ofFail(I18nUtil.getString("system_param_empty"));
		}
		if (ids.size() != 1) {
			return Response.ofFail("只允许删除单条数据");
		}
		int id = ids.get(0);

		// exist
		XxlApiProject existProkect = xxlApiProjectDao.load(id);
		if (existProkect == null) {
			return Response.ofFail( "项目ID非法");
		}

		if (!hasBizPermission(request, response, existProkect.getBizId())) {
			return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
		}

		// 项目下是否存在分组
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(id);
		if (groupList!=null && groupList.size()>0) {
			return Response.ofFail( "该项目下存在分组信息，拒绝删除");
		}

		// 项目下是否存在接口
		List<XxlApiDocument> documents = xxlApiDocumentDao.loadAll(id, -1);
		if (documents!=null && documents.size()>0) {
			return Response.ofFail( "该项目下存在接口信息，拒绝删除");
		}

		int ret = xxlApiProjectDao.delete(id);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

}
