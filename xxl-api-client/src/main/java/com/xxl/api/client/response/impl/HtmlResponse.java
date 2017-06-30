package com.xxl.api.client.response.impl;


import com.xxl.api.client.response.XxlWebResponse;
import com.xxl.api.client.response.annotation.XxlWebContentType;

/**
 * Created by xuxueli on 17/5/25.
 */
public class HtmlResponse extends XxlWebResponse {

    private String content;
    public HtmlResponse(String content){
        this.content = content;
    }

    @Override
    public XxlWebContentType contentType() {
        return XxlWebContentType.HTML;
    }

    @Override
    public String content() {
        return content;
    }

}
