package com.xxl.api.admin.mapper;

import com.xxl.api.admin.model.XxlApiUser;
import com.xxl.tool.response.Response;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuxueli on 17/3/29.
 */
@Mapper
public interface XxlApiUserMapper {

    int add(XxlApiUser xxlApiUser);

    int update(XxlApiUser xxlApiUser);

    int delete(@Param("id") int id);

    XxlApiUser findByUserName(@Param("userName") String userName);

    XxlApiUser findById(@Param("id") int id);

    List<XxlApiUser> loadAll();

    List<XxlApiUser> pageList(@Param("offset") int offset,
                              @Param("pagesize") int pagesize,
                              @Param("userName") String userName,
                              @Param("type") int type);
    int pageListCount(@Param("offset") int offset,
                      @Param("pagesize") int pagesize,
                      @Param("userName") String userName,
                      @Param("type") int type);

    int updateToken(@Param("id") int id, @Param("token") String token);

}
