package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiDocument;

import java.util.List;

/**
 * Created by xuxueli on 17/3/31.
 */
public interface IXxlApiDocumentDao {

    public int add(XxlApiDocument xxlApiDocument);
    public int update(XxlApiDocument xxlApiDocument);
    public int delete(int id);

    public XxlApiDocument load(int id);
    public List<XxlApiDocument> loadAll(int productId, int groupId);

    public List<XxlApiDocument> loadByGroupId(int id);

    List<XxlApiDocument> findByResponseDataTypeId(int responseDatatypeId);

}
