<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true"%>
<!DOCTYPE HTML>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>买家管理员页面</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="Admin Panel Template">
		<meta name="author" content="Westilian: Kamrujaman Shohel">
		<!-- styles -->
		<link rel="stylesheet" href="<c:url value="/resources/library/css/bootstrap.min.css"/>">
		<%--<link rel="stylesheet" href="<c:url value="/resources/library/css/bootstrap-responsive.css"/>">--%>
		<link rel="stylesheet" href="<c:url value="/resources/library/css/semantic.min.css"/>">
		<link rel="stylesheet" href="<c:url value="/resources/library/css/font-awesome.css"/>">
		<!--[if IE 7]>
		<link rel="stylesheet" href="<c:url value="/resources/library/css/font-awesome-ie7.min.css"/>">
		<![endif]-->
		<link rel="stylesheet" href="<c:url value="/resources/library/css/chosen.css"/>">
		<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/styles.css"/>">
		<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/new_style.css"/>">
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
		<!--fav and touch icons -->
		<link rel="shortcut icon" href="<c:url value="/resources/wella/common/ico/wellassist.png"/>">
		<link rel="apple-touch-icon-precomposed" sizes="144x144" href="<c:url value="/resources/wella/common/ico/wellassist-144.png"/>">
		<link rel="apple-touch-icon-precomposed" sizes="114x114" href="<c:url value="/resources/wella/common/ico/wellassist-114.png"/>">
		<link rel="apple-touch-icon-precomposed" sizes="72x72" href="<c:url value="/resources/common/ico/wellassist-72.png"/>">
		<link rel="apple-touch-icon-precomposed" href="<c:url value="/resources/wella/common/ico/wellassist-57.png"/>">
		<!--============ javascript ===========-->
		<script src="<c:url value="/resources/library/js/jquery.js"/>"></script>
		<script src="<c:url value="/resources/library/js/jquery-ui-1.10.1.custom.min.js"/>"></script>
		<script src="<c:url value="/resources/library/js/bootstrap.min.js"/>"></script>
		<script src="<c:url value="/resources/library/js/semantic.min.js"/>"></script>
		<script src="<c:url value="/resources/wella/common/js/accordion.nav.js"/>"></script>
		<script src="<c:url value="/resources/common/js/global.js"/>"></script>
		
		<script src="${pageContext.request.contextPath}/resources/common/js/jquery.validate.js"></script>
	    <script src="${pageContext.request.contextPath}/resources/houtai/js/jquery.form.js"></script>
	    
	    <link   href="${pageContext.request.contextPath}/resources/houtai/css/jquery.fancybox.css" rel="stylesheet" />
		<script src="${pageContext.request.contextPath}/resources/houtai/js/jquery.fancybox.js"></script>
		
		<script src="${pageContext.request.contextPath}/resources/library/js/My97DatePicker/WdatePicker.js"></script>
		<script src="${pageContext.request.contextPath}/resources/common/js/common.js"></script>

		<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/leftmenu.css"/>">
		<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/front_common.css"/>">
	</head>
	<style>
		.more a:hover {
			background: #71160d;
		}
		
		/*li.topmenuselected{
			background: #0557ab;
		}*/
	</style>

	<!-- navigation bar -->
	<div class="abovenavbar" style="height:45px">
		<div align=center>
			<div style="width:1100px;">
 					<span class="dropdown" style="float:left;">
						<a data-toggle="dropdown" class="dropdown">${userName}&nbsp;<b class="icon-angle-down"></b></a>
						<div class="dropdown-menu">
							<div id="logout"><a style = "color: #1F2325; text-decoration: none;" href = "${pageContext.request.contextPath}/front/customer/CustomerLoginCtrl-logout">退出登陆</a></div>
						</div>
 					</span>
					<span style="float: right;"><a href="<c:url value="/front/customer/CustomerHomeCtrl-home"/>">返回首页</a></span>
			</div>
		</div>
	</div>

	<!-- top menu -->
	<%--<div class="navbar navbar-inverse top-nav" align=center style="height:50px;overflow:hidden">
		<div class="navbar-inner">
			<div class="container">
				<div align=left style="width:1100px;">
					<span class="title_account" style="line-height:100%; margin-top:9px">管理员</span>
					<div class="nav-collapse" style="margin-left:22px;">
						<ul class="nav">
							<li id="chanpincenter" class="dropdown menu-break-left <c:if test = "${parentMenuNo == '5' }"> topmenuselected</c:if>">
								<a class="dropdown-toggle" href="${pageContext.request.contextPath}/front/customer/CustomerProdCtrl-prodList" style="font-size:16px;color:white;font-weight:bold;">产品中心</a>
							</li>
							<li id="dingdancenter" class="dropdown menu-break-left <c:if test = "${parentMenuNo == '1' }"> topmenuselected</c:if>">
								<a class="dropdown-toggle" href="${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-prodOrderList" style="font-size:16px;color:white;font-weight:bold;">订单中心</a>
							</li>
							<li id="financecenter" class="dropdown menu-break-left <c:if test = "${parentMenuNo == '2' }"> topmenuselected</c:if>">
								<a class="dropdown-toggle" href="${pageContext.request.contextPath}/front/customer/FinanceController-accountInfo" style="font-size:16px;color:white;font-weight:bold;">财务中心</a>
							</li>
							<li id="newscenter" class="dropdown menu-break-left <c:if test = "${parentMenuNo == '3' }"> topmenuselected</c:if>">
								<a class="dropdown-toggle" href="${pageContext.request.contextPath}/front/customer/NewsController-xxList" style="font-size:16px;color:white;font-weight:bold;">消息中心</a>
							</li>
							<li id="company_manage" class="dropdown menu-break-left menu-break-right <c:if test = "${parentMenuNo == '4' }"> topmenuselected</c:if>">
								<a class="dropdown-toggle" href="${pageContext.request.contextPath}/front/customer/CompanyController-companyInfo" style="font-size:16px;color:white;font-weight:bold;">企业管理</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>--%>
