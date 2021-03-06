<%@ include file="../header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<style>
	.ulSelected{
            color: #617B90;
            border-bottom: 2px solid #617B90;
		}
		
</style>
<div class="container1">
	<div class="container2">
<div id = "content-rect" style="width:90%">
	 <ul class="ds-bl pd-10 ft-sz-15">
            <li class="ds-bl fl-lt pd-10  <c:if test="${empty param.state ||(param.state != '0'&&param.state != '2' && param.state != '4')}" >ulSelected</c:if>" onclick="$('#vehicleState').val('');searchData(1);" style="padding-left:0px">全部订单</li>
            <li class="ds-bl fl-lt pd-10 <c:if test="${param.state == '0'}">ulSelected</c:if>" onclick="$('#vehicleState').val('0');searchData(1);">待选择</li>
            <li class="ds-bl fl-lt pd-10 <c:if test="${param.state == '2'}">ulSelected</c:if>" onclick="$('#vehicleState').val('2');searchData(1);">待付款</li>
            <li class="ds-bl fl-lt pd-10 <c:if test="${param.state == '4'}">ulSelected</c:if>" onclick="$('#vehicleState').val('4');searchData(1);">发货中</li>

        </ul>
		<br>
		
	<form id="searchFrm" method="post" action="${pageContext.request.contextPath}/customer/logisticsInfoList">
		<input type="hidden" id="page" name="page" value="${param.page}">
		<input type="hidden" id="vehicleState" name="state" value="${param.state}">
	</form>
        <table class="ui basic table ft-sz-14">
            <thead>
                <tr class="grey-4 tx-ct">
                    <th width="30%">产品详情</th>
                    <th>数量（吨）</th>
                    <th>金额（元）</th>
					<!-- <th>交易状态</th> -->
					<th>
							<div class="ui pointing dropdown  item"  id="dropdown2" tabindex="0">
							<span class="text">交易状态</span>
							<i class="dropdown icon"></i>
							<div class="menu transition hidden" tabindex="-1">
                                <div onclick="$('#vehicleState').val('');searchData(1);" class="item">全部</div>
								<div onclick="$('#vehicleState').val('-1');searchData(1);" class="item">已取消</div>
								<div onclick="$('#vehicleState').val('0');searchData(1);" class="item">待确认</div>
								<div onclick="$('#vehicleState').val('2');searchData(1);" class="item">待支付</div>
								<div onclick="$('#vehicleState').val('3');searchData(1);" class="item">待提货</div>
								<div onclick="$('#vehicleState').val('4');searchData(1);" class="item">已发货</div>
								<div onclick="$('#vehicleState').val('5');searchData(1);" class="item">已完成</div>
							</div>
							</div>
					</th>
                    <th>交易操作</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="item" items="${info}">
                <tr class="blue-3">
                    <td colspan="4"><span>${item.orderDate}</span><span>订单编号：${item.orderNo}</span></td>
                    <td> <span  class="pointer" onClick="toURL('cancelOrder', '${item.orderId}')" style="font-size:15px;float:right"><i class="trash icon"></i></span></td>
                </tr>
                <tr>
                    <td class="right-border">
                        <img src="${item.prodImg}" alt="" width="100px" height="74px" class="ds-bl fl-lt">
                        <span class="ds-bl fl-lt pd-lf-20 ft-wt-bd " style="padding-top: 25px;width: 190px;"> ${item.prodName}</span>
                    </td>
                    <td class="right-border tx-ct" >
                        ${item.num}吨
                    </td>
                    <td class="right-border tx-ct" >
                        <span>
                        <c:if test="${empty item.orderPrice}">未选择物流</c:if>
					    <c:if test="${!empty item.orderPrice}">${item.orderPrice}元</c:if>
                         </span>
                    </td>
                    <td class="right-border tx-ct" >
                        <span class="ds-bl fl-lt pd-lf-20 ft-wt-bd " style="">
                            <c:if test="${item.orderState==-2}">管理员取消订单</c:if>
                           	<c:if test="${item.state==-1}">已取消</c:if>
								<c:if test="${item.state==0}">待确认</c:if>
                            <c:if test="${item.state==2}">
                            <c:if test="${item.orderState==1}">
                                <c:if test="${item.logisticsPayState==-2}">线下付款审核不通过</c:if>
                                <c:if test="${item.logisticsPayState==-1}">授信付款驳回</c:if>
                                <c:if test="${item.logisticsPayState==0}">待付款</c:if>
                                <c:if test="${item.logisticsPayState==1}">授信付款等待到账</c:if>
                                <c:if test="${item.logisticsPayState==2}">线下付款审核中</c:if>
                                <c:if test="${item.logisticsPayState==3}">付款处理中...</c:if>
                                <c:if test="${item.logisticsPayState==5}">待商品订单付款</c:if>
                            </c:if>
                            </c:if>
                            <c:if test="${item.orderState==2}">待提货</c:if>
                            <c:if test="${item.state==4}">已发货</c:if>
								<c:if test="${item.state==5}">
                                    <c:if test="${item.logistics2ndpayState==0}">已完成</c:if>
                                    <c:if test="${item.logistics2ndpayState==6}">结算中</c:if>
                                    <c:if test="${item.logistics2ndpayState==7}">已结算</c:if>
                                    </c:if>
                            <c:if test="${item.state==6}">已结算</c:if>
                        </span>
                        <br>
                        <span class="ds-bl fl-lt  pd-lf-20">
                            <c:if test="${item.vehicleState==4 || item.vehicleState==5}">
						   <a style="cursor:pointer;color:black;" onclick="toURL('detailVehicle', '${item.logisticsId}')">物流信息</a>
							
						</c:if>
                        </span>
                    </td>
                    <td class="right-border tx-ct pointer" >
                         <span class="ds-bl fl-lt pd-lf-20 ft-wt-bd " style=""> 
                           <c:if test="${item.state==0}">
						<span class="span_btn" onClick="toURL('qiangdan', '${item.logisticsId}')">抢单详情</span>
					</c:if>
                             <c:if test="${item.state==2}">
                             <c:if test="${item.orderState==1}">
                                 <c:if test="${item.logisticsPayState==-2}"><span class="span_btn" onClick="toURL('payLogistics', '${item.logisticsId}')">付款</span></c:if>
                                 <c:if test="${item.logisticsPayState==0}"><span class="span_btn" onClick="toURL('payLogistics', '${item.logisticsId}')">付款</span></c:if>
                             </c:if>
                         </c:if>
                            <c:if test="${item.state==5}">
                                <c:if test="${item.logistics2ndpayState==0}"><span class="span_btn" onClick="settleLogistics(${item.logisticsId})" id="settleLogistics">结算</span></c:if>
                            </c:if>
                         </span>
                       
                    </td>
                    
                  
				</tr>
				</c:forEach>
			</tbody>
			<tfoot>	
				 <tr>
					 <th colspan="6">
						<div class="right-pagination ui right floated pagination menu">
								<%@ include file="../../pagination.jsp"%>
							</div>
					 </th>
				 </tr>
			</tfoot>
        </table>
