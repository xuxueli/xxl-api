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
                            <select class="form-control" id="bizId">
                                <option value="-1" >全部</option>
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
                        <button class="btn btn-sm btn-info add" type="button"><i class="fa fa-plus" ></i>${I18n.system_opt_add}</button>
                        <button class="btn btn-sm btn-warning selectOnlyOne update" type="button"><i class="fa fa-edit"></i>${I18n.system_opt_edit}</button>
                        <button class="btn btn-sm btn-danger selectAny delete" type="button"><i class="fa fa-remove "></i>${I18n.system_opt_del}</button>
                        <button class="btn btn-sm btn-primary selectOnlyOne enterProject" type="button"> 进入项目</button>
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
                        <h4 class="modal-title" >新增项目</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal form" role="form" >
                            <div class="form-group">
                                <label for="lastname" class="col-sm-4 control-label">业务线<font color="red">*</font></label>
                                <div class="col-sm-8">
                                    <select class="form-control" name="bizId" >
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
                                <label for="lastname" class="col-sm-4 control-label">根地址(线上)<font color="red">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlProduct" placeholder="请输入根地址(线上)" maxlength="200" ></div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-4 control-label">根地址(预发布)<font color="black">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlPpe" placeholder="请输入根地址(预发布)" maxlength="200" ></div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-4 control-label">根地址(测试)<font color="black">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlQa" placeholder="请输入根地址(测试)" maxlength="200" ></div>
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
                        <h4 class="modal-title" >更新项目</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal form" role="form" >
                            <div class="form-group">
                                <label for="lastname" class="col-sm-4 control-label">业务线<font color="red">*</font></label>
                                <div class="col-sm-8">
                                    <select class="form-control" name="bizId" >
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
                                <label for="lastname" class="col-sm-4 control-label">根地址(线上)<font color="red">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlProduct" placeholder="请输入根地址(线上)" maxlength="200" ></div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-4 control-label">根地址(预发布)<font color="black">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlPpe" placeholder="请输入根地址(预发布)" maxlength="200" ></div>
                            </div>
                            <div class="form-group">
                                <label for="lastname" class="col-sm-4 control-label">根地址(测试)<font color="black">*</font></label>
                                <div class="col-sm-8"><input type="text" class="form-control" name="baseUrlQa" placeholder="请输入根地址(测试)" maxlength="200" ></div>
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

        /**
         * 业务线切换
         */
        /*$('#bizId').change(function () {
            var bizId = $('#bizId').val();
            window.location.href = base_url + "/project?bizId=" + bizId;
        });*/


        /**
         * 进入项目
         */
        $("#data_operation").on('click', '.enterProject',function() {
            // get select rows
            var rows = $.adminTable.table.bootstrapTable('getSelections');
            if (rows.length !== 1) {
                layer.msg(I18n.system_please_choose + I18n.system_one + I18n.system_data);
                return;
            }
            let row = rows[0];

            // open tab
            let url = base_url + '/group?projectId='+ row.id;
            openTab(url, "项目接口列表", false);
        });

        // ---------- ---------- ---------- table + curd  ---------- ---------- ----------

        /**
         * init table
         */
        $.adminTable.initTable({
            table: '#data_list',
            url: base_url + "/project/pageList",
            queryParams: function (params) {
                var obj = {};
                obj.bizId = $('#bizId').val();
                obj.name = $('#name').val();
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
                    title: '项目名称',
                    field: 'name',
                    width: '30',
                    widthUnit: '%',
                    align: 'left'
                },{
                    title: '项目描述',
                    field: 'desc',
                    width: '70',
                    widthUnit: '%',
                    align: 'left'
                }
            ]
        });

        /**
         * init delete
         */
        $.adminTable.initDelete({
            url: base_url + "/project/delete"
        });

        /**
         * init add
         */
        $.adminTable.initAdd( {
            url: base_url + "/project/add",
            rules : {
                name : {
                    required : true,
                    rangelength: [4, 50]
                },
                baseUrlProduct : {
                    required : true,
                    rangelength: [4, 200]
                }
            },
            messages : {
                name : {
                    required :"请输入项目名称",
                    rangelength : "长度限制为4~50"
                },
                baseUrlProduct : {
                    required :"请输入根地址(线上)",
                    rangelength : "长度限制为4~200"
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
            url: base_url + "/project/update",
            writeFormData: function(row) {

                // base data
                $("#updateModal .form input[name='id']").val( row.id );
                $("#updateModal .form input[name='name']").val( row.name );
                $("#updateModal .form input[name='desc']").val( row.desc );
                $("#updateModal .form input[name='baseUrlProduct']").val( row.baseUrlProduct );
                $("#updateModal .form input[name='baseUrlPpe']").val( row.baseUrlPpe );
                $("#updateModal .form input[name='baseUrlQa']").val( row.baseUrlQa );
                $("#updateModal .form select[name='bizId']").find("option[value='"+ row.bizId +"']").attr("selected",true);

            },
            rules : {
                name : {
                    required : true,
                    rangelength: [4, 50]
                },
                baseUrlProduct : {
                    required : true,
                    rangelength: [4, 200]
                }
            },
            messages : {
                name : {
                    required :"请输入项目名称",
                    rangelength : "长度限制为4~50"
                },
                baseUrlProduct : {
                    required :"请输入根地址(线上)",
                    rangelength : "长度限制为4~200"
                }
            },
            readFormData: function() {
                return $("#updateModal .form").serialize();
            }
        });

    });
</script>
<!-- 3-script end -->

</body>
</html>