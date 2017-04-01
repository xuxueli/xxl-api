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

	$(".textarea_remark").wysihtml5();

	/**
	 * 请求头部，新增一行
	 */
	$('#requestHeaders_add').click(function () {
		var requestHeaders_item = $('#requestHeaders_example').html();
		$('#requestHeaders_parent').append(requestHeaders_item);

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
		$(this).parents('.item').remove();
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
		$(this).parents('.item').remove();
	});




});
