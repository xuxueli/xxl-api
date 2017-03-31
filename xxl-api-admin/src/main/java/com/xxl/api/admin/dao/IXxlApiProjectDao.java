package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiProject;

import java.util.List;

/**
 * Created by xuxueli on 17/3/30.
 */
public interface IXxlApiProjectDao {

    public int add(XxlApiProject xxlApiProject);
    public int update(XxlApiProject xxlApiProject);
    public int delete(int id);

    public XxlApiProject load(int id);
    public List<XxlApiProject> pageList(int offset, int pagesize, String name);
    public int pageListCount(int offset, int pagesize, String name);

}
