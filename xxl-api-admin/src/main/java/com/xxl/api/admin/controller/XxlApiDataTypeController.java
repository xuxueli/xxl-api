package com.xxl.api.admin.controller;

import com.xxl.api.admin.dao.IXxlApiDataTypeDao;
import com.xxl.api.admin.dao.IXxlApiDataTypeFieldDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by xuxueli on 17/6/3.
 */
@Controller
@RequestMapping("/datatype")
public class XxlApiDataTypeController {

    @Resource
    private IXxlApiDataTypeDao xxlApiDataTypeDao;
    @Resource
    private IXxlApiDataTypeFieldDao xxlApiDataTypeFieldDao;



}
