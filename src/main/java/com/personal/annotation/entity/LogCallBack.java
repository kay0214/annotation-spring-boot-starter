/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.personal.annotation.entity;

/**
 * @author sunpeikai
 * @version LogCallBack, v0.1 2020/10/12 10:32
 * @description
 */
public class LogCallBack {

    private String desc;

    private String clz;

    private String method;

    private String uri;

    private String requestMethod;

    private String ip;

    private long time;

    private String contentType;

    private long contentLength;

    private String header;

    private String paramEncode;

    private String request;

    private String response;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClz() {
        return clz;
    }

    public void setClz(String clz) {
        this.clz = clz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getParamEncode() {
        return paramEncode;
    }

    public void setParamEncode(String paramEncode) {
        this.paramEncode = paramEncode;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String toString(String split) {
        StringBuilder builder = new StringBuilder();
        if(this.desc != null && this.desc.length() > 0){
            builder.append("desc[").append(this.desc).append("]").append(split);
        }
        if(this.clz != null && this.clz.length() > 0){
            builder.append("clz[").append(this.clz).append("]").append(split);
        }
        if(this.method != null && this.method.length() > 0){
            builder.append("method[").append(this.method).append("]").append(split);
        }
        if(this.uri != null && this.uri.length() > 0){
            builder.append("uri[").append(this.uri).append("]").append(split);
        }
        if(this.requestMethod != null && this.requestMethod.length() > 0){
            builder.append("requestMethod[").append(this.requestMethod).append("]").append(split);
        }
        if(this.ip != null && this.ip.length() > 0){
            builder.append("ip[").append(this.ip).append("]").append(split);
        }
        if(this.time > 0L){
            builder.append("time[").append(this.time).append("ms]").append(split);
        }
        if(this.contentType != null && this.contentType.length() > 0){
            builder.append("contentType[").append(this.contentType).append("]").append(split);
        }
        if(this.contentLength > 0L){
            builder.append("contentLength[").append(this.contentLength).append("]").append(split);
        }
        if(this.header != null && this.header.length() > 0){
            builder.append("header[").append(this.header).append("]").append(split);
        }
        if(this.paramEncode != null && this.paramEncode.length() > 0){
            builder.append("paramEncode[").append(this.paramEncode).append("]").append(split);
        }
        if(this.request != null && this.request.length() > 0){
            builder.append("request[").append(this.request).append("]").append(split);
        }
        if(this.response != null && this.response.length() > 0){
            builder.append("response[").append(this.response).append("]").append(split);
        }
        return builder.length() > 0 ? builder.substring(0, builder.length() - 1) : "";
    }
}
