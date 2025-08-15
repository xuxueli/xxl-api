package com.xxl.api.admin.controller;

import com.xxl.api.admin.web.annotation.PermessionLimit;
import com.xxl.api.admin.model.XxlApiBiz;
import com.xxl.api.admin.util.tool.StringTool;
import com.xxl.api.admin.mapper.XxlApiBizMapper;
import com.xxl.api.admin.mapper.XxlApiDataTypeMapper;
import com.xxl.api.admin.mapper.XxlApiProjectMapper;
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
public class XxlApiBizController {

    @Resource
    private XxlApiBizMapper xxlApiBizDao;
    @Resource
    private XxlApiProjectMapper xxlApiProjectDao;
    @Resource
    private XxlApiDataTypeMapper xxlApiDataTypeDao;

    @RequestMapping
    @PermessionLimit(superUser = true)
    public String index(Model model) {
        return "biz/biz.list";
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @PermessionLimit(superUser = true)
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String bizName) {
        // page list
        List<XxlApiBiz> list = xxlApiBizDao.pageList(start, length, bizName);
        int list_count = xxlApiBizDao.pageListCount(start, length, bizName);

        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list_count);		// 总记录数
        maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
        maps.put("data", list);  					// 分页列表
        return maps;
    }

    @RequestMapping("/add")
    @ResponseBody
    @PermessionLimit(superUser = true)
    public Response<String> add(XxlApiBiz xxlApiBiz) {
        if (StringTool.isBlank(xxlApiBiz.getBizName())) {
            return Response.ofFail( "业务线名称不可为空");
        }

        int ret = xxlApiBizDao.add(xxlApiBiz);
        return ret>0?Response.ofSuccess():Response.ofFail();
    }

    @RequestMapping("/update")
    @ResponseBody
    @PermessionLimit(superUser = true)
    public Response<String> update(XxlApiBiz xxlApiBiz) {
        if (StringTool.isBlank(xxlApiBiz.getBizName())) {
            return Response.ofFail("业务线名称不可为空");
        }

        int ret = xxlApiBizDao.update(xxlApiBiz);
        return ret>0?Response.ofSuccess():Response.ofFail();
    }

    @RequestMapping("/delete")
    @ResponseBody
    @PermessionLimit(superUser = true)
    public Response<String> delete(int id) {

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
