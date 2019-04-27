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
