$(function(){
	// 复选框
    $('input').iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue',
      increaseArea: '20%' // optional
    });
    
	// 登陆.规则校验
	var loginFormValid = $("#loginForm").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {  
        	userName : {  
        		required : true ,
                rangelength: [4, 50]
            },  
            password : {  
            	required : true ,
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
			$.post(base_url + "/login", $("#loginForm").serialize(), function(data, status) {
				if (data.code == "200") {
                    layer.msg( '登陆成功' );
                    setTimeout(function(){
                        window.location.href = base_url;
                    }, 500);
				} else {
                    layer.open({
                        icon: '2',
                        content: (data.msg||'登陆失败')
                    });
				}
			});
		}
	});
});