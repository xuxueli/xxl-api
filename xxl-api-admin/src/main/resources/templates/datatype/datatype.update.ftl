<html>
<head>
    <#-- import macro -->
    <#import "../common/common.macro.ftl" as netCommon>

    <!-- 1-style start -->
    <@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/select2/css/select2.min.css">
    <style>
        /* select2 */
        .select2-container--default .select2-selection--single {
            border: 1px solid #d2d6de;
            border-radius: 0;
            height: 34px;
            padding: 6px 12px;
        }
    </style>
    <!-- 1-style end -->

</head>
<body class="hold-transition" style="background-color: #ecf0f5;">
<div class="wrapper">
    <section class="content">

        <!-- 2-content start -->
        <form class="form-horizontal" id="datatypeForm" >
            <input type="hidden" name="id" value="${apiDataType.id}" >

            <#--基础信息-->
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">基础信息</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-default btn-xs" type="button"
                                onclick="javascript:openTab('${request.contextPath}/datatype/dataTypeDetail?dataTypeId=${apiDataType.id}', '查看数据类型', true)" >返回详情页</button>
                        <button class="btn btn-info btn-xs" type="submit" >保存数据类型</button>
                    </div>
                </div>

                <div class="box-body">
                    <div class="form-group">
                        <label class="col-sm-1 control-label">名称</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" name="name" placeholder="请输入数据类型名称" maxlength="100" value="${apiDataType.name}" >
                        </div>

                        <label class="col-sm-1 control-label">业务线</label>
                        <div class="col-sm-4">
                            <select class="form-control" name="bizId" >
                                <#if bizList?exists && bizList?size gt 0>
                                    <#list bizList as biz>
                                        <option value="${biz.id}" <#if apiDataType.bizId==biz.id>selected</#if> >${biz.bizName}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-1 control-label">描述</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" name="about" placeholder="请输入数据类型描述" maxlength="200" value="${apiDataType.about}" >
                        </div>
                    </div>

                </div>
            </div>

            <#-- 字段列表 -->
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">字段列表</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" id="queryParams_add" ><i class="fa fa-plus"></i></button>
                    </div>
                </div>

                <div id="queryParams_example" style="display: none;" >
                    <div class="form-group queryParams_item" >

                        <label class="col-sm-1 control-label">名称</label>
                        <div class="col-sm-2 item">
                            <input type="text" class="form-control fieldName">
                        </div>

                        <label class="col-sm-1 control-label">描述</label>
                        <div class="col-sm-2 item">
                            <input type="text" class="form-control fieldAbout">
                        </div>

                        <div class="col-sm-3 item">
                            <select class="form-control select2_tag_new fieldDatatypeId" style="width: 100%;">
                            </select>
                        </div>

                        <div class="col-sm-2 item">
                            <select class="form-control select2_tag_new fieldType" style="width: 100%;">
                                <option value="0">默认</option>
                                <option value="1">数组</option>
                            </select>
                        </div>

                        <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                    </div>
                </div>

                <div class="box-body" id="queryParams_parent" >
                    <#if apiDataType.fieldList?exists && apiDataType.fieldList?size gt 0>
                        <#list apiDataType.fieldList as field>
                            <div class="form-group queryParams_item" >

                                <label class="col-sm-1 control-label">名称</label>
                                <div class="col-sm-2 item">
                                    <input type="text" class="form-control fieldName" value="${field.fieldName}" >
                                </div>

                                <label class="col-sm-1 control-label">描述</label>
                                <div class="col-sm-2 item">
                                    <input type="text" class="form-control fieldAbout" value="${field.fieldAbout}" >
                                </div>

                                <div class="col-sm-3 item">
                                    <select class="form-control select2_tag_new fieldDatatypeId" style="width: 100%;">
                                        <option value="${field.fieldDatatypeId}">${field.fieldDatatype.name}</option>
                                    </select>
                                </div>

                                <div class="col-sm-2 item">
                                    <select class="form-control select2_tag_new fieldType" style="width: 100%;">
                                        <option value="0" <#if field.fieldType==0>selected</#if> >默认</option>
                                        <option value="1" <#if field.fieldType==1>selected</#if> >数组</option>
                                    </select>
                                </div>

                                <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                            </div>
                        </#list>
                    </#if>
                </div>

            </div>

        </form>

        <!-- 2-content end -->

    </section>
</div>

