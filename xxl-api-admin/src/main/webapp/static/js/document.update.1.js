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
							ComAlert.show(2, '新增接口失败，请检查"请求头部"数据是否填写完整');
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
							ComAlert.show(2, '新增接口失败，请检查"请求参数"数据是否填写完整');
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
							ComAlert.show(2, '新增接口失败，请检查"响应结果参数"数据是否填写完整');
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
					setTimeout(function () {
						ComAlert.show(1, "更新成功", function(){
							var id = $('#ducomentForm input[name=id]').val();
							window.location.href  = base_url + '/document/detailPage?id=' + id;
						});
					}, 315);
				} else {
					ComAlert.show(2, (data.msg || "更新失败") );
				}
			});
		}
	});


});
