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
					ComAlert.show(1, "Mock数据保存成功", function(){
						window.location.reload();
					});
				} else {
					ComAlert.show(2, (data.msg || "Mock数据保存失败") );
				}
			},
		});

	});

	/**
	 * 删除，Mock数据
	 */
	$('.deleteMock').click(function () {
		var id = $(this).attr('_id');
		ComConfirm.show("确认删除该Mock数据?", function(){
			$.ajax({
				type : 'POST',
				url : base_url + "/mock/delete",
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
					ComAlert.show(1, "Mock数据保存成功", function(){
						window.location.reload();
					});
				} else {
					ComAlert.show(2, (data.msg || "Mock数据保存失败") );
				}
			},
		});
	});


	/**
	 * 删除，Test历史
	 */
	$('.deleteTest').click(function () {
		var id = $(this).attr('_id');
		ComConfirm.show("确认删除该Test历史?", function(){
			$.ajax({
				type : 'POST',
				url : base_url + "/test/delete",
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
				}
			});
		});
	});

});
