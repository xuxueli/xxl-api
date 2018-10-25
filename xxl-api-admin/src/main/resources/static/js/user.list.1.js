$(function() {

	// init date tables
	var userListTable = $("#user_list").dataTable({
        "deferRender": true,
        "processing" : true,
        "serverSide": true,
        "ajax": {
            url: base_url + "/user/pageList",
            type:"post",
            data : function ( d ) {
                var obj = {};
                obj.userName = $('#userName').val();
                obj.type = $('#type').val();
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
			{ "data": 'userName', "visible" : true, "bSortable": false, 'width': '40%'},
			{
				"data": 'type',
				"visible" : true,
				"bSortable": false,
				'width': '30%',
                "render": function ( data, type, row ) {
					// 用户类型：0-普通用户、1-管理员
					var htm = '';
					if (data == 0) {
						htm = '普通用户';
					} else {
						htm = '管理员';
					}
					return htm;
				}
			},
			{
				"data": '操作' ,
				"width":'30%',
				"bSortable": false,
				"render": function ( data, type, row ) {
					return function(){

                        var permissionBiz = '';
                        if (row.type != 1) {
                            permissionBiz = '<button class="btn btn-warning btn-xs permissionBiz" type="button">分配业务线权限</button>  ';
                        }

						// html
						var html = '<p id="'+ row.id +'" '+
							' userName="'+ row.userName +'" '+
							' password="'+ row.password +'" '+
							' type="'+ row.type +'" '+
                            ' permissionBiz="'+ row.permissionBiz +'" '+
							'>'+
							'<button class="btn btn-warning btn-xs update" >编辑</button>  '+
                            permissionBiz +
							'<button class="btn btn-danger btn-xs delete" >删除</button>  '+
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
        userListTable.fnDraw();
    });

	// job operate
	$("#user_list").on('click', '.delete',function() {
		var id = $(this).parent('p').attr("id");

        layer.confirm( "确认删除该用户?" , {
            icon: 3,
            title: '系统提示' ,
            btn: [ '确定', '取消' ]
        }, function(index){
            layer.close(index);

			$.ajax({
				type : 'POST',
				url : base_url + "/user/delete",
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
                                userListTable.fnDraw(false);
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

    // jquery.validate 自定义校验
    jQuery.validator.addMethod("userNameValid", function(value, element) {
        var length = value.length;
        var valid = /^[a-z][a-z0-9.]*$/;
        return this.optional(element) || valid.test(value);
    }, "限制以小写字母开头，由小写字母、数字组成");

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
                rangelength: [4, 50],
				userNameValid: true
			},
			password : {
            	required : true,
                rangelength: [4, 50]
            }
        }, 
        messages : {
			userName : {
            	required :"请输入登录账号",
                rangelength : "登录账号长度限制为4~50"
            },
			password : {
            	required :"请输入密码",
                rangelength : "密码长度限制为4~50"
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

                    layer.open({
                        icon: '1',
                        content: "新增成功" ,
                        end: function(layero, index){
                            userListTable.fnDraw(false);
                        }
                    });
    			} else {
                    layer.open({
                        icon: '2',
                        content: (data.msg || "新增失败")
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


    $("#updateModal .form input[name='passwordInput']").change(function () {
        var passwordInput = $("#updateModal .form input[name='passwordInput']").prop('checked');
        $("#updateModal .form input[name='password']").val( '' );
        if (passwordInput) {
            $("#updateModal .form input[name='password']").removeAttr("readonly");
        } else {
            $("#updateModal .form input[name='password']").attr("readonly","readonly");
        }
    });


	// 更新
	$("#user_list").on('click', '.update',function() {

		// base data
		$("#updateModal .form input[name='id']").val($(this).parent('p').attr("id"));
		$("#updateModal .form input[name='userName']").val($(this).parent('p').attr("userName"));
		$("#updateModal .form input[name='type']").eq($(this).parent('p').attr("type")).click();

        $("#updateModal .form input[name='passwordInput']").prop('checked', false);
        $("#updateModal .form input[name='password']").val( '' );
        $("#updateModal .form input[name='password']").attr("readonly","readonly");

		// show
		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,
        rules : {
            password : {
                required : true,
                rangelength: [4, 50]
            }
        },
        messages : {
            password : {
                required :"请输入密码",
                rangelength : "密码长度限制为4~50"
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
    		$.post(base_url + "/user/update", $("#updateModal .form").serialize(), function(data, status) {
    			if (data.code == "200") {
					$('#updateModal').modal('hide');

                    layer.open({
                        icon: '1',
                        content: "更新成功" ,
                        end: function(layero, index){
                            userListTable.fnDraw(false);
                        }
                    });
    			} else {
                    layer.open({
                        icon: '2',
                        content: (data.msg || "更新失败")
                    });
    			}
    		});
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
		$("#updateModal .form")[0].reset()
	});


    // 分配项目权限
    $("#user_list").on('click', '.permissionBiz',function() {
        var id = $(this).parent('p').attr("id");
        var permissionBiz = $(this).parent('p').attr("permissionBiz");
        $("#updatePermissionBizModal .form input[name='id']").val( id );

        var permissionBizChoose;
        if (permissionBiz) {
            permissionBizChoose = $(permissionBiz.split(","));
        }
        $("#updatePermissionBizModal .form input[name='permissionBiz']").each(function () {
            if ( $.inArray($(this).val(), permissionBizChoose) > -1 ) {
                $(this).prop("checked",true);
            } else {
                $(this).prop("checked",false);
            }
        });

        $('#updatePermissionBizModal').modal('show');
    });
    $('#updatePermissionBizModal .ok').click(function () {
        $.post(base_url + "/user/updatePermissionBiz", $("#updatePermissionBizModal .form").serialize(), function(data, status) {
            if (data.code == 200) {
                layer.open({
                    icon: '1',
                    content: '操作成功' ,
                    end: function(layero, index){
                        userListTable.fnDraw(false);
                        $('#updatePermissionBizModal').modal('hide');
                    }
                });
            } else {
                layer.open({
                    icon: '2',
                    content: (data.msg||'操作失败')
                });
            }
        });
    });
    $("#updatePermissionBizModal").on('hide.bs.modal', function () {
        $("#updatePermissionBizModal .form")[0].reset()
    });

});
