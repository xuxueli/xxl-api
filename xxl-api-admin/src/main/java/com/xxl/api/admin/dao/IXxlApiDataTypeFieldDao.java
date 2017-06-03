package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiDataTypeField;

import java.util.List;

/**
 * Created by xuxueli on 17/6/3.
 */
public interface IXxlApiDataTypeFieldDao {

    public int add(List<XxlApiDataTypeField> xxlApiDataTypeFieldList);

    public int deleteByParentDatatypeId(int parentDatatypeId);

    public List<XxlApiDataTypeField> findByParentDatatypeId(int parentDatatypeId);

    public List<XxlApiDataTypeField> findByFieldDatatypeId(int fieldDatatypeId);

}
