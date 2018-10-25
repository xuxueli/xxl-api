package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 17/3/30.
 */
@Mapper
public interface IXxlApiGroupDao {

    public int add(XxlApiGroup xxlApiGroup);
    public int update(XxlApiGroup xxlApiGroup);
    public int delete(@Param("id") int id);

    public XxlApiGroup load(@Param("id") int id);
    public List<XxlApiGroup> loadAll(@Param("projectId") int projectId);

}
