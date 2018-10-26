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
	<@netCommon.commonLeft "datatype" />

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>数据类型管理</h1>
		</section>

		<!-- Main content -->
	    <section class="content">

            <div class="row">

                <div class="col-xs-4">
                    <div class="input-group">
                        <span class="input-group-addon">业务线</span>
                        <select class="form-control" id="bizId" >
                            <option value="-1" >全部</option>
                            <#if bizList?exists && bizList?size gt 0>
                            <#list bizList as biz>
                                <option value="${biz.id}"  >${biz.bizName}</option>
                            </#list>
                            </#if>
                        </select>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="input-group">
                        <span class="input-group-addon">数据类型名称</span>
                        <input type="text" class="form-control" id="name" autocomplete="on" >
                    </div>
                </div>
                <div class="col-xs-2">
                    <button class="btn btn-block btn-info" id="search">搜索</button>
                </div>
                <div class="col-xs-2 pull-right">
                    <button class="btn btn-block btn-success" type="button" onclick="javascript:window.open('${request.contextPath}/datatype/addDataTypePage')" >+新增数据类型</button>
                </div>
            </div>

			<div class="row">
				<div class="col-xs-12">
                    <div class="box">
                        <!-- /.box-header -->
                        <div class="box-body">
                            <table id="project_list" class="table table-bordered table-striped" width="100%" >
                                <thead>
									<tr>
										<th>ID</th>
										<th>数据类型名称</th>
										<th>数据类型简介</th>
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

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<!-- moment -->
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script>
    // 业务线权限
    var superUser = <#if XXL_API_LOGIN_IDENTITY.type == 1 >true<#else>false</#if>;
    var permissionBiz = '${XXL_API_LOGIN_IDENTITY.permissionBiz!""}';

    var permissionBizArr;
    if (permissionBiz) {
        permissionBizArr = $(permissionBiz.split(","));
    };
    function hasBizPermission(bizId) {
        if ( superUser || $.inArray( bizId+'', permissionBizArr) > -1 ) {
            return true;
        } else {
            return false;
        }
    }
</script>
<script src="${request.contextPath}/static/js/datatype.list.1.js"></script>
</body>
</html>
