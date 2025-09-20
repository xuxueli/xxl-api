<!DOCTYPE html>
<html>
<head>
    <#-- import macro -->
    <#import "../common/common.macro.ftl" as netCommon>

    <!-- 1-style start -->
    <@netCommon.commonStyle />
    <!-- 1-style end -->

</head>
<body class="hold-transition" style="background-color: #ecf0f5;">
<div class="wrapper">
    <section class="content">

        <!-- 2-content start -->

        <!-- 内容区域 -->
        <div class="row">
            <!-- 接口分组 -->
            <div class="col-md-3">
                <a class="btn btn-primary btn-block margin-bottom">${project.name}</a>
                <div class="box box-solid">
                    <div class="box-header with-border">
                        <h3 class="box-title">接口分组</h3>

                        <#if hasBizPermission>
                            <div class="box-tools">
                                <!-- 新增分组-->
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
                                </a>
                            </li>
                            <#if groupList?exists && groupList?size gt 0>
                                <#list groupList as group>
                                    <li <#if groupId == group.id >class="active"</#if> groupId="${group.id}" >
                                        <a href="${request.contextPath}/group?projectId=${projectId}&groupId=${group.id}" >
                                            <i class="fa fa-inbox"></i>${group.name}
                                        </a>
                                    </li>
                                </#list>
                            </#if>
                        </ul>
                    </div>
                </div>
            </div>

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
                            <!-- 分组操作 -->
                            <#if groupInfo?exists>&nbsp;&nbsp;
                                <button class="btn btn-warning btn-xs" type="button" id="updateGroup" >编辑分组</button>
                                <button class="btn btn-danger btn-xs" type="button" id="deleteGroup" _id="${groupInfo.id}" _projectId="${groupInfo.projectId}" >删除分组</button>
                                ｜
                            </#if>
                            <!-- 新增接口 -->
                            <button type="button" class="btn btn-info btn-xs" onclick="javascript:openTab('${request.contextPath}/document/addPage?projectId=${projectId}&groupId=${groupId}', '新建接口', false)"  >+新增接口</button>
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
                                                    <!-- 查看接口详情 -->
                                                    <a onclick="javascript:openTab('${request.contextPath}/document/detailPage?id=${document.id}', '查看接口信息', false)" style="cursor: pointer;" >
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
                                                        <button class="btn btn-warning btn-xs" onclick="javascript:openTab('${request.contextPath}/document/updatePage?id=${document.id}', '修改接口信息', false)" >编辑</button>
                                                        <button class="btn btn-danger btn-xs deleteDocument" _id="${document.id}" _name="${document.name}" >删除</button>
                                                        <button class="btn btn-primary btn-xs " onclick="javascript:openTab('${request.contextPath}/test?documentId=${document.id}', '接口功能测试', false)" >测试</button>
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

        <!-- 2-content end -->

    </section>
</div>

