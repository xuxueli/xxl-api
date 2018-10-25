<!DOCTYPE html>
<html>
<head>
  	<title>API管理平台</title>
  	<#import "../common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">

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
			<h1>接口管理</h1>
		</section>

        <section class="content">
            <div class="row">
                <!-- 接口分组 -->
                <div class="col-md-3">
                    <a class="btn btn-primary btn-block margin-bottom">${project.name}</a>
                    <div class="box box-solid">
                        <div class="box-header with-border">
                            <h3 class="box-title">接口分组</h3>

                            <#if hasBizPermission>
                                <div class="box-tools">
                                    <button type="button" class="btn btn-box-tool" id="addGroup" ><i class="fa fa-plus"></i></button>
                                </div>
                            </#if>

                        </div>
                        <div class="box-body no-padding">
                            <ul class="nav nav-pills nav-stacked">
                                <li <#if groupId == -1>class="active"</#if> groupId="-1" >
                                    <a href="${request.contextPath}/group?projectId=${projectId}&groupId=-1" >
                                        <i class="fa fa-inbox"></i>全部
                                        <#--<span class="label label-primary pull-right">12</span>-->
                                    </a>
                                </li>
                                <li <#if groupId == 0>class="active"</#if> groupId="0" >
                                    <a href="${request.contextPath}/group?projectId=${projectId}&groupId=0" >
                                        <i class="fa fa-inbox"></i>默认分组
                                        <#--<span class="label label-primary pull-right">12</span>-->
                                    </a>
                                </li>
                                <#if groupList?exists && groupList?size gt 0>
                                    <#list groupList as group>
                                        <li <#if groupId == group.id >class="active"</#if> groupId="${group.id}" >
                                            <a href="${request.contextPath}/group?projectId=${projectId}&groupId=${group.id}" >
                                                <i class="fa fa-inbox"></i>${group.name}
                                                <#--<span class="label label-primary pull-right">12</span>-->
                                            </a>
                                        </li>
                                    </#list>
                                </#if>
                            </ul>
                        </div>
                        <!-- /.box-body -->
                    </div>

                </div>
                <!-- /.col -->

                <#--接口列表-->
                <div class="col-md-9">
                    <div class="box box-primary">
                        <#--标题栏-->
                        <div class="box-header with-border">
                            <h3 class="box-title">
                                <#if groupId==-1 >全部
                                <#elseif groupId==0 >默认分组
                                <#else>
                                    <#if groupInfo?exists>${groupInfo.name}</#if>
                                </#if>
                            </h3>

                            <#if hasBizPermission>
                                <#if groupInfo?exists>
                                    &nbsp;&nbsp;
                                    <button class="btn btn-warning btn-xs" type="button" id="updateGroup" >编辑分组</button>
                                    <button class="btn btn-danger btn-xs" type="button" id="deleteGroup" _id="${groupInfo.id}" _projectId="${groupInfo.projectId}" >删除分组</button>
                                    |
                                </#if>
                                <button class="btn btn-info btn-xs" type="button" onclick="javascript:window.open('${request.contextPath}/document/addPage?projectId=${projectId}&groupId=${groupId}')" >+新增接口</button>
                                &nbsp;&nbsp;
                            </#if>

                            共<#if documentList?exists>${documentList?size}<#else>0</#if>个接口

                            <div class="box-tools pull-right">
                                <div class="has-feedback">
                                    <input type="text" class="form-control input-sm" id="searchUrl" placeholder="接口搜索">
                                    <span class="glyphicon glyphicon-search form-control-feedback"></span>
                                </div>
                            </div>
                        </div>

                        <div class="box-body no-padding">
                            <#--接口列表-->
                            <div class="table-responsive mailbox-messages">
                                <table class="table table-hover table-striped" id="documentList" >
                                    <thead>
                                        <tr>
                                            <th width="5%" ><i class="fa fa-star text-yellow"></i></th>
                                            <th width="35%" >URL</th>
                                            <th width="25%" >名称</th>
                                            <th width="17%" >分组</th>
                                            <th width="18%" >操作</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <#if documentList?exists && documentList?size gt 0>
                                            <#list documentList as document>
                                                <tr requestUrl="${document.requestUrl}" >
                                                    <td class="mailbox-star">
                                                        <a href="#" class="markStar" _starLevel="${document.starLevel}" _id="${document.id}" >
                                                            <#if document.starLevel == 1><i class="fa fa-star text-yellow"></i>
                                                            <#else><i class="fa fa-star-o text-yellow"></i>
                                                            </#if>
                                                        </a>
                                                    </td>
                                                    <td class="mailbox-attachment" title="${document.requestUrl}" >
                                                        <span class="label label-success">${document.requestMethod}</span>&nbsp;&nbsp;<#if document.requestUrl?length gt 25>${document.requestUrl?substring(0, 25)}...<#else>${document.requestUrl}</#if>
                                                    </td>
                                                    <td class="mailbox-name" title="${document.name}" >
                                                        <#if document.status==0><i class="fa fa-circle-o text-green"></i>
                                                        <#elseif document.status==1><i class="fa fa-circle-o text-yellow"></i>
                                                        <#else><i class="fa fa-circle-o text-light-gray"></i></#if>
                                                        <a href="${request.contextPath}/document/detailPage?id=${document.id}" >
                                                            <#if document.name?length gt 12>${document.name?substring(0, 12)}<#else>${document.name}</#if>
                                                        </a>
                                                    </td>
                                                    <td class="mailbox-date">
                                                        <#if groupId==0 >默认分组
                                                        <#else>
                                                            <#if groupList?exists && groupList?size gt 0>
                                                                <#list groupList as group>
                                                                    <#if group.id == document.groupId >${group.name}</#if>
                                                                </#list>
                                                            </#if>
                                                        </#if>
                                                    </td>
                                                    <td class="mailbox-date" >
                                                        <#if hasBizPermission>
                                                            <button class="btn btn-warning btn-xs update" onclick="window.location.href='${request.contextPath}/document/updatePage?id=${document.id}'" >编辑</button>
                                                            <button class="btn btn-danger btn-xs deleteDocument" _id="${document.id}" _name="${document.name}" >删除</button>
                                                            <button class="btn btn-primary btn-xs update" onclick="window.open('${request.contextPath}/test?documentId=${document.id}');" >测试</button>
                                                        </#if>
                                                    </td>
                                                </tr>
                                            </#list>
                                        </#if>
                                    </tbody>
                                </table>
                                <!-- /.table -->
                            </div>
                        </div>

                    </div>
                    <!-- /. box -->
                </div>
                <!-- /.col -->
            </div>
            <!-- /.row -->
        </section>
        <!-- /.content -->

	</div>

	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<!-- 新增-分组.模态框 -->
<div class="modal fade" id="addGroupModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增接口分组</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">分组名称<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="name" placeholder="请输入“分组名称”" maxlength="12" ></div>
					</div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">分组排序<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="order" placeholder="请输入“分组排序”" maxlength="5" ></div>
                    </div>

					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>

                            <input type="hidden" name="projectId" value="${projectId}" >
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 更新-分组.模态框 -->
<div class="modal fade" id="updateGroupModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >更新接口分组</h4>
         	</div>
         	<div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">分组名称<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="name" placeholder="请输入“分组名称”" maxlength="12" value="<#if groupInfo?exists>${groupInfo.name}</#if>" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">分组排序<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="order" placeholder="请输入“分组排序”" maxlength="5" value="<#if groupInfo?exists>${groupInfo.order}</#if>" ></div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="submit" class="btn btn-primary"  >保存</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>

                            <input type="hidden" name="id" value="<#if groupInfo?exists>${groupInfo.id}</#if>" >
                        </div>
                    </div>
                </form>
         	</div>
		</div>
	</div>
</div>

<@netCommon.commonScript />
<!-- moment -->
<script src="${request.contextPath}/static/js/group.list.1.js"></script>
</body>
</html>
