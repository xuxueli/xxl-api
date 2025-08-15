package com.xxl.api.admin.mapper;

import com.xxl.api.admin.model.XxlApiDataType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 17/6/3.
 */
@Mapper
public interface XxlApiDataTypeMapper {

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
