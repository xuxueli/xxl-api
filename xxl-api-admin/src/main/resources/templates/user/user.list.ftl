<!DOCTYPE html>
<html>
<head>
  	<title>API管理平台</title>
  	<#import "../common/common.macro.ftl" as netCommon>
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">
	<@netCommon.commonStyle />
	<!-- DataTables -->


</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["adminlte_settings"]?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "userList" />

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>用户管理</h1>
		</section>

		<!-- Main content -->
	    <section class="content">

            <div class="row">

                <div class="col-xs-4">
                    <div class="input-group">
                        <span class="input-group-addon">用户类型</span>
                        <select class="form-control" id="type">
                            <option value="-1" >默认</option>
                            <option value="0" >普通用户</option>
                            <option value="1" >管理员</option>
                        </select>
                    </div>
                </div>

                <div class="col-xs-4">
                    <div class="input-group">
                        <span class="input-group-addon">登录账号</span>
                        <input type="text" class="form-control" id="userName" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-2">
                    <button class="btn btn-block btn-info" id="search">搜索</button>
                </div>
                <div class="col-xs-2 pull-right">
                    <button class="btn btn-block btn-success" type="button" id="add" >+新增用户</button>
                </div>
            </div>

			<div class="row">
				<div class="col-xs-12">

                    <div class="box">
                        <!-- /.box-header -->
                        <div class="box-body">
                            <table id="user_list" class="table table-bordered table-striped" width="100%" >
                                <thead>
									<tr>
										<th>ID</th>
										<th>账号</th>
										<th>类型</th>
                                        <th>操作</th>
									</tr>
                                </thead>
                            </table>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->

				</div>
			</div>
	    </section>
	</div>

	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<!-- 新增.模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >新增用户</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
					<div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">登录账号<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="userName" placeholder="请输入“登录账号”" maxlength="50" ></div>
					</div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">登录密码<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="password" placeholder="请输入“登录密码”" maxlength="50" value="123456" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">用户类型<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="type" value="0" checked >普通用户
                            <input type="radio" name="type" value="1" >管理员
						</div>
                    </div>

					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >保存</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
						</div>
					</div>
				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >更新任务</h4>
         	</div>
         	<div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">登录账号<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="userName" placeholder="请输入“登录账号”" maxlength="50" readonly ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">登录密码<font color="black">*</font></label>
                        <div class="col-sm-10">
                            <div class="input-group">
									<span class="input-group-addon">
										重置密码<input type="checkbox" name="passwordInput" >
									</span>
                                <input type="text" class="form-control" name="password" placeholder="为空则不更新密码" maxlength="50" readonly >
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">用户类型<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="type" value="0" checked >普通用户
                            <input type="radio" name="type" value="1" >管理员
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
                            <button type="submit" class="btn btn-primary"  >保存</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>

                            <input type="hidden" name="id" >
                        </div>
                    </div>
                </form>
         	</div>
		</div>
	</div>
</div>

<!-- 分配业务线权限.模态框 -->
<div class="modal fade" id="updatePermissionBizModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" >分配项目权限</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-8">
                            <div class="form-group">
                            <#list bizList as biz>
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="permissionBiz" value="${biz.id}" >${biz.bizName}
                                    </label>
                                </div>
                            </#list>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" class="btn btn-primary ok" >保存</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>

                            <input type="hidden" name="id"  >
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<!-- moment -->
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/moment.min.js"></script>

<script src="${request.contextPath}/static/js/user.list.1.js"></script>
</body>
</html>
