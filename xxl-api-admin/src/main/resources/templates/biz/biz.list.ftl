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
                            <span class="input-group-addon">业务线名称</span>
                            <input type="text" class="form-control" id="bizName" autocomplete="on" >
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
                        <h4 class="modal-title" >新增业务线</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal form" role="form" >
                            <div class="form-group">
                                <label for="lastname" class="col-sm-3 control-label">业务线名称<font color="red">*</font></label>
                                <div class="col-sm-9"><input type="text" class="form-control" name="bizName" placeholder="请输入“业务线名称”" maxlength="50" ></div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-3 control-label">排序<font color="red">*</font></label>
                                <div class="col-sm-9"><input type="text" class="form-control" name="order" placeholder="请输入“排序”" maxlength="5" ></div>
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
            <div class="modal-dialog modal-sm2">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" >更新业务线</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal form" role="form" >
                            <div class="form-group">
                                <label for="lastname" class="col-sm-3 control-label">业务线名称<font color="red">*</font></label>
                                <div class="col-sm-9"><input type="text" class="form-control" name="bizName" placeholder="请输入“业务线名称”" maxlength="50" ></div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-3 control-label">排序<font color="red">*</font></label>
                                <div class="col-sm-9"><input type="text" class="form-control" name="order" placeholder="请输入“排序”" maxlength="5" ></div>
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
            url: base_url + "/biz/pageList",
            queryParams: function (params) {
                var obj = {};
                obj.bizName = $('#bizName').val();
                obj.start = params.offset;
                obj.length = params.limit;
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
                    title: '业务线',
                    field: 'bizName',
                    width: '50',
                    widthUnit: '%',
                    align: 'left'
                },{
                    title: '排序',
                    field: 'order',
                    width: '30',
                    widthUnit: '%',
                    align: 'left'
                }
            ]
        });

        /**
         * init delete
         */
        $.adminTable.initDelete({
            url: base_url + "/biz/delete"
        });

        // jquery.validate 自定义校验 “英文字母开头，只含有英文字母、数字和下划线”
        jQuery.validator.addMethod("userNameValid", function(value, element) {
            var length = value.length;
            var valid = /^[a-zA-Z][a-zA-Z0-9_]*$/;
            return this.optional(element) || valid.test(value);
        }, "只支持英文字母开头，只含有英文字母、数字和下划线");

        /**
         * init add
         */
        $.adminTable.initAdd( {
            url: base_url + "/biz/add",
            rules : {
                bizName : {
                    required : true
                },
                order : {
                    required : true,
                    digits: true,
                    min:1,
                    max:1000
                }
            },
            messages : {
                bizName : {
                    required :"请输入“业务线名称”"
                },
                order : {
                    required :"请输入“排序”",
                    digits: "只能输入整数",
                    min: "输入值不能小于1",
                    max: "输入值不能大于1000"
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
            url: base_url + "/biz/update",
            writeFormData: function(row) {

                // base data
                $("#updateModal .form input[name='id']").val( row.id );
                $("#updateModal .form input[name='bizName']").val( row.bizName );
                $("#updateModal .form input[name='order']").val( row.order );
            },
            rules : {
                bizName : {
                    required : true
                },
                order : {
                    required : true,
                    digits: true,
                    min:1,
                    max:1000
                }
            },
            messages : {
                bizName : {
                    required :"请输入“业务线名称”"
                },
                order : {
                    required :"请输入“排序”",
                    digits: "只能输入整数",
                    min: "输入值不能小于1",
                    max: "输入值不能大于1000"
                }
            },
            readFormData: function() {
                return $("#updateModal .form").serialize();
            }
        });

        /*
        // 新增-添加参数
        $("#addModal .addParam").on('click', function () {
            var html = '<div class="form-group newParam">'+
                    '<label for="lastname" class="col-sm-2 control-label">参数&nbsp;<button class="btn btn-danger btn-xs removeParam" type="button">移除</button></label>'+
                    '<div class="col-sm-4"><input type="text" class="form-control" name="key" placeholder="请输入参数key[将会强转为String]" maxlength="200" /></div>'+
                    '<div class="col-sm-6"><input type="text" class="form-control" name="value" placeholder="请输入参数value[将会强转为String]" maxlength="200" /></div>'+
                '</div>';
            $(this).parents('.form-group').parent().append(html);

            $("#addModal .removeParam").on('click', function () {
                $(this).parents('.form-group').remove();
            });
        });
        */

    });

</script>
<!-- 3-script end -->

</body>
</html>
