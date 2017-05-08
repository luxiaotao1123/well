<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true"%>
<!DOCTYPE HTML>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>卖家管理员页面</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="Admin Panel Template">
		<meta name="author" content="Westilian: Kamrujaman Shohel">

		<link rel="stylesheet" href="<c:url value="/resources/library/css/bootstrap.css"/>">
		<link rel="stylesheet" href="<c:url value="/resources/library/css/bootstrap-responsive.css"/>">
		<link rel="stylesheet" href="<c:url value="/resources/library/css/font-awesome.css"/>">
		<!--[if IE 7]>
		<link rel="stylesheet" href="<c:url value="/resources/library/css/font-awesome-ie7.min.css"/>">
		<![endif]-->
		<link rel="stylesheet" href="<c:url value="/resources/library/css/chosen.css"/>">
		<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/styles.css"/>">
		<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/pagetempl.css"/>">
		<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/theme-blue.css"/>">
		<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/library/css/ie/ie7.css"/>">
		<![endif]-->
		<!--[if IE 8]>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/library/css/ie/ie8.css"/>">
		<![endif]-->
		<!--[if IE 9]>
		<link rel="stylesheet" type="text/css" href="<c:url value="/resources/library/css/ie/ie9.css"/>">
		<![endif]-->
		<link rel="shortcut icon" href="<c:url value="/resources/wella/common/ico/wellassist.png"/>">
		<link rel="apple-touch-icon-precomposed" sizes="144x144" href="<c:url value="/resources/wella/common/ico/wellassist-144.png"/>">
		<link rel="apple-touch-icon-precomposed" sizes="114x114" href="<c:url value="/resources/wella/common/ico/wellassist-114.png"/>">
		<link rel="apple-touch-icon-precomposed" sizes="72x72"   href="<c:url value="/resources/common/ico/wellassist-72.png"/>">
		<link rel="apple-touch-icon-precomposed" href="<c:url value="/resources/wella/common/ico/wellassist-57.png"/>">
		<!--============ javascript ===========-->
		<script src="<c:url value="/resources/library/js/jquery.js"/>"></script>
		<script src="<c:url value="/resources/library/js/jquery-ui-1.10.1.custom.min.js"/>"></script>
		<script src="<c:url value="/resources/library/js/bootstrap.js"/>"></script>
		<script src="<c:url value="/resources/wella/common/js/accordion.nav.js"/>"></script>
		<script src="<c:url value="/resources/common/js/global.js"/>"></script>
		<script src="<c:url value="/resources/common/js/AlertWindow.js"/>"></script>
		
		<script src="${pageContext.request.contextPath}/resources/common/js/jquery.validate.js"></script>
	    <script src="${pageContext.request.contextPath}/resources/houtai/js/jquery.form.js"></script>
	    
	    <link   href="${pageContext.request.contextPath}/resources/houtai/css/jquery.fancybox.css" rel="stylesheet" />
		<script src="${pageContext.request.contextPath}/resources/houtai/js/jquery.fancybox.js"></script>
		
		<script src="${pageContext.request.contextPath}/resources/library/js/My97DatePicker/WdatePicker.js"></script>
		<script src="${pageContext.request.contextPath}/resources/common/js/common.js"></script>

		<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/leftmenu.css"/>">
		<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/front_common.css"/>">
		
		<style>
			.more a:hover {
				background: #71160d;
			}
			li.topmenuselected{
				background: #0557ab;
			}
		</style>
	</head>

	<!-- navigation bar -->
	<div class="abovenavbar">
		<div align=center>
			<div style="width:1100px;">
				<span class="dropdown" style="float:left;">
					<a data-toggle="dropdown" class="dropdown">${userName}&nbsp;<b class="icon-angle-down"></b></a>
					<div class="dropdown-menu">
						<div id="logout"><a style = "color: #1F2325; text-decoration: none;" href = "${pageContext.request.contextPath}/front/front/SellerLoginController-logout">退出登陆</a></div>
					</div>
				</span>
				<span style="float: right;"><a href="<c:url value="/front/SellerHomeController-home"/>">返回首页</a></span>
			</div>
		</div>
	</div>

	<!-- top menu -->
	<div class="navbar navbar-inverse top-nav" align=center style="height:50px;overflow:hidden">
		<div class="navbar-inner">
			<div class="container">
				<div align=left style="width:1100px;">
					<span class="title_account" style="line-height:100%; margin-top:9px">管理员</span>
					<div class="nav-collapse" style="margin-left:22px;">
						<ul class="nav">
							<li id="dingdancenter" class="dropdown menu-break-left <c:if test = "${parentMenuNo == '1' }"> topmenuselected</c:if>">
								<a class="dropdown-toggle" href="${pageContext.request.contextPath}/front/seller/SellerOrderController-prodPub" style="font-size:16px;color:white;font-weight:bold;">订单中心</a>
							</li>
							<li id="financecenter" class="dropdown menu-break-left <c:if test = "${parentMenuNo == '2' }"> topmenuselected</c:if>">
								<a class="dropdown-toggle" href="${pageContext.request.contextPath}/front/seller/SellerFinanceController-accountInfo" style="font-size:16px;color:white;font-weight:bold;">财务中心</a>
							</li>
							<li id="newscenter" class="dropdown menu-break-left <c:if test = "${parentMenuNo == '3' }"> topmenuselected</c:if>">
								<a class="dropdown-toggle" href="${pageContext.request.contextPath}/front/seller/SellerNewsController-xxList" style="font-size:16px;color:white;font-weight:bold;">消息中心</a>
							</li>
							<li id="company_manage" class="dropdown menu-break-left menu-break-right <c:if test = "${parentMenuNo == '4' }"> topmenuselected</c:if>">
								<a class="dropdown-toggle" href="${pageContext.request.contextPath}/front/seller/company/SellerCompanyController-companyInfo" style="font-size:16px;color:white;font-weight:bold;">企业管理</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="main_content" align=center style="background:white;width: 1100px;margin: 0px auto;padding-top: 32px; display:none">
		<div id="leftmenu" class="" style="border:solid 1px #d0d0d0;width:190px;float:left;">
			<c:if test = "${parentMenuNo == '1'}">
				<ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">产品管理</div>
					</li>
					<ul style="display: block;">
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '1' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/seller/SellerOrderController-prodPub">产品发布</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '2' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/seller/SellerOrderController-prodList">产品列表</a></li>
					</ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">订单管理</div>
					</li>
					<ul style="display: block;">				
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '3' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/seller/SellerOrderController-orderList">订单列表</a></li>
					</ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">客户管理</div>
					</li>
					<ul style="display: block;">
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '4' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/seller/SellerOrderController-userOrderList">已有客户</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '6' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/seller/SellerOrderController-userProdList">客户商品</a></li>
					</ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">评价返馈</div>
					</li>
					<ul>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '5' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/seller/SellerOrderController-estiList">评价列表</a></li>
					</ul>
				</ul>		
			</c:if>	
			<c:if test = "${parentMenuNo == '2'}">
				<ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">财务中心</div>
					</li>
					<ul style="display: block;">
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '1' }">selected</c:if>" ><a href = "${pageContext.request.contextPath}/front/seller/SellerFinanceController-accountInfo">账户信息</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '2' }">selected</c:if>" ><a href = "${pageContext.request.contextPath}/front/seller/SellerFinanceController-txList">提现记录</a></li>
					</ul>
				</ul>
			</c:if>
			<c:if test = "${parentMenuNo == '3'}">
				<ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">消息通知</div>
					</li>
					<ul style="display: block;">
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '1' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/seller/SellerNewsController-xxList">消息列表</a></li>
					</ul>
				</ul>
			</c:if>
			<c:if test = "${parentMenuNo == '4'}">
				<ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">账号设置</div>
					</li>
					<ul style="display: block;">
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '1' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/seller/company/SellerCompanyController-companyInfo">公司信息</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '2' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/seller/company/SellerCompanyController-contactMode">联系方式</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '3' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/seller/company/SellerCompanyController-changePass">修改密码</a></li>
					</ul>
				</ul>	
			</c:if>
		</div>
	</div>
	<script type="text/javascript">
		function menuparentclicked( obj){
			var nextobj = $(obj).nextAll("ul:first");
			if( $(nextobj).css("display")!="none"){
				$(nextobj).slideUp();
				$(obj).css("list-style-image", "url('<c:url value="/resources/wella/front/images/lftmnuitmcollapsed.png"/>')");
			} else {
				$(nextobj).slideDown();
				$(obj).css("list-style-image", "url('<c:url value="/resources/wella/front/images/lftmnuitmexpanded.png"/>')");
			}
		}
	</script>