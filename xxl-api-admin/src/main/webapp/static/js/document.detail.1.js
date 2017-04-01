$(function() {

	testEditormdView2 = editormd.markdownToHTML("remark", {
		htmlDecode      : "style,script,iframe",  // you can filter tags decode
		emoji           : false,
		taskList        : false,
		tex             : false,  // 默认不解析
		flowChart       : false,  // 默认不解析
		sequenceDiagram : false,  // 默认不解析
	});

	var remarkEditor = editormd.markdownToHTML("remark2", {
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
	});

});
