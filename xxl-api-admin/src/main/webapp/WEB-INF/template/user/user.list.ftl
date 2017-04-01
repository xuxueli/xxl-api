<!DOCTYPE html>
<html>
<head>
  	<title>API管理平台</title>
  	<#import "/common/common.macro.ftl" as netCommon>
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">
	<@netCommon.commonStyle />
	<!-- DataTables -->


</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "userList" />

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>用户管理<small>API管理平台</small></h1>
		</section>

		<!-- Main content -->
	    <section class="content">
			<div class="row">
				<div class="col-xs-12">

                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">用户列表</h3>
                            <button class="btn btn-info btn-xs pull-right" id="add" >+新增用户</button>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <table id="user_list" class="table table-bordered table-striped">
                                <thead>
									<tr>
										<th>ID</th>
										<th>账号</th>
										<th>密码</th>
										<th>类型</th>
										<th>真实姓名</th>
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
                        <div class="col-sm-10"><input type="text" class="form-control" name="userName" placeholder="请输入“登录账号”" maxlength="20" ></div>
					</div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">登录密码<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="password" placeholder="请输入“登录密码”" maxlength="20" value="123456" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">用户类型<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="type" value="0" checked >普通用户
                            <input type="radio" name="type" value="1" >超级管理员
						</div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">真实姓名<font color="black">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="realName" placeholder="请输入“真实姓名”" maxlength="20" ></div>
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
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >更新任务</h4>
         	</div>
         	<div class="modal-body">
                <form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">登录账号<font color="red">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="userName" placeholder="请输入“登录账号”" maxlength="20" readonly ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">登录密码<font color="black">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="password" placeholder="请输入新“登录密码”，为空表示不更新" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">用户类型<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="type" value="0" checked >普通用户
                            <input type="radio" name="type" value="1" >超级管理员
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">真实姓名<font color="black">*</font></label>
                        <div class="col-sm-10"><input type="text" class="form-control" name="realName" placeholder="请输入“真实姓名”" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">性别<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="sex" value="0" checked >男
                            <input type="radio" name="sex" value="1" >女
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

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<!-- moment -->
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/moment.min.js"></script>

<script>
	userList = eval('('+ '${userList}' +')');
</script>
<script src="${request.contextPath}/static/js/user.list.1.js"></script>
</body>
</html>
