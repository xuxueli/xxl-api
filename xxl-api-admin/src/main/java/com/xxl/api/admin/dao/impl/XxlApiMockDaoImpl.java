package com.xxl.api.admin.dao.impl;

import com.xxl.api.admin.core.model.XxlApiMock;
import com.xxl.api.admin.dao.IXxlApiMockDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 17/4/1.
 */
@Repository
public class XxlApiMockDaoImpl implements IXxlApiMockDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(XxlApiMock xxlApiMock) {
        return sqlSessionTemplate.insert("XxlApiMockMapper.add", xxlApiMock);
    }

    @Override
    public int update(XxlApiMock xxlApiMock) {
        return sqlSessionTemplate.update("XxlApiMockMapper.update", xxlApiMock);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("XxlApiMockMapper.delete", id);
    }

    @Override
    public List<XxlApiMock> loadAll(int documentId) {
        return sqlSessionTemplate.selectList("XxlApiMockMapper.loadAll", documentId);
    }

    @Override
    public XxlApiMock load(int id) {
        return sqlSessionTemplate.selectOne("XxlApiMockMapper.load", id);
    }

    @Override
    public XxlApiMock loadByUuid(String uuid) {
        return sqlSessionTemplate.selectOne("XxlApiMockMapper.loadByUuid", uuid);
    }
}
