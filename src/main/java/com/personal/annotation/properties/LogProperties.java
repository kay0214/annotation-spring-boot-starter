/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.personal.annotation.properties;


import com.personal.annotation.enums.LogContentEnums;
import com.personal.annotation.enums.LogModeEnums;
import com.personal.annotation.record.AbstractLogRecord;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
}
