package com.xxl.api.client.example.handler;

import com.xxl.api.client.handler.XxlWebHandler;
import com.xxl.api.client.handler.annotation.XxlWebHandlerMapping;
import com.xxl.api.client.response.XxlWebResponse;
import com.xxl.api.client.response.impl.JsonResponse;
import com.xxl.api.client.example.request.DemoRequest;
import com.xxl.api.client.example.response.DemoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 	HexHandler示例, 该示例需求为: 计算参数a和b的和并返回, 启动a和b不可同时为0, 接口约定加密口令passphrase;
 *
 * 	每个API接口, 由三部分组成: XxlWebHandler + XxlWebRequest + Response
 * 	开发流程:
 *
 * 		1、需要继承ApiHandler<T>父类, 并且定义T(Request对象), 该对象为该API接口的入参;
 * 		2、实现父类的validate方法, 可进行安全性校验, 以及业务参数校验等。返回null表示校验成功;
 * 		3、实现父类的handle方法, 开发业务逻辑即可, 并且定义Response, 其含义为API接口返回值。
 * 		4、新增 "@ApiHandlerMapping" 注解, 注解value值为该API接口的唯一标示, Client端调用时将会使用到;
 */

@XxlWebHandlerMapping("default/demohandler.xxlapi")
public class DemoHandler extends XxlWebHandler<DemoRequest> {
	private static transient Logger logger = LoggerFactory.getLogger(DemoHandler.class);

	@Override
	public XxlWebResponse validate(DemoRequest request) {
		logger.info("request param:{}", request!=null?request.toString():"null");
		if (request==null) {
			return new JsonResponse(JsonResponse.CODE_FAIL, "参数非法/缺失");
		}

	    // 安全性校验
        if (!"qwerasdf".equals(request.getPassphrase())) {
            return new JsonResponse(JsonResponse.CODE_FAIL, "加密口令校验失败 (安全性校验) ");
        }

        // 业务参数校验
		if (request.getA()==0 && request.getB()==0) {
			return new JsonResponse(JsonResponse.CODE_FAIL, "参数不可全部为0 (业务参数校验)");
		}

		return null;
	}

	@Override
	public XxlWebResponse handle(DemoRequest request) {

		int sum = request.getA() + request.getB();

		DemoResponse demoResponse = new DemoResponse();
		demoResponse.setSum(sum);

		return new JsonResponse<DemoResponse>(demoResponse);
	}

}
