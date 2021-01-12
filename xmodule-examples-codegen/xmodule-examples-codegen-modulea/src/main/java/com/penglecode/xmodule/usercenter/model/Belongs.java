package com.penglecode.xmodule.usercenter.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 所属
 * 
 * @author Customized-Mybatis-Generator
 * @date	2021年01月10日 下午 22:58:30
 */
@Model(name="所属信息", alias="Belongs")
public class Belongs implements BaseModel<Belongs> {
     
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    private Long id;

    /** uuid */
    private String uuid;

    /** 用户uuid : 用户类型为
3:老师
4:学校负责人
时使用此列作为与用户(users)的关联 */
    private String userUuid;

    /** 省uuid */
    private String provinceUuid;

    /** 城市uuid */
    private String cityUuid;

    /** 地区uuid */
    private String areaUuid;

    /** 学校uuid */
    private String schoolUuid;

    /** 年级uuid */
    private String gradeUuid;

    /** 班级uuid */
    private String classUuid;

    /** 儿童机uuid : 用户类型为
1:主监护人
2:副监护人
时使用此列作为与儿童机(children)的关联 */
    private String childUuid;

    /** 类型 : 1:家长
2:老师
3:学校 */
    private Integer type;

    /** 创建时间 */
    private String createdTime;

    /** 更新时间 */
    private String modifiedTime;

    /** 住校标示
0:不住校
1:住校 */
    private Integer residenceFlag;

    /** 科目uuid */
    private String subjectUuid;

    /** 教师类型
1:普通
2:班主任 */
    private Integer chargedType;

    /** 对应海康人员ID */
    private String hikstudentid;

    /** 对应云痕学生ID */
    private String yunhstudid;

    /** 对应云痕学生编号 */
    private String yunhstudcode;

    /** 对应云痕学生学号 */
    private String yunhstudno;

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

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getProvinceUuid() {
        return provinceUuid;
    }

    public void setProvinceUuid(String provinceUuid) {
        this.provinceUuid = provinceUuid;
    }

    public String getCityUuid() {
        return cityUuid;
    }

    public void setCityUuid(String cityUuid) {
        this.cityUuid = cityUuid;
    }

    public String getAreaUuid() {
        return areaUuid;
    }

    public void setAreaUuid(String areaUuid) {
        this.areaUuid = areaUuid;
    }

    public String getSchoolUuid() {
        return schoolUuid;
    }

    public void setSchoolUuid(String schoolUuid) {
        this.schoolUuid = schoolUuid;
    }

    public String getGradeUuid() {
        return gradeUuid;
    }

    public void setGradeUuid(String gradeUuid) {
        this.gradeUuid = gradeUuid;
    }

    public String getClassUuid() {
        return classUuid;
    }

    public void setClassUuid(String classUuid) {
        this.classUuid = classUuid;
    }

    public String getChildUuid() {
        return childUuid;
    }

    public void setChildUuid(String childUuid) {
        this.childUuid = childUuid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Integer getResidenceFlag() {
        return residenceFlag;
    }

    public void setResidenceFlag(Integer residenceFlag) {
        this.residenceFlag = residenceFlag;
    }

    public String getSubjectUuid() {
        return subjectUuid;
    }

    public void setSubjectUuid(String subjectUuid) {
        this.subjectUuid = subjectUuid;
    }

    public Integer getChargedType() {
        return chargedType;
    }

    public void setChargedType(Integer chargedType) {
        this.chargedType = chargedType;
    }

    public String getHikstudentid() {
        return hikstudentid;
    }

    public void setHikstudentid(String hikstudentid) {
        this.hikstudentid = hikstudentid;
    }

    public String getYunhstudid() {
        return yunhstudid;
    }

    public void setYunhstudid(String yunhstudid) {
        this.yunhstudid = yunhstudid;
    }

    public String getYunhstudcode() {
        return yunhstudcode;
    }

    public void setYunhstudcode(String yunhstudcode) {
        this.yunhstudcode = yunhstudcode;
    }

    public String getYunhstudno() {
        return yunhstudno;
    }

    public void setYunhstudno(String yunhstudno) {
        this.yunhstudno = yunhstudno;
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
         
        private final Map<String, Object> modelProperties = new LinkedHashMap<>();

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

        public MapBuilder withUserUuid(String ... userUuid) {
            modelProperties.put("userUuid", BaseModel.first(userUuid, getUserUuid()));
            return this;
        }

        public MapBuilder withProvinceUuid(String ... provinceUuid) {
            modelProperties.put("provinceUuid", BaseModel.first(provinceUuid, getProvinceUuid()));
            return this;
        }

        public MapBuilder withCityUuid(String ... cityUuid) {
            modelProperties.put("cityUuid", BaseModel.first(cityUuid, getCityUuid()));
            return this;
        }

        public MapBuilder withAreaUuid(String ... areaUuid) {
            modelProperties.put("areaUuid", BaseModel.first(areaUuid, getAreaUuid()));
            return this;
        }

        public MapBuilder withSchoolUuid(String ... schoolUuid) {
            modelProperties.put("schoolUuid", BaseModel.first(schoolUuid, getSchoolUuid()));
            return this;
        }

        public MapBuilder withGradeUuid(String ... gradeUuid) {
            modelProperties.put("gradeUuid", BaseModel.first(gradeUuid, getGradeUuid()));
            return this;
        }

        public MapBuilder withClassUuid(String ... classUuid) {
            modelProperties.put("classUuid", BaseModel.first(classUuid, getClassUuid()));
            return this;
        }

        public MapBuilder withChildUuid(String ... childUuid) {
            modelProperties.put("childUuid", BaseModel.first(childUuid, getChildUuid()));
            return this;
        }

        public MapBuilder withType(Integer ... type) {
            modelProperties.put("type", BaseModel.first(type, getType()));
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

        public MapBuilder withResidenceFlag(Integer ... residenceFlag) {
            modelProperties.put("residenceFlag", BaseModel.first(residenceFlag, getResidenceFlag()));
            return this;
        }

        public MapBuilder withSubjectUuid(String ... subjectUuid) {
            modelProperties.put("subjectUuid", BaseModel.first(subjectUuid, getSubjectUuid()));
            return this;
        }

        public MapBuilder withChargedType(Integer ... chargedType) {
            modelProperties.put("chargedType", BaseModel.first(chargedType, getChargedType()));
            return this;
        }

        public MapBuilder withHikstudentid(String ... hikstudentid) {
            modelProperties.put("hikstudentid", BaseModel.first(hikstudentid, getHikstudentid()));
            return this;
        }

        public MapBuilder withYunhstudid(String ... yunhstudid) {
            modelProperties.put("yunhstudid", BaseModel.first(yunhstudid, getYunhstudid()));
            return this;
        }

        public MapBuilder withYunhstudcode(String ... yunhstudcode) {
            modelProperties.put("yunhstudcode", BaseModel.first(yunhstudcode, getYunhstudcode()));
            return this;
        }

        public MapBuilder withYunhstudno(String ... yunhstudno) {
            modelProperties.put("yunhstudno", BaseModel.first(yunhstudno, getYunhstudno()));
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