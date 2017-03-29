package com.xxl.api.admin.dao.impl;

import com.xxl.api.admin.core.model.XxlApiUser;
import com.xxl.api.admin.dao.IXxlApiUser;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 17/3/29.
 */
@Repository
public class XxlApiUserImpl implements IXxlApiUser {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(XxlApiUser xxlApiUser) {
        return sqlSessionTemplate.insert("IXxlApiUserMapper.add", xxlApiUser);
    }

    @Override
    public int update(XxlApiUser xxlApiUser) {
        return sqlSessionTemplate.update("IXxlApiUserMapper.update", xxlApiUser);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("IXxlApiUserMapper.delete", id);
    }

    @Override
    public int findByUserName(String userName) {
        return sqlSessionTemplate.selectOne("IXxlApiUserMapper.findByUserName", userName);
    }

    @Override
    public int findById(int id) {
        return sqlSessionTemplate.selectOne("IXxlApiUserMapper.findById", id);
    }

    @Override
    public List<XxlApiUser> loadAll() {
        return sqlSessionTemplate.selectList("IXxlApiUserMapper.loadAll");
    }
}
