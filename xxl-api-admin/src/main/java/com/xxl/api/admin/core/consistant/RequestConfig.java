package com.xxl.api.admin.core.consistant;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xuxueli on 17/4/1.
 */
public class RequestConfig {

    /**
     * Request Method
     */
    public enum  RequestMethodEnum {
        POST,GET,PUT,DELETE,HEAD,OPTIONS,PATCH;
    }

    /**
     * Request Headers
     */
    public static List<String> requestHeadersList = new LinkedList<String>();
    static {
        requestHeadersList.add("Accept");
        requestHeadersList.add("Accept-Charset");
        requestHeadersList.add("Accept-Encoding");
        requestHeadersList.add("Accept-Language");
        requestHeadersList.add("Accept-Ranges");
        requestHeadersList.add("Authorization");
        requestHeadersList.add("Cache-Control");
        requestHeadersList.add("Connection");
        requestHeadersList.add("Cookie");
        requestHeadersList.add("Content-Length");
        requestHeadersList.add("Content-Type");
        requestHeadersList.add("Date");
        requestHeadersList.add("Expect");
        requestHeadersList.add("From");
        requestHeadersList.add("Host");
        requestHeadersList.add("If-Match");
        requestHeadersList.add("If-Modified-Since");
        requestHeadersList.add("If-None-Match");
        requestHeadersList.add("If-Range");
        requestHeadersList.add("If-Unmodified-Since");
        requestHeadersList.add("Max-Forwards");
        requestHeadersList.add("Pragma");
        requestHeadersList.add("Proxy-Authorization");
        requestHeadersList.add("Range");
        requestHeadersList.add("Referer");
        requestHeadersList.add("TE");
        requestHeadersList.add("Upgrade");
        requestHeadersList.add("User-Agent");
        requestHeadersList.add("Via");
        requestHeadersList.add("Warning");

    }


}
