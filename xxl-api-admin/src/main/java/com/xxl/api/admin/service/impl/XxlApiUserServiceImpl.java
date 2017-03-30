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

    public static final String LOGIN_IDENTITY_KEY = "XXL_API_LOGIN_IDENTITY";
    private static String makeToken (XxlApiUser xxlApiUser) {
        String tokenStr = xxlApiUser.getUserName() + "_" + xxlApiUser.getPassword() + "_" + xxlApiUser.getType();
        String token = new BigInteger(1, tokenStr.getBytes()).toString(16);
        return token;
    }
    private static XxlApiUser parseToken(HttpServletRequest request){
        String token = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
        if (token != null) {
            String tokenStr = new String(new BigInteger(token, 16).toByteArray());
            String[] tokenArr = tokenStr.split("_");
            if (tokenArr!=null && tokenArr.length==3) {
                XxlApiUser xxlApiUser = new XxlApiUser();
                xxlApiUser.setUserName(tokenArr[0]);
                xxlApiUser.setPassword(tokenArr[1]);
                xxlApiUser.setType(Integer.valueOf(tokenArr[2]));
                return xxlApiUser;
            }
        }

        return null;
    }

    @Resource
    private IXxlApiUserDao xxlApiUserDao;

    @Override
    public ReturnT<String> login(HttpServletRequest request, HttpServletResponse response, boolean ifRemember, String userName, String password) {

        XxlApiUser loginUser = ifLogin(request);
        if (loginUser != null) {
            return ReturnT.SUCCESS;
        }

        XxlApiUser xxlApiUser = xxlApiUserDao.findByUserName(userName);
        if (xxlApiUser == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "登录用户不存在");
        }
        if (!xxlApiUser.getPassword().equals(password)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "登录密码错误");
        }

        String token = makeToken(xxlApiUser);
        CookieUtil.set(response, LOGIN_IDENTITY_KEY, token, ifRemember);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response) {

        XxlApiUser loginUser = ifLogin(request);
        if (loginUser == null) {
            return ReturnT.SUCCESS;

        }

        CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
        return ReturnT.SUCCESS;
    }

    @Override
    public XxlApiUser ifLogin(HttpServletRequest request) {
        XxlApiUser loginUser = parseToken(request);
        return loginUser;
    }
}
