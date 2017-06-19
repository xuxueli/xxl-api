<!DOCTYPE html>
<html>
<head>
    <title>API管理平台</title>
    <link rel="shortcut icon" href="${request.contextPath}/favicon.ico" type="image/x-icon" />
    <#import "/common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
    <!-- header -->
<@netCommon.commonHeader />
    <!-- left -->
<@netCommon.commonLeft "datatype" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>数据类型详情<small>API管理平台</small></h1>
        </section>

        <section class="content">
            <form class="form-horizontal" id="ducomentForm" >
                <input type="hidden" name="id" value="${document.id}" >
                <input type="hidden" name="projectId" value="${document.projectId}" >

                <#--基础信息-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">基础信息</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-default btn-xs" type="button" onclick="javascript:window.location.href='${request.contextPath}/datatype/updateDataTypePage?dataTypeId=${apiDataType.id}'" >前往修改页面</button>
                        </div>
                    </div>

                    <div class="box-body">
                        <div class="form-group">
                            <label class="col-sm-1">业务线：</label>
                            <div class="col-sm-5">
                                <#if apiDataType.bizId == 0>公共
                                <#else>
                                    <#if bizList?exists && bizList?size gt 0>
                                        <#list bizList as biz>
                                            <#if biz.id == apiDataType.bizId>${biz.bizName}</#if>
                                        </#list>
                                    </#if>
                                </#if>
                            </div>
                            <label class="col-sm-1">名称：</label>
                            <div class="col-sm-5">${apiDataType.name}</div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-1">描述：</label>
                            <div class="col-sm-10">${apiDataType.about}</div>
                        </div>

                    </div>
                </div>


                <#--字段列表-->
                <div class="box box-primary">
                    <div class="box-header">
                        <h3 class="box-title">字段列表</h3>
                    </div>
                    <div class="box-body no-padding" >
                        <table class="table table-striped">
                            <tr>
                                <th style="width: 25%;" >字段名称</th>
                                <th style="width: 25%;" >数据类型</th>
                                <th style="width: 50%;" >字段描述</th>
                            </tr>
                            <#if apiDataType.fieldList?exists && apiDataType.fieldList?size gt 0>
                            <#list apiDataType.fieldList as field>
                                <tr>
                                    <td>${field.fieldName}</td>
                                    <td>
                                        <a href="${request.contextPath}//datatype/dataTypeDetail?dataTypeId=${field.fieldDatatypeId}" target="_blank">
                                            ${field.fieldDatatype.name}
                                            <#if field.fieldType==1>[]</#if>
                                        </a>
                                    </td>
                                    <td>${field.fieldAbout}</td>
                                </tr>
                            </#list>
                            </#if>
                        </table>
                    </div>
                </div>

                <#--数据结构展开-->
                <div class="box box-primary">
                    <div class="box-header">
                        <h3 class="box-title">数据结构展开</h3>
                    </div>
                    <div class="box-body " >

                        <#macro fieldShow fieldList >
                            <#if fieldList?exists && fieldList?size gt 0>
                                <ul style="display: block;" >
                                    <#list fieldList as field>
                                        <li>
                                            <span style="font-weight: bold;margin-right: 10px;" >${field.fieldName}</span> {类型：${field.fieldDatatype.name} <#if field.fieldType==1>[]</#if>，描述：${field.fieldAbout}}
                                            <@fieldShow field.fieldDatatype.fieldList />
                                        </li>
                                    </#list>
                                </ul>
                            </#if>
                        </#macro>

                        <p>
                            <span style="font-weight: bold;margin-right: 10px;" >${apiDataType.name}:</span> &nbsp;&nbsp;&nbsp;&nbsp; ${apiDataType.about}
                            <@fieldShow apiDataType.fieldList />
                        </p>

                    </div>
                </div>

                <#-- 代码生成 -->
                <#if codeContent?exists>
                    <div class="box box-primary"  id="codeContent" >
                        <div class="box-header">
                            <h3 class="box-title">数据结构-代码生成</h3>
                        </div>
                        <div class="box-body " >
                            <pre style="width: 100%; height: 100%; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; margin-top: 15px;" >${codeContent}</pre>
                        </div>
                    </div>
                </#if>

            </form>

        </section>

    </div>

    <!-- footer -->
<@netCommon.commonFooter />
</div>

<!-- 新增-分组.模态框 -->
<div class="modal fade" id="addMockModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >新增Mock数据</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <div class="col-sm-12">
                            响应数据类型(MIME)：
                        <#list ResponseContentType as item>
                            <input type="radio" class="iCheck" name="respType" value="${item}" <#if item_index==0>checked</#if> >${item}  &nbsp;&nbsp;
                        </#list>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-12">
                            <textarea name="respExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;" ></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="button" class="btn btn-primary save"  >保存</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <input type="hidden" name="documentId" value="${document.id}" >
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 更新-分组.模态框 -->
<div class="modal fade" id="updateMockModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >更新Mock数据</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <div class="col-sm-12">
                            响应数据类型(MIME)：
                        <#list ResponseContentType as item>
                            <input type="radio" class="iCheck" name="respType" value="${item}" >${item}  &nbsp;&nbsp;
                        </#list>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-12">
                            <textarea name="respExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;" ></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="button" class="btn btn-primary save"  >保存</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                            <input type="hidden" name="documentId" value="${document.id}" >

                            <input type="hidden" name="id" >
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<@netCommon.commonScript />
</body>
</html>
