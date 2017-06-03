package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.model.ReturnT;
import com.xxl.api.admin.core.model.XxlApiBiz;
import com.xxl.api.admin.core.model.XxlApiDataType;
import com.xxl.api.admin.core.model.XxlApiDataTypeField;
import com.xxl.api.admin.core.util.JacksonUtil;
import com.xxl.api.admin.dao.IXxlApiBizDao;
import com.xxl.api.admin.dao.IXxlApiDataTypeDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on 17/5/23.
 */
@Controller
@RequestMapping("/datatype")
public class XxlApiDataTypeController {

    @Resource
    private IXxlApiDataTypeDao xxlApiDataTypeDao;
    @Resource
    private IXxlApiBizDao xxlApiBizDao;


    @RequestMapping
    public String index(Model model) {

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        return "apihome/datatype/datatype.list";
    }


    @RequestMapping("/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        int bizId, String name) {
        // page list
        List<XxlApiDataType> list = xxlApiDataTypeDao.pageList(start, length, bizId, name);
        int count = xxlApiDataTypeDao.pageListCount(start, length, bizId, name);
        
        // package result
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", count);		// 总记录数
        maps.put("recordsFiltered", count);	    // 过滤后的总记录数
        maps.put("data", list);  				// 分页列表
        return maps;
    }

    @RequestMapping("/addDataTypePage")
    public String addDataTypePage(Model model) {

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        return "apihome/datatype/datatype.add";
    }

    @RequestMapping("/addDataType")
    @ResponseBody
    public ReturnT<Integer> addDataType(XxlApiDataType apiDataTypeDTO, String fieldTypeJson) {
        // parse json field
        if (StringUtils.isNotBlank(fieldTypeJson)) {
            List<XxlApiDataTypeField> fieldList = JacksonUtil.readValue(fieldTypeJson, List.class);
            if (CollectionUtils.isNotEmpty(fieldList)) {
                apiDataTypeDTO.setFieldList(fieldList);
            }
        }

        int id = xxlApiDataTypeDao.add(apiDataTypeDTO);
        return id>0? new ReturnT<Integer>(id) : new ReturnT<Integer>(ReturnT.FAIL_CODE, "");
    }

    @RequestMapping("/updateDataTypePage")
    public String updateDataTypePage(Model model, int dataTypeId) {

        XxlApiDataType apiDataType = xxlApiDataTypeDao.load(dataTypeId);
        if (apiDataType == null) {
            throw new RuntimeException("数据类型ID非法");
        }
        model.addAttribute("apiDataType", apiDataType);

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        return "apihome/datatype/datatype.update";
    }

    @RequestMapping("/updateDataType")
    @ResponseBody
    public ReturnT<String> updateDataType(XxlApiDataType apiDataTypeDTO, String fieldTypeJson) {
        // parse json field
        if (StringUtils.isNotBlank(fieldTypeJson)) {
            List<XxlApiDataTypeField> fieldList = JacksonUtil.readValue(fieldTypeJson, List.class);
            if (CollectionUtils.isNotEmpty(fieldList)) {
                apiDataTypeDTO.setFieldList(fieldList);
            }
        }

        int ret = xxlApiDataTypeDao.update(apiDataTypeDTO);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/dataTypeDetail")
    public String dataTypeDetail(Model model, int dataTypeId) {

        XxlApiDataType apiDataType = xxlApiDataTypeDao.load(dataTypeId);
        if (apiDataType == null) {
            throw new RuntimeException("数据类型ID非法");
        }
        model.addAttribute("apiDataType", apiDataType);

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        return "apihome/datatype/datatype.detail";
    }

    @RequestMapping("/deleteDataType")
    @ResponseBody
    public ReturnT<String> deleteDataType(int id) {
        int  ret = xxlApiDataTypeDao.delete(id);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

}
