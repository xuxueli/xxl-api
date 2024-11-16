package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.consistant.FieldTypeEnum;
import com.xxl.api.admin.core.model.*;
import com.xxl.api.admin.core.util.ApiDataTypeToCode;
import com.xxl.api.admin.core.util.tool.ArrayTool;
import com.xxl.api.admin.core.util.tool.StringTool;
import com.xxl.api.admin.dao.IXxlApiBizDao;
import com.xxl.api.admin.dao.IXxlApiDataTypeDao;
import com.xxl.api.admin.dao.IXxlApiDataTypeFieldDao;
import com.xxl.api.admin.dao.IXxlApiDocumentDao;
import com.xxl.api.admin.service.IXxlApiDataTypeService;
import com.xxl.api.admin.service.impl.LoginService;
import com.xxl.tool.gson.GsonTool;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private IXxlApiDataTypeFieldDao xxlApiDataTypeFieldDao;
    @Resource
    private IXxlApiBizDao xxlApiBizDao;
    @Resource
    private IXxlApiDocumentDao xxlApiDocumentDao;
    @Resource
    private IXxlApiDataTypeService xxlApiDataTypeService;


    @RequestMapping
    public String index(Model model) {

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        return "datatype/datatype.list";
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

    private boolean hasBizPermission(HttpServletRequest request, int bizId){
        XxlApiUser loginUser = (XxlApiUser) request.getAttribute(LoginService.LOGIN_IDENTITY);
        if ( loginUser.getType()==1 ||
                ArrayTool.contains(StringTool.split(loginUser.getPermissionBiz(), ","), String.valueOf(bizId))
                ) {
            return true;
        } else {
            return false;
        }
    }

    @RequestMapping("/addDataTypePage")
    public String addDataTypePage(Model model) {

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        return "datatype/datatype.add";
    }

    @RequestMapping("/addDataType")
    @ResponseBody
    public ReturnT<Integer> addDataType(HttpServletRequest request, XxlApiDataType apiDataTypeDTO, String fieldTypeJson) {
        // parse json field
        if (StringTool.isNotBlank(fieldTypeJson)) {
            List<XxlApiDataTypeField> fieldList = GsonTool.fromJsonList(fieldTypeJson, XxlApiDataTypeField.class);
            if (fieldList!=null && fieldList.size()>0) {
                apiDataTypeDTO.setFieldList(fieldList);
            }
        }

        // valid datatype
        if (StringTool.isBlank(apiDataTypeDTO.getName())) {
            return new ReturnT<Integer>(ReturnT.FAIL_CODE, "数据类型名称不可为空");
        }
        if (StringTool.isBlank(apiDataTypeDTO.getAbout())) {
            return new ReturnT<Integer>(ReturnT.FAIL_CODE, "数据类型描述不可为空");
        }

        if (!hasBizPermission(request, apiDataTypeDTO.getBizId())) {
            return new ReturnT<Integer>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
        }

        XxlApiBiz apiBiz = xxlApiBizDao.load(apiDataTypeDTO.getBizId());
        if (apiBiz == null) {
            return new ReturnT<Integer>(ReturnT.FAIL_CODE, "业务线ID非法");
        }

        XxlApiDataType existsByName = xxlApiDataTypeDao.loadByName(apiDataTypeDTO.getName());
        if (existsByName != null) {
            return new ReturnT<Integer>(ReturnT.FAIL_CODE, "数据类型名称不可重复，请更换");
        }
        // valid field
        if (apiDataTypeDTO.getFieldList()!=null && apiDataTypeDTO.getFieldList().size()>0) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                // valid
                if (StringTool.isBlank(field.getFieldName())) {
                    return new ReturnT<Integer>(ReturnT.FAIL_CODE, "字段名称不可为空");
                }

                XxlApiDataType filedDataType = xxlApiDataTypeService.loadDataType(field.getFieldDatatypeId());
                if (filedDataType == null) {
                    return new ReturnT<Integer>(ReturnT.FAIL_CODE, "字段数据类型ID非法");
                }

                if (FieldTypeEnum.match(field.getFieldType())==null) {
                    return new ReturnT<Integer>(ReturnT.FAIL_CODE, "字段形式非法");
                }
            }
        }

        // add datatype
        int addRet = xxlApiDataTypeDao.add(apiDataTypeDTO);
        if (addRet < 1) {
            return new ReturnT<Integer>(ReturnT.FAIL_CODE, "数据类型新增失败");
        }
        if (apiDataTypeDTO.getFieldList()!=null && apiDataTypeDTO.getFieldList().size()>0) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                field.setParentDatatypeId(apiDataTypeDTO.getId());
            }
            // add field
            int ret = xxlApiDataTypeFieldDao.add(apiDataTypeDTO.getFieldList());
            if (ret < 1) {
                return new ReturnT<Integer>(ReturnT.FAIL_CODE, "数据类型新增失败");
            }
        }

        return apiDataTypeDTO.getId()>0? new ReturnT<Integer>(apiDataTypeDTO.getId()) : new ReturnT<Integer>(ReturnT.FAIL_CODE, "");
    }

    @RequestMapping("/updateDataTypePage")
    public String updateDataTypePage(HttpServletRequest request, Model model, int dataTypeId) {

        XxlApiDataType apiDataType = xxlApiDataTypeService.loadDataType(dataTypeId);
        if (apiDataType == null) {
            throw new RuntimeException("数据类型ID非法");
        }
        model.addAttribute("apiDataType", apiDataType);

        if (!hasBizPermission(request, apiDataType.getBizId())) {
            throw new RuntimeException("您没有相关业务线的权限,请联系管理员开通");
        }

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        return "datatype/datatype.update";
    }

    @RequestMapping("/updateDataType")
    @ResponseBody
    public ReturnT<String> updateDataType(HttpServletRequest request, XxlApiDataType apiDataTypeDTO, String fieldTypeJson) {
        // parse json field
        if (StringTool.isNotBlank(fieldTypeJson)) {
            List<XxlApiDataTypeField> fieldList = GsonTool.fromJsonList(fieldTypeJson, XxlApiDataTypeField.class);
            if (fieldList!=null && fieldList.size()>0) {
                apiDataTypeDTO.setFieldList(fieldList);
            }
        }

        // valid datatype
        if (StringTool.isBlank(apiDataTypeDTO.getName())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "数据类型名称不可为空");
        }
        if (StringTool.isBlank(apiDataTypeDTO.getAbout())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "数据类型描述不可为空");
        }

        if (!hasBizPermission(request, apiDataTypeDTO.getBizId())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
        }

        XxlApiBiz apiBiz = xxlApiBizDao.load(apiDataTypeDTO.getBizId());
        if (apiBiz == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "业务线ID非法");
        }

        XxlApiDataType existsByName = xxlApiDataTypeDao.loadByName(apiDataTypeDTO.getName());
        if (existsByName != null && existsByName.getId()!=apiDataTypeDTO.getId()) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "数据类型名称不可重复，请更换");
        }
        // valid field
        if (apiDataTypeDTO.getFieldList()!=null && apiDataTypeDTO.getFieldList().size()>0) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                // valid
                if (StringTool.isBlank(field.getFieldName())) {
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "字段名称不可为空");
                }

                XxlApiDataType filedDataType = xxlApiDataTypeDao.load(field.getFieldDatatypeId());
                if (filedDataType == null) {
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "字段数据类型ID非法");
                }

                if (FieldTypeEnum.match(field.getFieldType())==null) {
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "字段形式非法");
                }
            }
        }

        // update datatype
        int ret1 = xxlApiDataTypeDao.update(apiDataTypeDTO);
        if (ret1 > 0) {
            // remove old, add new
            xxlApiDataTypeFieldDao.deleteByParentDatatypeId(apiDataTypeDTO.getId());
            if (apiDataTypeDTO.getFieldList()!=null && apiDataTypeDTO.getFieldList().size()>0) {
                for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                    field.setParentDatatypeId(apiDataTypeDTO.getId());
                }
                int ret = xxlApiDataTypeFieldDao.add(apiDataTypeDTO.getFieldList());
                if (ret < 1) {
                    return new ReturnT<String>(ReturnT.FAIL_CODE, "数据类型新增失败");
                }
            }
        }
        return ret1>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

    @RequestMapping("/dataTypeDetail")
    public String dataTypeDetail(HttpServletRequest request, Model model, int dataTypeId) {

        XxlApiDataType apiDataType = xxlApiDataTypeService.loadDataType(dataTypeId);
        if (apiDataType == null) {
            throw new RuntimeException("数据类型ID非法");
        }
        model.addAttribute("apiDataType", apiDataType);

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        // 代码生成
        String codeContent = ApiDataTypeToCode.parseDataTypeToCode(apiDataType);
        model.addAttribute("codeContent", codeContent);

        // 权限
        model.addAttribute("hasBizPermission", hasBizPermission(request, apiDataType.getBizId()));

        return "datatype/datatype.detail";
    }

    @RequestMapping("/deleteDataType")
    @ResponseBody
    public ReturnT<String> deleteDataType(HttpServletRequest request, int id) {

        XxlApiDataType apiDataType = xxlApiDataTypeDao.load(id);
        if (apiDataType == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "数据类型ID非法");
        }

        if (!hasBizPermission(request, apiDataType.getBizId())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "您没有相关业务线的权限,请联系管理员开通");
        }

        // 被其他数据类型引用，拒绝删除
        List<XxlApiDataTypeField> list = xxlApiDataTypeFieldDao.findByFieldDatatypeId(id);
        if (list!=null && list.size()>0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "该数据类型被引用中，拒绝删除");
        }

        // 该数据类型被API引用，拒绝删除
        List<XxlApiDocument> apiList = xxlApiDocumentDao.findByResponseDataTypeId(id);
        if (apiList!=null && apiList.size()>0) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "该数据类型被API引用，拒绝删除");
        }

        // delete
        int  ret = xxlApiDataTypeDao.delete(id);
        xxlApiDataTypeFieldDao.deleteByParentDatatypeId(id);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

}
