<!DOCTYPE html>
<html>
<head>
  	<title>API管理平台</title>
    <link rel="shortcut icon" href="${request.contextPath}/favicon.ico" type="image/x-icon" />
  	<#import "/common/common.macro.ftl" as netCommon>
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/select2/select2.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/_all.css">
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
			<h1>更新接口<small>API管理平台</small></h1>
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
                            <button class="btn btn-default btn-xs" type="button" onclick="javascript:window.location.href='${request.contextPath}/document/detailPage?id=${document.id}'" >接口详情页</button>
                            <button class="btn btn-info btn-xs" type="submit" >更新接口</button>
                        </div>
                    </div>

                    <div class="box-body">
                        <div class="form-group">
                            <label class="col-sm-1 control-label">接口分组</label>
                            <div class="col-sm-4">
                                <select class="form-control select2" style="width: 100%;" name="groupId">
                                    <option value="0" <#if 0 == document.groupId>selected</#if> >默认分组</option>
                                    <#if groupList?exists && groupList?size gt 0>
                                        <#list groupList as group>
                                            <option value="${group.id}" <#if group.id == document.groupId>selected</#if> >${group.name}</option>
                                        </#list>
                                    </#if>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">接口状态</label>
                            <div class="col-sm-6">
                                <input type="radio" class="iCheck" name="status" value="0" <#if 0 == document.status>checked</#if> >启用  &nbsp;&nbsp;
                                <input type="radio" class="iCheck" name="status" value="1" <#if 1 == document.status>checked</#if> >维护  &nbsp;&nbsp;
                                <input type="radio" class="iCheck" name="status" value="2" <#if 2 == document.status>checked</#if> >废弃
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-1 control-label">请求方法</label>
                            <div class="col-sm-4">
                                <select class="form-control select2" style="width: 100%;" name="requestMethod">
                                    <#list RequestMethodEnum as item>
                                        <option value="${item}" <#if item == document.requestMethod>selected</#if> >${item}</option>
                                    </#list>
                                </select>
                            </div>
                            <label class="col-sm-1 control-label">接口URL</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" name="requestUrl" value="${document.requestUrl}" placeholder="请输入接口URL（相对地址）" maxlength="100" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-1 control-label">接口名称</label>
                            <div class="col-sm-11">
                                <input type="text" class="form-control" name="name" value="${document.name}" placeholder="请输入接口名称" maxlength="50" >
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
                        <#if requestHeadersList?exists >
                            <#list requestHeadersList as requestHeadersMap>
                                <#assign key = requestHeadersMap['key'] />
                                <#assign value = requestHeadersMap['value'] />
                                <div class="form-group requestHeaders_item" >
                                    <label class="col-sm-1 control-label">头部标签</label>
                                    <div class="col-sm-4 item">
                                        <select class="form-control select2_tag key" >
                                            <option value="" <#if key==null>selected</#if> ></option>
                                            <#list requestHeadersEnum as item>
                                                <option value="${item}" <#if key==item>selected</#if> >${item}</option>
                                            </#list>
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label">头部内容</label>
                                    <div class="col-sm-5 item">
                                        <input type="text" class="form-control value" value="${value}" >
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
                        <#if queryParamList?exists>
                            <#list queryParamList as queryParam>
                                <div class="form-group queryParams_item" >
                                    <div class="col-sm-2 item">
                                        <select class="form-control select2_tag notNull" style="width: 100%;">
                                            <option value="true" <#if queryParam.notNull == "true" >selected</#if> >必填</option>
                                            <option value="false" <#if queryParam.notNull == "false" >selected</#if> >非必填</option>
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label">参数类型</label>
                                    <div class="col-sm-2 item">
                                        <select class="form-control select2_tag type" style="width: 100%;">
                                            <#list QueryParamTypeEnum as item>
                                                <option value="${item}" <#if queryParam.type == item>selected</#if> >${item}</option>
                                            </#list>
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label">参数名称</label>
                                    <div class="col-sm-2 item">
                                        <input type="text" class="form-control name" value="${queryParam.name}" >
                                    </div>
                                    <label class="col-sm-1 control-label">参数说明</label>
                                    <div class="col-sm-2 item">
                                        <input type="text" class="form-control desc" value="${queryParam.desc}" >
                                    </div>
                                    <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                                </div>
                            </#list>
                        </#if>
                    </div>
                </div>

                <#--响应结果-->
                <div class="nav-tabs-custom">
                    <!-- Tabs within a box -->
                    <ul class="nav nav-tabs pull-right">
                        <li><a href="#fail_resp" data-toggle="tab">失败响应结果</a></li>
                        <li class="active"><a href="#success_resp" data-toggle="tab">成功响应结果</a></li>
                        <li class="pull-left header">响应结果</li>
                    </ul>
                    <div class="tab-content no-padding">
                        <!-- Morris chart - Sales -->
                        <div class="chart tab-pane active" id="success_resp" style="position: relative; height: 365px;">
                            <div class="box-body">
                                响应数据类型(MIME)：
                                <#list ResponseContentType as item>
                                    <input type="radio" class="iCheck" name="successRespType" value="${item}" <#if document.successRespType==item>checked</#if> >${item}  &nbsp;&nbsp;
                                </#list>
                                <br>
                                <textarea name="successRespExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;margin-top: 15px;" >${document.successRespExample}</textarea>
                            </div>
                        </div>
                        <div class="chart tab-pane" id="fail_resp" style="position: relative; height: 365px;">
                            <div class="box-body">
                                响应数据类型(MIME)：
                                <#list ResponseContentType as item>
                                    <input type="radio" class="iCheck" name="failRespType" value="${item}" <#if document.failRespType==item>checked</#if> >${item}  &nbsp;&nbsp;
                                </#list>
                                <br>
                                <textarea name="failRespExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;margin-top: 15px;" >${document.failRespExample}</textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <#--响应数据类型-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">响应数据类型</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-info btn-xs" type="button" onclick="javascript:window.open('${request.contextPath}/datatype/addDataTypePage');" >+ 新增数据类型</button>
                        </div>
                    </div>
                    <div class="box-body" >

                        <label class="col-sm-1 control-label">数据类型</label>
                        <div class="col-sm-4 item">
                            <select class="form-control" style="width: 100%;" id="responseDatatypeId" name="responseDatatypeId"  >
                            <#if responseDatatype?exists>
                                <option value="${responseDatatype.id}">${responseDatatype.name}</option>
                            </#if>
                            </select>
                        </div>

                    </div>
                </div>

                <#--响应结果参数-->
                <#--<div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">响应结果参数</h3>
                        <div class="box-tools pull-right">
                            <button type="button" class="btn btn-box-tool" id="responseParams_add" ><i class="fa fa-plus"></i></button>
                        </div>
                    </div>

                    <div id="responseParams_example" style="display: none;" >
                        <div class="form-group responseParams_item" >
                            <div class="col-sm-2 item">
                                <select class="form-control select2_tag_new notNull" style="width: 100%;">
                                    <option value="true">非空</option>
                                    <option value="false">可空</option>
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
                            <div class="col-sm-2 item">
                                <input type="text" class="form-control desc">
                            </div>
                            <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                        </div>
                    </div>

                    <div class="box-body" id="responseParams_parent" >
                        <#if responseParamList?exists>
                            <#list responseParamList as responseParam>
                                <div class="form-group responseParams_item" >
                                    <div class="col-sm-2 item">
                                        <select class="form-control select2_tag notNull" style="width: 100%;">
                                            <option value="true" <#if responseParam.notNull == "true" >selected</#if> >必填</option>
                                            <option value="false" <#if responseParam.notNull == "false" >selected</#if> >非必填</option>
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label">参数类型</label>
                                    <div class="col-sm-2 item">
                                        <select class="form-control select2_tag type" style="width: 100%;">
                                            <#list QueryParamTypeEnum as item>
                                                <option value="${item}" <#if responseParam.type == item>selected</#if> >${item}</option>
                                            </#list>
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label">参数名称</label>
                                    <div class="col-sm-2 item">
                                        <input type="text" class="form-control name" value="${responseParam.name}" >
                                    </div>
                                    <label class="col-sm-1 control-label">参数说明</label>
                                    <div class="col-sm-2 item">
                                        <input type="text" class="form-control desc" value="${responseParam.desc}" >
                                    </div>
                                    <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                                </div>
                            </#list>
                        </#if>
                    </div>
                </div>-->

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

<script src="${request.contextPath}/static/adminlte/plugins/select2/select2.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor.md-1.5.0/main/editormd.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<script src="${request.contextPath}/static/js/document.update.1.js"></script>
</body>
</html>
