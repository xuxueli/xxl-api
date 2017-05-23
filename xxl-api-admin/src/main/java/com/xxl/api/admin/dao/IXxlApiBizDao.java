package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiBiz;

import java.util.List;

/**
 * Created by xuxueli on 17/5/23.
 */
public interface IXxlApiBizDao {

    public int add(XxlApiBiz xxlApiBiz);

    public int udpate(XxlApiBiz xxlApiBiz);

    public int delete(int id);

    public List<XxlApiBiz> loadAll();

}
