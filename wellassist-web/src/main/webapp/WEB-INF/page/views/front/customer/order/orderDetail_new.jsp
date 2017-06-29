<%@ include file="../header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <title>卖家订单详情</title>
    <%--<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">--%>
    <!--<link href="css/style.css" rel="stylesheet">-->
    <%--<link rel="stylesheet" href="https://cdn.jsdelivr.net/semantic-ui/2.2.4/semantic.min.css">
    <script src="http://libs.baidu.com/jquery/1.9.1/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/semantic-ui/2.2.4/semantic.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/vue/2.1.3/vue.js"></script>--%>
    <style>
        .mid_box {
            width: 1100px;
            margin: 0px auto;
            border: 1px solid rgba(34, 36, 38, .15);
            box-shadow: 0 2px 4px 0 rgba(34, 36, 38, .12), 0 2px 10px 0 rgba(34, 36, 38, .15);
            padding: 1em;
            font-size: 14px;
            position: absolute;
        }
        
        ul {
            padding: 0;
        }
        
        ul li {
            list-style-type: none;
            padding: 0px;
        }
        
        .tx-rg {
            text-align: right;
        }
        .tx-lf{
            text-align:left;
        }
        
        .span_status {
            color: #adadad;
        }
        
        .fl-rg {
            float: right;
        }
        
        .fl-lf {
            float: left;
        }
        
        .pd-lf-10 {
            padding-left: 10px;
        }
        
        .pd-bt-10 {
            padding-bottom: 10px;
        }
        
        table {
            margin: 10px;
        }
        
        .container .item span {
            display: block;
            width: 90%;
           
        }
        
        .comment .avatar {
            width: 120px !important;
            height: 120px !important
        }
        
        .comment .content a {
            line-height: 120px !important;
            padding-left: 1em;
        }
        
        .col {
            display: block;
            padding-top: 10px;
            padding-bottom: 10px;
        }
        
        .col-line {
            height: 30px;
        }
        .blue-c{
            color:#2185D0;
        }
    </style>
</head>

