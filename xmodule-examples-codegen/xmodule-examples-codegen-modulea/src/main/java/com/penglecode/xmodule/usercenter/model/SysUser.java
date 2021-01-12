package com.penglecode.xmodule.usercenter.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户表
 * 
 * @author Customized-Mybatis-Generator
 * @date	2021年01月09日 下午 22:06:27
 */
@Model(name="管理用户", alias="SysUser")
public class SysUser implements BaseModel<SysUser> {
     
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Id
    private String id;

    /** 归属部门 */
    private String officeId;

    /** 登录名 */
    private String loginName;

    /** 密码 */
    private String password;

    /** 工号 */
    private String no;

    /** 姓名 */
    private String name;

    /** 邮箱 */
    private String email;

    /** 电话 */
    private String phone;

    /** 手机 */
    private String mobile;

    /** 用户类型 */
    private String userType;

    /** 用户头像 */
    private String photo;

    /** 最后登陆IP */
    private String loginIp;

    /** 最后登陆时间 */
    private String loginDate;

    /** 是否可登录 */
    private String loginFlag;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private String createDate;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private String updateDate;

    /** 备注信息 */
    private String remarks;

    /** 删除标记 */
    private String delFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }

    public String getLoginFlag() {
        return loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public MapBuilder mapBuilder() {
        return new MapBuilder();
    }

    /**
     * Auto generated by Mybatis Generator
     */
    public class MapBuilder {
         
        private final Map<String, Object> modelProperties = new LinkedHashMap<>();

        MapBuilder() {
            super();
        }

        public MapBuilder withId(String ... id) {
            modelProperties.put("id", BaseModel.first(id, getId()));
            return this;
        }

        public MapBuilder withOfficeId(String ... officeId) {
            modelProperties.put("officeId", BaseModel.first(officeId, getOfficeId()));
            return this;
        }

        public MapBuilder withLoginName(String ... loginName) {
            modelProperties.put("loginName", BaseModel.first(loginName, getLoginName()));
            return this;
        }

        public MapBuilder withPassword(String ... password) {
            modelProperties.put("password", BaseModel.first(password, getPassword()));
            return this;
        }

        public MapBuilder withNo(String ... no) {
            modelProperties.put("no", BaseModel.first(no, getNo()));
            return this;
        }

        public MapBuilder withName(String ... name) {
            modelProperties.put("name", BaseModel.first(name, getName()));
            return this;
        }

        public MapBuilder withEmail(String ... email) {
            modelProperties.put("email", BaseModel.first(email, getEmail()));
            return this;
        }

        public MapBuilder withPhone(String ... phone) {
            modelProperties.put("phone", BaseModel.first(phone, getPhone()));
            return this;
        }

        public MapBuilder withMobile(String ... mobile) {
            modelProperties.put("mobile", BaseModel.first(mobile, getMobile()));
            return this;
        }

        public MapBuilder withUserType(String ... userType) {
            modelProperties.put("userType", BaseModel.first(userType, getUserType()));
            return this;
        }

        public MapBuilder withPhoto(String ... photo) {
            modelProperties.put("photo", BaseModel.first(photo, getPhoto()));
            return this;
        }

        public MapBuilder withLoginIp(String ... loginIp) {
            modelProperties.put("loginIp", BaseModel.first(loginIp, getLoginIp()));
            return this;
        }

        public MapBuilder withLoginDate(String ... loginDate) {
            modelProperties.put("loginDate", BaseModel.first(loginDate, getLoginDate()));
            return this;
        }

        public MapBuilder withLoginFlag(String ... loginFlag) {
            modelProperties.put("loginFlag", BaseModel.first(loginFlag, getLoginFlag()));
            return this;
        }

        public MapBuilder withCreateBy(String ... createBy) {
            modelProperties.put("createBy", BaseModel.first(createBy, getCreateBy()));
            return this;
        }

        public MapBuilder withCreateDate(String ... createDate) {
            modelProperties.put("createDate", BaseModel.first(createDate, getCreateDate()));
            return this;
        }

        public MapBuilder withUpdateBy(String ... updateBy) {
            modelProperties.put("updateBy", BaseModel.first(updateBy, getUpdateBy()));
            return this;
        }

        public MapBuilder withUpdateDate(String ... updateDate) {
            modelProperties.put("updateDate", BaseModel.first(updateDate, getUpdateDate()));
            return this;
        }

        public MapBuilder withRemarks(String ... remarks) {
            modelProperties.put("remarks", BaseModel.first(remarks, getRemarks()));
            return this;
        }

        public MapBuilder withDelFlag(String ... delFlag) {
            modelProperties.put("delFlag", BaseModel.first(delFlag, getDelFlag()));
            return this;
        }

        public Map<String, Object> build() {
            return modelProperties;
        }
    }
}