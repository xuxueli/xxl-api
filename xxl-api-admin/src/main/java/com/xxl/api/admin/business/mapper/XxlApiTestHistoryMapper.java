package com.xxl.api.admin.business.mapper;

import com.xxl.api.admin.business.model.XxlApiTestHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 2017-04-04 18:40:11
 */
@Mapper
public interface XxlApiTestHistoryMapper {

    public int add(XxlApiTestHistory xxlApiTestHistory);
    public int update(XxlApiTestHistory xxlApiTestHistory);
    public int delete(@Param("id") int id);

    public XxlApiTestHistory load(@Param("id") int id);
    public List<XxlApiTestHistory> loadByDocumentId(@Param("documentId") int documentId);
}
