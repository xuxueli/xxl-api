package com.xxl.api.admin.controller.biz;

import com.xxl.api.admin.constant.Consts;
import com.xxl.api.admin.model.XxlApiBiz;
import com.xxl.api.admin.mapper.XxlApiBizMapper;
import com.xxl.api.admin.mapper.XxlApiDataTypeMapper;
import com.xxl.api.admin.mapper.XxlApiProjectMapper;
import com.xxl.api.admin.util.I18nUtil;
import com.xxl.sso.core.annotation.XxlSso;
import com.xxl.tool.core.CollectionTool;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.PageModel;
import com.xxl.tool.response.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 17/5/23.
 */
@Controller
@RequestMapping("/biz")
public class BizController {

    @Resource
    private XxlApiBizMapper xxlApiBizDao;
    @Resource
    private XxlApiProjectMapper xxlApiProjectDao;
    @Resource
    private XxlApiDataTypeMapper xxlApiDataTypeDao;

    @RequestMapping
    @XxlSso(role = Consts.ROLE_ADMIN)
    public String index(Model model) {
        return "biz/biz.list";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @XxlSso(role = Consts.ROLE_ADMIN)
    public Response<PageModel<XxlApiBiz>> pageList(@RequestParam(required = false, defaultValue = "0") int offset,
                                                   @RequestParam(required = false, defaultValue = "10") int pagesize,
                                                   String bizName) {
        // page list
        List<XxlApiBiz> list = xxlApiBizDao.pageList(offset, pagesize, bizName);
        int list_count = xxlApiBizDao.pageListCount(offset, pagesize, bizName);

        // package result
        PageModel<XxlApiBiz> pageModel = new PageModel<>();
        pageModel.setData( list);
        pageModel.setTotal( list_count);

        return Response.ofSuccess(pageModel);
    }

    @RequestMapping("/add")
    @ResponseBody
    @XxlSso(role = Consts.ROLE_ADMIN)
    public Response<String> add(XxlApiBiz xxlApiBiz) {
        if (StringTool.isBlank(xxlApiBiz.getBizName())) {
            return Response.ofFail( "业务线名称不可为空");
        }
        // xss valid
        if (xxlApiBiz.getBizName().contains("<")) {
            return Response.ofFail( "业务线名称非法");
        }

        int ret = xxlApiBizDao.add(xxlApiBiz);
        return ret>0?Response.ofSuccess():Response.ofFail();
    }

    @RequestMapping("/update")
    @ResponseBody
    @XxlSso(role = Consts.ROLE_ADMIN)
    public Response<String> update(XxlApiBiz xxlApiBiz) {
        if (StringTool.isBlank(xxlApiBiz.getBizName())) {
            return Response.ofFail("业务线名称不可为空");
        }
        // xss valid
        if (xxlApiBiz.getBizName().contains("<")) {
            return Response.ofFail( "业务线名称非法");
        }

        int ret = xxlApiBizDao.update(xxlApiBiz);
        return ret>0?Response.ofSuccess():Response.ofFail();
    }

    @RequestMapping("/delete")
    @ResponseBody
    @XxlSso(role = Consts.ROLE_ADMIN)
    public Response<String> delete(@RequestParam("ids[]") List<Integer> ids) {

        // valid
        if (CollectionTool.isEmpty(ids)) {
            return Response.ofFail(I18nUtil.getString("system_param_empty"));
        }
        if (ids.size() != 1) {
            return Response.ofFail("只允许删除单条数据");
        }
        int id = ids.get(0);

        // valid
        int count = xxlApiProjectDao.pageListCount(0, 10, null, id);
        if (count > 0) {
            return Response.ofFail("拒绝删除，业务线下存在项目");
        }

        int dtCount = xxlApiDataTypeDao.pageListCount(0, 10, id, null);
        if (dtCount > 0) {
            return Response.ofFail( "拒绝删除，业务线下数据类型");
        }

        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        if (bizList.size() <= 1) {
            return Response.ofFail("拒绝删除，需要至少预留一个业务线");
        }

        int ret = xxlApiBizDao.delete(id);
        return ret>0?Response.ofSuccess():Response.ofFail();
    }

}
