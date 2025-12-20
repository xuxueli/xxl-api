package com.xxl.api.admin.controller.biz;

import com.xxl.api.admin.constant.FieldTypeEnum;
import com.xxl.api.admin.mapper.XxlApiBizMapper;
import com.xxl.api.admin.mapper.XxlApiDataTypeFieldMapper;
import com.xxl.api.admin.mapper.XxlApiDataTypeMapper;
import com.xxl.api.admin.mapper.XxlApiDocumentMapper;
import com.xxl.api.admin.model.XxlApiBiz;
import com.xxl.api.admin.model.XxlApiDataType;
import com.xxl.api.admin.model.XxlApiDataTypeField;
import com.xxl.api.admin.model.XxlApiDocument;
import com.xxl.api.admin.service.IXxlApiDataTypeService;
import com.xxl.api.admin.util.ApiDataTypeToCode;
import com.xxl.api.admin.util.I18nUtil;
import com.xxl.sso.core.helper.XxlSsoHelper;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.core.CollectionTool;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.json.GsonTool;
import com.xxl.tool.response.PageModel;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by xuxueli on 17/5/23.
 */
@Controller
@RequestMapping("/datatype")
public class DataTypeController {

    @Resource
    private XxlApiDataTypeMapper xxlApiDataTypeDao;
    @Resource
    private XxlApiDataTypeFieldMapper xxlApiDataTypeFieldDao;
    @Resource
    private XxlApiBizMapper xxlApiBizDao;
    @Resource
    private XxlApiDocumentMapper xxlApiDocumentDao;
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
    public Response<PageModel<XxlApiDataType>> pageList(@RequestParam(required = false, defaultValue = "0") int offset,
                                                        @RequestParam(required = false, defaultValue = "10") int pagesize,
                                                        int bizId,
                                                        String name) {
        // page list
        List<XxlApiDataType> list = xxlApiDataTypeDao.pageList(offset, pagesize, bizId, name);
        int count = xxlApiDataTypeDao.pageListCount(offset, pagesize, bizId, name);

        // package result
        PageModel<XxlApiDataType> pageModel = new PageModel<XxlApiDataType>();
        pageModel.setData( list);
        pageModel.setTotal( count);

        return Response.ofSuccess(pageModel);
    }

    public static boolean hasBizPermission(HttpServletRequest request, HttpServletResponse response, int bizId){
        Response<LoginInfo> loginInfoResponse = XxlSsoHelper.loginCheckWithAttr(request);
        return XxlSsoHelper.hasRole(loginInfoResponse.getData(), "ADMIN").isSuccess()
                || XxlSsoHelper.hasPermission(loginInfoResponse.getData(), String.valueOf(bizId)).isSuccess();
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
    public Response<Integer> addDataType(HttpServletRequest request, HttpServletResponse response, XxlApiDataType apiDataTypeDTO, String fieldTypeJson) {
        // parse json field
        if (StringTool.isNotBlank(fieldTypeJson)) {
            List<XxlApiDataTypeField> fieldList = GsonTool.fromJsonList(fieldTypeJson, XxlApiDataTypeField.class);
            if (fieldList!=null && fieldList.size()>0) {
                apiDataTypeDTO.setFieldList(fieldList);
            }
        }

        // valid datatype
        if (StringTool.isBlank(apiDataTypeDTO.getName())) {
            return Response.ofFail("数据类型名称不可为空");
        }
        if (StringTool.isBlank(apiDataTypeDTO.getAbout())) {
            return Response.ofFail( "数据类型描述不可为空");
        }

        if (!hasBizPermission(request, response, apiDataTypeDTO.getBizId())) {
            return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
        }

        XxlApiBiz apiBiz = xxlApiBizDao.load(apiDataTypeDTO.getBizId());
        if (apiBiz == null) {
            return Response.ofFail("业务线ID非法");
        }

        XxlApiDataType existsByName = xxlApiDataTypeDao.loadByName(apiDataTypeDTO.getName());
        if (existsByName != null) {
            return Response.ofFail( "数据类型名称不可重复，请更换");
        }
        // valid field
        if (apiDataTypeDTO.getFieldList()!=null && apiDataTypeDTO.getFieldList().size()>0) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                // valid
                if (StringTool.isBlank(field.getFieldName())) {
                    return Response.ofFail("字段名称不可为空");
                }

                XxlApiDataType filedDataType = xxlApiDataTypeService.loadDataType(field.getFieldDatatypeId());
                if (filedDataType == null) {
                    return Response.ofFail("字段数据类型ID非法");
                }

                if (FieldTypeEnum.match(field.getFieldType())==null) {
                    return Response.ofFail( "字段形式非法");
                }
            }
        }

