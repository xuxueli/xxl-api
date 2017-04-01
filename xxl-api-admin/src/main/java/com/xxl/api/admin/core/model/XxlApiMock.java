package com.xxl.api.admin.core.model;

/**
 * Created by xuxueli on 17/4/1.
 */
public class XxlApiMock {

    private int id;
    private int documentId;         // 接口ID
    private String uuid;
    private String respType;        // Response Content-type：如JSON、XML、HTML、TEXT、JSONP
    private String respExample;     // Response Content

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRespType() {
        return respType;
    }

    public void setRespType(String respType) {
        this.respType = respType;
    }

    public String getRespExample() {
        return respExample;
    }

    public void setRespExample(String respExample) {
        this.respExample = respExample;
    }
}
