package com.penglecode.xmodule.master4j.spring.transaction.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * @author pengpeng
 * @version 1.0
 * @date 2020/9/21 23:12
 */
public class AccessLog implements BaseModel<AccessLog> {

    private static final long serialVersionUID = 1L;

    private Long logId;

    private String logTitle;

    private String invokeMethod;

    private String createTime;

    public AccessLog() {
    }

    public AccessLog(String logTitle, String invokeMethod, String createTime) {
        this.logTitle = logTitle;
        this.invokeMethod = invokeMethod;
        this.createTime = createTime;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getLogTitle() {
        return logTitle;
    }

    public void setLogTitle(String logTitle) {
        this.logTitle = logTitle;
    }

    public String getInvokeMethod() {
        return invokeMethod;
    }

    public void setInvokeMethod(String invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AccessLog{" +
                "logId=" + logId +
                ", logTitle='" + logTitle + '\'' +
                ", invokeMethod='" + invokeMethod + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
