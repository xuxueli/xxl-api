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


});
