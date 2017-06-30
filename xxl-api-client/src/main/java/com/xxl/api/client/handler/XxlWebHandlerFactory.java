package com.xxl.api.client.handler;

import com.xxl.api.client.config.XxlConstant;
import com.xxl.api.client.handler.annotation.XxlWebHandlerMapping;
import com.xxl.api.client.response.XxlWebResponse;
import com.xxl.api.client.response.impl.JsonResponse;
import com.xxl.api.client.response.impl.JsonpResponse;
import com.xxl.api.client.exception.XxlWebException;
import com.xxl.api.client.request.XxlWebRequest;
import com.xxl.api.client.response.impl.HtmlResponse;
import com.xxl.api.client.util.FieldReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xuxueli on 16/9/14.
 */
public class XxlWebHandlerFactory implements ApplicationContextAware {
    private static Logger logger = LoggerFactory.getLogger(XxlWebHandlerFactory.class);

    // handler repository
    private static ConcurrentHashMap<String, XxlWebHandler> handlerRepository = new ConcurrentHashMap<String, XxlWebHandler>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        //Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(XxlWebHandlerMapping.class);

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        if (beanDefinitionNames!=null && beanDefinitionNames.length>0) {
            for (String beanDefinitionName : beanDefinitionNames) {

                boolean isApiHandler = applicationContext.isTypeMatch(beanDefinitionName, XxlWebHandler.class);
                if (isApiHandler) { // if (beanDefinition instanceof XxlWebHandler) {
                    Object beanDefinition = applicationContext.getBean(beanDefinitionName);
                    // valid annotation
                    XxlWebHandlerMapping annotation = beanDefinition.getClass().getAnnotation(XxlWebHandlerMapping.class);
                    if (annotation!=null && annotation.value()!=null && annotation.value().trim().length()>0 ) {
                        handlerRepository.put(annotation.value(), (XxlWebHandler) beanDefinition);
                        logger.warn(">>>>>>>>>>> xxl-hex, bind hex handler success : {}", annotation.value());
                    }
                }

            }
        }

    }

    /**
     * parse mapping
     *
     * @param request
     * @return
     */
    private static String buildMapping(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (requestURI.length() <= contextPath.length() + 1) {
            logger.warn("buildMapping fail:" + requestURI);
            return null;
        }
        String mapping = requestURI.substring(contextPath.length() + 1).toLowerCase();
        return mapping;
    }

    /**
     * parse request
     *
     * @param request
     * @param response
     * @param requestClass
     * @return
     */
    private static XxlWebRequest buildRequest(HttpServletRequest request, HttpServletResponse response, Class requestClass) throws IllegalAccessException {
        
        /*String request_json = request.getParameter(WebHandlerClient.JSON);
        if (request_json==null || request_json.trim().length()==0) {
            return new JsonResponse(JsonResponse.CODE_FAIL, "必要参数缺失[request_json]").jsonp(jsonp);
        }
        XxlWebRequest hexrequest = (XxlWebRequest) JacksonUtil.readValue(request_json, requestClass);*/

        // 1、new instance
        XxlWebRequest hexrequest = null;
        try {
            hexrequest = (XxlWebRequest) requestClass.newInstance();
            hexrequest.setRequest(request);
            hexrequest.setResponse(response);
        } catch (InstantiationException e) {
            logger.error(e.getMessage(), e);
            throw new XxlWebException("buildRequest error:" + e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new XxlWebException("buildRequest error:" + e.getMessage());
        }

        // 2、field set
        Field[] fields = requestClass.getDeclaredFields();
        if (fields!=null) {
            for (Field field: fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                String[] paramValues = request.getParameterValues(field.getName());
                Object fieldValue = null;

                if (paramValues!=null && paramValues.length>0) {
                    if (paramValues.length == 1) {
                        fieldValue = FieldReflectionUtil.parseValue(field, paramValues[0]);
                    } else {
                        // TODO
                        throw new XxlWebException("buildRequest error, not support array param：" + field.getName() + "=" + paramValues);
                    }
                }

                if (fieldValue!=null) {
                    field.setAccessible(true);
                    field.set(hexrequest, fieldValue);
                }
            }
        }

        return hexrequest;
    }

    /**
     * parse response
     *
     * @param request
     * @param response
     * @param apiResponse
     * @return
     */
    public static XxlWebResponse buildResponse(HttpServletRequest request, HttpServletResponse response, XxlWebResponse apiResponse) {
        // support jsonp
        if (apiResponse instanceof JsonResponse) {
            String jsonp = request.getParameter(XxlConstant.JSONP);
            if (jsonp!=null && jsonp.trim().length()>0) {
                apiResponse = new JsonpResponse(jsonp.trim(), (JsonResponse) apiResponse);
            }
        }
        return apiResponse;
    }

    /**
     * dispatch handler
     *
     * @param request
     * @param response
     * @return
     */
    public static XxlWebResponse dispatchHandler(HttpServletRequest request, HttpServletResponse response){

        // mapping
        String mapping = buildMapping(request);

        // valid mapping
        if (mapping.equals("console.xxlapi")) {
            StringBuffer sb = new StringBuffer();
            sb.append("在线HexHandler列表 (" + handlerRepository.size() + ") :<hr>");
            if (handlerRepository !=null && handlerRepository.size()>0) {
                for (Map.Entry<String, XxlWebHandler> item: handlerRepository.entrySet()) {
                    Type[] requestClassTypps = ((ParameterizedType)item.getValue().getClass().getGenericSuperclass()).getActualTypeArguments();
                    Class requestClass = (Class) requestClassTypps[0];
                    sb.append(item.getKey());
                    sb.append(" : ");
                    sb.append(item.getValue().getClass());
                    sb.append("<br>");
                }
            }

            XxlWebResponse finalResponse = buildResponse(request, response, new HtmlResponse(sb.toString()));
            return finalResponse;
        }

        try {
            // handler
            XxlWebHandler handler = handlerRepository.get(mapping);
            if (handler == null) {
                XxlWebResponse finalResponse = buildResponse(request, response, new JsonResponse(JsonResponse.CODE_FAIL, "handler不存在(" + mapping + ")"));
                return finalResponse;
            }

            // request-class
            Type[] requestClassTypps = ((ParameterizedType)handler.getClass().getGenericSuperclass()).getActualTypeArguments();
            Class requestClass = (Class) requestClassTypps[0];      // ((Class) requestClassTypps[0]).isAssignableFrom(XxlWebRequest.class)

            // request-dto
            XxlWebRequest hexrequest = buildRequest(request, response, requestClass);
            if (hexrequest==null) {
                XxlWebResponse finalResponse = buildResponse(request, response, new JsonResponse(JsonResponse.CODE_FAIL, "params parse fail."));
                return finalResponse;
            }

            // do validate
            XxlWebResponse validateResponse = handler.validate(hexrequest);
            if (validateResponse!=null) {
                return validateResponse;
            }

            // do invoke
            XxlWebResponse apiResponse = handler.handle(hexrequest);
            XxlWebResponse finalResponse = buildResponse(request, response, apiResponse);
            return finalResponse;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            XxlWebResponse finalResponse = buildResponse(request, response, new JsonResponse(JsonResponse.CODE_FAIL, e.getMessage()));
            return finalResponse;
        }

    }

}
