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

        <!-- 查询区域 -->

        <div class="box" style="margin-bottom:9px;">
            <div class="box-body">
                <div class="row" id="data_filter" >
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
                    <div class="col-xs-1">
                        <button class="btn btn-block btn-primary searchBtn" >${I18n.system_search}</button>
                    </div>
                    <div class="col-xs-1">
                        <button class="btn btn-block btn-default resetBtn" >${I18n.system_reset}</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 数据表格区域 -->
        <div class="row">
            <div class="col-xs-12">
                <div class="box">
                    <div class="box-header pull-left" id="data_operation" >
                        <button class="btn btn-sm btn-info add" type="button"><i class="fa fa-plus" ></i>${I18n.system_opt_add}</button>
                        <button class="btn btn-sm btn-warning selectOnlyOne update" type="button"><i class="fa fa-edit"></i>${I18n.system_opt_edit}</button>
                        <button class="btn btn-sm btn-danger selectAny delete" type="button"><i class="fa fa-remove "></i>${I18n.system_opt_del}</button>
                        <button class="btn btn-sm btn-primary selectOnlyOne permissionBiz" type="button"></i>分配业务线权限</button>
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
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="userName" placeholder="请输入“登录账号”" maxlength="20" readonly >
                                </div>
                            </div>
                            <div class="form-group" >
                                <label for="lastname" class="col-sm-2 control-label">登录密码<font color="black">*</font></label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="password" placeholder="为空则不更新密码" maxlength="20" >
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

        <!-- 2-content end -->

    </section>
</div>

<!-- 3-script start -->
<@netCommon.commonScript />
<script src="${request.contextPath}/static/plugins/bootstrap-table/bootstrap-table.min.js"></script>
<script src="${request.contextPath}/static/plugins/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<#-- admin -->
<script src="${request.contextPath}/static/biz/common/admin.table.js"></script>
<script>
    $(function() {

        /**
         * init table
         */
        $.adminTable.initTable({
            table: '#data_list',
            url: base_url + "/user/pageList",
            queryParams: function (params) {
                var obj = {};
                obj.userName = $('#userName').val();
                obj.type = $('#type').val();
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
                    title: '账号名称',
                    field: 'userName',
                    width: '30',
                    widthUnit: '%',
                    align: 'left'
                }, {
                    title: '登录密码',
                    field: 'password',
                    width: '20',
                    widthUnit: '%',
                    align: 'left'
                },{
                    title: '账号类型',
                    field: 'order',
                    width: '30',
                    widthUnit: '%',
                    align: 'left',
                    formatter: function (value, row, index) {
                        return value === 1?'管理员':'普通用户';
                    }
                }
            ]
        });

        /**
         * init delete
         */
        $.adminTable.initDelete({
            url: base_url + "/user/delete"
        });

        // jquery.validate 自定义校验
        jQuery.validator.addMethod("userNameValid", function(value, element) {
            var length = value.length;
            var valid = /^[a-z][a-z0-9.]*$/;
            return this.optional(element) || valid.test(value);
        }, "限制以小写字母开头，由小写字母、数字组成");

        /**
         * init add
         */
        $.adminTable.initAdd( {
            url: base_url + "/user/add",
            rules : {
                userName : {
                    required : true,
                    rangelength: [4, 20],
                    userNameValid: true
                },
                password : {
                    required : true,
                    rangelength: [4, 20]
                }
            },
            messages : {
                userName : {
                    required :"请输入登录账号",
                    rangelength : "登录账号长度限制为4~50"
                },
                password : {
                    required :"请输入密码",
                    rangelength : "密码长度限制为4~50"
                }
            },
            readFormData: function() {
                return $("#addModal .form").serialize();
            }
        });

        /**
         * init update
         */
        $.adminTable.initUpdate( {
            url: base_url + "/user/update",
            writeFormData: function(row) {

                // base data
                $("#updateModal .form input[name='id']").val( row.id );
                $("#updateModal .form input[name='userName']").val( row.userName );
                $("#updateModal .form input[name='password']").val( '' );
                $("#updateModal .form input[name='type'][value='" + row.type + "']").prop('checked', true);
            },
            readFormData: function() {
                return $("#updateModal .form").serialize();
            }
        });

        /**
         * 分配项目权限
          */
        $("#data_operation .permissionBiz").click(function(){
            // get select rows
            var rows = $.adminTable.table.bootstrapTable('getSelections');
            if (rows.length !== 1) {
                layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
                return;
            }
            var row = rows[0];

            // set field
            $("#updatePermissionBizModal .form input[name='id']").val( row.id );
            var permissionBizChoose;
            if (row.permissionBiz) {
                permissionBizChoose = $(row.permissionBiz.split(","));
            }
            $("#updatePermissionBizModal .form input[name='permissionBiz']").each(function () {
                if ( $.inArray($(this).val(), permissionBizChoose) > -1 ) {
                    $(this).prop("checked",true);
                } else {
                    $(this).prop("checked",false);
                }
            });

            $('#updatePermissionBizModal').modal('show');
        });
        $('#updatePermissionBizModal .ok').click(function () {
            $.post(base_url + "/user/updatePermissionBiz", $("#updatePermissionBizModal .form").serialize(), function(data, status) {
                if (data.code === 200) {
                    layer.open({
                        icon: '1',
                        content: '操作成功' ,
                        end: function(layero, index){
                            // refresh table
                            $('#data_filter .searchBtn').click();
                            $('#updatePermissionBizModal').modal('hide');
                        }
                    });
                } else {
                    layer.open({
                        icon: '2',
                        content: (data.msg||'操作失败')
                    });
                }
            });
        });
        $("#updatePermissionBizModal").on('hide.bs.modal', function () {
            $("#updatePermissionBizModal .form")[0].reset()
        });

    });

</script>
<!-- 3-script end -->

</body>
</html>