<!-- 3-script start -->
<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/bower_components/select2/js/select2.min.js"></script>
<#-- admin -->
<script src="${request.contextPath}/static/biz/common/admin.util.js"></script>
<script>
    $(function() {

        // base init
        $(".select2").select2();
        $(".select2_tag").select2({tags: true});

        function initSelectTag() {
            $("#queryParams_parent .select2_tag_new").each(function () {
                var $select2 = $(this);
                $($select2).removeClass('select2_tag_new');
                $($select2).addClass('select2_tag');

                if ($select2.hasClass("fieldDatatypeId")) {
                    // 数据类型 ajax 加载
                    $($select2).select2({
                        ajax: {
                            type:'POST',
                            url: base_url + "/datatype/pageList",
                            dataType: 'json',
                            delay: 250,
                            data: function (params) {
                                return {
                                    bizId: -1,
                                    offset:0,
                                    pagesize:100,
                                    name: params.term, // search term
                                    page: params.page
                                };
                            },
                            processResults: function (data, params) {
                                params.page = params.page || 1;

                                var itemList = [];//当数据对象不是{id:0,text:'ANTS'}这种形式的时候，可以使用类似此方法创建新的数组对象
                                var arr = data.data.data;
                                for(i in arr){
                                    itemList.push({id: arr[i].id, text: arr[i].name})
                                }
                                return {
                                    results: itemList,
                                    pagination: {
                                        more: (params.page * 30) < data.data.total
                                    }
                                };
                            },
                            cache: true
                        },
                        placeholder:'请选择数据类型',//默认文字提示
                        language: "zh-CN",
                        tags: false,//允许手动添加
                        allowClear: true,//允许清空
                        escapeMarkup: function (markup) { return markup; }, // 自定义格式化防止xss注入
                        minimumInputLength: 1,//最少输入多少个字符后开始查询
                        formatResult: function formatRepo(repo){return repo.text;}, // 函数用来渲染结果
                        formatSelection: function formatRepoSelection(repo){return repo.text;} // 函数用于呈现当前的选择
                    });
                } else {
                    $($select2).select2();
                }

            });
        }
        initSelectTag();

        /**
         * 请求参数，新增一行
         */
        $('#queryParams_add').click(function () {
            var html = $('#queryParams_example').html();
            $('#queryParams_parent').append(html);
            initSelectTag();
        });

        /**
         * 请求参数，删除一行
         */
        $('#queryParams_parent').on('click', '.delete',function () {
            $(this).parents('.queryParams_item').remove();
        });

        // jquery.validate 自定义校验 “长度1-100位的大小写字母、数字”
        jQuery.validator.addMethod("dataTypeName", function(value, element) {
            var length = value.length;
            var valid = /^[a-zA-Z0-9]{1,100}$/;
            return this.optional(element) || valid.test(value);
        }, "正确格式为：长度1-100位的大小写字母和数字组成");

        /**
         * 保存接口
         */
        var addModalValidate = $("#datatypeForm").validate({
            errorElement : 'span',
            errorClass : 'help-block',
            focusInvalid : true,
            rules : {
                name : {
                    required : true,
                    maxlength: 100,
                    dataTypeName:true
                },
                about : {
                    required : true,
                    maxlength: 200
                }
            },
            messages : {
                name : {
                    required :"请输入数据类型名称",
                    maxlength: "长度不可多余100"
                },
                about : {
                    required :"请输入数据类型描述",
                    maxlength: "长度不可多余200"
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

                // query params
                var ifError;
                var queryParamList = new Array();
                if ($('#queryParams_parent').find('.queryParams_item').length > 0) {
                    $('#queryParams_parent').find('.queryParams_item').each(function () {
                        var fieldName = $(this).find('.fieldName').val();
                        var fieldAbout = $(this).find('.fieldAbout').val();
                        var fieldDatatypeId = $(this).find('.fieldDatatypeId').val();
                        var fieldType = $(this).find('.fieldType').val();

                        // valid
                        if (!fieldName) {
                            ifError = true;
                            layer.open({
                                icon: '2',
                                content: '字段名称不可为空'
                            });
                            return;
                        }
                        if (!fieldAbout) {
                            ifError = true;
                            layer.open({
                                icon: '2',
                                content: '字段描述不可为空'
                            });
                            return;
                        }
                        if (!fieldDatatypeId) {
                            ifError = true;
                            layer.open({
                                icon: '2',
                                content: '字段数据类型不可为空'
                            });
                            return;
                        }

                        queryParamList.push({
                            'fieldName':fieldName,
                            'fieldAbout':fieldAbout,
                            'fieldDatatypeId':fieldDatatypeId,
                            'fieldType':fieldType
                        });

                    });
                }
                var fieldTypeJson = JSON.stringify(queryParamList);
                if (ifError) return;

                // final params
                var data = {
                    "id": $("#datatypeForm input[name=id]").val(),
                    "bizId": $("#datatypeForm select[name=bizId]").val(),
                    "name": $("#datatypeForm input[name=name]").val(),
                    "about": $("#datatypeForm input[name=about]").val(),
                    "fieldTypeJson":fieldTypeJson
                }

                $.post(base_url + "/datatype/updateDataType", data, function(data, status) {
                    if (data.code == "200") {
                        $('#addModal').modal('hide');
                        layer.open({
                            icon: '1',
                            content: "更新成功" ,
                            end: function(layero, index){
                                // 关闭当前，跳转详情页
                                let url = base_url + '/datatype/dataTypeDetail?dataTypeId=' + $("#datatypeForm input[name=id]").val();
                                openTab(url, '查看数据类型', true);

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


    });
</script>
<!-- 3-script end -->

</body>
</html>