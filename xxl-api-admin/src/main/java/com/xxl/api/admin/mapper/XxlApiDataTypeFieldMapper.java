package com.xxl.api.admin.mapper;

import com.xxl.api.admin.model.XxlApiDataTypeField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 17/6/3.
 */
@Mapper
public interface XxlApiDataTypeFieldMapper {

    public int add(List<XxlApiDataTypeField> xxlApiDataTypeFieldList);

    public int deleteByParentDatatypeId(@Param("parentDatatypeId") int parentDatatypeId);

    public List<XxlApiDataTypeField> findByParentDatatypeId(@Param("parentDatatypeId") int parentDatatypeId);

    public List<XxlApiDataTypeField> findByFieldDatatypeId(@Param("fieldDatatypeId") int fieldDatatypeId);

}
