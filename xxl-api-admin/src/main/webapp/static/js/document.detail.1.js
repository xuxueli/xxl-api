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
                    layer.open({
                        icon: '1',
                        content: "Mock数据保存成功" ,
                        end: function(layero, index){
                            window.location.reload();
                        }
                    });
				} else {
                    layer.open({
                        icon: '2',
                        content: (data.msg||"Mock数据保存失败")
                    });
				}
			},
		});

	});

	/**
	 * 删除，Mock数据
	 */
	$('.deleteMock').click(function () {
		var id = $(this).attr('_id');

        layer.confirm( "确认删除该Mock数据?" , {
            icon: 3,
            title: '系统提示' ,
            btn: [ '确定', '取消' ]
        }, function(index){
            layer.close(index);

            $.ajax({
                type : 'POST',
                url : base_url + "/mock/delete",
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
                    layer.open({
                        icon: '1',
                        content: "Mock数据保存成功" ,
                        end: function(layero, index){
                            window.location.reload();
                        }
                    });
				} else {
                    layer.open({
                        icon: '2',
                        content: (data.msg||"Mock数据保存失败")
                    });
				}
			},
		});
	});


	/**
	 * 删除，Test历史
	 */
	$('.deleteTest').click(function () {
		var id = $(this).attr('_id');

        layer.confirm( "确认删除该Test历史?" , {
            icon: 3,
            title: '系统提示' ,
            btn: [ '确定', '取消' ]
        }, function(index){
            layer.close(index);

            $.ajax({
                type : 'POST',
                url : base_url + "/test/delete",
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
                }
            });
        });

	});


    /**
     * json pre view
     */
    $('.jsonViewPre').each(function () {

        try {
            var jsonStr = $(this).text();
            var json = $.parseJSON(jsonStr);
            $(this).JSONView(json, { collapsed: false, nl2br: true, recursive_collapser: true });
        } catch (e) {
            console.log('jsonViewDiv parse json view error,' + e );
        }
    });

});
