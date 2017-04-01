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

    public enum  QueryParamTypeEnum {

        STRING("string"),
        BOOLEAN("boolean"),
        SHORT("short"),
        INT("int"),
        LONG("long"),
        FLOAT("float"),
        DOUBLE("double"),
        DATE("date"),
        DATETIME("datetime"),
        JSON("json"),
        BYTE("byte");

        public String title;
        QueryParamTypeEnum(String title) {
            this.title = title;
        }
    }

    public static class QueryParam {

        private boolean notNull;    // 是否必填
        private String name;        // 参数名称
        private String type;        // 参数类型
        private String desc;        // 参数说明

        public boolean isNotNull() {
            return notNull;
        }

        public void setNotNull(boolean notNull) {
            this.notNull = notNull;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

}
