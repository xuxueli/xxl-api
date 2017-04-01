package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.consistant.RequestConfig;
import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiDocument;
import com.xxl.api.admin.core.model.XxlApiGroup;
import com.xxl.api.admin.core.model.XxlApiProject;
import com.xxl.api.admin.core.util.JacksonUtil;
import com.xxl.api.admin.dao.IXxlApiDocumentDao;
import com.xxl.api.admin.dao.IXxlApiGroupDao;
import com.xxl.api.admin.dao.IXxlApiProjectDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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


	@RequestMapping("/markStar")
	@ResponseBody
	public ReturnT<String> markStar(int id, int starLevel) {

		XxlApiDocument document = xxlApiDocumentDao.load(id);
		if (document == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "操作失败，接口ID非法");
		}

		document.setStarLevel(starLevel);

		int ret = xxlApiDocumentDao.update(document);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {

		// 存在Test记录，拒绝删除

		// 存在Mock记录，拒绝删除

		int ret = xxlApiDocumentDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 新增，API
	 *
	 * @param productId
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(Model model, int productId) {

		// project
		XxlApiProject project = xxlApiProjectDao.load(productId);
		if (project == null) {
			throw new RuntimeException("操作失败，项目ID非法");
		}
		model.addAttribute("productId", productId);

		// groupList
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(productId);
		model.addAttribute("groupList", groupList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

		return "document/document.add";
	}
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(XxlApiDocument xxlApiDocument) {
		int ret = xxlApiDocumentDao.add(xxlApiDocument);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 更新，API
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage(Model model, int id) {

		// document
		XxlApiDocument xxlApiDocument = xxlApiDocumentDao.load(id);
		if (xxlApiDocument == null) {
			throw new RuntimeException("操作失败，接口ID非法");
		}
		model.addAttribute("document", xxlApiDocument);
		model.addAttribute("requestHeadersList", (StringUtils.isNotBlank(xxlApiDocument.getRequestHeaders()))?JacksonUtil.readValue(xxlApiDocument.getRequestHeaders(), List.class):null );
		model.addAttribute("queryParamList", (StringUtils.isNotBlank(xxlApiDocument.getQueryParams()))?JacksonUtil.readValue(xxlApiDocument.getQueryParams(), List.class):null );

		// project
		int projectId = xxlApiDocument.getProjectId();
		model.addAttribute("productId", projectId);

		// groupList
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

		return "document/document.update";
	}
	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlApiDocument xxlApiDocument) {

		// fill not-change val
		XxlApiDocument oldVo = xxlApiDocumentDao.load(xxlApiDocument.getId());
		xxlApiDocument.setProjectId(oldVo.getProjectId());
		xxlApiDocument.setStarLevel(oldVo.getStarLevel());
		xxlApiDocument.setAddTime(oldVo.getAddTime());

		int ret = xxlApiDocumentDao.update(xxlApiDocument);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 详情页，API，待完善
	 * @return
	 */
	@RequestMapping("/detailPage")
	public String detailPage(Model model, int id) {

		// document
		XxlApiDocument xxlApiDocument = xxlApiDocumentDao.load(id);
		if (xxlApiDocument == null) {
			throw new RuntimeException("操作失败，接口ID非法");
		}
		model.addAttribute("document", xxlApiDocument);
		model.addAttribute("requestHeadersList", (StringUtils.isNotBlank(xxlApiDocument.getRequestHeaders()))?JacksonUtil.readValue(xxlApiDocument.getRequestHeaders(), List.class):null );
		model.addAttribute("queryParamList", (StringUtils.isNotBlank(xxlApiDocument.getQueryParams()))?JacksonUtil.readValue(xxlApiDocument.getQueryParams(), List.class):null );

		// project
		int projectId = xxlApiDocument.getProjectId();
		model.addAttribute("productId", projectId);

		// groupList
		List<XxlApiGroup> groupList = xxlApiGroupDao.loadAll(projectId);
		model.addAttribute("groupList", groupList);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());

		return "document/document.detail";
	}

	/**
	 * 保存Mock数据
	 * @param xxlApiDocument
	 * @return
	 */
	@RequestMapping("/saveMock")
	@ResponseBody
	public ReturnT<String> saveMock(XxlApiDocument xxlApiDocument) {
		int ret = xxlApiDocumentDao.update(xxlApiDocument);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 接口测试，待完善
	 * @return
	 */
	@RequestMapping("/testPage")
	public String testPage() {
		return "document/document.test";
	}
	@RequestMapping("/test")
	@ResponseBody
	public ReturnT<String> test() {
		return ReturnT.SUCCESS;
	}

	/**
	 * 接口测试记录，保存接口
	 * @return
	 */
	@RequestMapping("/testSave")
	@ResponseBody
	public ReturnT<String> testSave() {
		return ReturnT.SUCCESS;
	}


}
