<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<%@include file="/pub/pub.jsp"%>
<base href="<%=basePath%>">
<title></title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!-- https://www.cnblogs.com/MuYunyun/p/5692385.html -->
<style>

ul, li {
	list-style: none;
}

li {
	float: left;
	padding: 1.2rem 2.8rem;
	background: #fbfbfb;
	text-align: center;
}

li i {
	font-style: normal;
	display: block;
}

.btns ul {
	margin: 0 auto;
	display: table;
}

.iconfont {
	font-size: 2.4rem;
}

.border_left {
	border-left: 1px solid #ccc;
}

.border_right {
	border-right: 1px solid #ccc;
}

.tm_m_b {
	padding-bottom: 6.4rem;
}


.tmhide{
	display: none;
}

.wrapHeight{
	height:45rem;
}

@media only screen and (max-width: 650px) {
	.iconfont {
		font-size: 1.6rem;
	}
	li {
		padding: 0.2rem 0.8rem;
	}
	.tm_m_b {
		padding-bottom: 5.5rem;
	}
	
	.table>tbody>tr>td{
		padding:2px;
	}
}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row clearfix">
			<div class="col-md-12 column" style="padding: 0;">
				<div class="wrapHeight">
					<table class="table table-hover" id="stockList">
					</table>
				</div>
				
				<div id="zhiShuPointer">
					上证：<span id="sh"></span>
					深证：<span id="sz"></span>
					创业板：<span id="cy"></span>
				</div>
				<div class="m-style" id="pagination"></div>
				<div class="tm_m_b"></div>
			</div>
		</div>
	</div>

	<nav class="navbar navbar-default navbar-fixed-bottom">
	<div class="container">
		<div class="btns">
			<ul class="clearfix">
				<li class="clearfix border_left" onclick="toAll()">
					<i class="iconfont  icon-leimu"></i> <i>所有</i>
				</li>
				<li class="clearfix border_left" onclick="toAdd()">
					<i class="iconfont  icon-jiahao2fill"></i> <i>添加</i>
				</li>
				<li class="clearfix border_left">
					<i class="iconfont  icon-dingdan"></i> <i>编辑</i>
				</li>
				<li class="clearfix border_left">
					<i class="iconfont  icon-gonglve"></i> <i>预警</i>
				</li>
				<li class="clearfix border_left border_right">
					<i class="iconfont  icon-shezhi"></i> <i>邮件</i>
				</li>
			</ul>
		</div>
	</div>
	</nav>
	<script>
	
	
	
		var pageSize = 10; //每页显示多少条记录
		var total; //总共多少记录
		var pageForRefresh=0;
		$(function() {
			Init(0); //注意参数，初始页面默认传到后台的参数，第一页是0;    
			$("#pagination").pagination({ 
				    pageCount: total,
				    jump: true,
				    coping: true,
				    homePage: '首页',
				    endPage: '末页',
				    prevContent: '上页',
				    nextContent: '下页',
				    callback: function (api) {
				       Init(api.getCurrent())
				    }
			});
			
			
			setInterval(function() {
				Init(pageForRefresh);
			}, 1000)
			
		});
	
		function Init(pageIndex) { //这个参数就是点击的那个分页的页数索引值，第一页为0，上面提到了，下面这部分就是AJAX传值了。
			$.ajax({
				type : "post",
				url : "stock/quShi?page=" + pageIndex+"&rows=" + pageSize,
				async : false,
				dataType : "json",
				success : function(data) {
					var dataTotal = data.total;
					var dataObj = data.result;
					pageForRefresh = pageIndex;
					total = getTotalPage(dataTotal,pageSize);
					dataToHtml(dataObj);
				},
				error : function() {
					alert("请求超时，请重试！");
				}
			});
		}
		
		
		
		function getTotalPage(total,pageSize){
			var page = 0;
			if(total%pageSize==0){
				page =  total/pageSize;
			}else{
				page = Math.ceil(total/pageSize);
			}
			return page;
		}
		
		
		
			
		function dataToHtml(code,dataObj){
		 	$("#stockList").empty();
			var _html ="";
			if(!$.isEmptyObject(dataObj)){
				 _html = "<tr class='warning'><td >代码</td><td >名称</td><td >当前</td><td >昨收</td><td >前二天</td><td >前三天</td><td >前四天</td><td >前五天</td><td >前六天</td><td >前七天</td></tr>";
				for (var i = 0; i < dataObj.length; i++) {
					_html += "<tr class='info' style='color:red'><td >" + dataObj[i].code + "</td><td >" + dataObj[i].name + "</td><td >" + dataObj[i].price+"</td><td >" + dataObj[i].preClosePrice + "</td><td >" + dataObj[i].price + "</td><td >" + dataObj[i].price + "</td><td >" + dataObj[i].price + "</td><td >" + dataObj[i].price + "</td><td >" + dataObj[i].price + "</td><td >" + dataObj[i].price + "</td></tr>";
	
				}
				}else{
				_html ="<tr colspan='6' style='text-align='center''>无对应信息<td></td></tr>";
			}
			
			$("#stockList").append(_html);
		}
	</script>
</body>
</html>
