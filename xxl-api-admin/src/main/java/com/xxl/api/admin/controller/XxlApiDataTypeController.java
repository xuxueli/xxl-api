package com.xxl.api.admin.controller;

import com.xxl.api.admin.core.consistant.FieldTypeEnum;
import com.xxl.api.admin.core.model.*;
import com.xxl.api.admin.core.util.JacksonUtil;
import com.xxl.api.admin.dao.IXxlApiBizDao;
import com.xxl.api.admin.dao.IXxlApiDataTypeDao;
import com.xxl.api.admin.dao.IXxlApiDataTypeFieldDao;
import com.xxl.api.admin.dao.IXxlApiDocumentDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
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
    private IXxlApiDataTypeFieldDao xxlApiDataTypeFieldDao;
    @Resource
    private IXxlApiBizDao xxlApiBizDao;
    @Resource
    private IXxlApiDocumentDao xxlApiDocumentDao;


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

    /**
     * load datatype
     *
     * @param dataTypeId
     * @return
     */
    private XxlApiDataType loadDataType(int dataTypeId) {
        XxlApiDataType dataType = xxlApiDataTypeDao.load(dataTypeId);
        if (dataType == null) {
            return dataType;
        }

        // fill field datatype
        int maxRelateLevel = 5;
        dataType = fillFileDataType(dataType, maxRelateLevel);
        return dataType;
    }
    /**
     * parse field of datatype (注意，循环引用问题；此处显示最长引用链路长度为5；)
     *
     * @param dataType
     * @param maxRelateLevel
     * @return
     */
    private XxlApiDataType fillFileDataType(XxlApiDataType dataType, int maxRelateLevel){
        // init field list
        List<XxlApiDataTypeField> fieldList = xxlApiDataTypeFieldDao.findByParentDatatypeId(dataType.getId());
        dataType.setFieldList(fieldList);
        // parse field list
        if (CollectionUtils.isNotEmpty(dataType.getFieldList()) && maxRelateLevel>0) {
            for (XxlApiDataTypeField field: dataType.getFieldList()) {
                XxlApiDataType fieldDataType = loadDataType(field.getFieldDatatypeId());
                fieldDataType = fillFileDataType(fieldDataType, --maxRelateLevel);
                field.setFieldDatatype(fieldDataType);
            }
        }
        return dataType;
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
    public ReturnT<Integer> addDataType(XxlApiDataType apiDataTypeDTO, String fieldTypeJson) {
        // parse json field
        if (StringUtils.isNotBlank(fieldTypeJson)) {
            List<XxlApiDataTypeField> fieldList = JacksonUtil.readValueRefer(fieldTypeJson, new TypeReference<List<XxlApiDataTypeField>>() { });
            if (CollectionUtils.isNotEmpty(fieldList)) {
                apiDataTypeDTO.setFieldList(fieldList);
            }
        }

        // valid datatype
        if (StringUtils.isBlank(apiDataTypeDTO.getName())) {
            return new ReturnT<Integer>(ReturnT.FAIL_CODE, "数据类型名称不可为空");
        }
        if (StringUtils.isBlank(apiDataTypeDTO.getAbout())) {
            return new ReturnT<Integer>(ReturnT.FAIL_CODE, "数据类型描述不可为空");
        }
        XxlApiDataType existsByName = xxlApiDataTypeDao.loadByName(apiDataTypeDTO.getName());
        if (existsByName != null) {
            return new ReturnT<Integer>(ReturnT.FAIL_CODE, "数据类型名称不可重复，请更换");
        }
        // valid field
        if (CollectionUtils.isNotEmpty(apiDataTypeDTO.getFieldList())) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                // valid
                if (StringUtils.isBlank(field.getFieldName())) {
                    return new ReturnT<Integer>(ReturnT.FAIL_CODE, "字段名称不可为空");
                }

                XxlApiDataType filedDataType = loadDataType(field.getFieldDatatypeId());
                if (filedDataType == null) {
                    return new ReturnT<Integer>(ReturnT.FAIL_CODE, "字段数据类型ID非法");
                }

                if (FieldTypeEnum.match(field.getFieldType())==null) {
                    return new ReturnT<Integer>(ReturnT.FAIL_CODE, "字段形式非法");
                }
            }
        }

        // add datatype
        int datatypeId = xxlApiDataTypeDao.add(apiDataTypeDTO);
        if (datatypeId < 1) {
            return new ReturnT<Integer>(ReturnT.FAIL_CODE, "数据类型新增失败");
        }
        if (CollectionUtils.isNotEmpty(apiDataTypeDTO.getFieldList())) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                field.setParentDatatypeId(datatypeId);
            }
            // add field
            int ret = xxlApiDataTypeFieldDao.add(apiDataTypeDTO.getFieldList());
            if (ret < 1) {
                return new ReturnT<Integer>(ReturnT.FAIL_CODE, "数据类型新增失败");
            }
        }

        return datatypeId>0? new ReturnT<Integer>(datatypeId) : new ReturnT<Integer>(ReturnT.FAIL_CODE, "");
    }

    @RequestMapping("/updateDataTypePage")
    public String updateDataTypePage(Model model, int dataTypeId) {

        XxlApiDataType apiDataType = loadDataType(dataTypeId);
        if (apiDataType == null) {
            throw new RuntimeException("数据类型ID非法");
        }
        model.addAttribute("apiDataType", apiDataType);

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        return "datatype/datatype.update";
    }

    @RequestMapping("/updateDataType")
    @ResponseBody
    public ReturnT<String> updateDataType(XxlApiDataType apiDataTypeDTO, String fieldTypeJson) {
        // parse json field
        if (StringUtils.isNotBlank(fieldTypeJson)) {
            List<XxlApiDataTypeField> fieldList = JacksonUtil.readValueRefer(fieldTypeJson, new TypeReference<List<XxlApiDataTypeField>>() { });
            if (CollectionUtils.isNotEmpty(fieldList)) {
                apiDataTypeDTO.setFieldList(fieldList);
            }
        }

        // valid datatype
        if (StringUtils.isBlank(apiDataTypeDTO.getName())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "数据类型名称不可为空");
        }
        if (StringUtils.isBlank(apiDataTypeDTO.getAbout())) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "数据类型描述不可为空");
        }
        XxlApiDataType existsByName = xxlApiDataTypeDao.loadByName(apiDataTypeDTO.getName());
        if (existsByName != null && existsByName.getId()!=apiDataTypeDTO.getId()) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "数据类型名称不可重复，请更换");
        }
        // valid field
        if (CollectionUtils.isNotEmpty(apiDataTypeDTO.getFieldList())) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                // valid
                if (StringUtils.isBlank(field.getFieldName())) {
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
            if (CollectionUtils.isNotEmpty(apiDataTypeDTO.getFieldList())) {
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
    public String dataTypeDetail(Model model, int dataTypeId) {

        XxlApiDataType apiDataType = loadDataType(dataTypeId);
        if (apiDataType == null) {
            throw new RuntimeException("数据类型ID非法");
        }
        model.addAttribute("apiDataType", apiDataType);

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        return "datatype/datatype.detail";
    }

    @RequestMapping("/deleteDataType")
    @ResponseBody
    public ReturnT<String> deleteDataType(int id) {
        // 被其他数据类型引用，拒绝删除
        List<XxlApiDataTypeField> list = xxlApiDataTypeFieldDao.findByFieldDatatypeId(id);
        if (CollectionUtils.isNotEmpty(list)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "该数据类型被引用中，拒绝删除");
        }

        // 该数据类型被API引用，拒绝删除
        List<XxlApiDocument> apiList = xxlApiDocumentDao.findByResponseDataTypeId(id);
        if (CollectionUtils.isNotEmpty(apiList)) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "该数据类型被API引用，拒绝删除");
        }

        // delete
        int  ret = xxlApiDataTypeDao.delete(id);
        xxlApiDataTypeFieldDao.deleteByParentDatatypeId(id);
        return ret>0?ReturnT.SUCCESS:ReturnT.FAIL;
    }

}