        // add datatype
        int addRet = xxlApiDataTypeDao.add(apiDataTypeDTO);
        if (addRet < 1) {
            return Response.ofFail("数据类型新增失败");
        }
        if (apiDataTypeDTO.getFieldList()!=null && apiDataTypeDTO.getFieldList().size()>0) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                field.setParentDatatypeId(apiDataTypeDTO.getId());
            }
            // add field
            int ret = xxlApiDataTypeFieldDao.add(apiDataTypeDTO.getFieldList());
            if (ret < 1) {
                return Response.ofFail( "数据类型新增失败");
            }
        }

        return apiDataTypeDTO.getId()>0? Response.ofSuccess(apiDataTypeDTO.getId()) : Response.ofFail("");
    }

    @RequestMapping("/updateDataTypePage")
    public String updateDataTypePage(HttpServletRequest request, HttpServletResponse response, Model model, int dataTypeId) {

        XxlApiDataType apiDataType = xxlApiDataTypeService.loadDataType(dataTypeId);
        if (apiDataType == null) {
            throw new RuntimeException("数据类型ID非法");
        }
        model.addAttribute("apiDataType", apiDataType);

        if (!hasBizPermission(request, response, apiDataType.getBizId())) {
            throw new RuntimeException("您没有相关业务线的权限,请联系管理员开通");
        }

        // 业务线列表
        List<XxlApiBiz> bizList = xxlApiBizDao.loadAll();
        model.addAttribute("bizList", bizList);

        return "datatype/datatype.update";
    }

    @RequestMapping("/updateDataType")
    @ResponseBody
    public Response<String> updateDataType(HttpServletRequest request, HttpServletResponse response, XxlApiDataType apiDataTypeDTO, String fieldTypeJson) {
        // parse json field
        if (StringTool.isNotBlank(fieldTypeJson)) {
            List<XxlApiDataTypeField> fieldList = GsonTool.fromJsonList(fieldTypeJson, XxlApiDataTypeField.class);
            if (fieldList!=null && fieldList.size()>0) {
                apiDataTypeDTO.setFieldList(fieldList);
            }
        }

        // valid datatype
        if (StringTool.isBlank(apiDataTypeDTO.getName())) {
            return Response.ofFail("数据类型名称不可为空");
        }
        if (StringTool.isBlank(apiDataTypeDTO.getAbout())) {
            return Response.ofFail( "数据类型描述不可为空");
        }

        if (!hasBizPermission(request, response, apiDataTypeDTO.getBizId())) {
            return Response.ofFail("您没有相关业务线的权限,请联系管理员开通");
        }

        XxlApiBiz apiBiz = xxlApiBizDao.load(apiDataTypeDTO.getBizId());
        if (apiBiz == null) {
            return Response.ofFail( "业务线ID非法");
        }

        XxlApiDataType existsByName = xxlApiDataTypeDao.loadByName(apiDataTypeDTO.getName());
        if (existsByName != null && existsByName.getId()!=apiDataTypeDTO.getId()) {
            return Response.ofFail( "数据类型名称不可重复，请更换");
        }
        // valid field
        if (apiDataTypeDTO.getFieldList()!=null && apiDataTypeDTO.getFieldList().size()>0) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                // valid
                if (StringTool.isBlank(field.getFieldName())) {
                    return Response.ofFail( "字段名称不可为空");
                }

                XxlApiDataType filedDataType = xxlApiDataTypeDao.load(field.getFieldDatatypeId());
                if (filedDataType == null) {
                    return Response.ofFail( "字段数据类型ID非法");
                }

                if (FieldTypeEnum.match(field.getFieldType())==null) {
                    return Response.ofFail( "字段形式非法");
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
                    return Response.ofFail( "数据类型新增失败");
                }
            }
        }
        return ret1>0?Response.ofSuccess():Response.ofFail();
    }

    @RequestMapping("/dataTypeDetail")
    public String dataTypeDetail(HttpServletRequest request, HttpServletResponse response, Model model, int dataTypeId) {

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
        model.addAttribute("hasBizPermission", hasBizPermission(request, response, apiDataType.getBizId()));

        return "datatype/datatype.detail";
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Response<String> delete(HttpServletRequest request, HttpServletResponse response, @RequestParam("ids[]") List<Integer> ids) {

        // valid
        if (CollectionTool.isEmpty(ids)) {
            return Response.ofFail(I18nUtil.getString("system_param_empty"));
        }
        if (ids.size() != 1) {
            return Response.ofFail("只允许删除单条数据");
        }
        int id = ids.get(0);

        XxlApiDataType apiDataType = xxlApiDataTypeDao.load(id);
        if (apiDataType == null) {
            return Response.ofFail( "数据类型ID非法");
        }

        if (!hasBizPermission(request, response, apiDataType.getBizId())) {
            return Response.ofFail( "您没有相关业务线的权限,请联系管理员开通");
        }

        // 被其他数据类型引用，拒绝删除
        List<XxlApiDataTypeField> list = xxlApiDataTypeFieldDao.findByFieldDatatypeId(id);
        if (list!=null && list.size()>0) {
            return Response.ofFail( "该数据类型被引用中，拒绝删除");
        }

        // 该数据类型被API引用，拒绝删除
        List<XxlApiDocument> apiList = xxlApiDocumentDao.findByResponseDataTypeId(id);
        if (apiList!=null && apiList.size()>0) {
            return Response.ofFail( "该数据类型被API引用，拒绝删除");
        }

        // delete
        int  ret = xxlApiDataTypeDao.delete(id);
        xxlApiDataTypeFieldDao.deleteByParentDatatypeId(id);
        return ret>0?Response.ofSuccess():Response.ofFail();
    }

}
