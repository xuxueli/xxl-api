<!DOCTYPE html>
<html>
<head>
    <#-- import macro -->
    <#import "../common/common.macro.ftl" as netCommon>

    <!-- 1-style start -->
    <@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/select2/css/select2.min.css">
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
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
                <#--基础信息-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">基础信息</h3>
                        <div class="box-tools pull-right">
                            <button class="btn btn-default btn-xs" type="button" onclick="javascript:openTab('${request.contextPath}/document/detailPage?id=${document.id}', '查看接口信息', false)" >返回接口详情页</button>
                        </div>
                    </div>

                    <div class="box-body">
                        <div class="form-group">

                            <label class="col-sm-1 control-label">URL</label>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="requestUrl" placeholder="请输入接口URL" maxlength="100"
                                       <#if testHistory?exists && testHistory.requestUrl?exists >value="${testHistory.requestUrl}"</#if> >
                            </div>
                            <label class="col-sm-1 control-label">Method</label>
                            <div class="col-sm-2">
                                <select class="form-control select2" id="requestMethod">
                                <#list RequestMethodEnum as item>
                                    <option value="${item}" <#if testHistory?exists && testHistory.requestMethod==item>selected</#if> >${item}</option>
                                </#list>
                                </select>
                            </div>
                            <#if project?exists>
                                <div class="col-sm-2">
                                    <select class="form-control select2" id="projectBaseUrlUpdate" >
                                    <#if project.baseUrlProduct?exists && project.baseUrlProduct!="" >
                                        <option value="${project.baseUrlProduct}${document.requestUrl}"
                                            <#if testHistory?exists && testHistory.requestUrl == (project.baseUrlProduct + document.requestUrl)>selected</#if> >线上环境</option>
                                    </#if>
                                    <#if project.baseUrlPpe?exists && project.baseUrlPpe!="" >
                                        <option value="${project.baseUrlPpe}${document.requestUrl}"
                                            <#if testHistory?exists && testHistory.requestUrl == (project.baseUrlPpe + document.requestUrl)>selected</#if> >预发布环境</option>
                                    </#if>
                                    <#if project.baseUrlQa?exists && project.baseUrlQa!="" >
                                        <option value="${project.baseUrlQa}${document.requestUrl}"
                                            <#if testHistory?exists && testHistory.requestUrl == (project.baseUrlQa + document.requestUrl)>selected</#if> >测试环境</option>
                                    </#if>
                                    </select>
                                </div>
                            </#if>
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
                        <#if requestHeaders?exists>
                            <#list requestHeaders as item>
                                <#assign key = item['key'] />
                                <#assign value = item['value'] />
                                <div class="form-group requestHeaders_item" >
                                    <label class="col-sm-1 control-label">Key</label>
                                    <div class="col-sm-4 item">
                                        <select class="form-control select2_tag key" >
                                            <option value=""></option>
                                            <#list requestHeadersEnum as item>
                                                <option value="${item}" <#if item == key>selected</#if> >${item}</option>
                                            </#list>
                                        </select>
                                    </div>
                                    <label class="col-sm-1 control-label">Value</label>
                                    <div class="col-sm-5 item">
                                        <input type="text" class="form-control value" value="${value}">
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
                            <div class="col-sm-4 item">
                                <input type="text" class="form-control key">
                            </div>
                            <label class="col-sm-1 control-label">值</label>
                            <div class="col-sm-5 item">
                                <input type="text" class="form-control value">
                            </div>
                            <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                        </div>
                    </div>

                    <div class="box-body" id="queryParams_parent" >
                        <#if queryParams?exists>
                            <#list queryParams as item>
                                <div class="form-group queryParams_item" >
                                    <label class="col-sm-1 control-label">参数</label>
                                    <div class="col-sm-4 item">
                                        <input type="text" class="form-control key" value="<#if testId gt 0>${item.key}<#else>${item.name}</#if>" >
                                    </div>
                                    <label class="col-sm-1 control-label">值</label>
                                    <div class="col-sm-5 item">
                                        <input type="text" class="form-control value" value="<#if testId gt 0>${item.value}<#else></#if>" >
                                    </div>
                                    <button type="button" class="col-sm-1 btn btn-box-tool delete" ><i class="fa fa-fw fa-close"></i></button>
                                </div>
                            </#list>
                        </#if>
                    </div>
                </div>

                <#--响应结果-->
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">响应结果</h3>
                        <div class="box-tools pull-right">
                            <#if document?exists>
                                <button class="btn btn-default btn-xs" type="button" id="save" testId="${testId}" documentId="${document.id}" >保存</button>
                            </#if>
                            <button class="btn btn-info btn-xs" type="button" id="run" >运行</button>
                        </div>
                    </div>
                    <div class="box-body" id="respType_parent">
                        响应数据类型(MIME)：
                        <#list ResponseContentType as item>
                            <input type="radio" class="iCheck" name="respType" value="${item}" <#if item_index==0>checked</#if> >${item}  &nbsp;&nbsp;
                        </#list>
                        <br>
                        <pre id="respContent" ><br><br><br><br><br></pre>
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
<script src="${request.contextPath}/static/plugins/jsontree/jquery.jsonview.js"></script>
<#-- biz common -->
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
         * projectBaseUrlUpdate
         */
        $('#projectBaseUrlUpdate').change(function () {
            $('#requestUrl').val($(this).val());
        });
        $('#projectBaseUrlUpdate').change();

        /**
         * 运行
         */
        $('#run').click(function () {

            // param
            var requestMethod = $('#requestMethod').val();
            var requestUrl = $('#requestUrl').val();
            var respType = $('#respType_parent input[name=respType]:checked').val();

            if (!requestUrl) {
                layer.open({
                    icon: '2',
                    content: '请输入"接口URL"'
                });
                return;
            }

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
                                content: '请检查"请求头部"数据是否填写完整'
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
                    var key = $(this).find('.key').val();
                    var value = $(this).find('.value').val();
                    if (key) {
                        queryParamList.push({
                            'key':key,
                            'value':value
                        });
                    } else {
                        if (desc) {
                            layer.open({
                                icon: '2',
                                content: '请检查"请求参数"数据是否填写完整'
                            });
                            return;
                        }
                    }
                });
            }
            var queryParams = JSON.stringify(queryParamList);

            // final params
            var params = {
                'requestMethod':requestMethod,
                'requestUrl':requestUrl,
                'requestHeaders':requestHeaders,
                'queryParams':queryParams,
                'respType':respType
            }


            $.post(base_url + "/test/run", params, function(data, status) {
                var $respContent = $('#respContent');
                if (data.code == "200") {
                    $($respContent).text(data.data);

                    if ('JSON'==respType || 'JSONP'==respType) {
                        var json = eval('('+ data.data +')');
                        $('#respContent').JSONView(json, { collapsed: false, nl2br: true, recursive_collapser: true });
                    }
                } else {
                    layer.open({
                        icon: '2',
                        content: (data.msg||'请求失败')
                    });
                }
            });

        });


        /**
         * 保存
         */
        $('#save').click(function () {
            // param
            var requestMethod = $('#requestMethod').val();
            var requestUrl = $('#requestUrl').val();
            var respType = $('#respType_parent input[name=respType]:checked').val();

            if (!requestUrl) {
                layer.open({
                    icon: '2',
                    content: '请输入"接口URL"'
                });
                return;
            }

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
                                content: '请检查"请求头部"数据是否填写完整'
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
                    var key = $(this).find('.key').val();
                    var value = $(this).find('.value').val();
                    if (key) {
                        queryParamList.push({
                            'key':key,
                            'value':value
                        });
                    } else {
                        if (desc) {
                            layer.open({
                                icon: '2',
                                content: '请检查"请求参数"数据是否填写完整'
                            });
                            return;
                        }
                    }
                });
            }
            var queryParams = JSON.stringify(queryParamList);

            // id
            var documentId = $(this).attr('documentId');
            var testId = $(this).attr('testId');

            // final params
            var params = {
                'requestMethod':requestMethod,
                'requestUrl':requestUrl,
                'requestHeaders':requestHeaders,
                'queryParams':queryParams,
                'respType':respType,
                'documentId':documentId,
                'id':testId
            }

            // url
            var url = base_url + "/test/add";
            if (testId > 0) {
                url = base_url + "/test/update";
            }

            $.post(url, params, function(data, status) {
                if (data.code == "200") {
                    if (testId == 0 && data.data>0) {
                        $('#save').attr('testId', data.data);
                    }
                    layer.open({
                        icon: '1',
                        content: '保存成功'
                    });
                } else {
                    layer.open({
                        icon: '2',
                        content: '保存失败'
                    });
                }
            });
        });




    });

</script>
<!-- 3-script end -->

</body>
</html>