<%--顶部导航条--%>
		
<div class="navbar navbar-blue " style="margin-top:5px;">
      <div class="container">
        <div class="navbar-header">
          <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target=".navbar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand hidden-sm" href="#" onclick="_hmt.push(['_trackEvent', 'navbar', 'click', 'navbar-首页'])">管理员</a>
        </div>
        <div class="navbar-collapse collapse" role="navigation" aria-expanded="false" style="height: 1px;">
          <ul class="nav navbar-nav">
               <li id="chanpincenter" class=" <c:if test = "${parentMenuNo == '5' }"> topmenuselected</c:if>">
								<a  href="${pageContext.request.contextPath}/front/customer/CustomerProdCtrl-prodList"> <i class="block layout icon"></i>产品中心</a>
							</li>
      						<li  class=" <c:if test = "${parentMenuNo == '1' }"> topmenuselected </c:if>" id="dingdancenter">
							  <a href="${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-prodOrderList">订单中心</a></li>
      						<li id="financecenter" class="<c:if test = "${parentMenuNo == '2' }"> topmenuselected</c:if>" >
							  <a href="${pageContext.request.contextPath}/front/customer/FinanceController-accountInfo">财务中心</a></li>
      						<li id="newscenter" class="<c:if test = "${parentMenuNo == '3' }"> topmenuselected</c:if>">
							  <a href="${pageContext.request.contextPath}/front/customer/NewsController-xxList">消息中心</a></li>
							<li id="company_manage" class="<c:if test = "${parentMenuNo == '4' }"> topmenuselected</c:if>">
							<a href="${pageContext.request.contextPath}/front/customer/CompanyController-companyInfo">财务管理</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right hidden-sm">
            <li><a href="/about/" onclick="#">关于</a></li>
          </ul>
        </div>
      </div>
    </div>
