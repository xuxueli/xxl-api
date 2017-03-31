package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiGroup;

import java.util.List;

/**
 * Created by xuxueli on 17/3/30.
 */
public interface IXxlApiGroupDao {

    public int add(XxlApiGroup xxlApiGroup);
    public int update(XxlApiGroup xxlApiGroup);
    public int delete(int id);

    public XxlApiGroup load(int id);
    public List<XxlApiGroup> loadAll(int productId);

}
