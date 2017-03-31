package com.xxl.api.admin.dao.impl;

import com.xxl.api.admin.core.model.XxlApiGroup;
import com.xxl.api.admin.dao.IXxlApiGroupDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 17/3/30.
 */
@Repository
public class XxlApiGroupDaoImpl implements IXxlApiGroupDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(XxlApiGroup xxlApiGroup) {
        return sqlSessionTemplate.insert("XxlApiGroupMapper.add", xxlApiGroup);
    }

    @Override
    public int update(XxlApiGroup xxlApiGroup) {
        return sqlSessionTemplate.update("XxlApiGroupMapper.update", xxlApiGroup);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("XxlApiGroupMapper.delete", id);
    }

    @Override
    public XxlApiGroup load(int id) {
        return sqlSessionTemplate.selectOne("XxlApiGroupMapper.load", id);
    }

    @Override
    public List<XxlApiGroup> loadAll(int productId) {
        return sqlSessionTemplate.selectList("XxlApiGroupMapper.loadAll", productId);
    }

}
