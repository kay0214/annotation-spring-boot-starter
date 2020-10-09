/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.personal.annotation.aspect;

import com.alibaba.fastjson.JSONObject;
import com.personal.annotation.annotation.Log;
import com.personal.annotation.enums.LogContentEnums;
import com.personal.annotation.properties.LogProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
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

    public LogAspect(LogProperties logProperties) {
        this.split = logProperties.getSplit();
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
        // 定义了打印日志的内容
        StringBuilder builder = new StringBuilder();
        // 获取注解
        Log logAnnotation = getAnnotationLog(point);
        if(enums != null && enums.size() > 0){
            long startTime = System.currentTimeMillis();
            Object result = point.proceed();
            long time = System.currentTimeMillis() - startTime;
            // 放入描述
            if(logAnnotation != null && !StringUtils.isEmpty(logAnnotation.desc())){
                builder.append("desc[").append(logAnnotation.desc()).append("]").append(this.split);
            }
            for (int i=0 ; i<enums.size() ; i++){
                LogContentEnums content = enums.get(i);
                String split = i == (enums.size() - 1) ? "":this.split;
                switch (content){
                    case CLASS: builder.append("class[").append(point.getSignature().getDeclaringTypeName()).append("]").append(split);break;
                    case METHOD: builder.append("method[").append(point.getSignature().getName()).append("]").append(split);break;
                    case URI: builder.append("uri[").append(getRequest().getRequestURI()).append("]").append(split);break;
                    case REQUEST_METHOD: builder.append("requestMethod[").append(getRequest().getMethod()).append("]").append(split);break;
                    case IP: builder.append("ip[").append(getRemoteIp()).append("]").append(split);break;
                    case TIME: builder.append("time[").append(time).append("ms]").append(split);break;
                    case CONTENT_TYPE: builder.append("contentType[").append(getRequest().getContentType()).append("]").append(split);break;
                    case CONTENT_LENGTH: builder.append("contentLength[").append(getRequest().getContentLength()).append("]").append(split);break;
                    case HEADER: builder.append("header[").append(getHeader()).append("]").append(split);break;
                    case PARAM_ENCODE: builder.append("paramEncode[").append(getRequest().getCharacterEncoding()).append("]").append(split);break;
                    case PARAM: builder.append("param[").append(getParam()).append("]").append(split);break;
                    case RETURN: builder.append("return[").append(JSONObject.toJSONString(result)).append("]").append(split);break;
                }
            }
            log.info(builder.toString());
            return result;
        }else{
            // 放入描述
            if(logAnnotation != null && !StringUtils.isEmpty(logAnnotation.desc())){
                builder.append("desc[").append(logAnnotation.desc()).append("]");
            }
            // 未定义打印日志的内容 - 直接放行
            log.info(builder.toString());
            return point.proceed();
        }
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
