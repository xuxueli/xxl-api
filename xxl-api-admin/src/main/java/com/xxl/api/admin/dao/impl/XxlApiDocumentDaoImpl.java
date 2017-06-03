package com.xxl.api.admin.dao.impl;

import com.xxl.api.admin.core.model.XxlApiDocument;
import com.xxl.api.admin.dao.IXxlApiDocumentDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 17/3/31.
 */
@Repository
public class XxlApiDocumentDaoImpl implements IXxlApiDocumentDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(XxlApiDocument xxlApiDocument) {
        return sqlSessionTemplate.insert("XxlApiDocumentMapper.add", xxlApiDocument);
    }

    @Override
    public int update(XxlApiDocument xxlApiDocument) {
        return sqlSessionTemplate.update("XxlApiDocumentMapper.update", xxlApiDocument);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("XxlApiDocumentMapper.delete", id);
    }

    @Override
    public XxlApiDocument load(int id) {
        return sqlSessionTemplate.selectOne("XxlApiDocumentMapper.load", id);
    }

    @Override
    public List<XxlApiDocument> loadAll(int productId, int groupId) {
        Map<String ,Integer> params = new HashMap<String, Integer>();
        params.put("productId", productId);
        params.put("groupId", groupId);

        return sqlSessionTemplate.selectList("XxlApiDocumentMapper.loadAll", params);
    }

    @Override
    public List<XxlApiDocument> loadByGroupId(int groupId) {
        return sqlSessionTemplate.selectList("XxlApiDocumentMapper.loadByGroupId", groupId);
    }

    @Override
    public List<XxlApiDocument> findByResponseDataTypeId(int responseDatatypeId) {
        return sqlSessionTemplate.selectList("XxlApiDocumentMapper.findByResponseDataTypeId", responseDatatypeId);
    }

}
