/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.personal.annotation.properties;

import com.personal.annotation.record.AbstractLogRecord;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

/**
 * @author sunpeikai
 * @version LogProperties, v0.1 2020/9/25 15:17
 * @description
 */
@ConfigurationProperties(prefix = "log")
public class LogProperties {

    private boolean enable;
    private String split = ",";
    private Class<? extends AbstractLogRecord> recordClass;
    private LogModeEnums mode = LogModeEnums.NORMAL;
    private List<LogContentEnums> includes;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public Class<? extends AbstractLogRecord> getRecordClass() {
        return recordClass;
    }

    public void setRecordClass(Class<? extends AbstractLogRecord> recordClass) {
        this.recordClass = recordClass;
    }

    public LogModeEnums getMode() {
        return mode;
    }

    public void setMode(LogModeEnums mode) {
        this.mode = mode;
    }

    public List<LogContentEnums> getIncludes() {
        return includes;
    }

    public void setIncludes(List<LogContentEnums> includes) {
        this.includes = includes;
    }

    public List<LogContentEnums> getContentList(){
        switch (this.mode){
            case ALL: return LogProperties.LogContentEnums.getAllEnums();
            case MINIMAL: return LogProperties.LogContentEnums.getMinimalEnums();
            case CUSTOM: return this.includes;
            case NORMAL:
            default: return LogProperties.LogContentEnums.getNormalEnums();
        }
    }

    /**
     * 日志模式
     * */
    public enum LogModeEnums{
        ALL,NORMAL,MINIMAL,CUSTOM
    }

    /**
     * 日志内容
     * */
    public enum LogContentEnums{
        /**
         * LOG显示内容枚举
         * CLZ:显示类名
         * METHOD:显示方法名
         * URI:显示请求路径
         * REQUEST_METHOD:请求方式GET,POST
         * IP:显示客户端IP
         * TIME:显示耗时
         * CONTENT_TYPE:显示请求类型
         * CONTENT_LENGTH:显示请求长度
         * HEADER:显示请求头
         * PARAM_ENCODE:显示请求参数字符集
         * REQUEST:显示请求参数
         * RESPONSE:显示返回参数
         * */
        CLZ, METHOD, URI, REQUEST_METHOD, IP, TIME, CONTENT_TYPE, CONTENT_LENGTH, HEADER, PARAM_ENCODE, REQUEST, RESPONSE;

        /**
         * 获取所有类型
         * */
        public static List<LogContentEnums> getAllEnums(){
            return Arrays.asList(CLZ, METHOD, URI, REQUEST_METHOD, IP, TIME, CONTENT_TYPE, CONTENT_LENGTH, HEADER,PARAM_ENCODE, REQUEST, RESPONSE);
        }

        /**
         * 获取普通类型
         * */
        public static List<LogContentEnums> getNormalEnums(){
            return Arrays.asList(CLZ, METHOD, URI,REQUEST_METHOD, IP, TIME, REQUEST, RESPONSE);
        }

        /**
         * 获取极简类型
         * */
        public static List<LogContentEnums> getMinimalEnums(){
            return Arrays.asList(METHOD, URI, TIME);
        }
    }
}
