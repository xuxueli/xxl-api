$(function() {

	// init date tables
	var projectTable = $("#project_list").dataTable({
		"deferRender": true,
		"processing" : true,
		"serverSide": true,
		"ajax": {
			url: base_url + "/project/pageList",
			type:"post",
			data : function ( d ) {
				var obj = {};
				obj.jobGroup = $('#jobGroup').val();
				obj.executorHandler = $('#executorHandler').val();
				obj.start = d.start;
				obj.length = d.length;
				return obj;
			}
		},
		"searching": false,
		"ordering": false,
		//"scrollX": true,	// X轴滚动条，取消自适应
		"columns": [
			{ "data": 'id', "bSortable": false, "visible" : false},
			{ "data": 'name', "bSortable": false},
			{ "data": 'desc', "visible" : true},
			{ "data": 'permission', "visible" : true},
			{
				"data": 'baseUrlProduct',
				"width":'20%',
				"visible" : true,
				"render": function ( data, type, row ) {
					var htm = '';
					htm += '线上环境：' + row.baseUrlProduct + '<br>';
					htm += '预发布环境：' + row.baseUrlPpe + '<br>';
					htm += '测试环境：' + row.baseUrlQa + '<br>';
					return htm;
				}
			},
			{ "data": 'version', "visible" : true,"width":'20%'},
			{
				"data": '操作' ,
				"width":'15%',
				"render": function ( data, type, row ) {
					return function(){

						// html
						var html = '<p id="'+ row.id +'" '+
								' name="'+ row.name +'" '+
								' desc="'+ row.desc +'" '+
								' permission="'+ row.permission +'" '+
								' baseUrlProduct="'+ row.baseUrlProduct +'" '+
								' baseUrlPpe="'+ row.baseUrlPpe +'" '+
								' baseUrlQa="'+row.baseUrlQa +'" '+
								' version="'+row.version +'" '+
								'>'+
							'<button class="btn btn-warning btn-xs update" type="button">编辑</button>  '+
							'<button class="btn btn-danger btn-xs delete" type="button">删除</button>  '+
							'</p>';

						return html;
					};
				}
			}
		],
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "每页 _MENU_ 条记录",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )",
			"sInfoEmpty" : "无记录",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});

	// job operate
	$("#user_list").on('click', '.delete',function() {
		var id = $(this).parent('p').attr("id");
		ComConfirm.show("确认删除该项目?", function(){
			$.ajax({
				type : 'POST',
				url : base_url + "/project/delete",
				data : {
					"id" : id
				},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
						ComAlert.show(1, "删除成功", function(){
							window.location.reload();
						});
					} else {
						ComAlert.show(2, (data.msg || "删除失败") );
					}
				},
			});
		});
	});

	// jquery.validate 自定义校验 “英文字母开头，只含有英文字母、数字和下划线”
	jQuery.validator.addMethod("userNameValid", function(value, element) {
		var length = value.length;
		var valid = /^[a-zA-Z][a-zA-Z0-9_]*$/;
		return this.optional(element) || valid.test(value);
	}, "只支持英文字母开头，只含有英文字母、数字和下划线");

	// 新增
	$("#add").click(function(){
		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {
			userName : {
				required : true,
				minlength: 5,
				maxlength: 20,
				userNameValid: true
			},
			password : {
            	required : true,
				minlength: 5,
				maxlength: 20
            }
        }, 
        messages : {
			userName : {
            	required :"请输入“登录账号”",
				minlength: "长度不可少于5",
				maxlength: "长度不可多余20"
            },
			password : {
            	required :"请输入“登录密码”",
				minlength: "长度不可少于5",
				maxlength: "长度不可多余20"
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
        	$.post(base_url + "/user/add",  $("#addModal .form").serialize(), function(data, status) {
    			if (data.code == "200") {
					$('#addModal').modal('hide');
					setTimeout(function () {
						ComAlert.show(1, "新增成功", function(){
							window.location.reload();
						});
					}, 315);
    			} else {
					ComAlert.show(2, (data.msg || "新增失败") );
    			}
    		});
		}
	});
	$("#addModal").on('hide.bs.modal', function () {
		$("#addModal .form")[0].reset();
		addModalValidate.resetForm();
		$("#addModal .form .form-group").removeClass("has-error");
		$(".remote_panel").show();	// remote
	});

	// 更新
	$("#user_list").on('click', '.update',function() {

		// base data
		$("#updateModal .form input[name='id']").val($(this).parent('p').attr("id"));
		$("#updateModal .form input[name='userName']").val($(this).parent('p').attr("userName"));
        $("#updateModal .form input[name='password']").val("");
		$("#updateModal .form input[name='type']").eq($(this).parent('p').attr("type")).click();
		$("#updateModal .form input[name='realName']").val($(this).parent('p').attr("realName"));

		// show
		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,
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
			// post
    		$.post(base_url + "/user/update", $("#updateModal .form").serialize(), function(data, status) {
    			if (data.code == "200") {
					$('#updateModal').modal('hide');
					setTimeout(function () {
						ComAlert.show(1, "更新成功", function(){
							window.location.reload();
						});
					}, 315);
    			} else {
					ComAlert.show(2, (data.msg || "更新失败") );
    			}
    		});
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
		$("#updateModal .form")[0].reset()
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
