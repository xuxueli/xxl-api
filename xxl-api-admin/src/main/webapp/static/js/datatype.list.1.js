$(function() {

	// init date tables
	var projectTable = $("#project_list").dataTable({
		"deferRender": true,
		"processing" : true,
		"serverSide": true,
		"ajax": {
			url: base_url + "/datatype/pageList",
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
			{ "data": 'name', "bSortable": true, "width":'20%'},
			{ "data": 'about', "visible" : true, "width":'20%'},
			{
				"data": '操作' ,
				"width":'10%',
				"render": function ( data, type, row ) {
					return function(){

						var updateUrl = base_url + '/datatype/updateDataTypePage?dataTypeId='+ row.id;
						var detailUrl = base_url + '/datatype/dataTypeDetail?dataTypeId='+ row.id;

						// html
						var html = '<span id="'+ row.id +'" >'+
							'<button class="btn btn-warning btn-xs update" type="button" onclick="javascript:window.open(\'' + updateUrl + '\')" >编辑</button>  '+
							'<button class="btn btn-danger btn-xs delete" type="button">删除</button>'+
							'<button class="btn btn-info btn-xs" type="button" onclick="javascript:window.open(\'' + detailUrl + '\')" >详情页</button>  '+
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

	// job operate
	$("#project_list").on('click', '.delete',function() {
		var id = $(this).parent('span').attr("id");
		ComConfirm.show("确认删除该数据类型?", function(){
			$.ajax({
				type : 'POST',
				url : base_url + "/datatype/deleteDataType",
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


});
