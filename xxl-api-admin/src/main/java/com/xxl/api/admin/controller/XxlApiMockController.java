package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiDocument;
import com.xxl.api.admin.core.model.XxlApiMock;
import com.xxl.api.admin.dao.IXxlApiDocumentDao;
import com.xxl.api.admin.dao.IXxlApiMockDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/mock")
public class XxlApiMockController {

	@Resource
	private IXxlApiMockDao xxlApiMockDao;
	@Resource
	private IXxlApiDocumentDao xxlApiDocumentDao;

	/**
	 * 保存Mock数据
	 * @param xxlApiMock
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(XxlApiMock xxlApiMock) {

		XxlApiDocument document = xxlApiDocumentDao.load(xxlApiMock.getDocumentId());
		if (document == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "保存Mock数据失败，接口ID非法");
		}
		String uuid = UUID.randomUUID().toString();

		xxlApiMock.setUuid(uuid);
		int ret = xxlApiMockDao.add(xxlApiMock);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {
		int ret = xxlApiMockDao.delete(id);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlApiMock xxlApiMock) {
		int ret = xxlApiMockDao.update(xxlApiMock);
		return (ret>0)?ReturnT.SUCCESS:ReturnT.FAIL;
	}

}
