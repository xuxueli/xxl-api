<!DOCTYPE html>
<html>
<head>
    <#-- import macro -->
    <#import "../common/common.macro.ftl" as netCommon>

    <!-- 1-style start -->
    <@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/select2/css/select2.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
    <link rel="stylesheet" href="${request.contextPath}/static/plugins/editor.md-1.5.0/main/editormd.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/plugins/jsontree/jquery.jsonview.css">
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

        <form class="form-horizontal" id="ducomentForm" >
            <input type="hidden" name="id" value="${document.id}" >
            <input type="hidden" name="projectId" value="${document.projectId}" >

            <#--基础信息-->
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">基础信息</h3>
                    <div class="box-tools pull-right">
                        <#--<button class="btn btn-default btn-xs" type="button" onclick="javascript:openTab('${request.contextPath}/group?projectId=${projectId}', '项目接口列表', true)" >返回接口列表</button>-->
                        <button class="btn btn-default btn-xs" type="button" onclick="javascript:openTab('${request.contextPath}/document/detailPage?id=${document.id}', '查看接口信息', true)" >接口详情页</button>
                        <button class="btn btn-info btn-xs" type="submit" >更新接口</button>
                    </div>
                </div>

                <div class="box-body">
                    <div class="form-group">
                        <label class="col-sm-1 control-label">URL</label>
                        <div class="col-sm-6">
                            <input type="text" class="form-control" name="requestUrl" value="${document.requestUrl}" placeholder="请输入接口URL（相对地址）" maxlength="100" >
                        </div>
                        <label class="col-sm-1 control-label">分组</label>
                        <div class="col-sm-4">
                            <select class="form-control select2" style="width: 100%;" name="groupId">
                                <option value="0" <#if 0 == document.groupId>selected</#if> >默认分组</option>
                                <#if groupList?exists && groupList?size gt 0>
                                    <#list groupList as group>
                                        <option value="${group.id}" <#if group.id == document.groupId>selected</#if> >${group.name}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-1 control-label">Method</label>
                        <div class="col-sm-6">
                            <select class="form-control select2" style="width: 100%;" name="requestMethod">
                                <#list RequestMethodEnum as item>
                                    <option value="${item}" <#if item == document.requestMethod>selected</#if> >${item}</option>
                                </#list>
                            </select>
                        </div>
                        <label class="col-sm-1 control-label">状态</label>
                        <div class="col-sm-4">
                            <input type="radio" class="iCheck" name="status" value="0" <#if 0 == document.status>checked</#if> >启用  &nbsp;&nbsp;
                            <input type="radio" class="iCheck" name="status" value="1" <#if 1 == document.status>checked</#if> >维护  &nbsp;&nbsp;
                            <input type="radio" class="iCheck" name="status" value="2" <#if 2 == document.status>checked</#if> >废弃
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-1 control-label">名称</label>
                        <div class="col-sm-11">
                            <input type="text" class="form-control" name="name" value="${document.name}" placeholder="请输入接口名称" maxlength="50" >
                        </div>
                    </div>
                </div>
            </div>

            <#--请求头部-->
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">请求头部</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" id="requestHeaders_add" ><i class="fa fa-plus"></i></button>
                    </div>
                </div>

                <div id="requestHeaders_example" style="display: none;" >
                    <div class="form-group requestHeaders_item" >
                        <label class="col-sm-1 control-label">Key</label>
                        <div class="col-sm-4 item">
                            <select class="form-control select2_tag_new key" >
                                <option value=""></option>
                                <#list requestHeadersEnum as item>
                                    <option value="${item}">${item}</option>
                                </#list>
                            </select>
                        </div>
                        <label class="col-sm-1 control-label">Value</label>
                        <div class="col-sm-5 item">
                            <input type="text" class="form-control value">
                        </div>
                        <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                    </div>
                </div>

                <div class="box-body" id="requestHeaders_parent" >
                    <#if requestHeadersList?exists >
                        <#list requestHeadersList as requestHeadersMap>
                            <#assign key = requestHeadersMap['key'] />
                            <#assign value = requestHeadersMap['value'] />
                            <div class="form-group requestHeaders_item" >
                                <label class="col-sm-1 control-label">Key</label>
                                <div class="col-sm-4 item">
                                    <select class="form-control select2_tag key" >
                                        <option value="" <#if key?exists>selected</#if> ></option>
                                        <#list requestHeadersEnum as item>
                                            <option value="${item}" <#if key==item>selected</#if> >${item}</option>
                                        </#list>
                                    </select>
                                </div>
                                <label class="col-sm-1 control-label">Value</label>
                                <div class="col-sm-5 item">
                                    <input type="text" class="form-control value" value="${value}" >
                                </div>
                                <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>

            <#--请求参数-->
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">请求参数</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" id="queryParams_add" ><i class="fa fa-plus"></i></button>
                    </div>
                </div>

                <div id="queryParams_example" style="display: none;" >
                    <div class="form-group queryParams_item" >
                        <label class="col-sm-1 control-label">参数</label>
                        <div class="col-sm-2 item">
                            <input type="text" class="form-control name">
                        </div>
                        <label class="col-sm-1 control-label">说明</label>
                        <div class="col-sm-2 item">
                            <input type="text" class="form-control desc">
                        </div>
                        <label class="col-sm-1 control-label">类型</label>
                        <div class="col-sm-2 item">
                            <select class="form-control select2_tag_new type" style="width: 100%;">
                            <#list QueryParamTypeEnum as item>
                                <option value="${item}">${item}</option>
                            </#list>
                            </select>
                        </div>
                        <div class="col-sm-2 item">
                            <select class="form-control select2_tag_new notNull" style="width: 100%;">
                                <option value="true">必填</option>
                                <option value="false">非必填</option>
                            </select>
                        </div>

                        <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                    </div>
                </div>

                <div class="box-body" id="queryParams_parent" >
                    <#if queryParamList?exists>
                        <#list queryParamList as queryParam>
                            <div class="form-group queryParams_item" >
                                <label class="col-sm-1 control-label">参数</label>
                                <div class="col-sm-2 item">
                                    <input type="text" class="form-control name" value="${queryParam.name}" >
                                </div>
                                <label class="col-sm-1 control-label">说明</label>
                                <div class="col-sm-2 item">
                                    <input type="text" class="form-control desc" value="${queryParam.desc}" >
                                </div>
                                <label class="col-sm-1 control-label">类型</label>
                                <div class="col-sm-2 item">
                                    <select class="form-control select2_tag type" style="width: 100%;">
                                        <#list QueryParamTypeEnum as item>
                                            <option value="${item}" <#if queryParam.type == item>selected</#if> >${item}</option>
                                        </#list>
                                    </select>
                                </div>
                                <div class="col-sm-2 item">
                                    <select class="form-control select2_tag notNull" style="width: 100%;">
                                        <option value="true" <#if queryParam.notNull == "true" >selected</#if> >必填</option>
                                        <option value="false" <#if queryParam.notNull == "false" >selected</#if> >非必填</option>
                                    </select>
                                </div>

                                <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>

            <#--响应数据类型-->
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">响应数据类型</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-info btn-xs" type="button" onclick="javascript:openTab('${request.contextPath}/datatype/addDataTypePage', '新增数据类型', false)" >+ 新增数据类型</button>
                    </div>
                </div>
                <div class="box-body" >

                    <label class="col-sm-2 control-label">数据类型</label>
                    <div class="col-sm-4 item">
                        <select class="form-control" style="width: 100%;" id="responseDatatypeId" name="responseDatatypeId"  >
                            <#if responseDatatype?exists>
                                <option value="${responseDatatype.id}">${responseDatatype.name}</option>
                            </#if>
                        </select>
                    </div>

                </div>
            </div>

            <#--响应结果-->
            <div class="nav-tabs-custom">
                <!-- Tabs within a box -->
                <ul class="nav nav-tabs pull-right">
                    <li><a href="#fail_resp" data-toggle="tab">失败响应结果</a></li>
                    <li class="active"><a href="#success_resp" data-toggle="tab">成功响应结果</a></li>
                    <li class="pull-left header">响应结果</li>
                </ul>
                <div class="tab-content no-padding">
                    <!-- Morris chart - Sales -->
                    <div class="chart tab-pane active" id="success_resp" style="position: relative; height: 365px;">
                        <div class="box-body">
                            响应数据类型(MIME)：
                            <#list ResponseContentType as item>
                                <input type="radio" class="iCheck" name="successRespType" value="${item}" <#if document.successRespType==item>checked</#if> >${item}  &nbsp;&nbsp;
                            </#list>
                            <button type="button" class="btn btn-box-tool pull-right" id="successRespExample_2json" >JSON格式化</button>
                            <br>
                            <textarea name="successRespExample" id="successRespExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;margin-top: 15px;" >${document.successRespExample}</textarea>
                        </div>
                    </div>
                    <div class="chart tab-pane" id="fail_resp" style="position: relative; height: 365px;">
                        <div class="box-body">
                            响应数据类型(MIME)：
                            <#list ResponseContentType as item>
                                <input type="radio" class="iCheck" name="failRespType" value="${item}" <#if document.failRespType==item>checked</#if> >${item}  &nbsp;&nbsp;
                            </#list>
                            <button type="button" class="btn btn-box-tool pull-right" id="failRespExample_2json" >JSON格式化</button>
                            <br>
                            <textarea name="failRespExample" id="failRespExample"  style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;margin-top: 15px;" >${document.failRespExample}</textarea>
                        </div>
                    </div>
                </div>
            </div>

            <#--响应结果参数-->
            <#--<div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">响应结果参数</h3>
                    <div class="box-tools pull-right">
                        <button type="button" class="btn btn-box-tool" id="responseParams_add" ><i class="fa fa-plus"></i></button>
                    </div>
                </div>

                <div id="responseParams_example" style="display: none;" >
                    <div class="form-group responseParams_item" >
                        <div class="col-sm-2 item">
                            <select class="form-control select2_tag_new notNull" style="width: 100%;">
                                <option value="true">非空</option>
                                <option value="false">可空</option>
                            </select>
                        </div>
                        <label class="col-sm-1 control-label">参数类型</label>
                        <div class="col-sm-2 item">
                            <select class="form-control select2_tag_new type" style="width: 100%;">
                            <#list QueryParamTypeEnum as item>
                                <option value="${item}">${item}</option>
                            </#list>
                            </select>
                        </div>
                        <label class="col-sm-1 control-label">参数名称</label>
                        <div class="col-sm-2 item">
                            <input type="text" class="form-control name">
                        </div>
                        <label class="col-sm-1 control-label">参数说明</label>
                        <div class="col-sm-2 item">
                            <input type="text" class="form-control desc">
                        </div>
                        <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                    </div>
                </div>

                <div class="box-body" id="responseParams_parent" >
                    <#if responseParamList?exists>
                        <#list responseParamList as responseParam>
                            <div class="form-group responseParams_item" >
                                <div class="col-sm-2 item">
                                    <select class="form-control select2_tag notNull" style="width: 100%;">
                                        <option value="true" <#if responseParam.notNull == "true" >selected</#if> >必填</option>
                                        <option value="false" <#if responseParam.notNull == "false" >selected</#if> >非必填</option>
                                    </select>
                                </div>
                                <label class="col-sm-1 control-label">参数类型</label>
                                <div class="col-sm-2 item">
                                    <select class="form-control select2_tag type" style="width: 100%;">
                                        <#list QueryParamTypeEnum as item>
                                            <option value="${item}" <#if responseParam.type == item>selected</#if> >${item}</option>
                                        </#list>
                                    </select>
                                </div>
                                <label class="col-sm-1 control-label">参数名称</label>
                                <div class="col-sm-2 item">
                                    <input type="text" class="form-control name" value="${responseParam.name}" >
                                </div>
                                <label class="col-sm-1 control-label">参数说明</label>
                                <div class="col-sm-2 item">
                                    <input type="text" class="form-control desc" value="${responseParam.desc}" >
                                </div>
                                <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                            </div>
                        </#list>
                    </#if>
                </div>
            </div>-->

            <#-- 接口备注 -->
            <div class="box box-primary">
                <div class="box-header with-border">
                    <h3 class="box-title">接口备注</h3>
                </div>
                <div class="box-body" >
                    <div class="box-body pad" id="remark" ><textarea style="display:none;">${document.remark}</textarea></div>
                </div>
            </div>

        </form>

        <!-- 2-content end -->

    </section>
