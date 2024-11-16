package com.xxl.api.admin.service.impl;

import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiUser;
import com.xxl.api.admin.core.util.CookieUtil;
import com.xxl.api.admin.dao.IXxlApiUserDao;
import com.xxl.tool.gson.GsonTool;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * Created by xuxueli on 17/3/30.
 */
@Configuration
public class LoginService {

    public static final String LOGIN_IDENTITY = "XXL_API_LOGIN_IDENTITY";

    @Resource
    private IXxlApiUserDao xxlApiUserDao;

    private String makeToken(XxlApiUser xxlApiUser){
        String tokenJson = GsonTool.toJson(xxlApiUser);
        String tokenHex = new BigInteger(tokenJson.getBytes()).toString(16);
        return tokenHex;
    }
    private XxlApiUser parseToken(String tokenHex){
        XxlApiUser xxlApiUser = null;
        if (tokenHex != null) {
            String tokenJson = new String(new BigInteger(tokenHex, 16).toByteArray());      // username_password(md5)
            xxlApiUser = GsonTool.fromJson(tokenJson, XxlApiUser.class);
        }
        return xxlApiUser;
    }


    /**
     * login
     *
     * @param response
     * @param usernameParam
     * @param passwordParam
     * @param ifRemember
     * @return
     */
    public ReturnT<String> login(HttpServletResponse response, String usernameParam, String passwordParam, boolean ifRemember){

        XxlApiUser xxlApiUser = xxlApiUserDao.findByUserName(usernameParam);
        if (xxlApiUser == null) {
            return new ReturnT<String>(500, "账号或密码错误");
        }

        String passwordParamMd5 = DigestUtils.md5DigestAsHex(passwordParam.getBytes());
        if (!xxlApiUser.getPassword().equals(passwordParamMd5)) {
            return new ReturnT<String>(500, "账号或密码错误");
        }

        String loginToken = makeToken(xxlApiUser);

        // do login
        CookieUtil.set(response, LOGIN_IDENTITY, loginToken, ifRemember);
        return ReturnT.SUCCESS;
    }

    /**
     * logout
     *
     * @param request
     * @param response
     */
    public void logout(HttpServletRequest request, HttpServletResponse response){
        CookieUtil.remove(request, response, LOGIN_IDENTITY);
    }

    /**
     * logout
     *
     * @param request
     * @return
     */
    public XxlApiUser ifLogin(HttpServletRequest request){
        String cookieToken = CookieUtil.getValue(request, LOGIN_IDENTITY);
        if (cookieToken != null) {
            XxlApiUser cookieUser = parseToken(cookieToken);
            if (cookieUser != null) {
                XxlApiUser dbUser = xxlApiUserDao.findByUserName(cookieUser.getUserName());
                if (dbUser != null) {
                    if (cookieUser.getPassword().equals(dbUser.getPassword())) {
                        return dbUser;
                    }
                }
            }
        }
        return null;
    }

}
