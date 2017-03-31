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

}
