package com.xxl.api.client.response.annotation;

/**
 * @author xuxueli 2017-05-25 18:08:42
 */
public enum XxlWebContentType {

    JSON("application/json;charset=UTF-8"),
    /*XML("text/xml"),*/
    HTML("text/html;"),
    /*TEXT("text/plain"),*/
    JSONP("text/javascript");

    public final String type;
    XxlWebContentType(String type) {
        this.type = type;
    }

}