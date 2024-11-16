package com.xxl.api.admin.core.consistant;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by xuxueli on 17/4/1.
 */
public class RequestConst {

    /**
     * Request Method
     */
    public enum  RequestMethodEnum {
        POST,GET,PUT,DELETE,HEAD,OPTIONS,PATCH;
    }

    /**
     * Request Headers
     */
    public static List<String> requestHeadersEnum = new LinkedList<String>();
    static {
        requestHeadersEnum.add("Accept");
        requestHeadersEnum.add("Accept-Charset");
        requestHeadersEnum.add("Accept-Encoding");
        requestHeadersEnum.add("Accept-Language");
        requestHeadersEnum.add("Accept-Ranges");
        requestHeadersEnum.add("Authorization");
        requestHeadersEnum.add("Cache-Control");
        requestHeadersEnum.add("Connection");
        requestHeadersEnum.add("Cookie");
        requestHeadersEnum.add("Content-Length");
        requestHeadersEnum.add("Content-Type");
        requestHeadersEnum.add("Date");
        requestHeadersEnum.add("Expect");
        requestHeadersEnum.add("From");
        requestHeadersEnum.add("Host");
        requestHeadersEnum.add("If-Match");
        requestHeadersEnum.add("If-Modified-Since");
        requestHeadersEnum.add("If-None-Match");
        requestHeadersEnum.add("If-Range");
        requestHeadersEnum.add("If-Unmodified-Since");
        requestHeadersEnum.add("Max-Forwards");
        requestHeadersEnum.add("Pragma");
        requestHeadersEnum.add("Proxy-Authorization");
        requestHeadersEnum.add("Range");
        requestHeadersEnum.add("Referer");
        requestHeadersEnum.add("TE");
        requestHeadersEnum.add("Upgrade");
        requestHeadersEnum.add("User-Agent");
        requestHeadersEnum.add("Via");
        requestHeadersEnum.add("Warning");
    }

    /**
     * Query Param, Type
     */
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

    /**
     * Query Param
     */
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

    /**
     * Reponse Headers, Content-Type
     */
    public enum ResponseContentType{
        JSON("application/json;charset=UTF-8"),
        XML("text/xml"),
        HTML("text/html;"),
        TEXT("text/plain"),
        JSONP("application/javascript");

        public final String type;
        ResponseContentType(String type) {
            this.type = type;
        }
        public static ResponseContentType match(String name){
            if (name != null) {
                for (ResponseContentType item: ResponseContentType.values()) {
                    if (item.name().equals(name)) {
                        return item;
                    }
                }
            }
            return null;
        }
    }

}
