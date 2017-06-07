$(function() {

	// base init
	$(".select2").select2();
	$(".select2_tag").select2({tags: true});

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

			if ($select2.hasClass("fieldDatatypeId")) {
				// 数据类型 ajax 加载
				$($select2).select2({
					ajax: {
						type:'GET',
						url: base_url + "/datatype/pageList",
						dataType: 'json',
						delay: 250,
						data: function (params) {
							return {
								bizId: -1,
								start:0,
								length:100,
								name: params.term, // search term
								page: params.page
							};
						},
						processResults: function (data, params) {
							params.page = params.page || 1;

							var itemList = [];//当数据对象不是{id:0,text:'ANTS'}这种形式的时候，可以使用类似此方法创建新的数组对象
							var arr = data.data;
							for(i in arr){
								itemList.push({id: arr[i].id, text: arr[i].name})
							}
							return {
								results: itemList,	//data.items
								pagination: {
									more: (params.page * 30) < data.total_count
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
			} else {
				$($select2).select2();
			}

		});
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
						ComAlert.show(2, '字段名称不可为空');
						return;
					}
					if (!fieldAbout) {
						ifError = true;
						ComAlert.show(2, '字段描述不可为空');
						return;
					}
					if (!fieldDatatypeId) {
						ifError = true;
						ComAlert.show(2, '字段数据类型不可为空');
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
				"bizId": $("#datatypeForm select[name=bizId]").val(),
				"name": $("#datatypeForm input[name=name]").val(),
				"about": $("#datatypeForm input[name=about]").val(),
				"fieldTypeJson":fieldTypeJson
			}

			$.post(base_url + "/datatype/addDataType", data, function(data, status) {
				if (data.code == "200") {
					$('#addModal').modal('hide');
					setTimeout(function () {
						ComAlert.show(1, "新增成功", function(){
							window.location.href  = base_url + '/datatype/dataTypeDetail?dataTypeId=' + data.content;
						});
					}, 315);
				} else {
					ComAlert.show(2, (data.msg || "新增失败") );
				}
			});
		}
	});


});
