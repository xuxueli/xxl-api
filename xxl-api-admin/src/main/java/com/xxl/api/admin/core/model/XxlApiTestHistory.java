package com.xxl.api.admin.core.model;

import java.util.Date;

/**
 * Created by xuxueli on 17/4/4.
 */
public class XxlApiTestHistory {

    private int id;
    private int documentId;             // 接口ID
    private Date addTime;               // 创建时间
    private Date updateTime;            // 更新时间
    private String requestUrl;          // Request URL：相对地址
    private String requestMethod;       // Request Method：如POST、GET
    private String requestHeaders;      // Request Headers：Map-JSON格式字符串
    private String queryParams;         // Query String Parameters：VO-JSON格式字符串
    private String respType;            // Response Content-type

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getRespType() {
        return respType;
    }

    public void setRespType(String respType) {
        this.respType = respType;
    }
}
