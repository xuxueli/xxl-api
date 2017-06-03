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
				obj.bizId = $('#bizId').val();
				obj.name = $('#name').val();
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
			{ "data": 'name', "bSortable": false, "width":'20%'},
			{ "data": 'desc', "visible" : true, "width":'20%'},
			{
				"data": 'permission',
				"visible" : true,
				"width":'10%',
				"render": function ( data, type, row ) {
					// 0-公开、1-私有
					var htm = '';
					if (data == 0) {
						htm += '公开';
					} else {
						htm += '私有';
					}
					return htm;
				}
			},
			{
				"data": 'baseUrlProduct',
				"width":'30%',
				"visible" : true,
				"render": function ( data, type, row ) {
					var htm = '';
					if (row.baseUrlProduct) {
						htm += '线上：' + row.baseUrlProduct + '<br>';
					}
					if (row.baseUrlPpe) {
						htm += '预发：' + row.baseUrlPpe + '<br>';
					}
					if (row.baseUrlQa) {
						htm += '测试：' + row.baseUrlQa + '<br>';
					}

					return htm;
				}
			},
			{
				"data": '操作' ,
				"width":'10%',
				"render": function ( data, type, row ) {
					return function(){

						// 详情页
						var goUrl = base_url + '/group?productId='+ row.id;

						// html
						var html = '<span id="'+ row.id +'" '+
								' name="'+ row.name +'" '+
								' desc="'+ row.desc +'" '+
								' permission="'+ row.permission +'" '+
								' baseUrlProduct="'+ row.baseUrlProduct +'" '+
								' baseUrlPpe="'+ row.baseUrlPpe +'" '+
								' baseUrlQa="'+row.baseUrlQa +'" '+
								' bizId="'+row.bizId +'" '+
								'>'+
							'<button class="btn btn-warning btn-xs update" type="button">编辑</button>  '+
							'<button class="btn btn-danger btn-xs delete" type="button">删除</button>  <br>'+
							'<button class="btn btn-info btn-xs" type="button" onclick="javascript:window.open(\'' + goUrl + '\')" >进入项目</button>  '+
							'</span>';

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

	$("#search").click(function(){
		projectTable.fnDraw();
	});

	$('#bizId').change(function () {
		var bizId = $('#bizId').val();
		window.location.href = base_url + "/project?bizId=" + bizId;
	});

	// job operate
	$("#project_list").on('click', '.delete',function() {
		var id = $(this).parent('span').attr("id");
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

	// 新增
	$("#add").click(function(){
		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {
			name : {
				required : true,
				minlength: 5,
				maxlength: 50
			},
			baseUrlProduct : {
            	required : true,
				minlength: 5,
				maxlength: 200
            }
        }, 
        messages : {
			name : {
            	required :"请输入“项目名称”",
				minlength: "长度不可少于5",
				maxlength: "长度不可多余50"
            },
			baseUrlProduct : {
            	required :"请输入“跟地址：线上环境”",
				minlength: "长度不可少于5",
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
        	$.post(base_url + "/project/add",  $("#addModal .form").serialize(), function(data, status) {
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
	$("#project_list").on('click', '.update',function() {

		// base data
		$("#updateModal .form input[name='id']").val($(this).parent('span').attr("id"));
		$("#updateModal .form input[name='name']").val($(this).parent('span').attr("name"));
		$("#updateModal .form input[name='desc']").val($(this).parent('span').attr("desc"));
		$("#updateModal .form input[name='permission']").eq($(this).parent('span').attr("permission")).click();
		$("#updateModal .form input[name='baseUrlProduct']").val($(this).parent('span').attr("baseUrlProduct"));
		$("#updateModal .form input[name='baseUrlPpe']").val($(this).parent('span').attr("baseUrlPpe"));
		$("#updateModal .form input[name='baseUrlQa']").val($(this).parent('span').attr("baseUrlQa"));
		$("#updateModal .form select[name='bizId']").find("option[value='"+ $(this).parent('span').attr("bizId") +"']").attr("selected",true);

		// show
		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,
		rules : {
			name : {
				required : true,
				minlength: 5,
				maxlength: 50
			},
			baseUrlProduct : {
				required : true,
				minlength: 5,
				maxlength: 200
			}
		},
		messages : {
			name : {
				required :"请输入“项目名称”",
				minlength: "长度不可少于5",
				maxlength: "长度不可多余50"
			},
			baseUrlProduct : {
				required :"请输入“跟地址：线上环境”",
				minlength: "长度不可少于5",
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
			// post
    		$.post(base_url + "/project/update", $("#updateModal .form").serialize(), function(data, status) {
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