<body>
    <div class="ui container segment" id="app1" style="width:900px;left:110px">
        <h3 class="ui header">订单详情</h3>
        <div class="ui divider"></div>
        <div class="column " style="width:100%;height:40px">
            <div class="fl-lf " style="width: 40%;text-align:left"> 订单号：${info.orderNo}</div>
            <div class="right item fl-rg" style="width: 46%; text-align:left"><span class="ui inverted tx-rg ">下单时间:<fmt:formatDate value="${info.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate> </span>
            </div>
            
        </div>
        <hr>
        <div style="width:100%;height:130px">
            <div class="ui comments">
                <div class="comment">
                    <a class="avatar">
                        <img src="${info.prodImg}">
                    </a>
                    <div class="content" style="float:left;margin-left: 0.5em; font-size:16px">
                        <a class="author blue-c"> 商品名称：<span >${info.prodName}</span></a>
                    </div>
                </div>
            </div>
        </div>
        <br>
        <div style="display:flex"></div>
        <ul>
            <li class="col-line">
                <div class="fl-lf  tx-rg " style="width:200px;">货源类型：
                </div>
                <div class="fl-lf"><c:if test="${info.prodType==0}">天然气</c:if>
                    <c:if test="${info.prodType==1}">原油</c:if>
                    <c:if test="${info.prodType==2}">管道气</c:if></div>
            </li>
            <li class="col-line">
                <div class="fl-lf  tx-rg " style="width:200px;">总吨数：</div>
                <div class="fl-lf"><c:if test="${empty info.orderNumber && empty info.confirmNumber}">${info.saleNum}</c:if>
                <c:if test="${empty info.orderNumber && !empty info.confirmNumber}">${info.confirmNumber}</c:if>
                <c:if test="${!empty info.orderNumber}">${info.orderNumber}</c:if> 吨
                </div>
            </li>
            <li class="col-line">
                <div class="fl-lf  tx-rg " style="width:200px;">总价：</div>
                <div class="fl-lf"><c:if test="${empty info.orderNumber && empty info.confirmNumber}">${info.saleMoney}</c:if>
                    <c:if test="${empty info.orderNumber && !empty info.confirmNumber}">${info.confirmNumber*info.comfirmPrice}</c:if>
                    <c:if test="${!empty info.orderNumber }">${info.orderNumber*info.orderPrice}</c:if> 元</div>
            </li>
            <li class="col-line">
                <div class="fl-lf  tx-rg " style="width:200px;">单价：</div>
                <div class="fl-lf"><c:if test="${empty info.orderNumber && empty info.confirmNumber}">${info.saleMoney/info.saleNum}</c:if>
                    <c:if test="${empty info.orderNumber && !empty info.confirmNumber}">${info.comfirmPrice}</c:if>
                    <c:if test="${!empty info.orderNumber  }">${info.orderPrice}</c:if> 元</div>
            </li>
            <li class="col-line">
                <div class="fl-lf  tx-rg " style="width:200px;">付款状态：</div>
                <div class="fl-lf"><c:if test="${info.orderState<2}">未付款</c:if>
                    <c:if test="${info.orderState>=2}">已付款</c:if>
                </div>
            </li>
            <li class="col-line">
                <div class="fl-lf  tx-rg " style="width:200px;">已收吨数：</div>
                <div class="fl-lf"><c:if test="${info.orderState<=2}">未发货</c:if>
                    <c:if test="${info.orderState>=3}">${info.sumNum}</c:if>
                </div>
            </li>
            <li class="col-line">
                <div class="fl-lf  tx-rg " style="width:200px;">货运方式：</div>
                <div class="fl-lf"><c:if test="${info.isSelfCar==0}">自提</c:if>
                    <c:if test="${info.isSelfCar==1}">平台物流</c:if>
                </div>
            </li>
            <li class="col-line">
                <div class="fl-lf  tx-rg " style="width:200px;">预计发货时间：</div>
                <div class="fl-lf">${info.deliverDate}</div>
            </li>
            <li class="col-line">
                <div class="fl-lf  tx-rg " style="width:200px;">预计收货时间：</div>
                <div class="fl-lf">${info.receiveDate}</div>
            </li>
        </ul>
        <br><br>

        <c:if test="${!empty info.zorders}">
            <h4 class="ui header">物流信息</h4>
          <div class="ui divider"></div>
        <c:forEach items="${info.zorders}" var="zorder">
        <div class="ui ignored positive message">
       <div class="col-line tx-lf"> 本次发货时间：<fmt:formatDate value="${zorder.zorderDate}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></div>

        <div class="col-line tx-lf">子订单单价: ${zorder.zorderPrice} 元</div>
        <div class="col-line tx-lf">子订单发货量：${zorder.zorderNum} 吨</div>
        <div class="col-line tx-lf">子订单成交额：${zorder.zorderMoney} 元</div>
            <c:if test="${not empty zorder.sendComment}">
            <div class="col-line tx-lf">发货备注：${zorder.sendComment}</div>
            </c:if>
            <c:if test="${not empty zorder.receiveComment}">
            <div class="col-line tx-lf">收货备注：${zorder.receiveComment}</div>
            </c:if>
        </div>
        <table class="ui celled padded table " >
            <thead>
                <tr>
                    <th class="single line ">车队联系人</th>
                    <th>电话</th>
                    <th>车牌号</th>
                    <th>车挂号</th>
                    <th>容量</th>
                  
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${zorder.orderVehicles}" var="orderVehicle">
                <tr v-for="vehicle in Vehicles " v-cloak>
                    <td>
                        <h3 class="ui center aligned header xs " id="x" onclick="x()" value="${orderVehicle.driverName}">${orderVehicle.driverName}</h3>
                    </td>
                    <td class="single line ">${orderVehicle.driverPhone}</td>
                    <td> ${orderVehicle.vehicleNo}</td>
                    <td>${orderVehicle.vehicleHangingNo}</td>
                    <td>${orderVehicle.vehicleActualSize} 吨</td>
                   

                </tr>
            </c:forEach>

            </tbody>
            <tfoot class="full-width">
                <tr>
                    <th> 状态：</th>
                    <th colspan="4">
                        <c:if test="${zorder.zorderState==1}">
                            <div style="width: 500px;height:200px">
                                <span>comment:</span>
                                <textarea name="receiveComment" ></textarea>
                            </div>
                            <div class="ui right floated small primary icon button" onclick="confirmReceive(${zorder.zorderId},this)"> 确认收货 </div></c:if>
                         <c:if test="${zorder.zorderState==2}"><div class="ui  ">已收货 </div></c:if>

                    </th>
                </tr>
            </tfoot>
        </table>
<br>
             <hr>
              
        </c:forEach>
         
            </c:if>
        <button onclick="javascript:window.history.go(-1);">返回</button>
            </div>
    </div>
</body>
<script>
    function confirmReceive(zorderId,com){
        var receiveComment=com.previousElementSibling.children[1].value;
        if(confirm("你要确定要操作吗？")){
            $.get("${pageContext.request.contextPath}/customer/zorderConfirmReceive",{zorderId:zorderId,receiveComment:receiveComment},function(data){
                if(data.code==0){
                    window.location.reload();
                }
            },"json")
                .error(function(data){
                    alert("操作失败！");
                });
        }
    }
</script>

<%@ include file="../footer.jsp"%>