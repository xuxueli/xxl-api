package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.consistant.RequestConst;
import com.xxl.api.admin.core.model.*;
import com.xxl.api.admin.core.util.tool.ArrayTool;
import com.xxl.api.admin.core.util.tool.StringTool;
import com.xxl.api.admin.dao.*;
import com.xxl.api.admin.service.IXxlApiDataTypeService;
import com.xxl.api.admin.service.impl.LoginService;
import com.xxl.tool.gson.GsonTool;
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
@RequestMapping("/document")
public class XxlApiDocumentController {

	@Resource
	private IXxlApiDocumentDao xxlApiDocumentDao;
	@Resource
	private IXxlApiProjectDao xxlApiProjectDao;
	@Resource
	private IXxlApiGroupDao xxlApiGroupDao;
	@Resource
	private IXxlApiMockDao xxlApiMockDao;
	@Resource
	private IXxlApiTestHistoryDao xxlApiTestHistoryDao;
	@Resource
	private IXxlApiDataTypeService xxlApiDataTypeService;


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


	@RequestMapping("/markStar")
	@ResponseBody
	public ReturnT<String> markStar(HttpServletRequest request, int id, int starLevel) {

		XxlApiDocument document = xxlApiDocumentDao.load(id);
		if (document == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "操作失败，接口ID非法");
		}

		// 权限
		XxlApiProject apiProject = xxlApiProjectDao.load(document.getProjectId());
		if (!hasBizPermission(request, apiProject.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		document.setStarLevel(starLevel);

		int ret = xxlApiDocumentDao.update(document);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(HttpServletRequest request, int id) {

		XxlApiDocument document = xxlApiDocumentDao.load(id);
		if (document == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "操作失败，接口ID非法");
		}

		// 权限
		XxlApiProject apiProject = xxlApiProjectDao.load(document.getProjectId());
		if (!hasBizPermission(request, apiProject.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		// 存在Test记录，拒绝删除
		List<XxlApiTestHistory> historyList = xxlApiTestHistoryDao.loadByDocumentId(id);
		if (historyList!=null && historyList.size()>0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，该接口下存在Test记录，不允许删除");
		}

		// 存在Mock记录，拒绝删除
		List<XxlApiMock> mockList = xxlApiMockDao.loadAll(id);
		if (mockList!=null && mockList.size()>0) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "拒绝删除，该接口下存在Mock记录，不允许删除");
		}

		int ret = xxlApiDocumentDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 新增，API
	 *
	 * @param projectId
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(HttpServletRequest request, Model model, int projectId, @RequestParam(required = false, defaultValue = "0") int groupId) {

		// project
		XxlApiProject project = xxlApiProjectDao.load(projectId);
		if (project == null) {
			throw new RuntimeException("操作失败，项目ID非法");
		}
		model.addAttribute("projectId", projectId);
		model.addAttribute("groupId", groupId);


		// 权限
		if (!hasBizPermission(request, project.getBizId())) {
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
	public ReturnT<Integer> add(HttpServletRequest request, XxlApiDocument xxlApiDocument) {

		XxlApiProject project = xxlApiProjectDao.load(xxlApiDocument.getProjectId());
		if (project == null) {
			return new ReturnT<Integer>(ReturnT.FAIL_CODE, "操作失败，项目ID非法");
		}

		// 权限
		if (!hasBizPermission(request, project.getBizId())) {
			return new ReturnT<Integer>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}


		int ret = xxlApiDocumentDao.add(xxlApiDocument);
		return (ret>0)?new ReturnT<Integer>(xxlApiDocument.getId()):new ReturnT<Integer>(ReturnT.FAIL_CODE, null);
	}

	/**
	 * 更新，API
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage(HttpServletRequest request, Model model, int id) {

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
		if (!hasBizPermission(request, project.getBizId())) {
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
	public ReturnT<String> update(HttpServletRequest request, XxlApiDocument xxlApiDocument) {

		XxlApiDocument oldVo = xxlApiDocumentDao.load(xxlApiDocument.getId());
		if (oldVo == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "操作失败，接口ID非法");
		}

		// 权限
		XxlApiProject project = xxlApiProjectDao.load(oldVo.getProjectId());
		if (!hasBizPermission(request, project.getBizId())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
		}

		// fill not-change val
		xxlApiDocument.setProjectId(oldVo.getProjectId());
		xxlApiDocument.setStarLevel(oldVo.getStarLevel());
		xxlApiDocument.setAddTime(oldVo.getAddTime());

		int ret = xxlApiDocumentDao.update(xxlApiDocument);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 详情页，API
	 * @return
	 */
	@RequestMapping("/detailPage")
	public String detailPage(HttpServletRequest request, Model model, int id) {

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
		model.addAttribute("hasBizPermission", hasBizPermission(request, project.getBizId()));

		return "document/document.detail";
	}

}
