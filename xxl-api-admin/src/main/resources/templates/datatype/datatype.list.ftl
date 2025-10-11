<!DOCTYPE html>
<html>
<head>
    <#-- import macro -->
    <#import "../common/common.macro.ftl" as netCommon>

    <!-- 1-style start -->
    <@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/plugins/bootstrap-table/bootstrap-table.min.css">
    <!-- 1-style end -->

</head>
<body class="hold-transition" style="background-color: #ecf0f5;">
<div class="wrapper">
    <section class="content">

        <!-- 2-content start -->

        <#-- 查询区域 -->
        <div class="box" style="margin-bottom:9px;">
            <div class="box-body">
                <div class="row" id="data_filter" >

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
                    <div class="col-xs-1">
                        <button class="btn btn-block btn-primary searchBtn" >${I18n.system_search}</button>
                    </div>
                    <div class="col-xs-1">
                        <button class="btn btn-block btn-default resetBtn" >${I18n.system_reset}</button>
                    </div>

                </div>
            </div>
        </div>

        <#-- 数据表格区域 -->
        <div class="row">
            <div class="col-xs-12">
                <div class="box">
                    <div class="box-header pull-left" id="data_operation" >
                        <button class="btn btn-sm btn-info add2" type="button" onclick="javascript:openTab('${request.contextPath}/datatype/addDataTypePage', '新增数据类型', false)" ><i class="fa fa-plus" ></i>${I18n.system_opt_add}</button>
                        <button class="btn btn-sm btn-warning selectOnlyOne update" type="button"><i class="fa fa-edit"></i>${I18n.system_opt_edit}</button>
                        <button class="btn btn-sm btn-danger selectAny delete" type="button"><i class="fa fa-remove "></i>${I18n.system_opt_del}</button>
                        <button class="btn btn-sm btn-primary selectOnlyOne enterDetail" type="button"> 查看详情</button>
                    </div>
                    <div class="box-body" >
                        <table id="data_list" class="table table-bordered table-striped" width="100%" >
                            <thead></thead>
                            <tbody></tbody>
                            <tfoot></tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- 2-content end -->

    </section>
</div>

<!-- 3-script start -->
<@netCommon.commonScript />
<script src="${request.contextPath}/static/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${request.contextPath}/static/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<#-- admin -->
<script src="${request.contextPath}/static/biz/common/admin.table.js"></script>
<script src="${request.contextPath}/static/biz/common/admin.util.js"></script>
<script>
    $(function() {

        /**
         * 业务线权限过滤
         */
        var superUser = <#if xxl_sso_user.roleList?? && xxl_sso_user.roleList?seq_contains("ADMIN") >true<#else>false</#if>;
        var permissionBizArr = <#if xxl_sso_user.permissionList??> [<#list xxl_sso_user.permissionList as item>"${item?js_string}"<#if item_has_next>,</#if></#list>] <#else> [] </#if>;
        function hasBizPermission(bizId) {
            return superUser || $.inArray( bizId+'', permissionBizArr) > -1;
        }

        // ---------------------- datatype ----------------------

        /**
         * init table
         */
        $.adminTable.initTable({
            table: '#data_list',
            url: base_url + "/datatype/pageList",
            queryParams: function (params) {
                var obj = {};
                obj.bizId = $('#bizId').val();
                obj.name = $('#name').val();
                obj.offset = params.offset;
                obj.pagesize = params.limit;
                return obj;
            },
            columns:[
                {
                    checkbox: true,
                    field: 'state',
                    width: '5',
                    widthUnit: '%',
                    align: 'center',
                    valign: 'middle'
                }, {
                    title: '数据类型名称',
                    field: 'name',
                    width: '30',
                    widthUnit: '%',
                    align: 'left'
                },{
                    title: '数据类型描述',
                    field: 'about',
                    width: '50',
                    widthUnit: '%',
                    align: 'left'
                }
            ]
        });

        /**
         * init delete
         */
        $.adminTable.initDelete({
            url: base_url + "/datatype/delete"
        });


        // update
        $("#data_operation .update").click(function(){
            // get select rows
            var rows = $.adminTable.table.bootstrapTable('getSelections');

            // find select row
            if (rows.length !== 1) {
                layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
                return;
            }
            var row = rows[0];

            // valid permission
            if (!hasBizPermission(row.bizId)) {
                layer.msg(I18n.system_permission_limit);
                return;
            }

            // detail
            var updateUrl = base_url + '/datatype/updateDataTypePage?dataTypeId='+ row.id;
            openTab(updateUrl, '修改数据类型', false);
        });

        // enterDetail
        $("#data_operation .enterDetail").click(function(){
            // get select rows
            var rows = $.adminTable.table.bootstrapTable('getSelections');

            // find select row
            if (rows.length !== 1) {
                layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
                return;
            }
            var row = rows[0];

            // valid permission
            if (!hasBizPermission(row.bizId)) {
                layer.msg(I18n.system_permission_limit);
                return;
            }

            // detail
            var detailUrl = base_url + '/datatype/dataTypeDetail?dataTypeId='+ row.id;
            openTab(detailUrl, '查看数据类型', false);
        })

    });
</script>
<!-- 3-script end -->

</body>
</html>