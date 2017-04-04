package com.xxl.api.admin.dao.impl;

import com.xxl.api.admin.core.model.XxlApiTestHistory;
import com.xxl.api.admin.dao.IXxlApiTestHistoryDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 2017-04-04 18:40:34
 */
@Repository
public class XxlApiTestHistoryDaoImpl implements IXxlApiTestHistoryDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(XxlApiTestHistory xxlApiTestHistory) {
        return sqlSessionTemplate.insert("XxlApiTestHistoryMapper.add", xxlApiTestHistory);
    }

    @Override
    public int update(XxlApiTestHistory xxlApiTestHistory) {
        return sqlSessionTemplate.update("XxlApiTestHistoryMapper.update", xxlApiTestHistory);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("XxlApiTestHistoryMapper.delete", id);
    }

    @Override
    public XxlApiTestHistory load(int id) {
        return sqlSessionTemplate.selectOne("XxlApiTestHistoryMapper.load", id);
    }

    @Override
    public List<XxlApiTestHistory> loadByDocumentId(int documentId) {
        return sqlSessionTemplate.selectList("XxlApiTestHistoryMapper.loadByDocumentId", documentId);
    }


}
