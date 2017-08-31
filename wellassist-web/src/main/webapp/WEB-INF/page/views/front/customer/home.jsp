<%@ include file="header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<html lang="en">
<head>

	<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/pagetempl.css"/>">
	<link rel="stylesheet" href="<c:url value="/resources/library/css/semantic.min.css"/>">
	<title>买家网站</title>

	<style>
		.start_heading2{padding-top:20px;padding-bottom:20px;font-weight:500;font-size:24px;}
		.ware_button{float:right;color: white;background: #ff5500;padding: 6px;width: 61px;text-align: center;cursor:hand;font-size:16px;}
		.ware_maker_label{color: #ff5500;padding: 6px 0px;float: left;font-size:14px}
		.buyer_start_item{background: white;border: #c0c0c0;border-style: solid;border-width: 1px;}
		.home-table img{width:100%;}
		.img-title{width:100%; background:rgba(0, 0, 0, 0.5); position:absolute; bottom: 0px;}
		.img-title>span{padding:10px;line-height:30px;color:#ffffff; }
		
		.cd-rect{width: 1000px; margin: auto;overflow: auto;}
		.row-fld{width:230px; background: white;box-shadow: 3px 3px 3px #c0c0c0;padding:0px;overflow: auto; float: left; margin-top:10px; margin-right:20px;}
		.sp_img{width:230px; height:169px;}
		.container1{
			background: url(../img/watermark.png) repeat;
			background-color: #f2f2f2;
		}
		.extra {background-color: #E8EFF5;color: #617B90;}
	</style>

</head>
<body>
	<div class="container1">
    	<div style="margin:40px 0 0 210px;">

		<!-- navigation bar -->
		<!-- <div class="abovenavbar">
			<div align=center>
				<div style="width:1000px;">
					<span class="dropdown" style="float:left;">
						<a data-toggle="dropdown" class="dropdown">${user.userName}&nbsp;<b class="icon-angle-down"></b></a>
						<div class="dropdown-menu">
							<div id="logout">退出登录</div>
						</div>
					</span>
					<span style="float: right;"><a href="<c:url value="/login/customerMain"/>">管理员</a></span>
				</div>
			</div>
		</div> -->
			<div style="width:90%;">
				<img src="<c:url value="/resources/wella/front/images/customer/banner.png"/>" style="width:100%;">

				<div class="ui cards" style="margin-top:10px;">
					<div class="card" style="width:69%;">
						<div class="extra content" style="background-color: #E8EFF5;font-size:18px;">
							<span class="left floated" style="color: #617B90;font-weight:700;"><i class="selected radio icon"></i> 公告栏 </span>
							<span class="right floated"> More <i class="angle double right icon"></i></span>
						</div>
						<div class="content">

						</div>						
					</div>
					<div class="card" style="width:29%;">
						<div class="extra content" style="background-color: #E8EFF5;font-size:18px;">
							<span class="left floated" style="color: #617B90;font-weight:700;"><i class="selected radio icon"></i> 联系方式 </span>
							<span class="right floated"> More <i class="angle double right icon"></i></span>
						</div>
						<div class="content">
							<span>&emsp;联系人：李生
								<img src="<c:url value="/resources/wella/front/images/customer/lianxiicon1.png"/>">
								<img src="<c:url value="/resources/wella/front/images/customer/lianxiicon2.png"/>">
								<img src="<c:url value="/resources/wella/front/images/customer/lianxiicon3.png"/>"></span><br>
							<span>联系电话：${userInfo.companyLxrPhone}</span><br>
							<span>联系地址：${address}</span><br>
						</div>						
					</div>
				</div>
				<div class="ui card" style="width:100%;margin-top:20px;">
					<div class="extra content" style="background-color: #E8EFF5;font-size:18px;">
						<span class="left floated" style="color: #617B90;font-weight:700;"><i class="selected radio icon"></i> 企业信息 </span>
						<span class="right floated"> More <i class="angle double right icon"></i></span>
					</div>
					<div class="content">
						<p>&emsp;&emsp;中石油昆仑能源有限公司（简称昆仑能源公司）市委实现天然气业务上中下游一体化，更好地履行责任，服务社会， 经中国石油天然气集团公司批准、国家工商管理总局核准于2008年8月6日，由中石油天然气管道燃气投资有限公司、中国华油集团燃气事业部、中油然气有限责任公司、重组整合成立， 是中国石油城市燃气运菅的专业化公司。公司注册资本金60.6亿元</p>
					</div>
				</div>
				<!-- <div class="start_heading2">${user.userName}</div>
				<div style="display:block; overflow:auto">
					<div style="float:left;width:200px;height:200px;background:white;border:solid 1px #c0c0c0;">
						<table style="width:100%;height:100%;"><tr><td><p align=center><img src="<c:url value="${userInfo.companyImg}"/>"></p></td></tr></table>
					</div>
					<div style="float:left;margin-left:20px;margin-right:20px;width:504px;height:200px;background:white;border:solid 1px #c0c0c0;">
						<div style="padding-left: 10px;padding-top: 16px;font-size: 20px;font-weight:500;">公告栏</div>
						<marquee direction="up" style="height: 155px; margin-top: 5px;" loop="infinite" scrollamount="3">
							<c:forEach items="${noticeList}" var="item" varStatus="status">
		                    	<div onclick="viewNotice(${item.noticeId})" style="cursor:pointer;padding-left:25px;padding-top: 16px;font-size: 15px;color:#666666">${item.createDate} &nbsp;&nbsp; ${item.noticeTitle}</div>
		                    </c:forEach>
						</marquee>
					</div>
					<div style="float:left;width:250px;height:200px;background:white;border:solid 1px #c0c0c0;">
						<div style="padding-left: 10px;padding-top: 16px;font-size: 20px;font-weight:500;">联系方式</div>
						<table style="width:100%;margin-top:32px;font-size:15px;" >
						<tr>
							<td style="text-align:right; width:75px; padding-right:5px; height:30px">联系人:</td>
							<td>
								<img src="<c:url value="/resources/wella/front/images/customer/lianxiicon1.png"/>" style="float:left;">
								<img src="<c:url value="/resources/wella/front/images/customer/lianxiicon2.png"/>" style="float:left;">
								<img src="<c:url value="/resources/wella/front/images/customer/lianxiicon3.png"/>" style="float:left;">
							</td>
						</tr>
						<tr>
							<td style="text-align:right; width:75px; padding-right:5px; height:30px">联系电话:</td>
							<td>${userInfo.companyLxrPhone}</td>
						</tr>
						<tr>
							<td style="text-align:right; width:75px; padding-right:5px; height:30px">联系地址:</td>
							<td>${address}</td>
						</tr>
						</table>
					</div>
				</div>
				
				<div class="start_heading2" style="font-size:22px; margin-top:30px;">企业介绍</div>
				<div style="font-size:15px;line-height:150%;color:#666666">
				中石油昆仑能源有限公司（简称昆仑能源公司）市委实现天然气业务上中下游一体化，更好地履行责任，
			服务社会， 经中国石油天然气集团公司批准、国家工商管理总局核准于2008年8月6日， 由中石油天然气管道燃气投资有限公司、中国华油集团燃气事业部、中油然气有限责任公司、重组整合成立， 是中国石油城市燃气运菅的专业化公司。公司注册资本金60.6亿元。
				</div>

				<div class="start_heading2" style="font-size:22px; margin-top:30px;">产品列表</div>
				
				<div class = "cd-rect" style="padding:5px;">
					 <c:forEach var="item" items="${spList}">
							<div  class = "row-fld" style="">
								<div style="position:relative;">
									<%--<a href="${pageContext.request.contextPath}/front/customer/CustomerHomeCtrl-prodDetail?prodId=${item.prodId}">--%>
										<a href="#">
										<img class = "sp_img" src="${item.prodImg}" onerror = "noExitImg(this, '${pageContext.request.contextPath}');">
									</a>
									<div class="img-title"><span>
										<c:if test = "${item.prodType == '0'}">
											气体
										</c:if>
										<c:if test = "${item.prodType == '1'}">
											燃油
										</c:if>
										</span></div>
								</div>
								<div style="padding:10px;">
									<span class="ware_maker_label">${item.prodName}</span>
									<a href="${pageContext.request.contextPath}/customer/makeOrder?prodId=${item.prodId}">
										<span class="ware_button" style="float:right; <c:if test = "${user.userState != 1}">display:none;</c:if>">下单</span>
									</a>
									<div style="clear:both;"></div>
								</div>
							</div>
				 	</c:forEach> 
				</div> -->
			</div>
			<!-- <div style="width:98%;margin:0 auto;"> -->
				<div style="width:100%;background-color:#4F657C;color:#fff;position:fixed;bottom:0;">
					<marquee direction="left" behavior="scroll" scrollamount="10" scrolldelay="0" loop="-1" hspace="10" vspace="10">
						<i class="announcement icon"></i>实时消息：您已选择 智速货运公司 为订单 2017081715494008107464 提供物流服务，请支付物流订单后，物流公司将前往卖方地址为您载货。请注意查收！
					</marquee>
				</div>
			<!-- </div> -->
		</div>
		
	</div>
</body>
<script type="text/javascript">
	<%--/*function show_companies() {--%>
		<%--window.location.href="<c:url value="/front/customer/show_companies"/>";--%>
	<%--}*/--%>
	
	// 功能函数
	function toURL(action, id){
		var url = "";
	}
	
	$("#logout").click( function(){
		window.location.href="<c:url value="/login/out"/>";
	});
</script>
</html>