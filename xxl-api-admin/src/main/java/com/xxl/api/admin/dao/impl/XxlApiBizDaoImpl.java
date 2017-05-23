package com.xxl.api.admin.dao.impl;

import com.xxl.api.admin.core.model.XxlApiBiz;
import com.xxl.api.admin.dao.IXxlApiBizDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xuxueli on 17/5/23.
 */
@Repository
public class XxlApiBizDaoImpl implements IXxlApiBizDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(XxlApiBiz xxlApiBiz) {
        return sqlSessionTemplate.insert("XxlApiBizMapper.add", xxlApiBiz);
    }

    @Override
    public int udpate(XxlApiBiz xxlApiBiz) {
        return sqlSessionTemplate.update("XxlApiBizMapper.update", xxlApiBiz);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.delete("XxlApiBizMapper.delete", id);
    }

    @Override
    public List<XxlApiBiz> loadAll() {
        return sqlSessionTemplate.selectList("XxlApiBizMapper.loadAll");
    }
}
