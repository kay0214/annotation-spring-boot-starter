/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.personal.annotation.record;

import com.personal.annotation.entity.LogCallBack;

/**
 * @author sunpeikai
 * @version AbstractLogRecord, v0.1 2020/10/12 10:07
 * @description
 */
public abstract class AbstractLogRecord {

    public void record(LogCallBack callBack){
        this.doRecord(callBack);
    }

    protected abstract void doRecord(LogCallBack callBack);
}
