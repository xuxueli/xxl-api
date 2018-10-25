package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiDataType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 17/6/3.
 */
@Mapper
public interface IXxlApiDataTypeDao {

    public int add(XxlApiDataType xxlApiDataType);

    public int update(XxlApiDataType xxlApiDataType);

    public int delete(@Param("id") int id);

    public XxlApiDataType load(@Param("id") int id);

    public List<XxlApiDataType> pageList(@Param("offset") int offset,
                                         @Param("pagesize") int pagesize,
                                         @Param("bizId") int bizId,
                                         @Param("name") String name);
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("bizId") int bizId,
                             @Param("name") String name);

    public XxlApiDataType loadByName(@Param("name") String name);

}
