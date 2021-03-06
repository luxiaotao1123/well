﻿<%@ include file="../header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>

<link rel="stylesheet" href="<c:url value="/resources/wella/front/css/pagetempl.css"/>">
<style>
	div.formDd{
		width:1000px;
		text-align: left; 
		background: white;
		box-shadow: 2px 2px 8px #808080;
		padding:16px 0px 16px 0px;
	}
	div.headDd{
		margin-left:48px;
		margin-right:48px;
		padding-top:32px;
		margin-bottom: 24px;
		padding-bottom:12px;
		border-bottom:solid 2px #d0d0d0;
		font-size:18px;
	}
	div.rowDd{
		clear: both;
		height: 50px;
		line-height: 50px;
		font-size: 14px;	
	}
	.rowDd .labeldd{
		float: left;
		width: 100px;
	}
	.rowDd .contentdd{
		float: left;
		margin-left: 15px;
	}
	.backBtn{
		font-weight: bold;
    	cursor: pointer;
    	color: #2482df;
    	text-align:center;
    	font-size:16px;
    	float:left;
    	margin-top:6px;
	}
</style>	
<div class="container1">
    <div style="margin:40px 0 0 210px;">
    <div style="width:90%;">
		<h4 class="ui header">订单详情</h4>
        <div class="ui divider"></div>
		<form id="ddxqForm" action="${pageContext.request.contextPath}/seller/editOrderSubmit" method="post">
			<div style="font-size:14px;font-weight: 600;line-height: 30px; margin:20px 0 40px 60px;">
				<span>当前订单付款状态：</span>
	            <span>
	                <c:if test="${orderInfo.orderState=='0'}">待确认</c:if>
					<c:if test="${orderInfo.orderState=='1'}">待付款</c:if>
					<c:if test="${orderInfo.orderState=='2'}">已付款</c:if>
					<c:if test="${orderInfo.orderState=='3'}">未发货</c:if>
					<c:if test="${orderInfo.orderState=='4'}">已发货</c:if>
					<c:if test="${orderInfo.orderState=='5'}">待评价</c:if>
					<c:if test="${orderInfo.orderState=='6'}">已完成</c:if>
	            </span><br>
				
				<span>企业名称：</span>
				<span>${orderInfo.userName}</span><br>

            	<span>订单号：</span>
            	<span>${orderInfo.orderNo}</span>
            	<span style="margin-left:9%;">货运方式：</span>
	            <span>
	                <c:if test="${orderInfo.isSelfCar==0}">自提</c:if>
	                <c:if test="${orderInfo.isSelfCar==1}">平台物流</c:if>
	            </span><br>

	            <span>下单时间：</span>
	            <span><fmt:formatDate value="${orderInfo.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></span>
	            <span style="margin-left:11.5%;">联系人：</span>
	            <span>${orderInfo.contacts}</span><br>

	            <span>发货时间：</span>
	            <span><fmt:formatDate value="${orderInfo.deliverDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></span>
	            <span style="margin-left:11.5%;">联系电话：</span>
	            <span>${orderInfo.conTel}</span><br>

	            <span>预计收货时间：</span>
	            <span><fmt:formatDate value="${orderInfo.receiveDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></span>
				
			</div>

				<table class="ui table" style="border:0;background-color:#DAF3FF;height:45px;line-height:45px;border-radius:0;text-align:center;">
		            <tr>
		                <th colspan="2" width="30%">产品</th>
		                <th width="13%">品类</th>
		                <th width="15%">单价(元/吨)</th>
		                <th width="14%">数量(吨)</th>
		                <th width="15%">总价(元)</th>
		                <th width="13%">状态</th>
		            </tr>
		        </table>
		        <table class="ui line celled  table" style="margin-top:-8px;text-align:center;border-radius:0;">
		            <tr>
		                <td width="10%">
		                    <img src="${orderInfo.prodImg}" width="100" height="75">
		                </td>
		                <td width="20%" style="text-align:left;border-left:0;">${orderInfo.prodName}</td>
		                <td width="13%">
		                    <c:if test="${orderInfo.prodType==0}">天然气</c:if>
		                    <c:if test="${orderInfo.prodType==1}">原油</c:if>
		                    <c:if test="${orderInfo.prodType==2}">管道气</c:if>
		                </td>
		                <td width="14%">
		                    <c:if test="${empty orderInfo.orderNumber && empty orderInfo.confirmNumber}">${orderInfo.saleMoney/orderInfo.saleNum}</c:if>
		                    <c:if test="${!empty orderInfo.orderNumber  }">${orderInfo.orderPrice}</c:if>元</div>
		                    <c:if test="${not empty orderInfo.orderNumber and (orderInfo.orderPrice-(orderInfo.saleMoney/orderInfo.saleNum)>0.0000000001 or orderInfo.orderPrice-(orderInfo.saleMoney/orderInfo.saleNum)<-0.0000000001)}"><s>${orderInfo.saleMoney/orderInfo.saleNum}</s></c:if>
		                </td>
		                <td width="14%">
		                    <c:if test="${empty orderInfo.orderNumber && empty orderInfo.confirmNumber}">${orderInfo.saleNum}</c:if>
		                    <c:if test="${!empty orderInfo.orderNumber}">${orderInfo.orderNumber}</c:if>吨
		                    <c:if test="${not empty orderInfo.orderNumber and (orderInfo.orderNumber-orderInfo.saleNum>0.0000000001 or orderInfo.orderNumber-orderInfo.saleNum<-0.0000000001)}"><s>${orderInfo.saleNum}</s></c:if>
		                </td>
		                <td width="15%">  
		                    <c:if test="${empty orderInfo.orderNumber && empty orderInfo.confirmNumber}">${orderInfo.saleMoney}</c:if>
		                    <c:if test="${!empty orderInfo.orderNumber }">${orderInfo.orderNumber*orderInfo.orderPrice}</c:if>元</div>
		                    <c:if test="${not empty orderInfo.orderNumber and (orderInfo.orderNumber*orderInfo.orderPrice-orderInfo.saleMoney>0.0000000001 or orderInfo.orderNumber*orderInfo.orderPrice-orderInfo.saleMoney<-0.0000000001)}"><s>${orderInfo.saleMoney}</s></c:if>
		                </td>
		                <td width="13%">
		                    <c:if test="${fn:startsWith(orderInfo.orderState,'-')||fn:startsWith(orderInfo.orderState,0)||fn:startsWith(orderInfo.orderState,1)}">未付款</c:if>
		                    <c:if test="${!fn:startsWith(orderInfo.orderState,'-')&&fn:substring(orderInfo.orderState,0,1)>=2}">已付款</c:if>
		                </td>
		            </tr>
		        </table>
				<h4 class="ui header">修改订单</h4>
        		<div class="ui divider"></div>
        		<div class="rowDd">
					<div class="labeldd" align="right">供应量 :</div>
					<div class="contentdd ui input" style="margin-top:8px;">
						<input type="text" name="saleNum" value="${orderInfo.orderNumber}" placeholder="请输入供应量" onkeyup="return validateNumber(this,value,0)" />
					</div>
					<div class="labeldd" align="right">单价 :</div>
					<div class="contentdd ui input" style="margin-top:8px;">
						<input type="text" name="saleDj" value="${orderInfo.orderPrice}" placeholder="请输入单价"  onkeyup="return validateNumber(this,value,0)" />
					</div>
					<div class="labeldd" align="right">总价 :</div>
					<div class="contentdd"><span id="saleMoney">${orderInfo.orderPrice * orderInfo.orderNumber}</span>元</div>
				</div>
				<input type="hidden" name="orderId" value="${orderInfo.orderId}" />

				<div style="margin: 40px 0px 40px 80px;">
						<input type="button" class="ui button" value="返回" <%--onclick="goBack();"--%>onclick="javascript:window.history.go(-1);" />
						<input type="submit" id="submit" class="ui button primary" value="确认" />
				</div>
		</form>
	</div>
