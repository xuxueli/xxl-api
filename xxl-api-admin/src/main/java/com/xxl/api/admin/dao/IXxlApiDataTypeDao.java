package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiDataType;

import java.util.List;

/**
 * Created by xuxueli on 17/6/3.
 */
public interface IXxlApiDataTypeDao {

    public int add(XxlApiDataType xxlApiDataType);

    public int update(XxlApiDataType xxlApiDataType);

    public int delete(int id);

    public XxlApiDataType load(int id);

    public List<XxlApiDataType> pageList(int offset, int pagesize, int bizId, String name);
    public int pageListCount(int offset, int pagesize, int bizId, String name);

    public XxlApiDataType loadByName(String name);

}
