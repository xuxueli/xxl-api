package com.xxl.api.admin.controller;

import com.xxl.api.admin.controller.annotation.PermessionLimit;
import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiBiz;
import com.xxl.api.admin.core.util.JacksonUtil;
import com.xxl.api.admin.dao.IXxlApiBizDao;
import com.xxl.api.admin.dao.IXxlApiProjectDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuxueli on 17/5/23.
 */
@Controller
@RequestMapping("/biz")
public class XxlApiBizController {

    @Resource
    private IXxlApiBizDao xxlApiBizDao;
    @Resource
    private IXxlApiProjectDao xxlApiProjectDao;

    @RequestMapping
    public String index(Model model) {

        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        if (CollectionUtils.isEmpty(bizList)) {
            bizList = new ArrayList<>();
        }
        model.addAttribute("bizList", JacksonUtil.writeValueAsString(bizList));

        return "biz/biz.list";
    }

    @RequestMapping("/add")
    @ResponseBody
    @PermessionLimit(superUser = true)
    public ReturnT<String> add(XxlApiBiz xxlApiBiz) {
        if (StringUtils.isBlank(xxlApiBiz.getBizName())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "业务线名称不可为空");
        }

        int ret = xxlApiBizDao.add(xxlApiBiz);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/udpate")
    @ResponseBody
    @PermessionLimit(superUser = true)
    public ReturnT<String> udpate(XxlApiBiz xxlApiBiz) {
        if (StringUtils.isBlank(xxlApiBiz.getBizName())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "业务线名称不可为空");
        }

        int ret = xxlApiBizDao.udpate(xxlApiBiz);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @PermessionLimit(superUser = true)
    public ReturnT<String> delete(int id) {

        int count = xxlApiProjectDao.pageListCount(0, 10, null, -1);
        if (count > 0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "业务线下存在项目，不允许删除");
        }

        int ret = xxlApiBizDao.delete(id);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

}