<%--顶部导航条end--%>
	
	<div id="main_content" align=center style="background:white;width: 1100px;margin: 0px auto;padding-top: 32px;">
		<%--<div id="leftmenu" class="" style="border:solid 1px #d0d0d0;width:190px;float:left;">
			<c:if test = "${parentMenuNo == '1'}">
				<ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">订单管理</div>
					</li>
					<ul style="display: block;">
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '1' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-prodOrderList">产品订单</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '2' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-wlOrderList">物流订单</a></li>
					</ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">评价返馈</div>
					</li>
					<ul>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '3' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-estiList">评价列表</a></li>
					</ul>
				</ul>	
			</c:if>	
			<c:if test = "${parentMenuNo == '2'}">
				<ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">财务中心</div>
					</li>
					<ul style="display: block;">
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '1' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-accountInfo">账户信息</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '6' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-txList">提现记录</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '5' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-czList">充值记录</a></li>
					</ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">授信中心</div>
					</li>
					<ul>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '2' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-creditAccount">授信账户</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '3' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-sxSqList">申请记录</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '4' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-hkList">还款记录</a></li>
					</ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">发票管理</div>
					</li>
					<ul>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '7' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-fapiaoList">发票列表</a></li>
						
					</ul>
				</ul>
			</c:if>
			<c:if test = "${parentMenuNo == '3'}">
				<ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">消息通知</div>
					</li>
					<ul style="display: block;">
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '1' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/NewsController-xxList">消息列表</a></li>
					</ul>
				</ul>
			</c:if>
			<c:if test = "${parentMenuNo == '4'}">
				<ul>
					<li class="leftmenulevel1" onclick="menuparentclicked( this);">
						<div style="">账号设置</div>
					</li>
					<ul style="display: block;">
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '1' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/CompanyController-companyInfo">公司信息</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '2' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/CompanyController-contactMode">联系方式</a></li>
						<li class="leftmenuleaf <c:if test ="${childMenuNo == '3' }">selected</c:if>"><a href = "${pageContext.request.contextPath}/front/customer/CompanyController-changePass">修改密码</a></li>
					</ul>
				</ul>	
			</c:if>
			<c:if test = "${parentMenuNo == '5'}">
				<script type="text/javascript">
					$("#leftmenu").hide();
				</script>
			</c:if>
		</div>--%>
		<c:if test = "${parentMenuNo == '5'}">
				<script type="text/javascript">
						$("#leftmenu").hide();
				</script>
		</c:if>
		<div class="ui vertical accordion menu" id="leftmenu" style="max-width:205px;float:left">
		<c:if test = "${parentMenuNo == '2'}">
          <div class="item">
            <a class="title <c:if test="${childMenuNo == '1' or childMenuNo =='6'or childMenuNo =='5' }">active </c:if>"><i class="dropdown icon"></i> 财务中心 </a>
            <div class="<c:if test="${childMenuNo == '1' or childMenuNo =='6'or childMenuNo =='5'}"> active </c:if> content">
                <div class="ui form">
                    <div class="grouped fields">
                        <div class="field">
                            <div class="ui  checkbox">
                                <input type="checkbox" name="size"  <c:if test ="${childMenuNo == '1'}"> checked="checked" </c:if> >
                                <label> <a href = "${pageContext.request.contextPath}/front/customer/FinanceController-accountInfo">账户信息</a></label>
                            </div>
                        </div>
                        <div class="field">
                            <div class="ui checkbox">
                                <input type="checkbox" name="size" <c:if test ="${childMenuNo == '6'}"> checked="checked" </c:if> >
                                <label><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-txList">提现记录</a></label>
                            </div>
                        </div>
                        <div class="field">
                            <div class="ui checkbox">
                                <input type="checkbox" name="size" <c:if test ="${childMenuNo == '5'}"> checked="checked" </c:if> >
                                <label><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-czList">充值记录</a></label>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>

        <div class="item">
            <a class="title <c:if test="${childMenuNo == '2' or childMenuNo =='3'or childMenuNo =='4' }">active </c:if> "><i class="dropdown icon"></i> 授信中心 </a>
            <div class="content <c:if test="${childMenuNo == '2' or childMenuNo =='3'or childMenuNo =='4'}"> active </c:if>">
                <div class="ui form">
                    <div class="grouped fields">
                        <div class="field">
                            <div class="ui  checkbox">
                                <input type="checkbox" <c:if test ="${childMenuNo == '2'}"> checked="checked" </c:if>>
                                <label><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-creditAccount">授信账户</a></label>
                            </div>
                        </div>
                        <div class="field">
                            <div class="ui  checkbox">
                                <input type="checkbox" <c:if test ="${childMenuNo == '3'}"> checked="checked" </c:if>>
                                <label><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-sxSqList">申请记录</a></label>
                            </div>
                        </div>
                        <div class="field">
                            <div class="ui  checkbox">
                                <input type="checkbox" <c:if test ="${childMenuNo == '4'}"> checked="checked" </c:if>>
                                <label><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-hkList">还款记录</a></label>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <div class="item item3">
            <a class="title <c:if test="${childMenuNo == '7'}">active </c:if>"><i class="dropdown icon"></i> 发票管理 </a>
            <div class="content <c:if test="${childMenuNo == '7'}">active </c:if>">
                <div class="ui form">
                    <div class="grouped fields">
                         <div class="field">
                            <div class="ui  checkbox">
                                <input type="checkbox" <c:if test ="${childMenuNo == '7'}"> checked="checked" </c:if>>
                                <label><a href = "${pageContext.request.contextPath}/front/customer/FinanceController-fapiaoList">发票记录</a></label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
<%--财务中心end--%>
<%--消息中心--%>
<c:if test = "${parentMenuNo == '3'}">
          <div class="item">
            <a class="title <c:if test="${childMenuNo == '1'  }">active </c:if>"><i class="dropdown icon"></i> 消息通知 </a>
            <div class="<c:if test="${childMenuNo == '1' }"> active </c:if> content">
                <div class="ui form">
                    <div class="grouped fields">
                       
                        <div class="field">
                            <div class="ui checkbox">
                                <input type="checkbox" name="size" <c:if test ="${childMenuNo == '1'}"> checked="checked" </c:if> >
                                <label><a href = "${pageContext.request.contextPath}/front/customer/NewsController-xxList">消息列表</a></label>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>

    </c:if>
