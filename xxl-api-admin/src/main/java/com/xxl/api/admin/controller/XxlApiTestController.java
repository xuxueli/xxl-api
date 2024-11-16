package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.consistant.RequestConst;
import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiDocument;
import com.xxl.api.admin.core.model.XxlApiProject;
import com.xxl.api.admin.core.model.XxlApiTestHistory;
import com.xxl.api.admin.core.util.tool.StringTool;
import com.xxl.api.admin.core.util.ThrowableUtil;
import com.xxl.api.admin.dao.IXxlApiDocumentDao;
import com.xxl.api.admin.dao.IXxlApiProjectDao;
import com.xxl.api.admin.dao.IXxlApiTestHistoryDao;
import com.xxl.tool.gson.GsonTool;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author xuxueli 2017-04-04 18:10:54
 */
@Controller
@RequestMapping("/test")
public class XxlApiTestController {
	private static Logger logger = LoggerFactory.getLogger(XxlApiTestController.class);

	@Resource
	private IXxlApiDocumentDao xxlApiDocumentDao;
	@Resource
	private IXxlApiTestHistoryDao xxlApiTestHistoryDao;
	@Resource
	private IXxlApiProjectDao xxlApiProjectDao;

	/**
	 * 接口测试，待完善
	 * @return
	 */
	@RequestMapping
	public String index(Model model,
						int documentId,
						@RequestParam(required = false, defaultValue = "0") int testId) {


		// params
		XxlApiDocument document = document = xxlApiDocumentDao.load(documentId);
		if (document == null) {
			throw new RuntimeException("接口ID非法");
		}
		XxlApiProject project = xxlApiProjectDao.load(document.getProjectId());

		List<Map<String, String>> requestHeaders = null;
		List<Map<String, String>> queryParams = null;

		if (testId > 0) {
			XxlApiTestHistory testHistory = xxlApiTestHistoryDao.load(testId);
			if (testHistory == null) {
				throw new RuntimeException("测试用例ID非法");
			}
			model.addAttribute("testHistory", testHistory);

			requestHeaders = (StringTool.isNotBlank(testHistory.getRequestHeaders()))? GsonTool.fromJson(testHistory.getRequestHeaders(), List.class):null;
			queryParams = (StringTool.isNotBlank(testHistory.getQueryParams()))? GsonTool.fromJson(testHistory.getQueryParams(), List.class):null;
		} else {
			requestHeaders = (StringTool.isNotBlank(document.getRequestHeaders()))? GsonTool.fromJson(document.getRequestHeaders(), List.class):null;
			queryParams = (StringTool.isNotBlank(document.getQueryParams()))? GsonTool.fromJson(document.getQueryParams(), List.class):null;
		}

		model.addAttribute("document", document);
		model.addAttribute("project", project);
		model.addAttribute("requestHeaders", requestHeaders);
		model.addAttribute("queryParams", queryParams);
        model.addAttribute("documentId", documentId);
        model.addAttribute("testId", testId);

		// enum
		model.addAttribute("RequestMethodEnum", RequestConst.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConst.requestHeadersEnum);
		model.addAttribute("QueryParamTypeEnum", RequestConst.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConst.ResponseContentType.values());

		return "test/test.index";
	}

	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<Integer> add(XxlApiTestHistory xxlApiTestHistory) {
		int ret = xxlApiTestHistoryDao.add(xxlApiTestHistory);
		return ret>0?new ReturnT<Integer>(xxlApiTestHistory.getId()):new ReturnT<Integer>(ReturnT.FAIL_CODE, null);
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(XxlApiTestHistory xxlApiTestHistory) {
		int ret = xxlApiTestHistoryDao.update(xxlApiTestHistory);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id) {
		int ret = xxlApiTestHistoryDao.delete(id);
		return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
	}

	/**
	 * 测试Run
	 * @return
	 */
	@RequestMapping("/run")
	@ResponseBody
	public ReturnT<String> run(XxlApiTestHistory xxlApiTestHistory, HttpServletRequest request, HttpServletResponse response) {

		// valid
		RequestConst.ResponseContentType contentType = RequestConst.ResponseContentType.match(xxlApiTestHistory.getRespType());
		if (contentType == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "响应数据类型(MIME)非法");
		}

		if (StringTool.isBlank(xxlApiTestHistory.getRequestUrl())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入接口URL");
		}

		// request headers
		Map<String, String> requestHeaderMap = null;
		List<Map<String, String>> requestHeaders = StringTool.isNotBlank(xxlApiTestHistory.getRequestHeaders())?
				GsonTool.fromJson(xxlApiTestHistory.getRequestHeaders(), List.class)
				:null;
		if (requestHeaders!=null && requestHeaders.size()>0) {
			requestHeaderMap = new HashMap<String, String>();
			for (Map<String, String> item: requestHeaders) {
				requestHeaderMap.put(item.get("key"), item.get("value"));
			}
		}

		// query param
		Map<String, String> queryParamMap = null;
		List<Map<String, String>> queryParams = StringTool.isNotBlank(xxlApiTestHistory.getQueryParams())?
				GsonTool.fromJson(xxlApiTestHistory.getQueryParams(), List.class)
				:null;
		if (queryParams!=null && queryParams.size()>0) {
			queryParamMap = new HashMap<String, String>();
			for (Map<String, String> item: queryParams) {
				queryParamMap.put(item.get("key"), item.get("value"));
			}
		}

		// invoke 1/3
		HttpUriRequestBase remoteRequest = null;
		if (RequestConst.RequestMethodEnum.POST.name().equals(xxlApiTestHistory.getRequestMethod())) {
			HttpPost httpPost = new HttpPost(xxlApiTestHistory.getRequestUrl());
			// query params
			if (queryParamMap != null && !queryParamMap.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for(Map.Entry<String,String> entry : queryParamMap.entrySet()){
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8));	// "UTF-8"
			}
			remoteRequest = httpPost;
		} else if (RequestConst.RequestMethodEnum.GET.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpGet(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConst.RequestMethodEnum.PUT.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpPut(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConst.RequestMethodEnum.DELETE.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpDelete(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConst.RequestMethodEnum.HEAD.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpHead(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConst.RequestMethodEnum.OPTIONS.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpOptions(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else if (RequestConst.RequestMethodEnum.PATCH.name().equals(xxlApiTestHistory.getRequestMethod())) {
			remoteRequest = new HttpPatch(markGetUrl(xxlApiTestHistory.getRequestUrl(), queryParamMap));
		} else {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请求方法非法");
		}

		// invoke 2/3
		if (requestHeaderMap!=null && !requestHeaderMap.isEmpty()) {
			for(Map.Entry<String,String> entry : requestHeaderMap.entrySet()){
				remoteRequest.setHeader(entry.getKey(), entry.getValue());
			}
		}

		// write response
		String responseContent = remoteCall(remoteRequest);
		return new ReturnT<String>(responseContent);
	}

	private String markGetUrl(String url, Map<String, String> queryParamMap){
		String finalUrl = url;
		if (queryParamMap!=null && queryParamMap.size()>0) {
			finalUrl = url + "?";
			for(Map.Entry<String,String> entry : queryParamMap.entrySet()){
				finalUrl += entry.getKey() + "=" + entry.getValue() + "&";
			}
			finalUrl = finalUrl.substring(0, finalUrl.length()-1);	// 后缀处理，去除 ？ 或 & 符号
		}
		return finalUrl;
	}

	private String remoteCall(HttpUriRequestBase remoteRequest){
		// remote test
		String responseContent = null;

		CloseableHttpClient httpClient = null;
		try{
			RequestConfig requestConfig = RequestConfig
					.custom()
					.setConnectionRequestTimeout(5000, TimeUnit.MILLISECONDS)
					.setResponseTimeout(5000, TimeUnit.MILLISECONDS)
					.build();
			remoteRequest.setConfig(requestConfig);

			httpClient = HttpClients.custom().disableAutomaticRetries().build();

			// parse response
			CloseableHttpResponse response = httpClient.execute(remoteRequest);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				int statusCode = response.getCode();
				if (statusCode == 200) {
					responseContent = EntityUtils.toString(entity, "UTF-8");
				} else {
					responseContent = "请求状态异常：" + response.getCode();
					if (statusCode==302 && response.getFirstHeader("Location")!=null) {
						responseContent += "；Redirect地址：" + response.getFirstHeader("Location").getValue();
					}

				}
				EntityUtils.consume(entity);
			}
			logger.info("http statusCode error, statusCode:" + response.getCode());
		} catch (Exception e) {
			responseContent = "请求异常：" + ThrowableUtil.toString(e);
		} finally{
			if (httpClient!=null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return responseContent;
	}

}
