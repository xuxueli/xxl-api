<!DOCTYPE html>
<html>
<head>
    <title>API管理平台</title>
    <link rel="shortcut icon" href="${request.contextPath}/static/favicon.ico" type="image/x-icon" />
    <#import "../common/common.macro.ftl" as netCommon>
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/select2/select2.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/_all.css">
    <link rel="stylesheet" href="${request.contextPath}/static/plugins/jsontree/jquery.jsonview.css">
    <@netCommon.commonStyle />

</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["adminlte_settings"]?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
    <!-- header -->
<@netCommon.commonHeader />
    <!-- left -->
<@netCommon.commonLeft "projectList" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>接口测试</h1>
        </section>

        <section class="content">
            <form class="form-horizontal" id="ducomentForm" >
                <#--基础信息-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">基础信息</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-default btn-xs" type="button" onclick="javascript:window.location.href='${request.contextPath}/document/detailPage?id=${document.id}'" >返回接口详情页</button>
                        </div>
                    </div>

                    <div class="box-body">
                        <div class="form-group">

                            <label class="col-sm-1 control-label">URL</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="requestUrl" placeholder="请输入接口URL" maxlength="100"
                                       <#if testHistory?exists && testHistory.requestUrl?exists >value="${testHistory.requestUrl}"</#if> >
                            </div>
                            <label class="col-sm-1 control-label">Method</label>
                            <div class="col-sm-2">
                                <select class="form-control select2" id="requestMethod">
                                <#list RequestMethodEnum as item>
                                    <option value="${item}" <#if testHistory?exists && testHistory.requestMethod==item>selected</#if> >${item}</option>
                                </#list>
                                </select>
                            </div>
                            <#if project?exists>
                                <div class="col-sm-2">
                                    <select class="form-control select2" id="projectBaseUrlUpdate" >
                                    <#if project.baseUrlProduct?exists && project.baseUrlProduct!="" >
                                        <option value="${project.baseUrlProduct}${document.requestUrl}"
                                            <#if testHistory?exists && testHistory.requestUrl == (project.baseUrlProduct + document.requestUrl)>selected</#if> >线上环境</option>
                                    </#if>
                                    <#if project.baseUrlPpe?exists && project.baseUrlPpe!="" >
                                        <option value="${project.baseUrlPpe}${document.requestUrl}"
                                            <#if testHistory?exists && testHistory.requestUrl == (project.baseUrlPpe + document.requestUrl)>selected</#if> >预发布环境</option>
                                    </#if>
                                    <#if project.baseUrlQa?exists && project.baseUrlQa!="" >
                                        <option value="${project.baseUrlQa}${document.requestUrl}"
                                            <#if testHistory?exists && testHistory.requestUrl == (project.baseUrlQa + document.requestUrl)>selected</#if> >测试环境</option>
                                    </#if>
                                    </select>
                                </div>
                            </#if>
                        </div>
                    </div>
                </div>

                <#--请求头部-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">请求头部</h3>
                        <div class="box-tools pull-right">
                            <button type="button" class="btn btn-box-tool" id="requestHeaders_add" ><i class="fa fa-plus"></i></button>
                        </div>
                    </div>

                    <div id="requestHeaders_example" style="display: none;" >
                        <div class="form-group requestHeaders_item" >
                            <label class="col-sm-1 control-label">Key</label>
                            <div class="col-sm-4 item">
                                <select class="form-control select2_tag_new key" >
                                    <option value=""></option>
                                <#list requestHeadersEnum as item>
                                    <option value="${item}">${item}</option>
                                </#list>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">Value</label>
                            <div class="col-sm-5 item">
                                <input type="text" class="form-control value">
                            </div>
                            <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                        </div>
                    </div>

                    <div class="box-body" id="requestHeaders_parent" >
                        <#if requestHeaders?exists>
                            <#list requestHeaders as item>
                                <#assign key = item['key'] />
                                <#assign value = item['value'] />
                                <div class="form-group requestHeaders_item" >
                                    <label class="col-sm-1 control-label">Key</label>
                                    <div class="col-sm-4 item">
                                        <select class="form-control select2_tag key" >
                                            <option value=""></option>
                                            <#list requestHeadersEnum as item>
                                                <option value="${item}" <#if item == key>selected</#if> >${item}</option>
                                            </#list>
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label">Value</label>
                                    <div class="col-sm-5 item">
                                        <input type="text" class="form-control value" value="${value}">
                                    </div>
                                    <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                                </div>
                            </#list>
                        </#if>
                    </div>
                </div>

                <#--请求参数-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">请求参数</h3>
                        <div class="box-tools pull-right">
                            <button type="button" class="btn btn-box-tool" id="queryParams_add" ><i class="fa fa-plus"></i></button>
                        </div>
                    </div>

                    <div id="queryParams_example" style="display: none;" >
                        <div class="form-group queryParams_item" >
                            <label class="col-sm-1 control-label">参数</label>
                            <div class="col-sm-4 item">
                                <input type="text" class="form-control key">
                            </div>
                            <label class="col-sm-1 control-label">值</label>
                            <div class="col-sm-5 item">
                                <input type="text" class="form-control value">
                            </div>
                            <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                        </div>
                    </div>

                    <div class="box-body" id="queryParams_parent" >
                        <#if queryParams?exists>
                            <#list queryParams as item>
                                <div class="form-group queryParams_item" >
                                    <label class="col-sm-1 control-label">参数</label>
                                    <div class="col-sm-4 item">
                                        <input type="text" class="form-control key" value="<#if testId gt 0>${item.key}<#else>${item.name}</#if>" >
                                    </div>
                                    <label class="col-sm-1 control-label">值</label>
                                    <div class="col-sm-5 item">
                                        <input type="text" class="form-control value" value="<#if testId gt 0>${item.value}<#else></#if>" >
                                    </div>
                                    <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                                </div>
                            </#list>
                        </#if>
                    </div>
                </div>

                <#--响应结果-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">响应结果</h3>
                        <div class="box-tools pull-right">
                            <#if document?exists>
                                <button class="btn btn-default btn-xs" type="button" id="save" testId="${testId}" documentId="${document.id}" >保存</button>
                            </#if>
                            <button class="btn btn-info btn-xs" type="button" id="run" >运行</button>
                        </div>
                    </div>
                    <div class="box-body" id="respType_parent">
                        响应数据类型(MIME)：
                        <#list ResponseContentType as item>
                            <input type="radio" class="iCheck" name="respType" value="${item}" <#if item_index==0>checked</#if> >${item}  &nbsp;&nbsp;
                        </#list>
                        <br>
                        <pre id="respContent" ><br><br><br><br><br></pre>
                    </div>
                </div>

            </form>

        </section>

    </div>

    <!-- footer -->
<@netCommon.commonFooter />
</div>

<@netCommon.commonScript />

<script src="${request.contextPath}/static/adminlte/plugins/select2/select2.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/plugins/jsontree/jquery.jsonview.js"></script>
<script src="${request.contextPath}/static/js/test.index.1.js"></script>
</body>
</html>