<%--消息中心end	--%>

<%--财务管理--%>
<c:if test = "${parentMenuNo == '4'}">
          <div class="item">
            <a class="title active "><i class="dropdown icon"></i> 账户设置 </a>
            <div class=" active content">
                <div class="ui form">
                    <div class="grouped fields">
                        <div class="field">
                            <div class="ui  checkbox">
                                <input type="checkbox" name="size" <c:if test ="${childMenuNo == '1'}"> checked="checked" </c:if>  >
                                <label> <a href = "${pageContext.request.contextPath}/front/customer/CompanyController-companyInfo">公司信息</a></label>
                            </div>
                        </div>
                        <div class="field">
                            <div class="ui checkbox">
                                <input type="checkbox" name="size" <c:if test ="${childMenuNo == '2'}"> checked="checked" </c:if> >
                                <label><a href = "${pageContext.request.contextPath}/front/customer/CompanyController-contactMode">联系方式</a></label>
                            </div>
                        </div>
                        <div class="field">
                            <div class="ui checkbox">
                                <input type="checkbox" name="size" <c:if test ="${childMenuNo == '3'}"> checked="checked" </c:if> >
                                <label><a href = "${pageContext.request.contextPath}/front/customer/CompanyController-changePass">修改密码</a></label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
 </c:if>
<%--财务管理end--%>

<%--订单中心--%>
<c:if test = "${parentMenuNo == '1'}">
          <div class="item">
            <a class="title active "><i class="dropdown icon"></i> 订单管理 </a>
            <div class=" active content">
                <div class="ui form">
                    <div class="grouped fields">
                        <div class="field">
                            <div class="ui  checkbox">
                                <input type="checkbox" name="size" <c:if test ="${childMenuNo == '1'}"> checked="checked" </c:if>  >
                                <label> <a href = "${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-prodOrderList">产品订单</a></label>
                            </div>
                        </div>
                        <div class="field">
                            <div class="ui checkbox">
                                <input type="checkbox" name="size" <c:if test ="${childMenuNo == '2'}"> checked="checked" </c:if> >
                                <label><a href = "${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-wlOrderList">物流订单</a></label>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
		<div class="item">
            <a class="title active "><i class="dropdown icon"></i> 评价反馈 </a>
            <div class=" active content">
                <div class="ui form">
                    <div class="grouped fields">
                        <div class="field">
                            <div class="ui  checkbox">
                                <input type="checkbox" name="size" <c:if test ="${childMenuNo == '3'}"> checked="checked" </c:if>  >
                                <label> <a href = "${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-estiList">评价列表</a></label>
                            </div>
                        </div>
                        
                        
                    </div>
                </div>
            </div>
        </div>

 </c:if>

    </div>

		
	

<script type="text/javascript">
	$('.ui.accordion').accordion();
	
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
	$('.ui.accordion').accordion();	
	/* function menuclicked( url, obj){
		$("li.leftmenuleaf").removeClass("selected");
		$(obj).addClass("selected");
		$("#contentFrame").attr( "src", "${pageContext.request.contextPath}"+url);
	}
	
	function page_loaded(){
		$("#contentFrame").attr( "src", "${pageContext.request.contextPath}/front/customer/news/news_list");
	} */
	
	/* $("#dingdancenter").click( function(){
		topmenu_sel_clear();
		$("#dingdancenter").addClass("topmenuselected");
		$("#mainFrame").attr("src", "<c:url value="/front/customer/ordersheet_center"/>");
	});

	$("#financecenter").click( function(){
		topmenu_sel_clear();
		$("#financecenter").addClass("topmenuselected");
		$("#mainFrame").attr("src", "<c:url value="/front/customer/finance_center"/>");
	});
	
	$("#newscenter").click( function(){
		topmenu_sel_clear();
		$("#newscenter").addClass("topmenuselected");
		$("#mainFrame").attr("src", "<c:url value="/front/customer/news_center"/>");
	});

	$("#company_manage").click( function(){
		topmenu_sel_clear();
		$("#company_manage").addClass("topmenuselected");
		$("#mainFrame").attr("src", "<c:url value="/front/customer/company_manage"/>");
	});

	function topmenu_sel_clear(){
		$("li").removeClass("topmenuselected");
	}

	function page_loaded() {
		$("#mainFrame").height( $(window).height()-$("#mainFrame").outerHeight());
		$("#mainFrame").attr("src", "<c:url value="/front/customer/ordersheet_center"/>");

	}
	
	$("#logout").click( function(){
		window.location.href="<c:url value="/front/customer/logout"/>";
	}); */

</script>