<c:if test="${empty info || fn:length(info) == 0}">
		<div style = "margin-top:10px; margin-left:20px; float:left;">没有资料</div>	 
    </c:if>	
	
</div>
</div>
</div>
<div style="clear:both;width:50%;height:30px;"></div>

<script type = "text/javascript">
	// 初始化函数
	$(function(){
		// $('.fancybox').fancybox();
		 $('#dropdown2')
        .dropdown()
		var url = $("#searchFrm").attr("action")+"?"+$("#searchFrm").serialize();
 		setPageUrl(url);
		 	
;
	});
	
	function toURL(action, vehicleTrans, orderId){
		var url = "";
		if(vehicleTrans!=''){
			if(action=='qiangdan'){
				window.location.href = "${pageContext.request.contextPath}/customer/grabLogisticsList?logisticsInfoId=" + vehicleTrans;
			}else if(action=="payLogistics"){
				window.location.href="${pageContext.request.contextPath}/customer/goPayLogistics?logisticsInfoId="+vehicleTrans;
            }
			else if(action=='shouhuo'){
				window.location.href = "${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-editVehicleShouhuo?vehicleTrans=" + vehicleTrans + "&orderId=" + orderId;
			} else if(action=='fukuan'){
				alert("测试中。。。");return;
				if(confirm("你要确定付款操作吗？")){
					$.post("${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-setVehicleFukuan",{vehicleTrans:vehicleTrans},function(data){
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
			} else if(action=='detailVehicle'){
				window.location.href = "${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-detailVehicle?vehicleTrans=" + vehicleTrans;
			} else if(action=='cancel'){
				if(confirm("你要确定取消操作吗？")){
					$.post("${pageContext.request.contextPath}/front/customer/CustomerBackOrderCtrl-cancelVehicle",{vehicleTrans:vehicleTrans},function(data){
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
	}

	function settleLogistics(logisticsId){
        $("#settleLogistics").addClass("disabled");
	    $.post("${pageContext.request.contextPath}/customer/secondPayLogisticsSub",{logisticsId:logisticsId},function(r){
            window.location.reload();
            $("#settleLogistics").removeClass("disabled");
        },"json");
    }
</script>

<%@ include file="../footer.jsp"%>