package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiUser;

import java.util.List;

/**
 * Created by xuxueli on 17/3/29.
 */
public interface IXxlApiUser {

    public int add(XxlApiUser xxlApiUser);

    public int update(XxlApiUser xxlApiUser);

    public int delete(int id);

    public int findByUserName(String userName);

    public int findById(int id);

    public List<XxlApiUser> loadAll();

}
