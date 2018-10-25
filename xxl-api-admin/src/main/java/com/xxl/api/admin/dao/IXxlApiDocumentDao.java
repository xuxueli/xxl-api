package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiDocument;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 17/3/31.
 */
@Mapper
public interface IXxlApiDocumentDao {

    public int add(XxlApiDocument xxlApiDocument);
    public int update(XxlApiDocument xxlApiDocument);
    public int delete(@Param("id") int id);

    public XxlApiDocument load(@Param("id") int id);
    public List<XxlApiDocument> loadAll(@Param("projectId") int projectId,
                                        @Param("groupId") int groupId);

    public List<XxlApiDocument> loadByGroupId(@Param("groupId") int groupId);

    List<XxlApiDocument> findByResponseDataTypeId(@Param("responseDatatypeId") int responseDatatypeId);

}
