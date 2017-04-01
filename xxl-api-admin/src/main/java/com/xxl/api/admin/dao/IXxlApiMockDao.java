package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiMock;

import java.util.List;

/**
 * Created by xuxueli on 17/4/1.
 */
public interface IXxlApiMockDao {

    public int add(XxlApiMock xxlApiMock);
    public int update(XxlApiMock xxlApiMock);
    public int delete(int id);

    public List<XxlApiMock> loadAll(int documentId);
    public XxlApiMock load(int id);
    public XxlApiMock loadByUuid(String uuid);

}
