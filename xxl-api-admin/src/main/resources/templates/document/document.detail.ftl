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
                        <#--<button class="btn btn-default btn-xs" type="button" onclick="javascript:openTab('${request.contextPath}/group?projectId=${projectId}&groupId=${document.groupId}', '项目接口列表', true)" >返回接口列表</button>-->
                        <#if hasBizPermission>
                            <button class="btn btn-default btn-xs" type="button" onclick="javascript:openTab('${request.contextPath}/document/updatePage?id=${document.id}', '修改接口信息', true)" >修改接口</button>
                        </#if>
                    </div>
                </div>

                <div class="box-body">
                    <div class="form-group">
                        <label class="col-sm-1">URL</label>
                        <div class="col-sm-6">
                            <select id="projectBaseUrlUpdate" >
                            <#if project.baseUrlProduct?exists && project.baseUrlProduct!="" >
                                <option value="${project.baseUrlProduct}" >线上环境</option>
                            </#if>
                            <#if project.baseUrlPpe?exists && project.baseUrlPpe!="" >
                                <option value="${project.baseUrlPpe}" >预发布环境</option>
                            </#if>
                            <#if project.baseUrlQa?exists && project.baseUrlQa!="" >
                                <option value="${project.baseUrlQa}" >测试环境</option>
                            </#if>
                            </select>
                            &nbsp;&nbsp;
                            <span id="projectBaseUrl" >${project.baseUrlProduct}</span><span>${document.requestUrl}</span>
                        </div>
                        <label class="col-sm-1">分组</label>
                        <div class="col-sm-4">
                            <#if 0 == document.groupId>默认分组
                            <#else>
                                <#if groupList?exists && groupList?size gt 0>
                                    <#list groupList as group>
                                        <#if group.id == document.groupId>${group.name}</#if>
                                    </#list>
                                </#if>
                            </#if>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-1">Method</label>
                        <div class="col-sm-6">
                            <#list RequestMethodEnum as item>
                                <#if item == document.requestMethod>${item}</#if>
                            </#list>
                        </div>
                        <label class="col-sm-1">状态</label>
                        <div class="col-sm-4">
                            <#if 0 == document.status>启用
                            <#elseif 1 == document.status>维护
                            <#elseif 2 == document.status>废弃
                            </#if>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-1">名称</label>
                        <div class="col-sm-11">
                            <span style="color: #00a65a;">${document.name}</span>
                        </div>
                    </div>
                </div>
            </div>

            <#--请求头部-->
            <#if requestHeadersList?exists && requestHeadersList?size gt 0 >
                <div class="box box-primary">
                    <div class="box-header">
                        <h3 class="box-title">请求头部</h3>
                    </div>
                    <div class="box-body no-padding" >
                        <table class="table table-striped">
                            <tr>
                                <th style="width: 25%;" >头部标签</th>
                                <th style="width: 75%;" >头部内容</th>
                            </tr>
                            <#list requestHeadersList as requestHeadersMap>
                                <#assign key = requestHeadersMap['key'] />
                                <#assign value = requestHeadersMap['value'] />
                                <tr>
                                    <td>${key}</td>
                                    <td>${value}</td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </div>
            </#if>

            <#--请求参数-->
            <div class="box box-primary">
                <div class="box-header">
                    <h3 class="box-title">请求参数</h3>
                </div>

                <div class="box-body no-padding" >
                    <#if queryParamList?exists>
                        <table class="table table-striped">
                            <tr>
                                <th style="width: 25%;" >参数名称</th>
                                <th style="width: 25%;" >参数说明</th>
                                <th style="width: 25%;" >数据类型</th>
                                <th style="width: 25%;" >是否必填</th>
                            </tr>
                            <#list queryParamList as queryParam>
                                <tr>
                                    <td>${queryParam.name}</td>
                                    <td>${queryParam.desc}</td>
                                    <td>${queryParam.type}</td>
                                    <td>
                                        <#if queryParam.notNull == "true" >必填
                                        <#else>非必填
                                        </#if>
                                    </td>
                                </tr>
                            </#list>
                        </table>
                    </#if>
                </div>
            </div>

            <#--响应数据类型-->
            <#if responseDatatype?exists>
                <div class="box box-primary">
                    <div class="box-header">
                        <h3 class="box-title">响应数据类型</h3>
                    </div>
                    <div class="box-body " >

                        <#macro fieldShow fieldList >
                            <#if fieldList?exists && fieldList?size gt 0>
                                <ul style="display: block;" >
                                    <#list fieldList as field>
                                        <li>
                                            <span style="font-weight: bold;margin-right: 10px;" >${field.fieldName}</span> {类型：${field.fieldDatatype.name} <#if field.fieldType==1>[]</#if>，描述：${field.fieldAbout}}
                                            <@fieldShow field.fieldDatatype.fieldList />
                                        </li>
                                    </#list>
                                </ul>
                            </#if>
                        </#macro>

                        <p>
                            <span style="font-weight: bold;margin-right: 10px;" >${responseDatatype.name}:</span> ${responseDatatype.about}
                            <a href="javascript:;" onclick="javascript:openTab('${request.contextPath}/datatype/dataTypeDetail?dataTypeId=${responseDatatype.id}', '查看数据类型', false)" >>>>数据类型</a>
                            <@fieldShow responseDatatype.fieldList />
                        </p>

                    </div>
                </div>
            </#if>

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
                    <div class="chart tab-pane active" id="success_resp" style="position: relative; height: 100%;">
                        <div class="box-body">
                            ${document.successRespType}
                            <br>
                            <pre name="successRespExample" <#if "JSON"==document.successRespType >class="jsonViewPre"</#if> style="width: 100%; height: 100%; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;margin-top: 15px;" >${document.successRespExample}</pre>
                        </div>
                    </div>
                    <div class="chart tab-pane" id="fail_resp" style="position: relative; height: 100%;">
                        <div class="box-body">
                            ${document.failRespType}
                            <br>
                            <pre name="failRespExample" <#if "JSON"==document.successRespType >class="jsonViewPre"</#if> style="width: 100%; height: 100%; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;margin-top: 15px;" >${document.failRespExample}</pre>
                        </div>
                    </div>
                </div>
            </div>

            <#--响应结果参数-->
            <#--<#if responseParamList?exists && responseParamList?size gt 0 >
                <div class="box box-primary">
                    <div class="box-header">
                        <h3 class="box-title">响应结果参数</h3>
                    </div>

                    <div class="box-body no-padding" >
                        <table class="table table-striped">
                            <tr>
                                <th style="width: 25%;" >是否必填</th>
                                <th style="width: 25%;" >参数类型</th>
                                <th style="width: 25%;" >参数名称</th>
                                <th style="width: 25%;" >参数说明</th>
                            </tr>
                            <#list responseParamList as responseParam>
                                <tr>
                                    <td>
                                        <#if responseParam.notNull == "true" >非空
                                        <#else>可空
                                        </#if>
                                    </td>
                                    <td>${responseParam.type}</td>
                                    <td>${responseParam.name}</td>
                                    <td>${responseParam.desc}</td>
                                </tr>
                            </#list>
                        </table>
                    </div>
                </div>
            </#if>-->

            <#-- 接口备注 -->
            <#if document.remark?exists && document.remark?length gt 0 >
                <div class="box box-primary">
                    <div class="box-header with-border">
                        <h3 class="box-title">接口备注</h3>
                    </div>
                    <div class="box-body" >
                        <div class="box-body pad" id="remark" ><textarea style="display:none;">${document.remark}</textarea></div>
                    </div>
                </div>
            </#if>


            <#--Mock数据-->
            <div class="box box-primary">
                <div class="box-header">
                    <h3 class="box-title">Mock数据</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-info btn-xs" type="button" id="addMock" >+ Mock数据</button>
                    </div>
                </div>
                <div class="box-body no-padding" >
                    <table class="table table-striped">
                        <tr>
                            <th style="width: 25%;" >响应数据类型(MIME)</th>
                            <th style="width: 75%;" >操作</th>
                        </tr>
                        <#list mockList as mock>
                            <textarea id="respExample_${mock.id}" style="display: none;" >${mock.respExample}</textarea>
                            <tr>
                                <td>${mock.respType}</td>
                                <td>
                                    <a href="${request.contextPath}/mock/run/${mock.uuid}" target="_blank" >运行</a>
                                    &nbsp;
                                    <a href="javascript:;" class="updateMock" _id="${mock.id}" respType="${mock.respType}" style="color: gray;" onmouseover="this.style.cssText='color:silver;'" onmouseout="this.style.cssText='color:gray;'"><i class="fa fa-fw fa-wrench"></i>修改</a>
                                    &nbsp;
                                    <a href="javascript:;" class="deleteMock" _id="${mock.id}" style="color:gray;" onmouseover="this.style.cssText='color:silver;'" onmouseout="this.style.cssText='color:gray;'"><i class="fa fa-fw fa-trash-o"></i>删除</a>
                                </td>
                            </tr>
                        </#list>
                    </table>
                </div>
            </div>

            <#--Test历史-->
            <div class="box box-primary">
                <div class="box-header">
                    <h3 class="box-title">Test历史</h3>
                    <div class="box-tools pull-right">
                        <button class="btn btn-info btn-xs" type="button" onclick="javascript:openTab('${request.contextPath}/test?documentId=${document.id}', '接口功能测试', false)" >+ 接口测试</button>
                    </div>
                </div>
                <div class="box-body no-padding" >
                    <table class="table table-striped">
                        <tr>
                            <th style="width: 25%;" >创建时间</th>
                            <th style="width: 75%;" >操作</th>
                        </tr>
                        <#list testHistoryList as testInfo>
                            <tr>
                                <td>${testInfo.addTime?datetime}</td>
                                <td>
                                    <a href="javascript:;"
                                       onclick="javascript:openTab('${request.contextPath}/test?documentId=${document.id}&testId=${testInfo.id}', '接口功能测试', false)" >运行</a>
                                    &nbsp;
                                    <a href="javascript:;" class="deleteTest" _id="${testInfo.id}" style="color:gray;" onmouseover="this.style.cssText='color:silver;'" onmouseout="this.style.cssText='color:gray;'"><i class="fa fa-fw fa-trash-o"></i>删除</a>
                                </td>
                            </tr>
                        </#list>
                    </table>
                </div>
            </div>

        </form>

        <!-- 新增-分组.模态框 -->
        <div class="modal fade" id="addMockModal" tabindex="-1" role="dialog"  aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" >新增Mock数据</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal form" role="form" >
                            <div class="form-group">
                                <div class="col-sm-12">
                                    响应数据类型(MIME)：
                                    <#list ResponseContentType as item>
                                        <input type="radio" class="iCheck" name="respType" value="${item}" <#if item_index==0>checked</#if> >${item}  &nbsp;&nbsp;
                                    </#list>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-12">
                                    <textarea name="respExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;" ></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-6">
                                    <button type="button" class="btn btn-primary save"  >保存</button>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                    <input type="hidden" name="documentId" value="${document.id}" >
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- 更新-分组.模态框 -->
        <div class="modal fade" id="updateMockModal" tabindex="-1" role="dialog"  aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" >更新Mock数据</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal form" role="form" >
                            <div class="form-group">
                                <div class="col-sm-12">
                                    响应数据类型(MIME)：
                                    <#list ResponseContentType as item>
                                        <input type="radio" class="iCheck" name="respType" value="${item}" >${item}  &nbsp;&nbsp;
                                    </#list>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-12">
                                    <textarea name="respExample" style="width: 100%; height: 300px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;" ></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-3 col-sm-6">
                                    <button type="button" class="btn btn-primary save"  >保存</button>
                                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                                    <input type="hidden" name="documentId" value="${document.id}" >

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
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor.md-1.5.0/main/editormd.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor.md-1.5.0/lib/marked.min.js"></script>
<script src="${request.contextPath}/static/plugins/editor.md-1.5.0/lib/prettify.min.js"></script>
<script src="${request.contextPath}/static/plugins/jsontree/jquery.jsonview.js"></script>
<#-- biz common -->
<script src="${request.contextPath}/static/biz/common/admin.util.js"></script>
<script>
    $(function() {

        // base init
        $('.iCheck').iCheck({
            labelHover : false,
            cursor : true,
            checkboxClass : 'icheckbox_square-blue',
            radioClass : 'iradio_square-blue',
            increaseArea : '20%'
        });

        /**
         * project base url
         */
        $('#projectBaseUrlUpdate').change(function () {
            $('#projectBaseUrl').text( $(this).val() );
        });

        /**
         * remark view
         */
        remarkView = editormd.markdownToHTML("remark", {
            htmlDecode      : "style,script,iframe",  // you can filter tags decode
            emoji           : false,
            taskList        : false,
            tex             : false,  // 默认不解析
            flowChart       : false,  // 默认不解析
            sequenceDiagram : false,  // 默认不解析
        });

        /**
         * 新增，Mock数据
         */
        $("#addMock").click(function(){
            $('#addMockModal').modal({backdrop: false, keyboard: false}).modal('show');
        });
        $('#addMockModal .save').click(function () {
            $.ajax({
                type : 'POST',
                url : base_url + "/mock/add",
                data : $('#addMockModal form').serialize(),
                dataType : "json",
                success : function(data){
                    if (data.code == 200) {
                        layer.open({
                            icon: '1',
                            content: "Mock数据保存成功" ,
                            end: function(layero, index){
                                window.location.reload();
                            }
                        });
                    } else {
                        layer.open({
                            icon: '2',
                            content: (data.msg||"Mock数据保存失败")
                        });
                    }
                },
            });

        });

        /**
         * 删除，Mock数据
         */
        $('.deleteMock').click(function () {
            var id = $(this).attr('_id');

            layer.confirm( "确认删除该Mock数据?" , {
                icon: 3,
                title: '系统提示' ,
                btn: [ '确定', '取消' ]
            }, function(index){
                layer.close(index);

                $.ajax({
                    type : 'POST',
                    url : base_url + "/mock/delete",
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
                    }
                });
            });

        });

        /**
         * 修改，Mock数据
         */
        $(".updateMock").click(function(){
            var id = $(this).attr('_id');
            var respType = $(this).attr('respType');
            var respExample = $('#respExample_'+id).val();


            $('#updateMockModal input[name=id]').val(id);
            $('#updateMockModal input[name=respType][value="'+respType+'"]').iCheck('check');
            $('#updateMockModal textarea[name=respExample]').val(respExample);

            $('#updateMockModal').modal({backdrop: false, keyboard: false}).modal('show');
        });
        $('#updateMockModal .save').click(function () {
            $.ajax({
                type : 'POST',
                url : base_url + "/mock/update",
                data : $('#updateMockModal form').serialize(),
                dataType : "json",
                success : function(data){
                    if (data.code == 200) {
                        layer.open({
                            icon: '1',
                            content: "Mock数据保存成功" ,
                            end: function(layero, index){
                                window.location.reload();
                            }
                        });
                    } else {
                        layer.open({
                            icon: '2',
                            content: (data.msg||"Mock数据保存失败")
                        });
                    }
                },
            });
        });


        /**
         * 删除，Test历史
         */
        $('.deleteTest').click(function () {
            var id = $(this).attr('_id');

            layer.confirm( "确认删除该Test历史?" , {
                icon: 3,
                title: '系统提示' ,
                btn: [ '确定', '取消' ]
            }, function(index){
                layer.close(index);

                $.ajax({
                    type : 'POST',
                    url : base_url + "/test/delete",
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
                    }
                });
            });

        });


        /**
         * json pre view
         */
        $('.jsonViewPre').each(function () {

            try {
                var jsonStr = $(this).text();
                var json = $.parseJSON(jsonStr);
                $(this).JSONView(json, { collapsed: false, nl2br: true, recursive_collapser: true });
            } catch (e) {
                console.log('jsonViewDiv parse json view error,' + e );
            }
        });

    });

</script>
<!-- 3-script end -->

</body>
</html>