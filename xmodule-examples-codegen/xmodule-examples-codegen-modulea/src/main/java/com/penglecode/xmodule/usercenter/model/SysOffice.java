package com.penglecode.xmodule.usercenter.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 机构表
 * 
 * @author Customized-Mybatis-Generator
 * @date	2021年01月09日 下午 22:45:11
 */
@Model(name="组织机构", alias="SysOffice")
public class SysOffice implements BaseModel<SysOffice> {
     
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @Id
    private String id;

    /** 父级编号 */
    private String parentId;

    /** 所有父级编号 */
    private String parentIds;

    /** 名称 */
    private String name;

    /** 排序 */
    private Double sort;

    /** 归属区域 */
    private String areaId;

    /** 区域编码 */
    private String code;

    /** 机构类型 */
    private String type;

    /** 机构等级 */
    private String grade;

    /** 联系地址 */
    private String address;

    /** 邮政编码 */
    private String zipCode;

    /** 负责人 */
    private String master;

    /** 电话 */
    private String phone;

    /** 传真 */
    private String fax;

    /** 邮箱 */
    private String email;

    /** 是否启用 */
    private String useable;

    /** 主负责人 */
    private String primaryPerson;

    /** 副负责人 */
    private String deputyPerson;

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

    /** 年级类别 */
    private String gradeLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSort() {
        return sort;
    }

    public void setSort(Double sort) {
        this.sort = sort;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUseable() {
        return useable;
    }

    public void setUseable(String useable) {
        this.useable = useable;
    }

    public String getPrimaryPerson() {
        return primaryPerson;
    }

    public void setPrimaryPerson(String primaryPerson) {
        this.primaryPerson = primaryPerson;
    }

    public String getDeputyPerson() {
        return deputyPerson;
    }

    public void setDeputyPerson(String deputyPerson) {
        this.deputyPerson = deputyPerson;
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

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
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

        public MapBuilder withParentId(String ... parentId) {
            modelProperties.put("parentId", BaseModel.first(parentId, getParentId()));
            return this;
        }

        public MapBuilder withParentIds(String ... parentIds) {
            modelProperties.put("parentIds", BaseModel.first(parentIds, getParentIds()));
            return this;
        }

        public MapBuilder withName(String ... name) {
            modelProperties.put("name", BaseModel.first(name, getName()));
            return this;
        }

        public MapBuilder withSort(Double ... sort) {
            modelProperties.put("sort", BaseModel.first(sort, getSort()));
            return this;
        }

        public MapBuilder withAreaId(String ... areaId) {
            modelProperties.put("areaId", BaseModel.first(areaId, getAreaId()));
            return this;
        }

        public MapBuilder withCode(String ... code) {
            modelProperties.put("code", BaseModel.first(code, getCode()));
            return this;
        }

        public MapBuilder withType(String ... type) {
            modelProperties.put("type", BaseModel.first(type, getType()));
            return this;
        }

        public MapBuilder withGrade(String ... grade) {
            modelProperties.put("grade", BaseModel.first(grade, getGrade()));
            return this;
        }

        public MapBuilder withAddress(String ... address) {
            modelProperties.put("address", BaseModel.first(address, getAddress()));
            return this;
        }

        public MapBuilder withZipCode(String ... zipCode) {
            modelProperties.put("zipCode", BaseModel.first(zipCode, getZipCode()));
            return this;
        }

        public MapBuilder withMaster(String ... master) {
            modelProperties.put("master", BaseModel.first(master, getMaster()));
            return this;
        }

        public MapBuilder withPhone(String ... phone) {
            modelProperties.put("phone", BaseModel.first(phone, getPhone()));
            return this;
        }

        public MapBuilder withFax(String ... fax) {
            modelProperties.put("fax", BaseModel.first(fax, getFax()));
            return this;
        }

        public MapBuilder withEmail(String ... email) {
            modelProperties.put("email", BaseModel.first(email, getEmail()));
            return this;
        }

        public MapBuilder withUseable(String ... useable) {
            modelProperties.put("useable", BaseModel.first(useable, getUseable()));
            return this;
        }

        public MapBuilder withPrimaryPerson(String ... primaryPerson) {
            modelProperties.put("primaryPerson", BaseModel.first(primaryPerson, getPrimaryPerson()));
            return this;
        }

        public MapBuilder withDeputyPerson(String ... deputyPerson) {
            modelProperties.put("deputyPerson", BaseModel.first(deputyPerson, getDeputyPerson()));
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

        public MapBuilder withGradeLevel(String ... gradeLevel) {
            modelProperties.put("gradeLevel", BaseModel.first(gradeLevel, getGradeLevel()));
            return this;
        }

        public Map<String, Object> build() {
            return modelProperties;
        }
    }
}