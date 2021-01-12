package com.penglecode.xmodule.usercenter.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 年级
 * 
 * @author Customized-Mybatis-Generator
 * @date	2021年01月10日 下午 13:11:31
 */
@Model(name="年级信息", alias="SysRole")
public class Grade implements BaseModel<Grade> {
     
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    private Long id;

    /** 学校uuid */
    private String schoolUuid;

    /** uuid */
    private String uuid;

    /** 名称 */
    private String name;

    /** 状态 : 0:无效
1:有效 */
    private Integer status;

    /** 创建时间 */
    private String createdTime;

    /** 更新时间 */
    private String modifiedTime;

    /** 类别uuid */
    private String typeUuid;

    /** 届级 */
    private Integer gradeSession;

    /** 年级类别1-4  幼儿园-高中 */
    private String gradeLevel;

    /** 年级类型(0：一般年级，1：特殊年级) */
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolUuid() {
        return schoolUuid;
    }

    public void setSchoolUuid(String schoolUuid) {
        this.schoolUuid = schoolUuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTypeUuid() {
        return typeUuid;
    }

    public void setTypeUuid(String typeUuid) {
        this.typeUuid = typeUuid;
    }

    public Integer getGradeSession() {
        return gradeSession;
    }

    public void setGradeSession(Integer gradeSession) {
        this.gradeSession = gradeSession;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

        public MapBuilder withId(Long ... id) {
            modelProperties.put("id", BaseModel.first(id, getId()));
            return this;
        }

        public MapBuilder withSchoolUuid(String ... schoolUuid) {
            modelProperties.put("schoolUuid", BaseModel.first(schoolUuid, getSchoolUuid()));
            return this;
        }

        public MapBuilder withUuid(String ... uuid) {
            modelProperties.put("uuid", BaseModel.first(uuid, getUuid()));
            return this;
        }

        public MapBuilder withName(String ... name) {
            modelProperties.put("name", BaseModel.first(name, getName()));
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

        public MapBuilder withTypeUuid(String ... typeUuid) {
            modelProperties.put("typeUuid", BaseModel.first(typeUuid, getTypeUuid()));
            return this;
        }

        public MapBuilder withGradeSession(Integer ... gradeSession) {
            modelProperties.put("gradeSession", BaseModel.first(gradeSession, getGradeSession()));
            return this;
        }

        public MapBuilder withGradeLevel(String ... gradeLevel) {
            modelProperties.put("gradeLevel", BaseModel.first(gradeLevel, getGradeLevel()));
            return this;
        }

        public MapBuilder withType(String ... type) {
            modelProperties.put("type", BaseModel.first(type, getType()));
            return this;
        }

        public Map<String, Object> build() {
            return modelProperties;
        }
    }
}