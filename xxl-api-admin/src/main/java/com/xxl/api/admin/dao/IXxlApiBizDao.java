package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiBiz;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 17/5/23.
 */
@Mapper
public interface IXxlApiBizDao {

    public int add(XxlApiBiz xxlApiBiz);

    public int update(XxlApiBiz xxlApiBiz);

    public int delete(@Param("id") int id);

    public List<XxlApiBiz> loadAll();

    public List<XxlApiBiz> pageList(@Param("offset") int offset,
                                    @Param("pagesize") int pagesize,
                                    @Param("bizName") String bizName);
    public int pageListCount(@Param("offset") int offset,
                             @Param("pagesize") int pagesize,
                             @Param("bizName") String bizName);

    public XxlApiBiz load(@Param("id") int id);

}
