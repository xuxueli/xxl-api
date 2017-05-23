package com.xxl.api.admin.dao.impl;

import com.xxl.api.admin.core.model.XxlApiProject;
import com.xxl.api.admin.dao.IXxlApiProjectDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 17/3/30.
 */
@Repository
public class XxlApiProjectDaoImpl implements IXxlApiProjectDao {

    @Resource
    public SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int add(XxlApiProject xxlApiProject) {
        return sqlSessionTemplate.insert("XxlApiProjectMapper.add", xxlApiProject);
    }

    @Override
    public int update(XxlApiProject xxlApiProject) {
        return sqlSessionTemplate.update("XxlApiProjectMapper.update", xxlApiProject);
    }

    @Override
    public int delete(int id) {
        return sqlSessionTemplate.update("XxlApiProjectMapper.delete", id);
    }

    @Override
    public XxlApiProject load(int id) {
        return sqlSessionTemplate.selectOne("XxlApiProjectMapper.load", id);
    }

    @Override
    public List<XxlApiProject> pageList(int offset, int pagesize, String name, int bizId) {
        Map<String, Object> params = new HashMap();
        params.put("offset", offset);
        params.put("pagesize", pagesize);
        params.put("name", name);
        params.put("bizId", bizId);

        return sqlSessionTemplate.selectList("XxlApiProjectMapper.pageList", params);
    }

    @Override
    public int pageListCount(int offset, int pagesize, String name, int bizId) {
        Map<String, Object> params = new HashMap();
        params.put("offset", offset);
        params.put("pagesize", pagesize);
        params.put("name", name);
        params.put("bizId", bizId);

        return sqlSessionTemplate.selectOne("XxlApiProjectMapper.pageListCount", params);
    }

}