<!-- 3-script start -->
<@netCommon.commonScript />
<#-- admin -->
<script src="${request.contextPath}/static/biz/common/admin.util.js"></script>
<script>
    $(function() {

        // ---------------------- group ----------------------

        /**
         * 新增，分组
         */
        $("#addGroup").click(function(){
            $('#addGroupModal').modal({backdrop: false, keyboard: false}).modal('show');
        });
        var addGroupModalValidate = $("#addGroupModal .form").validate({
            errorElement : 'span',
            errorClass : 'help-block',
            focusInvalid : true,
            rules : {
                name : {
                    required : true,
                    minlength: 2,
                    maxlength: 12
                },
                order : {
                    required : true,
                    digits: true,
                    min:1,
                    max:1000
                }
            },
            messages : {
                name : {
                    required :"请输入“分组名称”",
                    minlength: "长度不可少于2",
                    maxlength: "长度不可多余12"
                },
                order : {
                    required :"请输入“分组排序”",
                    digits: "只能输入整数",
                    min: "输入值不能小于1",
                    max: "输入值不能大于1000"
                }
            },
            highlight : function(element) {
                $(element).closest('.form-group').addClass('has-error');
            },
            success : function(label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },
            errorPlacement : function(error, element) {
                element.parent('div').append(error);
            },
            submitHandler : function(form) {
                $.post(base_url + "/group/add",  $("#addGroupModal .form").serialize(), function(data, status) {
                    if (data.code == "200") {
                        $('#addGroupModal').modal('hide');
                        layer.open({
                            icon: '1',
                            content: "新增成功" ,
                            end: function(layero, index){
                                window.location.reload();
                            }
                        });
                    } else {
                        layer.open({
                            icon: '2',
                            content: (data.msg||'新增失败')
                        });
                    }
                });
            }
        });
        $("#addGroupModal").on('hide.bs.modal', function () {
            $("#addGroupModal .form")[0].reset();
            addGroupModalValidate.resetForm();
            $("#addGroupModal .form .form-group").removeClass("has-error");
            $(".remote_panel").show();	// remote
        });

        /**
         * 更新，分组
         */
        $("#updateGroup").click(function(){
            $('#updateGroupModal').modal({backdrop: false, keyboard: false}).modal('show');
        });
        var updateGroupModalValidate = $("#updateGroupModal .form").validate({
            errorElement : 'span',
            errorClass : 'help-block',
            focusInvalid : true,
            rules : {
                name : {
                    required : true,
                    minlength: 2,
                    maxlength: 12
                },
                order : {
                    required : true,
                    digits: true,
                    min:1,
                    max:1000
                }
            },
            messages : {
                name : {
                    required :"请输入“分组名称”",
                    minlength: "长度不可少于2",
                    maxlength: "长度不可多余12"
                },
                order : {
                    required :"请输入“分组排序”",
                    digits: "只能输入整数",
                    min: "输入值不能小于1",
                    max: "输入值不能大于1000"
                }
            },
            highlight : function(element) {
                $(element).closest('.form-group').addClass('has-error');
            },
            success : function(label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },
            errorPlacement : function(error, element) {
                element.parent('div').append(error);
            },
            submitHandler : function(form) {
                $.post(base_url + "/group/update",  $("#updateGroupModal .form").serialize(), function(data, status) {
                    if (data.code == "200") {
                        $('#updateGroupModal').modal('hide');
                        layer.open({
                            icon: '1',
                            content: "更新成功" ,
                            end: function(layero, index){
                                window.location.reload();
                            }
                        });
                    } else {
                        layer.open({
                            icon: '2',
                            content: (data.msg||'更新失败')
                        });
                    }
                });
            }
        });
        $("#updateGroupModal").on('hide.bs.modal', function () {
            $("#updateGroupModal .form")[0].reset();
            updateGroupModalValidate.resetForm();
            $("#updateGroupModal .form .form-group").removeClass("has-error");
            $(".remote_panel").show();	// remote
        });

        /**
         * 删除分组
         */
        $("#deleteGroup").click(function(){
            var id = $(this).attr("_id");
            var projectId = $(this).attr("_projectId");

            layer.confirm( "确认删除该接口分组?" , {
                icon: 3,
                title: '系统提示' ,
                btn: [ '确定', '取消' ]
            }, function(index){
                layer.close(index);

                $.ajax({
                    type : 'POST',
                    url : base_url + "/group/delete",
                    data : {
                        "id" : id
                    },
                    dataType : "json",
                    success : function(data){
                        if (data.code == 200) {
                            layer.open({
                                icon: '1',
                                content: "删除成功" ,
                                end: function(layero, index){
                                    window.location.href = base_url + '/group?projectId=' + projectId;
                                }
                            });
                        } else {
                            layer.open({
                                icon: '2',
                                content: (data.msg||'删除失败')
                            });
                        }
                    },
                });
            });

        });

        /**
         * 关键字搜索
         */
        $("#searchUrl").bind('input porpertychange',function(){
            var searchUrl = $("#searchUrl").val();
            if (searchUrl) {
                searchUrl = searchUrl.toLowerCase();
            }
            $('#documentList').find('tbody tr').each(function(){
                var requestUrl = $(this).attr('requestUrl').toLowerCase();
                if (searchUrl) {
                    if (requestUrl.indexOf(searchUrl) != -1) {
                        $(this).show();
                    } else {
                        $(this).hide();
                    }
                } else {
                    $(this).show();
                }
            });

        });

        // ---------------------- document ----------------------

        /**
         * 标记星级，星级较高，排序靠前
         */
        $(".markStar").click(function(){
            var $this = $(this);
            var id = $($this).attr("_id");
            var _starLevel = $($this).attr("_starLevel");

            // 星标等级：0-普通接口、1-一星接口
            var toStarLevel;
            var toStarHtm;
            if (_starLevel == 1) {
                toStarLevel = 0;
                toStarHtm = '<i class="fa fa-star-o text-yellow"></i>';
            } else {
                toStarLevel = 1;
                toStarHtm = '<i class="fa fa-star text-yellow"></i>';
            }

            $.ajax({
                type : 'POST',
                url : base_url + "/document/markStar",
                data : {
                    "id" : id,
                    "starLevel":toStarLevel
                },
                dataType : "json",
                success : function(data){
                    if (data.code == 200) {
                        $($this).attr("_starLevel", toStarLevel);
                        $($this).html(toStarHtm);
                    } else {
                        layer.open({
                            icon: '2',
                            content: (data.msg||'操作失败')
                        });
                    }
                },
            });

        });

        /**
         * 删除接口
         */
        $(".deleteDocument").click(function(){
            var id = $(this).attr("_id");
            var name = $(this).attr("_name");

            layer.confirm( "确认删除该接口["+name+"]，将会删除该接口下测试记录和Mock数据?" , {
                icon: 3,
                title: '系统提示' ,
                btn: [ '确定', '取消' ]
            }, function(index){
                layer.close(index);

                $.ajax({
                    type : 'POST',
                    url : base_url + "/document/delete",
                    data : {
                        "id" : id
                    },
                    dataType : "json",
                    success : function(data){
                        if (data.code == 200) {
                            layer.open({
                                icon: '1',
                                content: "删除成功" ,
                                end: function(layero, index){
                                    window.location.reload();
                                }
                            });
                        } else {
                            layer.open({
                                icon: '2',
                                content: (data.msg||'删除失败')
                            });
                        }
                    },
                });
            });

        });

    });
</script>
<!-- 3-script end -->

</body>
</html>