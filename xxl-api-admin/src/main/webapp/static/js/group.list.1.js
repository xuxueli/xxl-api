$(function() {

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
	$("#updateGroupModal").on('hide.bs.modal', function () {
		$("#updateGroupModal .form")[0].reset();
		updateGroupModalValidate.resetForm();
		$("#updateGroupModal .form .form-group").removeClass("has-error");
		$(".remote_panel").show();	// remote
	});

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
					htm += '线上：' + row.baseUrlProduct + '<br>';
					htm += '预发：' + row.baseUrlPpe + '<br>';
					htm += '测试：' + row.baseUrlQa + '<br>';
					return htm;
				}
			},
			{ "data": 'version', "visible" : true,"width":'10%'},
			{
				"data": '操作' ,
				"width":'10%',
				"render": function ( data, type, row ) {
					return function(){

						// 详情页
						var goUrl = '/group?productId='+ row.id;

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
							'<button class="btn btn-danger btn-xs delete" type="button">删除</button>  <br>'+
							'<button class="btn btn-info btn-xs" type="button" onclick="javascript:window.open(\'' + goUrl + '\')" >进入</button>  '+
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

	$("#search").click(function(){
		projectTable.fnDraw();
	});

	// job operate
	$("#project_list").on('click', '.delete',function() {
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
