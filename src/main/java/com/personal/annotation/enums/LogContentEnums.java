/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.personal.annotation.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author sunpeikai
 * @version LogContentEnums, v0.1 2020/9/25 15:09
 * @description
 */
public enum LogContentEnums {
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
