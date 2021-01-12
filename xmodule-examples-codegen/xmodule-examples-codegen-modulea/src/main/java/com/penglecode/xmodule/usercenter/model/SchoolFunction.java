package com.penglecode.xmodule.usercenter.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 学校扩展信息 (t_schools_function) 实体类
 * 
 * @author Customized-Mybatis-Generator
 * @date	2021年01月09日 下午 18:59:24
 */
@Model(name="学校扩展信息", alias="SchoolFunction")
public class SchoolFunction implements BaseModel<SchoolFunction> {
     
    private static final long serialVersionUID = 1L;

    /** id */
    @Id
    private Long id;

    /** 学校uuid */
    private String schooluuid;

    /** 0：未启用 1：启用 */
    private String hikCloud;

    /** 海康学校uuid */
    private String hkschooluuid;

    /** 0：未启用 1：启用 */
    private String yunHen;

    /** 云痕学校Id */
    private String yhschoolid;

    /** 云痕学校号码 */
    private String yunHenPhone;

    /** 0：未启用 1：启用 */
    private String yiXueYun;

    /** 亦学云学校ID */
    private String yxyschoolid;

    /** 创建时间 */
    private String createDate;

    /** 更新时间  */
    private String updateDate;

    /** 创建者 */
    private String createBy;

    /** 更新者 */
    private String updateBy;

    /** 删除标记 */
    private String delFlag;

    /** 备注 */
    private String remarks;

    /** 0：未启用 1：启用 */
    private String abcSchool;

    /** 学校班牌类型1:触沃,2:海康,3:大华 */
    private Integer epadType;

    /** 希沃学校Uid */
    private String seewoSchooluid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchooluuid() {
        return schooluuid;
    }

    public void setSchooluuid(String schooluuid) {
        this.schooluuid = schooluuid;
    }

    public String getHikCloud() {
        return hikCloud;
    }

    public void setHikCloud(String hikCloud) {
        this.hikCloud = hikCloud;
    }

    public String getHkschooluuid() {
        return hkschooluuid;
    }

    public void setHkschooluuid(String hkschooluuid) {
        this.hkschooluuid = hkschooluuid;
    }

    public String getYunHen() {
        return yunHen;
    }

    public void setYunHen(String yunHen) {
        this.yunHen = yunHen;
    }

    public String getYhschoolid() {
        return yhschoolid;
    }

    public void setYhschoolid(String yhschoolid) {
        this.yhschoolid = yhschoolid;
    }

    public String getYunHenPhone() {
        return yunHenPhone;
    }

    public void setYunHenPhone(String yunHenPhone) {
        this.yunHenPhone = yunHenPhone;
    }

    public String getYiXueYun() {
        return yiXueYun;
    }

    public void setYiXueYun(String yiXueYun) {
        this.yiXueYun = yiXueYun;
    }

    public String getYxyschoolid() {
        return yxyschoolid;
    }

    public void setYxyschoolid(String yxyschoolid) {
        this.yxyschoolid = yxyschoolid;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAbcSchool() {
        return abcSchool;
    }

    public void setAbcSchool(String abcSchool) {
        this.abcSchool = abcSchool;
    }

    public Integer getEpadType() {
        return epadType;
    }

    public void setEpadType(Integer epadType) {
        this.epadType = epadType;
    }

    public String getSeewoSchooluid() {
        return seewoSchooluid;
    }

    public void setSeewoSchooluid(String seewoSchooluid) {
        this.seewoSchooluid = seewoSchooluid;
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

        public MapBuilder withSchooluuid(String ... schooluuid) {
            modelProperties.put("schooluuid", BaseModel.first(schooluuid, getSchooluuid()));
            return this;
        }

        public MapBuilder withHikCloud(String ... hikCloud) {
            modelProperties.put("hikCloud", BaseModel.first(hikCloud, getHikCloud()));
            return this;
        }

        public MapBuilder withHkschooluuid(String ... hkschooluuid) {
            modelProperties.put("hkschooluuid", BaseModel.first(hkschooluuid, getHkschooluuid()));
            return this;
        }

        public MapBuilder withYunHen(String ... yunHen) {
            modelProperties.put("yunHen", BaseModel.first(yunHen, getYunHen()));
            return this;
        }

        public MapBuilder withYhschoolid(String ... yhschoolid) {
            modelProperties.put("yhschoolid", BaseModel.first(yhschoolid, getYhschoolid()));
            return this;
        }

        public MapBuilder withYunHenPhone(String ... yunHenPhone) {
            modelProperties.put("yunHenPhone", BaseModel.first(yunHenPhone, getYunHenPhone()));
            return this;
        }

        public MapBuilder withYiXueYun(String ... yiXueYun) {
            modelProperties.put("yiXueYun", BaseModel.first(yiXueYun, getYiXueYun()));
            return this;
        }

        public MapBuilder withYxyschoolid(String ... yxyschoolid) {
            modelProperties.put("yxyschoolid", BaseModel.first(yxyschoolid, getYxyschoolid()));
            return this;
        }

        public MapBuilder withCreateDate(String ... createDate) {
            modelProperties.put("createDate", BaseModel.first(createDate, getCreateDate()));
            return this;
        }

        public MapBuilder withUpdateDate(String ... updateDate) {
            modelProperties.put("updateDate", BaseModel.first(updateDate, getUpdateDate()));
            return this;
        }

        public MapBuilder withCreateBy(String ... createBy) {
            modelProperties.put("createBy", BaseModel.first(createBy, getCreateBy()));
            return this;
        }

        public MapBuilder withUpdateBy(String ... updateBy) {
            modelProperties.put("updateBy", BaseModel.first(updateBy, getUpdateBy()));
            return this;
        }

        public MapBuilder withDelFlag(String ... delFlag) {
            modelProperties.put("delFlag", BaseModel.first(delFlag, getDelFlag()));
            return this;
        }

        public MapBuilder withRemarks(String ... remarks) {
            modelProperties.put("remarks", BaseModel.first(remarks, getRemarks()));
            return this;
        }

        public MapBuilder withAbcSchool(String ... abcSchool) {
            modelProperties.put("abcSchool", BaseModel.first(abcSchool, getAbcSchool()));
            return this;
        }

        public MapBuilder withEpadType(Integer ... epadType) {
            modelProperties.put("epadType", BaseModel.first(epadType, getEpadType()));
            return this;
        }

        public MapBuilder withSeewoSchooluid(String ... seewoSchooluid) {
            modelProperties.put("seewoSchooluid", BaseModel.first(seewoSchooluid, getSeewoSchooluid()));
            return this;
        }

        public Map<String, Object> build() {
            return modelProperties;
        }
    }
}