package com.xxl.api.admin.web.xxlsso;

import com.xxl.api.admin.mapper.XxlApiUserMapper;
import com.xxl.api.admin.model.XxlApiUser;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.sso.core.store.LoginStore;
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

        // parse token-signature
        String token_sign = loginInfo.getSignature();

        // write token by UserId
        int ret = xxlApiUserMapper.updateToken(Integer.parseInt(loginInfo.getUserId()), token_sign);
        return ret > 0 ? Response.ofSuccess() : Response.ofFail("set token fail");
    }

    @Override
    public Response<String> update(LoginInfo loginInfo) {
        return Response.ofFail("not support");
    }

    @Override
    public Response<String> remove(String userId) {
        // delete token-signature
        int ret = xxlApiUserMapper.updateToken(Integer.valueOf(userId), "");
        return ret > 0 ? Response.ofSuccess() : Response.ofFail("remove token fail");
    }

    /**
     * check through DB query
     */
    @Override
    public Response<LoginInfo> get(String userId) {

        // load login-user
        XxlApiUser user = xxlApiUserMapper.findById(Integer.parseInt(userId));
        if (Objects.isNull(user)) {
            return Response.ofFail("userId invalid.");
        }

        // find permission
        List<String> roleList = user.getType()==1? List.of("admin") : null;
        List<String> permissionList = StringTool.isNotBlank(user.getPermissionBiz())
                ? Arrays.asList(StringTool.tokenizeToArray(user.getPermissionBiz(), ","))
                :null;

        // build LoginInfo
        LoginInfo loginInfo = new LoginInfo(userId, user.getToken());
        loginInfo.setUserName(user.getUserName());
        loginInfo.setRoleList(roleList);
        loginInfo.setPermissionList(permissionList);

        return Response.ofSuccess(loginInfo);
    }

}
