package com.xxl.api.admin.dao;

import com.xxl.api.admin.core.model.XxlApiUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by xuxueli on 17/3/29.
 */
@Component
public interface IXxlApiUserDao {

    public int add(XxlApiUser xxlApiUser);

    public int update(XxlApiUser xxlApiUser);

    public int delete(@Param("id") int id);

    public XxlApiUser findByUserName(@Param("userName") String userName);

    public XxlApiUser findById(@Param("id") int id);

    public List<XxlApiUser> loadAll();

}
