package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiDocument;
import com.xxl.api.admin.dao.IXxlApiDocumentDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/document")
public class XxlApiDocumentController {

	@Resource
	private IXxlApiDocumentDao xxlApiDocumentDao;


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
	 * 新增，API，待完善
	 *
	 * @param productId
	 * @return
	 */
	@RequestMapping("/addPage")
	public String addPage(int productId) {
		return "document/document.add";
	}
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(XxlApiDocument xxlApiDocument) {
		int ret = xxlApiDocumentDao.add(xxlApiDocument);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 更新，API，待完善
	 * @return
	 */
	@RequestMapping("/updatePage")
	public String updatePage() {
		return "document/document.update";
	}
	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlApiDocument xxlApiDocument) {
		int ret = xxlApiDocumentDao.update(xxlApiDocument);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
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
	 * 详情页，API，待完善
	 * @return
	 */
	@RequestMapping("/detailPage")
	public String detailPage() {
		return "document/document.detail";
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