</div>


<!-- 3-script start -->
<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/bower_components/select2/js/select2.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor.md-1.5.0/main/editormd.min.js"></script>
<script src="${request.contextPath}/static/plugins/jsontree/jquery.jsonview.js"></script>
<#-- admin -->
<script src="${request.contextPath}/static/biz/common/admin.util.js"></script>
<script>
    $(function() {

        // base init
        $(".select2").select2();
        $(".select2_tag").select2({tags: true});

        $('.iCheck').iCheck({
            labelHover : false,
            cursor : true,
            checkboxClass : 'icheckbox_square-blue',
            radioClass : 'iradio_square-blue',
            increaseArea : '20%'
        });

        var remarkEditor = editormd("remark", {
            width   : "100%",
            height  : 550,
            syncScrolling : "single",
            path    : base_url + "/static/plugins/editor.md-1.5.0/lib/",
            autoFocus:false,
            //markdown : "",
            toolbarIcons : function() {
                // Or return editormd.toolbarModes[name]; // full, simple, mini
                return editormd.toolbarModes['simple'];
                // Using "||" set icons align right.
                //return ["undo", "redo", "|", "bold", "hr", "|", "preview", "watch", "|", "fullscreen", "info", "testIcon", "testIcon2", "file", "faicon", "||", "watch", "fullscreen", "preview", "testIcon"]
            },
            placeholder      : "请输入备注"
        });


        /**
         * 请求头部，新增一行
         */
        $('#requestHeaders_add').click(function () {
            var html = $('#requestHeaders_example').html();
            $('#requestHeaders_parent').append(html);

            $("#requestHeaders_parent .select2_tag_new").each(function () {
                var $select2 = $(this);
                $($select2).removeClass('select2_tag_new');
                $($select2).addClass('select2_tag');
                $($select2).select2({tags: true});
            });
        });
        /**
         * 请求头部，删除一行
         */
        $('#requestHeaders_parent').on('click', '.delete',function () {
            $(this).parents('.requestHeaders_item').remove();
        });

        /**
         * 请求参数，新增一行
         */
        $('#queryParams_add').click(function () {
            var html = $('#queryParams_example').html();
            $('#queryParams_parent').append(html);

            $("#queryParams_parent .select2_tag_new").each(function () {
                var $select2 = $(this);
                $($select2).removeClass('select2_tag_new');
                $($select2).addClass('select2_tag');
                $($select2).select2();
            });
        });
        /**
         * 请求参数，删除一行
         */
        $('#queryParams_parent').on('click', '.delete',function () {
            $(this).parents('.queryParams_item').remove();
        });

        /**
         * 响应结果参数，新增一行
         */
        $('#responseParams_add').click(function () {
            var html = $('#responseParams_example').html();
            $('#responseParams_parent').append(html);

            $("#responseParams_parent .select2_tag_new").each(function () {
                var $select2 = $(this);
                $($select2).removeClass('select2_tag_new');
                $($select2).addClass('select2_tag');
                $($select2).select2();
            });
        });
        /**
         * 响应结果参数，删除一行
         */
        $('#responseParams_parent').on('click', '.delete',function () {
            $(this).parents('.responseParams_item').remove();
        });


        $('#responseDatatypeId').select2({
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
            placeholder:'请选择',//默认文字提示
            language: "zh-CN",
            tags: false,//允许手动添加
            allowClear: true,//允许清空
            escapeMarkup: function (markup) { return markup; }, // 自定义格式化防止xss注入
            minimumInputLength: 1,//最少输入多少个字符后开始查询
            formatResult: function formatRepo(repo){return repo.text;}, // 函数用来渲染结果
            formatSelection: function formatRepoSelection(repo){return repo.text;} // 函数用于呈现当前的选择
        });

        /**
         * 保存接口
         */
        var addModalValidate = $("#ducomentForm").validate({
            errorElement : 'span',
            errorClass : 'help-block',
            focusInvalid : true,
            rules : {
                requestUrl : {
                    required : true,
                    maxlength: 50
                },
                name : {
                    required : true,
                    minlength: 3,
                    maxlength: 50
                }
            },
            messages : {
                requestUrl : {
                    required :"请输入“接口URL”",
                    maxlength: "长度不可多余100"
                },
                name : {
                    required :"请输入“接口名称”",
                    minlength: "长度不可少于3",
                    maxlength: "长度不可多余50"
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

                // getMarkdown();、getHTML();、getPreviewedHTML();
                var remark = remarkEditor.getMarkdown();

                // request headers
                var requestHeaderList = new Array();
                if ($('#requestHeaders_parent').find('.requestHeaders_item').length > 0) {
                    $('#requestHeaders_parent').find('.requestHeaders_item').each(function () {
                        var key = $(this).find('.key').val();
                        var value = $(this).find('.value').val();
                        if (key) {
                            requestHeaderList.push({
                                'key':key,
                                'value':value
                            });
                        } else {
                            if (value) {
                                layer.open({
                                    icon: '2',
                                    content: '新增接口失败，请检查"请求头部"数据是否填写完整'
                                });
                                return;
                            }
                        }
                    });
                }
                var requestHeaders = JSON.stringify(requestHeaderList);

                // query params
                var queryParamList = new Array();
                if ($('#queryParams_parent').find('.queryParams_item').length > 0) {
                    $('#queryParams_parent').find('.queryParams_item').each(function () {
                        var notNull = $(this).find('.notNull').val();
                        var type = $(this).find('.type').val();
                        var name = $(this).find('.name').val();
                        var desc = $(this).find('.desc').val();
                        if (name) {
                            queryParamList.push({
                                'notNull':notNull,
                                'type':type,
                                'name':name,
                                'desc':desc
                            });
                        } else {
                            if (desc) {
                                layer.open({
                                    icon: '2',
                                    content: '新增接口失败，请检查"请求参数"数据是否填写完整'
                                });
                                return;
                            }
                        }
                    });
                }
                var queryParams = JSON.stringify(queryParamList);

                // response params
                var responseParamList = new Array();
                if ($('#responseParams_parent').find('.responseParams_item').length > 0) {
                    $('#responseParams_parent').find('.responseParams_item').each(function () {
                        var notNull = $(this).find('.notNull').val();
                        var type = $(this).find('.type').val();
                        var name = $(this).find('.name').val();
                        var desc = $(this).find('.desc').val();
                        if (name) {
                            responseParamList.push({
                                'notNull':notNull,
                                'type':type,
                                'name':name,
                                'desc':desc
                            });
                        } else {
                            if (desc) {
                                layer.open({
                                    icon: '2',
                                    content: '新增接口失败，请检查"响应结果参数"数据是否填写完整'
                                });
                                return;
                            }
                        }
                    });
                }
                var responseParams = JSON.stringify(responseParamList);

                // final params
                var params = $("#ducomentForm").serialize();
                params += '&' + $.param({
                    'remark':remark,
                    'requestHeaders':requestHeaders,
                    'queryParams':queryParams,
                    'responseParams':responseParams
                });

                $.post(base_url + "/document/update", params, function(data, status) {
                    if (data.code == "200") {
                        $('#addModal').modal('hide');
                        layer.open({
                            icon: '1',
                            content: "更新成功" ,
                            end: function(layero, index){

                                // 关闭当前，跳转详情页；
                                let id = $('#ducomentForm input[name=id]').val();
                                let url  = base_url + '/document/detailPage?id=' + id;
                                openTab(url, "查看接口信息" , true)
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

        // JSON 格式化并校验
        $('#successRespExample_2json').click(function () {
            try {
                var jsonStr = $('#successRespExample').val();
                var json = $.parseJSON(jsonStr);
                //$('#successRespExample').JSONView(json, { collapsed: false, nl2br: true, recursive_collapser: true });

                var prettyJson = JSON.stringify(json, undefined, 4);
                $('#successRespExample').val(prettyJson);
            } catch (e) {
                layer.open({
                    icon: '2',
                    content: "JSON格式化失败:" + e
                });
            }
        });
        $('#failRespExample_2json').click(function () {
            try {
                var jsonStr = $('#failRespExample').val();
                var json = $.parseJSON(jsonStr);

                var prettyJson = JSON.stringify(json, undefined, 4);
                $('#failRespExample').val(prettyJson);
            } catch (e) {
                layer.open({
                    icon: '2',
                    content: "JSON格式化失败:" + e
                });
            }
        });

    });
</script>
<!-- 3-script end -->

</body>
</html>