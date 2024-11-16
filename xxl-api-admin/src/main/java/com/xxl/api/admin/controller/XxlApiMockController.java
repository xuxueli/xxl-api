package com.xxl.api.admin.controller;

import com.xxl.api.admin.controller.annotation.PermessionLimit;
import com.xxl.api.admin.core.consistant.RequestConst;
import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiDocument;
import com.xxl.api.admin.core.model.XxlApiMock;
import com.xxl.api.admin.dao.IXxlApiDocumentDao;
import com.xxl.api.admin.dao.IXxlApiMockDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * @author xuxueli 2017-03-31 18:10:37
 */
@Controller
@RequestMapping("/mock")
public class XxlApiMockController {
	private static Logger logger = LoggerFactory.getLogger(XxlApiMockController.class);

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

	@RequestMapping("/run/{uuid}")
	@PermessionLimit(limit=false)
	public void run(@PathVariable("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) {
		XxlApiMock xxlApiMock = xxlApiMockDao.loadByUuid(uuid);
		if (xxlApiMock == null) {
			throw new RuntimeException("Mock数据ID非法");
		}

		RequestConst.ResponseContentType contentType = RequestConst.ResponseContentType.match(xxlApiMock.getRespType());
		if (contentType == null) {
			throw new RuntimeException("Mock数据响应数据类型(MIME)非法");
		}

		// write response
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType(contentType.type);

			PrintWriter writer = response.getWriter();
			writer.write(xxlApiMock.getRespExample());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

}
