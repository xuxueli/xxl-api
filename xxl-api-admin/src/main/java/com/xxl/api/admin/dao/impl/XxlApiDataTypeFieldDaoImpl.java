package com.xxl.api.admin.dao.impl;

import com.xxl.api.admin.core.model.XxlApiDataTypeField;
import com.xxl.api.admin.dao.IXxlApiDataTypeFieldDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 17/6/3.
 */
@Repository
public class XxlApiDataTypeFieldDaoImpl implements IXxlApiDataTypeFieldDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;


    @Override
    public int add(List<XxlApiDataTypeField> xxlApiDataTypeFieldList) {
        return sqlSessionTemplate.insert("XxlApiDataTypeFieldMapper.add", xxlApiDataTypeFieldList);
    }

    @Override
    public int deleteByParentDatatypeId(int parentDatatypeId) {
        return sqlSessionTemplate.delete("XxlApiDataTypeFieldMapper.deleteByParentDatatypeId", parentDatatypeId);
    }

    @Override
    public List<XxlApiDataTypeField> findByParentDatatypeId(int parentDatatypeId) {
        return sqlSessionTemplate.selectList("XxlApiDataTypeFieldMapper.findByParentDatatypeId", parentDatatypeId);
    }

    @Override
    public List<XxlApiDataTypeField> findByFieldDatatypeId(int fieldDatatypeId) {
        return sqlSessionTemplate.selectList("XxlApiDataTypeFieldMapper.findByFieldDatatypeId", fieldDatatypeId);
    }
}
