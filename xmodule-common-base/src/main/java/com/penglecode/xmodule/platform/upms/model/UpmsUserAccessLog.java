package com.penglecode.xmodule.platform.upms.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * UPMS用户访问日志 (upms_user_access_log) 实体类
 * 
 * @author Mybatis-Generator
 * @date	2019年12月24日 下午 17:12:12
 */
@Model(name="UPMS用户访问日志", alias="UserAccessLog")
public class UpmsUserAccessLog implements BaseModel<UpmsUserAccessLog> {
     
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    @Id
    private Long logId;

    /** 日志标题 */
    private String logTitle;

    /** 所属应用模块 */
    private String appModule;

    /** 请求URI */
    private String requestUri;

    /** HTTP请求方法,例如:POST,GET */
    private String httpMethod;

    /** MVC控制器方法 */
    private String mvcMethod;

    /** 请求头 */
    private String requestHeader;

    /** 请求的Content-Type */
    private String requestContentType;

    /** 请求内容(参数) */
    private String requestContent;

    /** 访问用户ID */
    private Long accessUserId;

    /** 访问时间 */
    private String accessTime;

    /** 客户端IP地址 */
    private String clientIpAddr;

    /** 访问的服务器IP:端口号 */
    private String serverIpAddr;

    /** 控制器方法的执行时长(毫秒) */
    private Long processTime;

    /** 日志记录是否结束,1-是0-否 */
    private Boolean loggingCompleted;

    /** 请求是否是异步的,1-是0-否 */
    private Boolean asyncRequest;

    /** 响应的Content-Type */
    private String responseContentType;

    /** 创建时间 */
    private String createTime;

    /** 响应内容 */
    private String responseContent;

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

    public String getAppModule() {
        return appModule;
    }

    public void setAppModule(String appModule) {
        this.appModule = appModule;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getMvcMethod() {
        return mvcMethod;
    }

    public void setMvcMethod(String mvcMethod) {
        this.mvcMethod = mvcMethod;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public Long getAccessUserId() {
        return accessUserId;
    }

    public void setAccessUserId(Long accessUserId) {
        this.accessUserId = accessUserId;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String getClientIpAddr() {
        return clientIpAddr;
    }

    public void setClientIpAddr(String clientIpAddr) {
        this.clientIpAddr = clientIpAddr;
    }

    public String getServerIpAddr() {
        return serverIpAddr;
    }

    public void setServerIpAddr(String serverIpAddr) {
        this.serverIpAddr = serverIpAddr;
    }

    public Long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Long processTime) {
        this.processTime = processTime;
    }

    public Boolean getLoggingCompleted() {
        return loggingCompleted;
    }

    public void setLoggingCompleted(Boolean loggingCompleted) {
        this.loggingCompleted = loggingCompleted;
    }

    public Boolean getAsyncRequest() {
        return asyncRequest;
    }

    public void setAsyncRequest(Boolean asyncRequest) {
        this.asyncRequest = asyncRequest;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public MapBuilder mapBuilder() {
        return new MapBuilder();
    }

    /**
     * Auto generated by Mybatis Generator
     */
    public class MapBuilder {
         
        private final Map<String, Object> modelProperties = new LinkedHashMap<String,Object>();

        MapBuilder() {
            
        }

        public MapBuilder withLogId(Long ... logId) {
            modelProperties.put("logId", BaseModel.first(logId, getLogId()));
            return this;
        }

        public MapBuilder withLogTitle(String ... logTitle) {
            modelProperties.put("logTitle", BaseModel.first(logTitle, getLogTitle()));
            return this;
        }

        public MapBuilder withAppModule(String ... appModule) {
            modelProperties.put("appModule", BaseModel.first(appModule, getAppModule()));
            return this;
        }

        public MapBuilder withRequestUri(String ... requestUri) {
            modelProperties.put("requestUri", BaseModel.first(requestUri, getRequestUri()));
            return this;
        }

        public MapBuilder withHttpMethod(String ... httpMethod) {
            modelProperties.put("httpMethod", BaseModel.first(httpMethod, getHttpMethod()));
            return this;
        }

        public MapBuilder withMvcMethod(String ... mvcMethod) {
            modelProperties.put("mvcMethod", BaseModel.first(mvcMethod, getMvcMethod()));
            return this;
        }

        public MapBuilder withRequestHeader(String ... requestHeader) {
            modelProperties.put("requestHeader", BaseModel.first(requestHeader, getRequestHeader()));
            return this;
        }

        public MapBuilder withRequestContentType(String ... requestContentType) {
            modelProperties.put("requestContentType", BaseModel.first(requestContentType, getRequestContentType()));
            return this;
        }

        public MapBuilder withRequestContent(String ... requestContent) {
            modelProperties.put("requestContent", BaseModel.first(requestContent, getRequestContent()));
            return this;
        }

        public MapBuilder withAccessUserId(Long ... accessUserId) {
            modelProperties.put("accessUserId", BaseModel.first(accessUserId, getAccessUserId()));
            return this;
        }

        public MapBuilder withAccessTime(String ... accessTime) {
            modelProperties.put("accessTime", BaseModel.first(accessTime, getAccessTime()));
            return this;
        }

        public MapBuilder withClientIpAddr(String ... clientIpAddr) {
            modelProperties.put("clientIpAddr", BaseModel.first(clientIpAddr, getClientIpAddr()));
            return this;
        }

        public MapBuilder withServerIpAddr(String ... serverIpAddr) {
            modelProperties.put("serverIpAddr", BaseModel.first(serverIpAddr, getServerIpAddr()));
            return this;
        }

        public MapBuilder withProcessTime(Long ... processTime) {
            modelProperties.put("processTime", BaseModel.first(processTime, getProcessTime()));
            return this;
        }

        public MapBuilder withLoggingCompleted(Boolean ... loggingCompleted) {
            modelProperties.put("loggingCompleted", BaseModel.first(loggingCompleted, getLoggingCompleted()));
            return this;
        }

        public MapBuilder withAsyncRequest(Boolean ... asyncRequest) {
            modelProperties.put("asyncRequest", BaseModel.first(asyncRequest, getAsyncRequest()));
            return this;
        }

        public MapBuilder withResponseContentType(String ... responseContentType) {
            modelProperties.put("responseContentType", BaseModel.first(responseContentType, getResponseContentType()));
            return this;
        }

        public MapBuilder withCreateTime(String ... createTime) {
            modelProperties.put("createTime", BaseModel.first(createTime, getCreateTime()));
            return this;
        }

        public MapBuilder withResponseContent(String ... responseContent) {
            modelProperties.put("responseContent", BaseModel.first(responseContent, getResponseContent()));
            return this;
        }

        public Map<String, Object> build() {
            return modelProperties;
        }
    }
}