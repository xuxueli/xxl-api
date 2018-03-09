package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiDataTypeField;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuxueli on 17/6/3.
 */
@Component
public interface IXxlApiDataTypeFieldDao {

    public int add(List<XxlApiDataTypeField> xxlApiDataTypeFieldList);

    public int deleteByParentDatatypeId(@Param("parentDatatypeId") int parentDatatypeId);

    public List<XxlApiDataTypeField> findByParentDatatypeId(@Param("parentDatatypeId") int parentDatatypeId);

    public List<XxlApiDataTypeField> findByFieldDatatypeId(@Param("fieldDatatypeId") int fieldDatatypeId);

}
