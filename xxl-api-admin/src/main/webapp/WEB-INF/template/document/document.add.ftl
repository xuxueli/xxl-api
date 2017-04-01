<!DOCTYPE html>
<html>
<head>
  	<title>API管理平台</title>
  	<#import "/common/common.macro.ftl" as netCommon>
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/select2/select2.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/_all.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
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
			<h1>新增接口<small>API管理平台</small></h1>
		</section>

        <section class="content">
            <form class="form-horizontal">
                <input type="hidden" name="projectId" value="${productId}" >

                <#--基础信息-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">基础信息</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-info btn-xs" type="button" onclick="javascript:window.open('${request.contextPath}/group?productId=${productId}')" >返回接口列表</button>
                        </div>
                    </div>

                    <div class="box-body">
                        <div class="form-group">
                            <label class="col-sm-1 control-label">接口分组</label>
                            <div class="col-sm-4">
                                <select class="form-control select2" style="width: 100%;" name="groupId">
                                    <option value="0">默认分组</option>
                                    <#if groupList?exists && groupList?size gt 0>
                                        <#list groupList as group>
                                            <option value="${group.id}">${group.name}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">接口状态</label>
                            <div class="col-sm-6">
                                <input type="radio" class="iCheck" name="status" value="0" checked >启用  &nbsp;&nbsp;
                                <input type="radio" class="iCheck" name="status" value="1" >维护  &nbsp;&nbsp;
                                <input type="radio" class="iCheck" name="status" value="2" >废弃
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-1 control-label">请求方法</label>
                            <div class="col-sm-4">
                                <select class="form-control select2" style="width: 100%;" name="requestMethod">
                                <#list RequestMethodEnum as item>
                                    <option value="item">${item}</option>
                                </#list>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">接口URL</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="requestUrl" placeholder="请输入接口URL（相对地址）" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-1 control-label">接口名称</label>
                            <div class="col-sm-11">
                                <input type="text" class="form-control" name="name" placeholder="请输入接口名称" >
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
                        <div class="form-group item" >
                            <label class="col-sm-1 control-label">头部标签</label>
                            <div class="col-sm-4 item">
                                <select class="form-control select2_tag_new key" style="width: 100%;">
                                    <option value=""></option>
                                <#list requestHeadersList as item>
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
                        <div class="form-group item" >
                            <div class="col-sm-1 item">
                                <select class="form-control select2_tag_new notNull" style="width: 100%;">
                                    <option value="true">必填</option>
                                    <option value="false">非必填</option>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">参数类型</label>
                            <div class="col-sm-2 item">
                                <select class="form-control select2_tag_new type" style="width: 100%;">
                                    <#list QueryParamTypeEnum as item>
                                        <option value="${item}">${item}</option>
                                    </#list>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">参数名称</label>
                            <div class="col-sm-2 item">
                                <input type="text" class="form-control name">
                            </div>
                            <label class="col-sm-1 control-label">参数说明</label>
                            <div class="col-sm-3 item">
                                <input type="text" class="form-control desc">
                            </div>
                            <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                        </div>
                    </div>

                    <div class="box-body" id="queryParams_parent" >
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
                        <div class="chart tab-pane active" id="success_resp" style="position: relative; height: 250px;">
                            <div class="box-body">
                                <#list ResponseContentType as item>
                                    <input type="radio" class="iCheck" name="successRespType" value="${item}" <#if item_index==0>checked</#if> >${item}  &nbsp;&nbsp;
                                </#list>
                                <br>
                                <textarea name="successRespExample" style="width: 100%;height: 200px;" ></textarea>
                            </div>
                        </div>
                        <div class="chart tab-pane" id="fail_resp" style="position: relative; height: 300px;">
                            <div class="box-body">
                            <#list ResponseContentType as item>
                                <input type="radio" class="iCheck" name="failRespType" value="${item}" <#if item_index==0>checked</#if> >${item}  &nbsp;&nbsp;
                            </#list>
                                <br>
                                <textarea name="failRespExample" style="width: 100%; height: 200px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;" ></textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <#-- 备注 -->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">备注</h3>
                    </div>
                    <div class="box-body pad">
                        <form>
                            <textarea class="textarea textarea_remark" name="remark" placeholder="请输入接口备注" style="width: 100%; height: 200px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;"></textarea>
                        </form>
                    </div>
                </div>

            </form>

        </section>

	</div>

	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/select2/select2.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<script src="${request.contextPath}/static/js/document.add.1.js"></script>
</body>
</html>
