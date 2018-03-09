package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiBiz;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuxueli on 17/5/23.
 */
@Component
public interface IXxlApiBizDao {

    public int add(XxlApiBiz xxlApiBiz);

    public int update(XxlApiBiz xxlApiBiz);

    public int delete(@Param("id") int id);

    public List<XxlApiBiz> loadAll();

}
