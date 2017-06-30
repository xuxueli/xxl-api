package com.xxl.api.client.dispatch;

import com.xxl.api.client.response.impl.JsonResponse;
import com.xxl.api.client.handler.XxlWebHandlerFactory;
import com.xxl.api.client.response.XxlWebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * hex servlet
 * @author xuxueli 
 * @version 2015-11-28 13:56:18
 */
public class XxlWebDispatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(XxlWebDispatchServlet.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// dispatch handler
		XxlWebResponse apiResponse = XxlWebHandlerFactory.dispatchHandler(request, response);

		String content = null;
		try {
			content = apiResponse.content();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			apiResponse = XxlWebHandlerFactory.buildResponse(request, response, new JsonResponse(JsonResponse.CODE_FAIL, e.getMessage()));
			try {
				content = apiResponse.content();
			} catch (Exception e1) {
				logger.error(e.getMessage(), e1);
				content = e.getMessage();
				// TODO
			}

		}

		// response
		response.setStatus(HttpServletResponse.SC_OK);
		response.setContentType(apiResponse.contentType().type);
		response.getWriter().println(content);
	}
	
}
