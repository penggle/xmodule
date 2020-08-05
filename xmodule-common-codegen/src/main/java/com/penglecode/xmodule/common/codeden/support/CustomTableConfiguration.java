package com.penglecode.xmodule.common.codeden.support;

import com.penglecode.xmodule.common.codegen.mybatis.QueryConditionOperator;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.common.util.BeanUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.TableConfiguration;

import java.util.*;


/**
 * Mybatis代码生成的表配置
 *
 * @author pengpeng
 * @version 1.0
 * @date 2020/8/4 11:26
 */
public class CustomTableConfiguration extends TableConfiguration {

    /**
     * 数据模型的名称(中文名)
     */
    private String modelName;

    /**
     * 数据模型的别名(必须英文)
     */
    private String modelAlias;

    /**
     * 指定insertModel的插入字段
     */
    private Set<String> insertModelColumns = new LinkedHashSet<>();

    /**
     * Model注释的author
     */
    private String modelCommentAuthor = "Mybatis-Generator";

    /**
     * 对应的Mapper接口上的注解
     * 默认添加@DefaultDatabase注解
     */
    private Set<String> mapperAnnotations = new LinkedHashSet<>(Collections.singleton(DefaultDatabase.class.getName()));

    /**
     * selectModelListByExample查询的where条件列
     *
     * @see QueryConditionOperator
     */
    private Map<String,String> exampleQueryConditions = new LinkedHashMap<>();

    public CustomTableConfiguration() {
        super(new Context(ModelType.FLAT));
        this.initDefaultTableConfigurations();
    }

    protected void initDefaultTableConfigurations() {
        disableDefaultTableConfigurations(this);
    }

    protected void disableDefaultTableConfigurations(TableConfiguration configuration) {
        //关闭public TableConfiguration(Context context)构造器中的默认设置
        //即关闭一些mybatis-generator的原来语句自动生成逻辑，改为完全自定义生成
        configuration.setSelectByExampleStatementEnabled(false);
        configuration.setDeleteByExampleStatementEnabled(false);
        configuration.setCountByExampleStatementEnabled(false);
        configuration.setUpdateByExampleStatementEnabled(false);
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelAlias() {
        return modelAlias;
    }

    public void setModelAlias(String modelAlias) {
        this.modelAlias = modelAlias;
    }

    public Set<String> getInsertModelColumns() {
        return insertModelColumns;
    }

    public void setInsertModelColumns(Set<String> insertModelColumns) {
        this.insertModelColumns = insertModelColumns;
    }

    public String getModelCommentAuthor() {
        return modelCommentAuthor;
    }

    public void setModelCommentAuthor(String modelCommentAuthor) {
        this.modelCommentAuthor = modelCommentAuthor;
    }

    public Set<String> getMapperAnnotations() {
        return mapperAnnotations;
    }

    public void setMapperAnnotations(Set<String> mapperAnnotations) {
        this.mapperAnnotations = mapperAnnotations;
    }

    public Map<String, String> getExampleQueryConditions() {
        return exampleQueryConditions;
    }

    public void setExampleQueryConditions(Map<String, String> exampleQueryConditions) {
        this.exampleQueryConditions = exampleQueryConditions;
    }

    public TableConfiguration asTableConfiguration(Context context) {
        TableConfiguration configuration = new TableConfiguration(context);
        BeanUtils.copyProperties(this, configuration);
        configuration.addProperty("modelName", modelName);
        configuration.addProperty("modelAlias", modelAlias);
        configuration.addProperty("insertModelColumns", JsonUtils.object2Json(insertModelColumns));
        configuration.addProperty("modelCommentAuthor", modelCommentAuthor);
        configuration.addProperty("mapperAnnotations", String.join(",", mapperAnnotations));
        configuration.addProperty("exampleQueryConditions", JsonUtils.object2Json(exampleQueryConditions));
        return configuration;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
