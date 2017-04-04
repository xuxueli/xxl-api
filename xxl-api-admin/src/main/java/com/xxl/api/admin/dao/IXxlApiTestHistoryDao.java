package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiTestHistory;

import java.util.List;

/**
 * Created by xuxueli on 2017-04-04 18:40:11
 */
public interface IXxlApiTestHistoryDao {

    public int add(XxlApiTestHistory xxlApiTestHistory);
    public int update(XxlApiTestHistory xxlApiTestHistory);
    public int delete(int id);

    public XxlApiTestHistory load(int id);
    public List<XxlApiTestHistory> loadByDocumentId(int documentId);
}
