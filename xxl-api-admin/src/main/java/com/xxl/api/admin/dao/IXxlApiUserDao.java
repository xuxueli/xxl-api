package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiUser;

import java.util.List;

/**
 * Created by xuxueli on 17/3/29.
 */
public interface IXxlApiUserDao {

    public int add(XxlApiUser xxlApiUser);

    public int update(XxlApiUser xxlApiUser);

    public int delete(int id);

    public XxlApiUser findByUserName(String userName);

    public XxlApiUser findById(int id);

    public List<XxlApiUser> loadAll();

}