</div>

<script type="text/javascript">

	var orderState = '${orderInfo.orderState}';

	$(".backBtn").click(function(){
		goBack();
	});
	
	var qrvhAClassCn = 0;
	$(".qrvhAClass").each(function(){
		qrvhAClassCn++;
	});
	if(qrvhAClassCn>0) $("#qrvhAllBtn").show();
	
	// validate检查
	$("#ddxqForm").validate({
	    rules: {},
	    messages: {},
	    errorPlacement: function (error, element) {},
	    submitHandler: function(form){

	    		var saleNum = $("input[name='saleNum']").val();
	    		var saleDj  = $("input[name='saleDj']").val();
	    		if(saleNum==""){
	    			alert("请输入供应量!");
	    			return false;
	    		}
				if(saleDj==""){
					alert("请输入单价!");
					return false;
	    		}
	    	if(confirm("你要确定操作吗？")){
		    	$.post($("#ddxqForm").attr("action"),$("#ddxqForm").serialize(),function(data){
		    		data = $.parseJSON(data);
		    		alert(data.content);
		            if(data.status=="1"){
		            	window.location.href = "${pageContext.request.contextPath}/seller/order";
		            }
		      	})
		      	.error(function(data){
		      		alert("操作失败！")
		      	});
	    	}
	    }
    });
	
	function setVehicle(vehicleId){
		if(vehicleId>0){
			
			var vehicleSjSize = 0;
			var zorderMoney   = 0;
			
			if(orderState=="3"){
				vehicleSjSize = $("#vehicleSjSize_"+vehicleId).val();
	    		zorderMoney   = $("#zorderMoney_"+vehicleId).val();
	    		
	    		if(vehicleSjSize==null || vehicleSjSize=="" || vehicleSjSize<=0){
	    			alert("请输入装载量!");
	    			return false;
	    		}
				if(zorderMoney==null || zorderMoney=="" || zorderMoney<=0){
					alert("请输入单价!");
					return false;
	    		}
	    	} else {
	    		return false;
	    	}
			
			if(confirm("你要确定操作吗？")){
				$.post("${pageContext.request.contextPath}/front/seller/SellerOrderController-setVehicle",{vehicleId:vehicleId, vehicleSjSize:vehicleSjSize, zorderMoney:zorderMoney},function(data){
		    		data = $.parseJSON(data);
		    		alert(data.content);
		            if(data.status=="1"){
		            	window.location.reload();
		            }
		      	})
		      	.error(function(data){
		      		alert("操作失败！")
		      	});
			}
		}
		
	}
	
	function setAllVehicle(){

		var vehicleArray  = new Array();
		var vehicleId 	  = 0;
		var vehicleSjSize = 0;
		var zorderMoney   = 0;
		var flag		  = false;
		
		if(orderState=="3"){
			
			$("input[name='vehicleSjSize']").each(function(){
				var vehicleObj	  = new Object();
				
				if(!flag){
					vehicleId = $(this).attr("vehicleid");
					if(vehicleId==null || vehicleId=="" || vehicleId<=0){
		    			alert("操作错误!");
		    			flag = true;
		    			return false;
		    		} else {
		    			vehicleObj.vehicleId = vehicleId;	
		    		}
				}

				if(!flag){
					vehicleSjSize = $(this).val();
					if(vehicleSjSize==null || vehicleSjSize=="" || vehicleSjSize<=0){
		    			alert("请输入装载量!");
		    			$(this).focus();
		    			flag = true;
		    			return false;
		    		} else {
		    			vehicleObj.vehicleSjSize = vehicleSjSize;	
		    		}
				}
				
				if(!flag){
				    $("input[name='zorderMoney']").each(function(){
					    if(vehicleId == $(this).attr("vehicleid")){
					    	zorderMoney   = $("#zorderMoney_"+vehicleId).val();
					    	if(zorderMoney==null || zorderMoney=="" || zorderMoney<=0){
								alert("请输入单价!");
								$(this).focus();
								flag = true;
								return false;
				    		} else {
					    		vehicleObj.zorderMoney = zorderMoney;
					    		
					    		vehicleArray[vehicleArray.length] = vehicleObj;
				    		}
					    }
					});
				}
			});
			
			if(flag || vehicleArray.length<1) return;
			
    	} else {
    		return false;
    	}
		
		if(confirm("你要确定操作吗？")){
			var vehicleData = JSON.stringify(vehicleArray);
			$.post("${pageContext.request.contextPath}/front/seller/SellerOrderController-setAllVehicle",{vehicleData:vehicleData},function(data){
	    		data = $.parseJSON(data);
	    		alert(data.content);
	            if(data.status=="1"){
	            	window.history.go(-1);
	            }
	      	})
	      	.error(function(data){
	      		alert("操作失败！")
	      	});
		}

	}
	
	function showDd(type){
		if(type==0){
			$("#ddXqDiv").show();
			$("#wlxxDiv").hide();
		} else {
			$("#ddXqDiv").hide();
			$("#wlxxDiv").show();
		}
	}
	
	function validateNumber(e, pnumber, type){
		var len = 0;
		/*if (!/^\d+$/.test(pnumber)){*/
        if (!/^[0-9]+.{0,1}[0-9]{0,2}$/.test(pnumber)){
			if(isNaN(pnumber) == '0') return;
			len = pnumber.length;
			$(e).val(pnumber.substring(0,len-1));
		}
		
		if(type==0){
			var saleNum = $("input[name='saleNum']").val();
			var saleDj  = $("input[name='saleDj']").val();
			
			if(saleNum=="" || saleDj==""){
				$("#saleMoney").html("0");
			} else {
				$("#saleMoney").html(saleNum*saleDj);	
			}
		}
		
		return false;
	}

</script>

<%@ include file="../footer.html"%>