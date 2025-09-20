<!DOCTYPE html>
<html>
<head>
    <#-- import macro -->
    <#import "../common/common.macro.ftl" as netCommon>

    <!-- 1-style start -->
    <@netCommon.commonStyle />
    <!-- 1-style end -->

</head>
<body class="hold-transition" style="background-color: #ecf0f5;">
<div class="wrapper">
    <section class="content">

        <!-- 2-content start -->

            <form class="form-horizontal" id="ducomentForm" >

                <#--基础信息-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">基础信息</h3>

                        <#if hasBizPermission>
                            <div class="box-tools pull-right">
                                <button class="btn btn-default btn-xs" type="button" onclick="javascript:openTab('${request.contextPath}/datatype/updateDataTypePage?dataTypeId=${apiDataType.id}', '修改数据类型', true)" >前往修改页面</button>
                            </div>
                        </#if>
                    </div>

                    <div class="box-body">
                        <div class="form-group">
                            <div class="col-sm-6">
                                <b>名称：</b> <span style="color: #00a65a;">${apiDataType.name}</span>
                            </div>
                            <div class="col-sm-6">
                                <b>业务线：</b>
                                <#if bizList?exists && bizList?size gt 0>
                                    <#list bizList as biz>
                                        <#if biz.id == apiDataType.bizId>${biz.bizName}</#if>
                                    </#list>
                                </#if>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-6">
                                <b>描述：</b>
                                ${apiDataType.about}
                            </div>
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
                                        <a href="javascript:;" onclick="javascript:openTab('${request.contextPath}/datatype/dataTypeDetail?dataTypeId=${field.fieldDatatypeId}', '查看数据类型', false)" >
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
                        <h3 class="box-title">数据结构-</h3>
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

        <!-- 2-content end -->

    </section>
</div>

<!-- 3-script start -->
<@netCommon.commonScript />
<#-- admin util -->
<script src="${request.contextPath}/static/biz/common/admin.util.js"></script>
<!-- 3-script end -->

</body>
</html>
