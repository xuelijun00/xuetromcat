package com.yks.bigdata.util;

import org.apache.http.Header;

/**
 * Created by liuxing on 2017/5/5.
 * http 请求资源 实体
 */
public class ResponseData {

    private int statusCode;

    private String responseText;

    private Header[] headers;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }
}
