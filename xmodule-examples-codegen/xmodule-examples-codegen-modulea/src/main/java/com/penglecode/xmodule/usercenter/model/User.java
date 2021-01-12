package com.penglecode.xmodule.usercenter.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户
 * 
 * @author Customized-Mybatis-Generator
 * @date	2021年01月09日 下午 14:53:56
 */
@Model(name="用户信息", alias="User")
public class User implements BaseModel<User> {
     
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    private Long id;

    /** uuid */
    private String uuid;

    /** 手机号 */
    private String phone;

    /** 密码 */
    private String password;

    /** 姓 */
    private String firstname;

    /** 名 */
    private String lastname;

    /** nickname */
    private String nickname;

    /** 类型 : 格式[101]
0:关闭，1:打开
右边第一位：家长
右边第二位：老师
右边第三位：学校 */
    private Integer type;

    /** 状态 : 0:无效
1:有效 */
    private Integer status;

    /** 创建时间 */
    private String createdTime;

    /** 更新时间 */
    private String modifiedTime;

    /** 图片地址 */
    private String picUrl;

    /** wonderful_time */
    private String wonderfulTime;

    /** 环信uuid */
    private String easemobUuid;

    /** phone_type */
    private Integer phoneType;

    /** access_token */
    private String accessToken;

    /** 用户最后一次登陆时间 */
    private String loginTime;

    /** 老师EMS账号 */
    private String userCode;

    /** 身份证号 */
    private String identityNum;

    /** 当前用户类型，1 家长，2 老师 */
    private String currentType;

    /** # 当前用户对应uuid。家长为childUuid，老师为classUuid */
    private String currentUuid;

    /** 对应云痕老师ID */
    private String yunhteacherid;

    /** 对应云痕老师编号 */
    private String yunhteachercode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getWonderfulTime() {
        return wonderfulTime;
    }

    public void setWonderfulTime(String wonderfulTime) {
        this.wonderfulTime = wonderfulTime;
    }

    public String getEasemobUuid() {
        return easemobUuid;
    }

    public void setEasemobUuid(String easemobUuid) {
        this.easemobUuid = easemobUuid;
    }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getIdentityNum() {
        return identityNum;
    }

    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    public String getCurrentType() {
        return currentType;
    }

    public void setCurrentType(String currentType) {
        this.currentType = currentType;
    }

    public String getCurrentUuid() {
        return currentUuid;
    }

    public void setCurrentUuid(String currentUuid) {
        this.currentUuid = currentUuid;
    }

    public String getYunhteacherid() {
        return yunhteacherid;
    }

    public void setYunhteacherid(String yunhteacherid) {
        this.yunhteacherid = yunhteacherid;
    }

    public String getYunhteachercode() {
        return yunhteachercode;
    }

    public void setYunhteachercode(String yunhteachercode) {
        this.yunhteachercode = yunhteachercode;
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
            super();
        }

        public MapBuilder withId(Long ... id) {
            modelProperties.put("id", BaseModel.first(id, getId()));
            return this;
        }

        public MapBuilder withUuid(String ... uuid) {
            modelProperties.put("uuid", BaseModel.first(uuid, getUuid()));
            return this;
        }

        public MapBuilder withPhone(String ... phone) {
            modelProperties.put("phone", BaseModel.first(phone, getPhone()));
            return this;
        }

        public MapBuilder withPassword(String ... password) {
            modelProperties.put("password", BaseModel.first(password, getPassword()));
            return this;
        }

        public MapBuilder withFirstname(String ... firstname) {
            modelProperties.put("firstname", BaseModel.first(firstname, getFirstname()));
            return this;
        }

        public MapBuilder withLastname(String ... lastname) {
            modelProperties.put("lastname", BaseModel.first(lastname, getLastname()));
            return this;
        }

        public MapBuilder withNickname(String ... nickname) {
            modelProperties.put("nickname", BaseModel.first(nickname, getNickname()));
            return this;
        }

        public MapBuilder withType(Integer ... type) {
            modelProperties.put("type", BaseModel.first(type, getType()));
            return this;
        }

        public MapBuilder withStatus(Integer ... status) {
            modelProperties.put("status", BaseModel.first(status, getStatus()));
            return this;
        }

        public MapBuilder withCreatedTime(String ... createdTime) {
            modelProperties.put("createdTime", BaseModel.first(createdTime, getCreatedTime()));
            return this;
        }

        public MapBuilder withModifiedTime(String ... modifiedTime) {
            modelProperties.put("modifiedTime", BaseModel.first(modifiedTime, getModifiedTime()));
            return this;
        }

        public MapBuilder withPicUrl(String ... picUrl) {
            modelProperties.put("picUrl", BaseModel.first(picUrl, getPicUrl()));
            return this;
        }

        public MapBuilder withWonderfulTime(String ... wonderfulTime) {
            modelProperties.put("wonderfulTime", BaseModel.first(wonderfulTime, getWonderfulTime()));
            return this;
        }

        public MapBuilder withEasemobUuid(String ... easemobUuid) {
            modelProperties.put("easemobUuid", BaseModel.first(easemobUuid, getEasemobUuid()));
            return this;
        }

        public MapBuilder withPhoneType(Integer ... phoneType) {
            modelProperties.put("phoneType", BaseModel.first(phoneType, getPhoneType()));
            return this;
        }

        public MapBuilder withAccessToken(String ... accessToken) {
            modelProperties.put("accessToken", BaseModel.first(accessToken, getAccessToken()));
            return this;
        }

        public MapBuilder withLoginTime(String ... loginTime) {
            modelProperties.put("loginTime", BaseModel.first(loginTime, getLoginTime()));
            return this;
        }

        public MapBuilder withUserCode(String ... userCode) {
            modelProperties.put("userCode", BaseModel.first(userCode, getUserCode()));
            return this;
        }

        public MapBuilder withIdentityNum(String ... identityNum) {
            modelProperties.put("identityNum", BaseModel.first(identityNum, getIdentityNum()));
            return this;
        }

        public MapBuilder withCurrentType(String ... currentType) {
            modelProperties.put("currentType", BaseModel.first(currentType, getCurrentType()));
            return this;
        }

        public MapBuilder withCurrentUuid(String ... currentUuid) {
            modelProperties.put("currentUuid", BaseModel.first(currentUuid, getCurrentUuid()));
            return this;
        }

        public MapBuilder withYunhteacherid(String ... yunhteacherid) {
            modelProperties.put("yunhteacherid", BaseModel.first(yunhteacherid, getYunhteacherid()));
            return this;
        }

        public MapBuilder withYunhteachercode(String ... yunhteachercode) {
            modelProperties.put("yunhteachercode", BaseModel.first(yunhteachercode, getYunhteachercode()));
            return this;
        }

        public Map<String, Object> build() {
            return modelProperties;
        }
    }
}