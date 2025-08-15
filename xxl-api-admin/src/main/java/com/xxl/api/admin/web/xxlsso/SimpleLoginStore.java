package com.xxl.api.admin.web.xxlsso;

import com.xxl.api.admin.mapper.XxlApiUserMapper;
import com.xxl.api.admin.model.XxlApiUser;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.sso.core.store.LoginStore;
import com.xxl.sso.core.token.TokenHelper;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Simple LoginStore
 *
 * 1、store by database；
 * 2、If you have higher performance requirements, it is recommended to use RedisLoginStore；
 *
 * @author xuxueli 2025-08-03
 */
@Component
public class SimpleLoginStore implements LoginStore {


    @Resource
    private XxlApiUserMapper xxlApiUserMapper;


    @Override
    public Response<String> set(LoginInfo loginInfo) {

        // build token
        Response<String> tokenResponse = TokenHelper.generateToken(loginInfo);
        if (!tokenResponse.isSuccess()) {
            return Response.ofFail("generate token fail");
        }
        String token = tokenResponse.getData();

        // write token by UserId
        int ret = xxlApiUserMapper.updateToken(Integer.valueOf(loginInfo.getUserId()), token);
        return ret > 0 ? Response.ofSuccess(token) : Response.ofFail("set token fail");
    }

    @Override
    public Response<String> update(LoginInfo loginInfo) {
        return Response.ofFail("not support");
    }

    @Override
    public Response<String> remove(String userId) {
        // delete token by UserId
        int ret = xxlApiUserMapper.updateToken(Integer.valueOf(userId), "");
        return ret > 0 ? Response.ofSuccess() : Response.ofFail("remove token fail");
    }

    /**
     * check through DB query
     */
    @Override
    public Response<LoginInfo> get(String userId) {

        // load user by UserId
        XxlApiUser xxlBootUser = xxlApiUserMapper.findById(Integer.valueOf(userId));
        if (Objects.isNull(xxlBootUser)) {
            return Response.ofFail("userId invalid.");
        }

        // parse token of UserId
        LoginInfo loginInfo = TokenHelper.parseToken(xxlBootUser.getToken());
        if (loginInfo==null) {
            return Response.ofFail("token invalid.");
        }

        // find permission
        List<String> roleList = xxlBootUser.getType()==1? List.of("admin") : null;
        List<String> permissionList = StringTool.isNotBlank(xxlBootUser.getPermissionBiz())
                ? Arrays.asList(StringTool.tokenizeToArray(xxlBootUser.getPermissionBiz(), ","))
                :null;

        // fill data of loginInfo
        loginInfo.setUserName(xxlBootUser.getUserName());
        loginInfo.setRoleList(roleList);
        loginInfo.setPermissionList(permissionList);

        return Response.ofSuccess(loginInfo);
    }

}
