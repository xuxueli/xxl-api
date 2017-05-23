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
	<@netCommon.commonLeft "bizList" />

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>业务线管理<small>API管理平台</small></h1>
		</section>

		<!-- Main content -->
	    <section class="content">
			<div class="row">
				<div class="col-xs-12">

                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">业务线列表</h3>
                            <button class="btn btn-info btn-xs pull-right" id="add" >+新增业务线</button>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <table id="user_list" class="table table-bordered table-striped">
                                <thead>
									<tr>
										<th>ID</th>
										<th>业务线名称</th>
										<th>排序</th>
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
                        <label for="lastname" class="col-sm-4 control-label">业务线名称<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="bizName" placeholder="请输入“业务线名称”" maxlength="50" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">排序<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="order" placeholder="请输入“排序”" maxlength="5" ></div>
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
                        <label for="lastname" class="col-sm-4 control-label">业务线名称<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="bizName" placeholder="请输入“业务线名称”" maxlength="50" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">排序<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="order" placeholder="请输入“排序”" maxlength="5" ></div>
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
    bizList = eval('('+ '${bizList}' +')');
</script>
<script src="${request.contextPath}/static/js/biz.list.1.js"></script>
</body>
</html>
