package com.xxl.api.admin.service.impl;

import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiUser;
import com.xxl.api.admin.core.util.CookieUtil;
import com.xxl.api.admin.dao.IXxlApiUserDao;
import com.xxl.api.admin.service.IXxlApiUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

/**
 * Created by xuxueli on 17/3/30.
 */
@Service
public class XxlApiUserServiceImpl implements IXxlApiUserService {

    private static final String LOGIN_IDENTITY_KEY = "XXL_API_LOGIN_IDENTITY";
    private static String makeToken (String userName, String password) {
        String temp = userName + "_" + password;
        String token = new BigInteger(1, temp.getBytes()).toString(16);
        return token;
    }

    @Resource
    private IXxlApiUserDao xxlApiUserDao;

    @Override
    public ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, boolean ifRemember, String userName, String password) {

        boolean ifLoginResult = ifLogin(request);
        if (ifLoginResult) {
            return ReturnT.SUCCESS;
        }

        XxlApiUser xxlApiUser = xxlApiUserDao.findByUserName(userName);
        if (xxlApiUser == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "登录用户不存在");
        }
        if (!xxlApiUser.getPassword().equals(password)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "登录密码错误");
        }

        String token = makeToken(userName, password);
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, token, ifRemember);

        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response) {

        boolean ifLoginResult = ifLogin(request);
        if (!ifLoginResult) {
            return ReturnT.SUCCESS;

        }

        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
        return ReturnT.SUCCESS;
    }

    @Override
    public boolean ifLogin(HttpServletRequest request) {

        String indentityInfo = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (indentityInfo==null || indentityInfo.trim().length()==0) {
            return false;
        }

        return true;
    }
}
