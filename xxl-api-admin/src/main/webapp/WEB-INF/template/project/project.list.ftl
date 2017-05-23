<!DOCTYPE html>
<html>
<head>
  	<title>API管理平台</title>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">

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
			<h1>项目管理<small>API管理平台</small></h1>
		</section>

		<!-- Main content -->
	    <section class="content">

            <div class="row">

                <div class="col-xs-4">
                    <div class="input-group">
                        <span class="input-group-addon">业务线</span>
                        <select class="form-control" id="bizId">
                            <option value="0" <#if bizId==0>selected</#if> >默认</option>
                            <#if bizList?exists && bizList?size gt 0>
                            <#list bizList as biz>
                                <option value="${biz.id}" <#if bizId==biz.id>selected</#if> >${biz.bizName}</option>
                            </#list>
                            </#if>
                        </select>
                    </div>
                </div>

                <div class="col-xs-4">
                    <div class="input-group">
                        <span class="input-group-addon">项目名称</span>
                        <input type="text" class="form-control" id="name" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-2">
                    <button class="btn btn-block btn-info" id="search">搜索</button>
                </div>
                <div class="col-xs-2 pull-right">
                    <button class="btn btn-block btn-success" type="button" id="add" >+新增项目</button>
                </div>
            </div>

			<div class="row">
				<div class="col-xs-12">
                    <div class="box">
                        <!-- /.box-header -->
                        <div class="box-body">
                            <table id="project_list" class="table table-bordered table-striped">
                                <thead>
									<tr>
										<th>ID</th>
										<th>项目名称</th>
										<th>项目描述</th>
										<th>访问权限</th>
										<th>跟地址</th>
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
            	<h4 class="modal-title" >新增项目</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">业务线<font color="red">*</font></label>
                        <div class="col-sm-8">
                            <select class="form-control" name="bizId" >
                                <option value="0" >默认</option>
                            <#if bizList?exists && bizList?size gt 0>
                                <#list bizList as biz>
                                    <option value="${biz.id}" >${biz.bizName}</option>
                                </#list>
                            </#if>
                            </select>
                        </div>
                    </div>
					<div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">项目名称<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="name" placeholder="请输入“项目名称”" maxlength="50" ></div>
					</div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">项目描述<font color="black">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="desc" placeholder="请输入“项目描述”" maxlength="200" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">访问权限<font color="red">*</font></label>
                        <div class="col-sm-8">
                            <input type="radio" name="permission" value="0" checked >公开  &nbsp;&nbsp;
                            <input type="radio" name="permission" value="1" >私有
						</div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">跟地址：线上环境<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlProduct" placeholder="请输入“跟地址：线上环境”" maxlength="200" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">跟地址：预发布环境<font color="black">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlPpe" placeholder="请输入“跟地址：预发布环境”" maxlength="200" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">跟地址：测试环境<font color="black">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlQa" placeholder="请输入“跟地址：测试环境”" maxlength="200" ></div>
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
                        <label for="lastname" class="col-sm-4 control-label">业务线<font color="red">*</font></label>
                        <div class="col-sm-8">
                            <select class="form-control" name="bizId" >
                                <option value="0" >默认</option>
                            <#if bizList?exists && bizList?size gt 0>
                                <#list bizList as biz>
                                    <option value="${biz.id}" >${biz.bizName}</option>
                                </#list>
                            </#if>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">项目名称<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="name" placeholder="请输入“项目名称”" maxlength="50" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">项目描述<font color="black">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="desc" placeholder="请输入“项目描述”" maxlength="200" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">访问权限<font color="red">*</font></label>
                        <div class="col-sm-8">
                            <input type="radio" name="permission" value="0" checked >公开  &nbsp;&nbsp;
                            <input type="radio" name="permission" value="1" >私有
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">跟地址：线上环境<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlProduct" placeholder="请输入“跟地址：线上环境”" maxlength="200" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">跟地址：预发布环境<font color="black">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlPpe" placeholder="请输入“跟地址：预发布环境”" maxlength="200" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-4 control-label">跟地址：测试环境<font color="black">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlQa" placeholder="请输入“跟地址：测试环境”" maxlength="200" ></div>
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

<script src="${request.contextPath}/static/js/project.list.1.js"></script>
</body>
</html>
