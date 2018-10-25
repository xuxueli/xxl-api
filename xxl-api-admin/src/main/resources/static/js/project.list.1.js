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
				"data": '操作' ,
				"width":'10%',
				"render": function ( data, type, row ) {
					return function(){

						var permissionDiv = '';
						if (hasBizPermission(row.bizId)) {
                            permissionDiv += '<button class="btn btn-warning btn-xs update" type="button">编辑</button>  ';
                            permissionDiv += '<button class="btn btn-danger btn-xs delete" type="button">删除</button> ';
						}

						// 详情页
						var goUrl = base_url + '/group?projectId='+ row.id;

						// html
						var html = '<span id="'+ row.id +'" '+
								' name="'+ row.name +'" '+
								' desc="'+ row.desc +'" '+
								' baseUrlProduct="'+ row.baseUrlProduct +'" '+
								' baseUrlPpe="'+ row.baseUrlPpe +'" '+
								' baseUrlQa="'+row.baseUrlQa +'" '+
								' bizId="'+row.bizId +'" '+
								'>'+
                            permissionDiv +
							'<button class="btn btn-info btn-xs" type="button" onclick="javascript:window.location.href=\'' + goUrl + '\'" >进入项目</button>  '+
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

        layer.confirm( "确认删除该项目?" , {
            icon: 3,
            title: '系统提示' ,
            btn: [ '确定', '取消' ]
        }, function(index){
            layer.close(index);

			$.ajax({
				type : 'POST',
				url : base_url + "/project/delete",
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
                                projectTable.fnDraw(false);
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
                    layer.open({
                        icon: '1',
                        content: "新增成功" ,
                        end: function(layero, index){
                            projectTable.fnDraw(false);
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
		/*$("#updateModal .form input[name='permission']").eq($(this).parent('span').attr("permission")).click();*/
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
                    layer.open({
                        icon: '1',
                        content: "更新成功" ,
                        end: function(layero, index){
                            projectTable.fnDraw(false);
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
	$("#updateModal").on('hide.bs.modal', function () {
		$("#updateModal .form")[0].reset()
	});


});
