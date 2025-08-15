package com.xxl.api.admin.controller.biz;

import com.xxl.api.admin.constant.RequestConst;
import com.xxl.api.admin.util.tool.StringTool;
import com.xxl.api.admin.mapper.*;
import com.xxl.api.admin.model.*;
import com.xxl.api.admin.service.IXxlApiDataTypeService;
import com.xxl.sso.core.helper.XxlSsoHelper;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.gson.GsonTool;
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
@RequestMapping("/document")
public class XxlApiDocumentController {

	@Resource
	private XxlApiDocumentMapper xxlApiDocumentDao;
	@Resource
	private XxlApiProjectMapper xxlApiProjectDao;
	@Resource
	private XxlApiGroupMapper xxlApiGroupDao;
	@Resource
	private XxlApiMockMapper xxlApiMockDao;
	@Resource
	private XxlApiTestHistoryMapper xxlApiTestHistoryDao;
	@Resource
	private IXxlApiDataTypeService xxlApiDataTypeService;


	@RequestMapping("/markStar")
	@ResponseBody
	public Response<String> markStar(HttpServletRequest request, HttpServletResponse response, int id, int starLevel) {

		XxlApiDocument document = xxlApiDocumentDao.load(id);
		if (document == null) {
			return Response.ofFail( "操作失败，接口ID非法");
		}

		// 权限
		XxlApiProject apiProject = xxlApiProjectDao.load(document.getProjectId());
		if (!hasBizPermission(request, response, apiProject.getBizId())) {
			return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
		}

		document.setStarLevel(starLevel);

		int ret = xxlApiDocumentDao.update(document);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Response<String> delete(HttpServletRequest request, HttpServletResponse response, int id) {

		XxlApiDocument document = xxlApiDocumentDao.load(id);
		if (document == null) {
			return Response.ofFail( "操作失败，接口ID非法");
		}

		// 权限
		XxlApiProject apiProject = xxlApiProjectDao.load(document.getProjectId());
		if (!hasBizPermission(request, response, apiProject.getBizId())) {
			return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
		}

		// 存在Test记录，拒绝删除
		List<XxlApiTestHistory> historyList = xxlApiTestHistoryDao.loadByDocumentId(id);
		if (historyList!=null && historyList.size()>0) {
			return Response.ofFail( "拒绝删除，该接口下存在Test记录，不允许删除");
		}

		// 存在Mock记录，拒绝删除
		List<XxlApiMock> mockList = xxlApiMockDao.loadAll(id);
		if (mockList!=null && mockList.size()>0) {
			return Response.ofFail( "拒绝删除，该接口下存在Mock记录，不允许删除");
		}

		int ret = xxlApiDocumentDao.delete(id);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	/**
	 * 新增，API
	 *
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request, HttpServletResponse response, Model model, int projectId, @RequestParam(required = false, defaultValue = "0") int groupId) {

		// project
		XxlApiProject project = xxlApiProjectDao.load(projectId);
		if (project == null) {
			throw new RuntimeException("操作失败，项目ID非法");
		}
		model.addAttribute("projectId", projectId);
		model.addAttribute("groupId", groupId);


		// 权限
		if (!hasBizPermission(request, response, project.getBizId())) {
			throw new RuntimeException("您没有相关业务线的权限,请联系管理员开通");
		}

		// groupList
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConst.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConst.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConst.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConst.ResponseContentType.values());

		return "document/document.add";
	}
	@RequestMapping("/add")
	@ResponseBody
	public Response<Integer> add(HttpServletRequest request, HttpServletResponse response, XxlApiDocument xxlApiDocument) {

		XxlApiProject project = xxlApiProjectDao.load(xxlApiDocument.getProjectId());
		if (project == null) {
			return Response.ofFail("操作失败，项目ID非法");
		}

		// 权限
		if (!hasBizPermission(request, response, project.getBizId())) {
			return Response.ofFail("您没有相关业务线的权限,请联系管理员开通");
		}


		int ret = xxlApiDocumentDao.add(xxlApiDocument);
		return (ret>0)?Response.ofSuccess(xxlApiDocument.getId()):Response.ofFail(null);
	}

	/**
	 * 更新，API
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage(HttpServletRequest request, HttpServletResponse response, Model model, int id) {

		// document
		XxlApiDocument xxlApiDocument = xxlApiDocumentDao.load(id);
		if (xxlApiDocument == null) {
			throw new RuntimeException("操作失败，接口ID非法");
		}
		model.addAttribute("document", xxlApiDocument);
		model.addAttribute("requestHeadersList", (StringTool.isNotBlank(xxlApiDocument.getRequestHeaders()))? GsonTool.fromJson(xxlApiDocument.getRequestHeaders(), List.class):null );
		model.addAttribute("queryParamList", (StringTool.isNotBlank(xxlApiDocument.getQueryParams()))?GsonTool.fromJson(xxlApiDocument.getQueryParams(), List.class):null );
		model.addAttribute("responseParamList", (StringTool.isNotBlank(xxlApiDocument.getResponseParams()))?GsonTool.fromJson(xxlApiDocument.getResponseParams(), List.class):null );

		// project
		int projectId = xxlApiDocument.getProjectId();
		model.addAttribute("projectId", projectId);


		// 权限
		XxlApiProject project = xxlApiProjectDao.load(xxlApiDocument.getProjectId());
		if (!hasBizPermission(request, response, project.getBizId())) {
			throw new RuntimeException("您没有相关业务线的权限,请联系管理员开通");
		}

		// groupList
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConst.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConst.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConst.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConst.ResponseContentType.values());

		// 响应数据类型
		XxlApiDataType responseDatatypeRet = xxlApiDataTypeService.loadDataType(xxlApiDocument.getResponseDatatypeId());
		model.addAttribute("responseDatatype", responseDatatypeRet);

		return "document/document.update";
	}
	@RequestMapping("/update")
	@ResponseBody
	public Response<String> update(HttpServletRequest request, HttpServletResponse response, XxlApiDocument xxlApiDocument) {

		XxlApiDocument oldVo = xxlApiDocumentDao.load(xxlApiDocument.getId());
		if (oldVo == null) {
			return Response.ofFail( "操作失败，接口ID非法");
		}

		// 权限
		XxlApiProject project = xxlApiProjectDao.load(oldVo.getProjectId());
		if (!hasBizPermission(request, response, project.getBizId())) {
			return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
		}

		// fill not-change val
		xxlApiDocument.setProjectId(oldVo.getProjectId());
		xxlApiDocument.setStarLevel(oldVo.getStarLevel());
		xxlApiDocument.setAddTime(oldVo.getAddTime());

		int ret = xxlApiDocumentDao.update(xxlApiDocument);
		return (ret>0)?Response.ofSuccess():Response.ofFail();
	}

	/**
	 * 详情页，API
	 * @return
	 */
	@RequestMapping("/detailPage")
	public String detailPage(HttpServletRequest request, HttpServletResponse response, Model model, int id) {

		// document
		XxlApiDocument xxlApiDocument = xxlApiDocumentDao.load(id);
		if (xxlApiDocument == null) {
			throw new RuntimeException("操作失败，接口ID非法");
		}
		model.addAttribute("document", xxlApiDocument);
		model.addAttribute("requestHeadersList", (StringTool.isNotBlank(xxlApiDocument.getRequestHeaders()))?GsonTool.fromJson(xxlApiDocument.getRequestHeaders(), List.class):null );
		model.addAttribute("queryParamList", (StringTool.isNotBlank(xxlApiDocument.getQueryParams()))?GsonTool.fromJson(xxlApiDocument.getQueryParams(), List.class):null );
		model.addAttribute("responseParamList", (StringTool.isNotBlank(xxlApiDocument.getResponseParams()))?GsonTool.fromJson(xxlApiDocument.getResponseParams(), List.class):null );

		// project
		int projectId = xxlApiDocument.getProjectId();
		XxlApiProject project = xxlApiProjectDao.load(projectId);
		model.addAttribute("projectId", projectId);
		model.addAttribute("project", project);

		// groupList
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// mock list
		List<XxlApiMock> mockList = xxlApiMockDao.loadAll(id);
		model.addAttribute("mockList", mockList);

		// test list
		List<XxlApiTestHistory> testHistoryList = xxlApiTestHistoryDao.loadByDocumentId(id);
		model.addAttribute("testHistoryList", testHistoryList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConst.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConst.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConst.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConst.ResponseContentType.values());

		// 响应数据类型
		XxlApiDataType responseDatatypeRet = xxlApiDataTypeService.loadDataType(xxlApiDocument.getResponseDatatypeId());
		model.addAttribute("responseDatatype", responseDatatypeRet);

		// 权限
		model.addAttribute("hasBizPermission", hasBizPermission(request, response, project.getBizId()));

		return "document/document.detail";
	}

}
