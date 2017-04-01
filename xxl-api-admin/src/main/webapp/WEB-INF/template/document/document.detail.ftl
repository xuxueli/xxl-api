<!DOCTYPE html>
<html>
<head>
  	<title>API管理平台</title>
    <link rel="shortcut icon" href="${request.contextPath}/favicon.ico" type="image/x-icon" />
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/plugins/editor.md-1.5.0/main/editormd.min.css">

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
			<h1>接口详情<small>API管理平台</small></h1>
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
                            <button class="btn btn-default btn-xs" type="button" onclick="javascript:window.location.href='${request.contextPath}/group?productId=${productId}'" >返回接口列表</button>
                            <button class="btn btn-default btn-xs" type="button" onclick="javascript:window.location.href='${request.contextPath}/document/updatePage?id=${document.id}'" >修改接口</button>
                            <button class="btn btn-info btn-xs" type="button" >Test</button>
                            <button class="btn btn-info btn-xs" type="button" >Mock</button>
                        </div>
                    </div>

                    <div class="box-body">
                        <div class="form-group">
                            <label class="col-sm-1 control-label">接口分组</label>
                            <div class="col-sm-4">
                                <#if 0 == document.groupId>默认分组
                                <#else>
                                    <#if groupList?exists && groupList?size gt 0>
                                        <#list groupList as group>
                                            <#if group.id == document.groupId>${group.name}</#if>
                                        </#list>
                                    </#if>
                                </#if>
                            </div>
                            <label class="col-sm-1 control-label">接口状态</label>
                            <div class="col-sm-6">
                                <#if 0 == document.status>启用
                                <#elseif 1 == document.status>维护
                                <#elseif 2 == document.status>废弃
                                </#if>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-1 control-label">请求方法</label>
                            <div class="col-sm-4">
                                <#list RequestMethodEnum as item>
                                    <#if item == document.requestMethod>${item}</#if>
                                </#list>
                            </div>
                            <label class="col-sm-1 control-label">接口URL</label>
                            <div class="col-sm-6">
                                ${document.requestUrl}
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-1 control-label">接口名称</label>
                            <div class="col-sm-11">
                                ${document.name}
                            </div>
                        </div>
                    </div>
                </div>

                <#--请求头部-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">请求头部</h3>
                    </div>

                    <div class="box-body" id="requestHeaders_parent" >
                        <#if requestHeadersList?exists >
                            <#list requestHeadersList as requestHeadersMap>
                                <#assign key = requestHeadersMap['key'] />
                                <#assign value = requestHeadersMap['value'] />
                                <div class="form-group requestHeaders_item" >
                                    <label class="col-sm-1 control-label">头部标签</label>
                                    <div class="col-sm-4 item">${key}</div>
                                    <label class="col-sm-1 control-label">头部内容</label>
                                    <div class="col-sm-5 item">${value}</div>
                                </div>
                            </#list>
                        </#if>
                    </div>
                </div>

                <#--请求参数-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">请求参数</h3>
                    </div>

                    <div class="box-body" id="queryParams_parent" >
                        <#if queryParamList?exists>
                            <#list queryParamList as queryParam>
                                <div class="form-group queryParams_item" >
                                    <div class="col-sm-2 item">
                                        <#if queryParam.notNull == "true" >必填
                                        <#else>非必填
                                        </#if>
                                    </div>
                                    <label class="col-sm-1 control-label">参数类型</label>
                                    <div class="col-sm-2 item">${queryParam.type}</div>
                                    <label class="col-sm-1 control-label">参数名称</label>
                                    <div class="col-sm-2 item">${queryParam.name}</div>
                                    <label class="col-sm-1 control-label">参数说明</label>
                                    <div class="col-sm-2 item">${queryParam.desc}</div>
                                </div>
                            </#list>
                        </#if>
                    </div>
                </div>

                <#--请求结果-->
                <div class="nav-tabs-custom">
                    <!-- Tabs within a box -->
                    <ul class="nav nav-tabs pull-right">
                        <li><a href="#fail_resp" data-toggle="tab">失败结果</a></li>
                        <li class="active"><a href="#success_resp" data-toggle="tab">成功结果</a></li>
                        <li class="pull-left header">请求结果</li>
                    </ul>
                    <div class="tab-content no-padding">
                        <!-- Morris chart - Sales -->
                        <div class="chart tab-pane active" id="success_resp" style="position: relative; height: 365px;">
                            <div class="box-body">
                                ${document.successRespType}
                                <br>
                                <pre name="successRespExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;margin-top: 15px;" >${document.successRespExample}</pre>
                            </div>
                        </div>
                        <div class="chart tab-pane" id="fail_resp" style="position: relative; height: 365px;">
                            <div class="box-body">
                                ${document.failRespType}
                                <br>
                                <pre name="failRespExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;margin-top: 15px;" >${document.failRespExample}</pre>
                            </div>
                        </div>
                    </div>
                </div>

                <#-- 接口备注 -->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">接口备注</h3>
                    </div>
                    <div class="box-body" >
                        <div class="box-body pad" id="remark" ><textarea style="display:none;">${document.remark}</textarea></div>
                    </div>
                </div>

            </form>

        </section>

	</div>

	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<@netCommon.commonScript />

<script src="${request.contextPath}/static/plugins/editor.md-1.5.0/main/editormd.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor.md-1.5.0/lib/marked.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor.md-1.5.0/lib/prettify.min.js"></script>
<script src="${request.contextPath}/static/js/document.detail.1.js"></script>
</body>
</html>
