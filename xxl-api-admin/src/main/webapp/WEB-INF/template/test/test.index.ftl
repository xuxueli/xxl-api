<!DOCTYPE html>
<html>
<head>
    <title>API管理平台</title>
    <link rel="shortcut icon" href="${request.contextPath}/favicon.ico" type="image/x-icon" />
    <#import "/common/common.macro.ftl" as netCommon>
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/select2/select2.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/_all.css">
    <@netCommon.commonStyle />

</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
    <!-- header -->
<@netCommon.commonHeader />
    <!-- left -->
<@netCommon.commonLeft "projectList" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>Test接口<small>API管理平台</small></h1>
        </section>

        <section class="content">
            <form class="form-horizontal" id="ducomentForm" >
                <#--基础信息-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">基础信息</h3>
                    </div>

                    <div class="box-body">
                        <div class="form-group">
                            <label class="col-sm-1 control-label">请求方法</label>
                            <div class="col-sm-4">
                                <select class="form-control select2" style="width: 100%;" id="requestMethod">
                                <#list RequestMethodEnum as item>
                                    <option value="${item}">${item}</option>
                                </#list>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">接口URL</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="requestUrl" placeholder="请输入接口URL" maxlength="100" >
                            </div>
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
                            <label class="col-sm-1 control-label">头部标签</label>
                            <div class="col-sm-4 item">
                                <select class="form-control select2_tag_new key" >
                                    <option value=""></option>
                                <#list requestHeadersEnum as item>
                                    <option value="${item}">${item}</option>
                                </#list>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">头部内容</label>
                            <div class="col-sm-5 item">
                                <input type="text" class="form-control value">
                            </div>
                            <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                        </div>
                    </div>

                    <div class="box-body" id="requestHeaders_parent" >
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
                            <label class="col-sm-1 control-label">参数名称</label>
                            <div class="col-sm-4 item">
                                <input type="text" class="form-control key">
                            </div>
                            <label class="col-sm-1 control-label">参数说明</label>
                            <div class="col-sm-5 item">
                                <input type="text" class="form-control value">
                            </div>
                            <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                        </div>
                    </div>

                    <div class="box-body" id="queryParams_parent" >
                    </div>
                </div>

                <#--响应结果-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">响应结果</h3>
                        <div class="box-tools pull-right">
                            <input type="hidden" id="documentId" value="${documentId}" >
                            <input type="hidden" id="testId" value="${testId}" >
                            <#if testHistory?exists >
                                <button class="btn btn-default btn-xs" type="button" >更新</button>
                            <#elseif document?exists >
                                <button class="btn btn-default btn-xs" type="button" >保存</button>
                            </#if>
                            <button class="btn btn-info btn-xs" type="button" id="run" >运行</button>
                        </div>
                    </div>
                    <div class="box-body">
                        响应数据类型(MIME)：
                        <#list ResponseContentType as item>
                            <input type="radio" class="iCheck" id="respType" value="${item}" <#if item_index==0>checked</#if> >${item}  &nbsp;&nbsp;
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
<script src="${request.contextPath}/static/js/test.index.1.js"></script>
</body>
</html>
