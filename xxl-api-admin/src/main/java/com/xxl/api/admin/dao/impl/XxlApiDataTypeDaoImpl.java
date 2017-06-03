package com.xxl.api.admin.dao.impl;

import com.xxl.api.admin.core.model.XxlApiDataType;
import com.xxl.api.admin.dao.IXxlApiDataTypeDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xuxueli on 17/6/3.
 */
@Repository
public class XxlApiDataTypeDaoImpl implements IXxlApiDataTypeDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;


    @Override
    public int add(XxlApiDataType xxlApiDataType) {
        return sqlSessionTemplate.insert("XxlApiDataTypeMapper.add", xxlApiDataType);
    }

    @Override
    public int update(XxlApiDataType xxlApiDataType) {
        return sqlSessionTemplate.update("XxlApiDataTypeMapper.update", xxlApiDataType);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.delete("XxlApiDataTypeMapper.delete", id);
    }

    @Override
    public XxlApiDataType load(int id) {
        return sqlSessionTemplate.selectOne("XxlApiDataTypeMapper.load", id);
    }

    @Override
    public List<XxlApiDataType> pageList(int offset, int pagesize, int bizId, String name) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("offset", offset);
        params.put("pagesize", pagesize);
        params.put("bizId", bizId);
        params.put("name", name);

        return sqlSessionTemplate.selectList("XxlApiDataTypeMapper.pageList", params);
    }

    @Override
    public int pageListCount(int offset, int pagesize, int bizId, String name) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("offset", offset);
        params.put("pagesize", pagesize);
        params.put("bizId", bizId);
        params.put("name", name);

        return sqlSessionTemplate.selectOne("XxlApiDataTypeMapper.pageListCount", params);
    }

    @Override
    public XxlApiDataType loadByName(String name) {
        return sqlSessionTemplate.selectOne("XxlApiDataTypeMapper.loadByName", name);
    }
}
