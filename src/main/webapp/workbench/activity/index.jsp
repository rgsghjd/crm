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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		$("#addbtn").click(function () {
			$.ajax({
				url:"workbench/activity/getUserList.do",
				dataType:"json",
				type:"get",
				success:function (data) {
					var ohtml="<option></option>";
					$.each(data,function (i,n) {
						//遍历获取的userList
						ohtml+="<option value='"+n.id+"'>"+n.name+"</option>"
					})
					$("#createOwner").html(ohtml);
					var id="${User.id}";
					//用模态窗口的jQuery对象的Modal,传递参数hide(隐藏窗口),show(显示)
					$("#createOwner").val(id);
					$("#createActivityModal").modal("show")
				}
			})


		})
		//创建活动的保存事件
		$("#saveacti").click(function () {
			$.ajax({
				url:"workbench/activity/save.do",
				data:{

					"owner":$.trim($("#createOwner").val()),
					"name":$.trim($("#create-Name").val()),
					"startDate":$.trim($("#create-startTime").val()),
					"endDate":$.trim($("#create-endTime").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-describe").val())


				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.success){
						//保存成功
						//刷新信息列表
						//刷新模态窗口

						$("#saveform")[0].reset();
						//关闭模态窗口
						$("#createActivityModal").modal("hide");
						PageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

					}else {
						alert("保存失败")
					}
				}
			})
		})
		PageList(1,2);
		$("#searchactivity").click(function () {
			/**
			 * 将查询条件保存在隐藏域中，每次异步请求读取隐藏域中的值
			*/
			$("#find-name").val($.trim($("#search-name").val()))
			$("#find-owner").val($.trim($("#search-owner").val()))
			$("#find-startDate").val($.trim($("#search-startTime").val()))
			$("#find-endDate").val($.trim($("#search-endTime").val()))
			PageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

		})
		$("#qx").click(function () {
			/**
			 * 为市场活动复选框绑定全选
			 */
			$("input[name=xz]").prop("checked",this.checked)

		})
		/**
		 * 为市场活动记录的选择框绑定事件，由于是动态生成的，因此不能用普通的绑定事件方法
		 *
		 */
		$("#activityList").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]:checked").length==$("input[name=xz]").length)
		})
		/**
		 * 为市场活动删除绑定事件
		 * */
		$("#deletebtn").click(function () {
			//获取要删除的记录
			var $obj=$("input[name=xz]:checked")

			if ($obj.length==0){
				alert("请选择你要删除的记录!");
			}else {
				//拼接参数
				var param="";
				$.each($obj,function (i,n) {
					param+="id="+ $($obj[i]).val();
					if(i<$obj.length-1){
						param+="&";
					}

				})

				if (confirm("确定删除吗？")){
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						dataType:"json",
						type:"post",
						success:function (data) {
							if(data.success){
								PageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							}else {
								alert("删除失败!")
							}
						}
					})
				}
			}
		})
		/**
		 *
		 * 修改市场活动模块，为市场活动修改按钮绑定事件
		 *
		 * */
		$("#editModel").click(function () {
			//查询所有者和一条市场活动
			var $obj=$("input[name=xz]:checked");
			if($obj.length==0){
				alert("请选择你要修改的记录!");
			}else if ($obj.length>1){
				alert("只能选择一条记录修改!");
			}else {
				//用户只选择了一条记录
				var id = $obj.val();
				$.ajax({
					url:"workbench/activity/edit.do",
					data:{
						"id":id
					},
					dataType:"json",
					type:"post",
					success:function (data) {

						var html=""
						//需要后端返回一个所有者用户列表，和一条选中的记录的详细信息
						$.each(data.uList,function (i,n) {
								html += "<option value='"+n.id+"'>"+n.name+"</option>"
						})
						$("#edit-Owner").html(html);
						$("#edit-Name").val(data.a.name);
						$("#edit-startTime").val(data.a.startDate);
						$("#edit-endTime").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-describe").val(data.a.description);
						$("#edit-id").val(data.a.id);
						$("#editActivityModal").modal("show");
					}
				})
			}
		})
		$("#updatebtn").click(function () {
			var id=$("#edit-id").val();
            var id2="${User.id}";
            //用模态窗口的jQuery对象的Modal,传递参数hide(隐藏窗口),show(显示)
            $("#edit-Owner").val(id2);
			$.ajax({
				data:{
					"id":id,
					"owner":$.trim($("#edit-Owner").val()),
					"name":$.trim($("#edit-Name").val()),
					"startDate":$.trim($("#edit-startTime").val()),
					"endDate":$.trim($("#edit-endTime").val()),
					"cost":$.trim($("#edit-cost").val()),
					"description":$.trim($("#edit-describe").val())
				},
				url:"workbench/activity/update.do",
				dataType:"json",
				type:"post",
				success:function(data){
					if(data.success){



						PageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						$("#editActivityModal").modal("hidden");
					}else {
						alert("修改失败!")
					}
				}
			})
		})
	});
	function PageList(PageNo,PageSize) {
		$("#qx").prop("checked",false);
		$("#search-name").val($.trim($("#find-name").val()))
		$("#search-owner").val($.trim($("#find-owner").val()))
		$("#search-startDate").val($.trim($("#find-startTime").val()))
		$("#search-endDate").val($.trim($("#find-endTime").val()))
		//发送异步请求，进行分页查询
		$.ajax({
			url:"workbench/activity/activityList.do",
			data:{
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startTime":$.trim($("#search-startTime").val()),
				"endTime":$.trim($("#search-endTime").val()),
				"PageNo":PageNo,
				"PageSize":PageSize
			},
			dataType:"json",
			type:"get",
			success:function (data) {
				/*
				需要后端返回两个数据，一个是市场活动列表(展现使用)，一个是获取的总条数(用于分页控件使用)
				data={"total":num,"dataList":dataList}
				 */
				//对拿到的市场活动展现
				var inhtml="";
				$.each(data.dataList,function (i,n) {

					inhtml +='<tr class="active">'
					inhtml +='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>'
					inhtml +='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>'
					inhtml +='<td>'+n.owner+'</td>'
					inhtml +='<td>'+n.startDate+'</td>'
					inhtml +='<td>'+n.endDate+'</td>'
					inhtml +='</tr>'

				}

				)
				$("#activityList").html(inhtml)
				var totalPages= data.total%PageSize==0?data.total/PageSize:parseInt(data.total/PageSize)+1;
				$("#activityPage").bs_pagination({
					currentPage: PageNo, // 页码
					rowsPerPage: PageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						PageList(data.currentPage , data.rowsPerPage);
					}
				});


			}
		})


	}
</script>
</head>
<body>
<input type="hidden" id="edit-id">
<input type="hidden" id="find-name"/>
<input type="hidden" id="find-owner"/>
<input type="hidden" id="find-startDate"/>
<input type="hidden" id="find-endDate"/>
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="saveform">
					
						<div class="form-group">
							<label for="createOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="createOwner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-Name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label" >开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label" >结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveacti">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-Owner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-Name" >
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label ">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime" >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label ">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="updatebtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon ">开始日期</div>
					  <input class="form-control time" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon ">结束日期</div>
					  <input class="form-control time" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchactivity">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addbtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editModel"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deletebtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityList">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;" id="activityPage">



			</div>
			
		</div>
		
	</div>
</body>
</html>