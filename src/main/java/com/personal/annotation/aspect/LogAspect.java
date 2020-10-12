/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.personal.annotation.aspect;

import com.alibaba.fastjson.JSONObject;
import com.personal.annotation.annotation.Log;
import com.personal.annotation.entity.LogCallBack;
import com.personal.annotation.enums.LogContentEnums;
import com.personal.annotation.properties.LogProperties;
import com.personal.annotation.record.AbstractLogRecord;
import com.personal.annotation.utils.SpringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunpeikai
 * @version LogAspect, v0.1 2020/9/25 14:09
 * @description
 */
@Aspect
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);
    private final List<LogContentEnums> enums;
    private final String split;
    private final Class<? extends AbstractLogRecord> recordClass;

    public LogAspect(LogProperties logProperties) {
        this.split = logProperties.getSplit();
        this.recordClass = logProperties.getRecordClass();
        switch (logProperties.getMode()){
            case ALL: this.enums = LogContentEnums.getAllEnums();break;
            case MINIMAL: this.enums = LogContentEnums.getMinimalEnums();break;
            case CUSTOM: this.enums = logProperties.getIncludes();break;
            case NORMAL:
            default: this.enums = LogContentEnums.getNormalEnums();break;
        }
    }

    @Pointcut("@annotation(com.personal.annotation.annotation.Log)")
    public void logPointcut(){}

    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取注解
        Log logAnnotation = getAnnotationLog(point);
        // 初始化Log记录
        LogCallBack logRecord = new LogCallBack();
        // 放入描述
        logRecord.setDesc(logAnnotation != null ? logAnnotation.desc() : "");
        if(enums != null && enums.size() > 0){
            long startTime = System.currentTimeMillis();
            Object result = point.proceed();
            long time = System.currentTimeMillis() - startTime;
            for (LogContentEnums content : enums) {
                switch (content) {
                    case CLZ: logRecord.setClz(point.getSignature().getDeclaringTypeName());break;
                    case METHOD: logRecord.setMethod(point.getSignature().getName());break;
                    case URI: logRecord.setUri(getRequest().getRequestURI());break;
                    case REQUEST_METHOD: logRecord.setRequestMethod(getRequest().getMethod());break;
                    case IP: logRecord.setIp(getRemoteIp());break;
                    case TIME: logRecord.setTime(time);break;
                    case CONTENT_TYPE: logRecord.setContentType(getRequest().getContentType());break;
                    case CONTENT_LENGTH: logRecord.setContentLength(getRequest().getContentLength());break;
                    case HEADER: logRecord.setHeader(getHeader());break;
                    case PARAM_ENCODE: logRecord.setParamEncode(getRequest().getCharacterEncoding());break;
                    case REQUEST: logRecord.setRequest(getParam());break;
                    case RESPONSE: logRecord.setResponse(result != null ? JSONObject.toJSONString(result) : "");break;
                }
            }
            // 日志回调和打印
            this.callBackAndPrint(logAnnotation, logRecord);
            return result;
        }else{
            // 未定义打印日志的内容 - 直接放行
            // 日志回调和打印
            this.callBackAndPrint(logAnnotation, logRecord);
            return point.proceed();
        }
    }

    /**
     * 日志回调和打印
     * */
    private void callBackAndPrint(Log logAnnotation, LogCallBack logRecord){
        // 日志回调
        if(logAnnotation != null && logAnnotation.record() && recordClass != null){
            try{
                // 先将class作为spring管理的bean的形式执行
                SpringUtils.getBean(recordClass).record(logRecord);
            }catch (NoSuchBeanDefinitionException e){
                try{
                    // 如果抛出异常,说明这个recordClass并不是spring管理的bean
                    recordClass.newInstance().record(logRecord);
                }catch (InstantiationException | IllegalAccessException exception){
                    log.error("log record call back fail => {}", exception.getMessage());
                }
            }
        }
        log.info(logRecord.toString(this.split));
    }

    private Log getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 获取上下文请求
     * */
    private HttpServletRequest getRequest(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取header
     * */
    private String getHeader(){
        Map<String, Object> result = new HashMap<>();
        HttpServletRequest request = getRequest();
        Enumeration<String> keys = request.getHeaderNames();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            result.put(key,request.getHeader(key));
        }
        return mapToJson(result);
    }

    /**
     * 获取请求参数
     * */
    private String getParam(){
        Map<String, Object> result = new HashMap<>();
        HttpServletRequest request = getRequest();
        Enumeration<String> keys = request.getParameterNames();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            result.put(key,request.getParameter(key));
        }
        return mapToJson(result);
    }

    /**
     * map转JSONString
     * */
    private String mapToJson(Map<String, Object> map){
        return new JSONObject(map).toJSONString();
    }

    /**
     * 获取客户端IP
     * */
    private String getRemoteIp(){
        HttpServletRequest request = getRequest();
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    if(inet!= null){
                        ipAddress = inet.getHostAddress();
                    }
                } catch (UnknownHostException e) {
                    log.error("error:" + e.getLocalizedMessage());
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
