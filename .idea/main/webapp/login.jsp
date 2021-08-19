<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-3.4.1.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript">

		$(function () {
			//页面刷新时将页面文本框内容清空
			$("#userAct").val("")
			$("#userPwd").val("")

			//刷新页面时，用户文本自动获得焦点
			$("#userAct").focus();
			//按回车健，自动提交信息
			$(window).keydown(function (event) {
				if (event.keyCode==13){
					login()
				}

			});
			$("#loginbtn").click(
					function () {
						login()
					}
			)


		})
		function login() {
		var act=$.trim($("#userAct").val());
		var pwd=$.trim($("#userPwd").val());

		if (act==""||pwd==""){
			$("#msg").html("用户名或则密码不能为空！");
		}else{
		    $.ajax({
                data:{"UserAct":act,"UserPwd":pwd},//传递的数据
                Type:"post",//请求方式
                url:"settings/user/login.do",//请求地址
                dataType:"json",//返回数据格式
                success:function (data) {
                    if(data.success==true){
                        //验证成功进行页面跳转
                        $(window).location.href="workbench/index.html"
                    }else {
                        $("#msg").html(data.msg);
                    }
                }
            })
        }

		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;"></span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="userAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="userPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<button id="loginbtn" type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>