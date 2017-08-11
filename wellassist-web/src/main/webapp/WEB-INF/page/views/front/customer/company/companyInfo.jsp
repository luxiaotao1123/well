<%@ include file="../header.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<head>

<!-- custom css -->
<link rel="stylesheet"
	href="<c:url value="/resources/wella/front/css/pagetempl.css"/>">

<script src="<c:url value="/resources/library/js/chosen.jquery.js"/>"></script>
<script src="<c:url value="/resources/library/js/bootstrap-fileupload.js"/>"></script>

<style>
.container1{min-height: 990px;width: 100%;}
select{width:120px;}
input {
	padding: 5px;
    font-size: 14px;
}
div.error{
	width: 200px;
   	display: inline-block;
   	padding-left: 42px;
}
        .file {
            position: relative;
            display: inline-block;
            background: #21BA45;
            border: 1px solid #21BA45;
            border-radius: 4px;
            padding: 4px 12px;
            overflow: hidden;
            color: #FFF;
            text-decoration: none;
            text-indent: 0;
            line-height: 20px;
        }

        .file input {
            position: absolute;
            font-size: 100px;
            right: 0;
            top: 0;
            opacity: 0;
        }

        .file:hover {
            background: #AADFFD;
            border-color: #78C3F3;
            color: #004974;
            text-decoration: none;
        }
        .white-color{color: #fff}
        .excel-inco{
                font-size: 25px !important;
			    width: 45px !important;
			    background: #fff;
			    border: none;
			    color: green;
			    float: right;
			    margin-top: -25px !important;
        }
</style>

</head>
<div class="container1">
	<div style="margin:40px 0 0 210px;">
		<div id = "content-rect" style="width:90%;">
			<div style="border-bottom:solid 1px #d0d0d0;padding-bottom:10px;font-size:15px;font-weight: 600;">企业信息</div>

			<form action="#" method="post">		
				<div style="font-size:14px;padding:32px 45px;display: inline-block;width: 90%;">
					<div>
						<div id="companyicon" style="float:left;width:12%;height:100px;border:solid 1px #ccc;">
							<img id="icon" style="margin:8px;" src="${userInfo.companyImg}">							
						</div>
						<div style="float:left;margin-left:32px;">
							<div style="font-size:20px;font-weight:bold;margin-bottom:12px;">${user.userName}</div>
							<div style="margin-bottom:12px;font-size:16px;">
								<img src="<c:url value="/resources/wella/front/images/seller/lianxiicon1.png"/>">
								<span style="margin-left:8px;margin-right:42px;">实名认证</span>
								<img src="<c:url value="/resources/wella/front/images/seller/lianxiicon2.png"/>">
								<span style="margin-left:8px;margin-right:42px;">手机认证</span>
								<img src="<c:url value="/resources/wella/front/images/seller/lianxiicon3.png"/>">
								<span style="margin-left:8px;margin-right:42px;">营业执照已认证</span>
							</div>
							<div style="margin-bottom:12px;font-size:16px;">
								<span style="margin-right:12px;">近三个月交易:</span>
								<span style="color:red;">${threeJyCn}</span>
								<span style="margin-right:24px;">笔</span>
								<span style="margin-right:12px;">进行中的交易:</span>
								<span style="color:red;">${ingJyCn}</span>
								<span style="margin-right:24px;">笔</span>
							</div>
							<div class="ui" style="margin-bottom:12px;font-size:16px;">
								<a id="upload"  class="ui button primary">更换头像</a>
								<!-- <a class="fancybox" rel="group" href="userInfo.companyImg">
									<img id="icon" src="userInfo.companyImg" width="136px" height="134px" />
								</a> -->
							</div>
						</div>
					</div>
				</div>
				<div style="margin-top:24px;display: inline-block;width: 90%;padding-left:45px;">
					<span style="display:inline-block;width:10%;font-size:14px;">所在地区</span>
					<select class="ui selection dropdown" id="provinceId" name="provinceId" onchange="selRegion(0);">
						<option>--请选择省--</option>
						<c:forEach items="${provinceList}" var="item" varStatus="status">
							<option value="${item.regionId}" <c:if test="${item.regionId==provinceId}" >selected</c:if> >${item.regionName}</option>
						</c:forEach>
					</select>
					<select class="ui selection dropdown" id="cityId" name="cityId" onchange="selRegion(1);">
						<option>--请选择市--</option>
						<c:forEach items="${cityList}" var="item" varStatus="status">
							<option value="${item.regionId}"  <c:if test="${item.regionId==cityId}" >selected</c:if>  >${item.regionName}</option>
						</c:forEach>
					</select>
					<select class="ui selection dropdown" id="regionId" name="regionId" onchange="selRegion(2);">
						<option>--请选择区--</option>
						<c:forEach items="${countyList}" var="item" varStatus="status">
							<option value="${item.regionId}"  <c:if test="${item.regionId==userInfo.zcRegionId}" >selected</c:if> >${item.regionName}</option>
						</c:forEach>
					</select>
				</div>
				<div style="margin-top:24px;padding-left:45px;display: inline-block;width: 90%;">
					<span style="display:inline-block;width:10%;font-size:14px;">详细地址</span>
					<div class="ui input" style="width:38.7%;">
						<input type="textarea" name="zcXxAddress" id="zcXxAddress" placeholder="请输入详细地址" style="width:40%;" value="${userInfo.zcXxAddress}">
					</div>
				</div>
		
		
				<div style="margin-top:24px;padding-left:45px;display: inline-block;width: 90%;">
					<span style="display:inline-block;width:10%;font-size:14px;">电子邮箱</span>
					<div class="ui input" style="width:38.7%;">
						<input type="text" name="userEmail" id="userEmail" placeholder="请输入电子邮箱" style="width:40%;" value="${user.userEmail}">
					</div>
				</div>
				
				<div style="margin-top:24px;padding-left:45px;display: inline-block;width: 90%;">
					<span style="display:inline-block;width:10%;font-size:14px;">手机号码</span>
					<div class="ui input" style="width:38.7%;">
						<input type="text" name="userPhone" id="userPhone" placeholder="请输入手机号码" style="width:40%;" value="${user.userPhone}">
					</div>
				</div>
				
				
				<div style="margin-bottom: 32px;margin-top: 32px;margin-left: 11%;">
					<!-- <span id="submit" class="bluebutton" style="padding-left: 16px;padding-right: 16px;padding-top: 8px;padding-bottom: 8px;font-size:20px;border-radius: 6px;" align=center>确认下单</span> -->
					<input type="submit" id="submit" class=" ui primary button" value="保存">
					
				</div>
                <input type="hidden" name="zc_region_id" id="zc_region_id" value="${userInfo.zcRegionId}"/>
                <div style="border-bottom:solid 1px #d0d0d0;padding-bottom:10px;font-size:15px;font-weight: 600;">资质信息</div>
			<div style="margin-top:24px;padding-left:45px;width: 90%;">
				<span>法人身份证：</span>
				<div class="ui input" style="width:30%;">
					<input type="text" name="IDcard" id="IDcard" placeholder="请输入法人的身份证号" value="">
				</div>
				
	                <div class="ui three column grid">
	                    <div class="row" style="margin-top:30px;">
                        <div  class=" ui cards">
                             <div class="card" style=" margin-left: 24px;">
                                <div class="content">
                                    <div class="header">
                                       营业执照
                                    </div>
                                    <div class="description">
                                        请上传jpg、png文件
                                    </div>
                                   
                                 </div>
                                <div class="extra content field" >
                                    <div class="meta"  style=" height: 35px">
                                    	<img src="" />
                                    </div>
                                    <a href="javascript:;" class="file" style="margin-top:55px;">
                                         <span class="white-color">选择文件</span>
                                          <i class="upload icon white-color" ></i>
                                         <input type="file"  id="upload1">
                                        <input type="hidden" name="companyYyZzImg">
                                        <%--<a id="bankBill" download="bankBill.xlsx">download</a>--%>
                                    </a>                                       
                                 </div>                                
                             </div>
                             <div class="card">
                                <div class="content">
                                    <div class="header">
                                       许可证
                                    </div>
                                    <div class="description">
                                        请上传jpg、png文件
                                    </div>
                                   
                                 </div>
                                <div class="extra content field" >
                                    <div class="meta"  style=" height: 35px">
                                    	<img src="../img/zxlogo.jpg" />
                                    </div>
                                    <a href="javascript:;" class="file"  style="margin-top:55px;">
                                         <span class="white-color">选择文件</span>
                                          <i class="upload icon white-color" ></i>
                                         <input type="file" id="upload2">
                                        <input type="hidden" name="openper">
                                    </a>
                                        
                                 </div>
                             </div>
                            <div class="card">
                                        <div class="content">
                                            <div class="header">
                                            特许经营许可证
                                            </div>
                                            <div class="description">
                                                请上传jpg、png文件
                                            </div>
                                        </div>
                                        <div class="extra content field" >
                                            <div class="meta"  style=" height: 35px">
		                                    	<img src="../img/logo.png" />
		                                    </div>
                                            <a href="javascript:;" class="file" style="margin-top:55px;">
                                                <span class="white-color">选择文件</span>
                                                <i class="upload icon white-color" ></i>
                                                <input type="file" id="upload3">
                                                <input type="hidden" name="companyart">
                                            </a>
                                                
                                        </div>
                                        
                             </div>
                        </div>
                    </div>                
	                </div><br>

	                <div class="ui primary  button">保存</div>              
            	
			</div>

			</form>

			
		</div>
	</div>
</div>
<script type="text/javascript">
	$("#companyicon").height($("#companyicon").width());
	$("#icon").height($("#companyicon").innerHeight()-16);
	$("#icon").width($("#companyicon").innerWidth()-16);
	
	$("form").validate({
        rules: {
        	zcXxAddress: "required",
        	userPhone: "required",
        	userEmail: "required",
            companyYyZzImg:'required',
            companyXkzImg:'required',
            companyTxkzImg:'required'
        },
        messages: {
        	zcXxAddress: "请输入详细地址！",
        	userPhone: "请输入手机号码！",
        	userEmail: "请输入电子邮箱！",
            companyYyZzImg:'请上传许可证',
            companyXkzImg:'许可证',
            companyTxkzImg:'特许经营许可证'
        },
	    submitHandler: function(form){
	    	var region_id = $("#zc_region_id").val();
	    	if(region_id=="")
    		{
    			alert("请选择地区信息");
    			return false;
    		}
	    	var act_url = "${pageContext.request.contextPath}/front/seller/company/SellerCompanyController-companyInfoUpdate";
	    	$(form).attr("action", act_url);
			$.post($(form).attr("action"),$(form).serialize(),function(data){
	    		data = $.parseJSON(data);
	    		if(data.state==1){
	            	alert("保存成功!");
	            }else{
	            	alert(data.content);
	            }
	      	})
	      	.error(function(data){
	      		alert("操作失败！")
	      	});
	    	
	    },
		errorElement: "div",
		errorClass: "error"
	});

	function selRegion(type){
		var regionId = '';
		
		if(type==0){
			regionId = $("#provinceId").val();
		} else if(type==1){
			regionId = $("#cityId").val();
		} else if(type==2){
			regionId = $("#regionId").val();
		} else return;
		$("#zc_region_id").val(regionId);
		if(regionId!=''){
			$.post("${pageContext.request.contextPath}/front/sender/SenderLoginController-getChildRegionListInSite", {regionId:regionId},	function(data) {
				if(data.state == 1) {
					html = "";
					for(var i=0; i<data.regionList.length; i++){
						region = data.regionList[i];
						html += "<option value='" + region.regionId + "'>" + region.regionName + "</option>";
					}
					
					if(type==0){
						$("#cityId").html("<option>--请选择市--</option>" + html);
						$("#regionId").html("<option>--请选择区--</option>");
					} else if(type==1){
						$("#regionId").html("<option>--请选择区--</option>" + html);
					}
				}
			}, 'json');
		}
	}
</script>
<script>
            $(function () {
                new AjaxUpload('#upload1', {
                    action: '${pageContext.request.contextPath}/uploadFile',
                    name: 'file',
                    autoSubmit:true,
                    responseType:"json",
                    onSubmit:function(file, extension){
                        if (!(extension && /^(jpg|png)$/.test(extension.toLowerCase()))){
                            alert('只支持jpg、png格式的文件！');
                            return false;
                        }
                    },
                    onComplete : function(file, data){
                        if(data.result=="-10") { ShowWindowAlert("提示",data.msg,"","确 定",""); return; }
                        $(":input[name='companyper'][type='hidden']").val(data.path);
                        /*$("a#bankBill").attr("href",data.path);*/
                        handleFiles(document.getElementById("upload1"),file);
                        return;
                    }
                });
                new AjaxUpload('#upload2', {
                    action: '${pageContext.request.contextPath}/uploadFile',
                    name: 'file',
                    autoSubmit:true,
                    responseType:"json",
                    onSubmit:function(file, extension){
                        if (!(extension && /^(jpg|png)$/.test(extension.toLowerCase()))){
                            alert('只支持jpg、png格式的文件！');
                            return false;
                        }
                    },
                    onComplete : function(file, data){
                        if(data.result=="-10") { ShowWindowAlert("提示",data.msg,"","确 定",""); return; }
                        $(":input[name='openper'][type='hidden']").val(data.path);
                        handleFiles(document.getElementById("upload2"),file);
                        return;
                    }
                });
                new AjaxUpload('#upload3', {
                    action: '${pageContext.request.contextPath}/uploadFile',
                    name: 'file',
                    autoSubmit:true,
                    responseType:"json",
                    onSubmit:function(file, extension){
                        if (!(extension && /^(jpg|png)$/.test(extension.toLowerCase()))){
                            alert('只支持jpg、png格式的文件！');
                            return false;
                        }
                    },
                    onComplete : function(file, data){
                        if(data.result=="-10") { ShowWindowAlert("提示",data.msg,"","确 定",""); return; }
                        $(":input[name='companyart'][type='hidden']").val(data.path);
                        handleFiles(document.getElementById("upload3"),file);
                        return;
                    }
                });
                $('.ui.form').form({
                    fields: {
                        companyper:{
                            identifier: 'companyper',
                            rules: [
                                {
                                    type: 'empty',
                                    prompt: '请上传营业执照'
                                }
                            ]
                        },
                        openper:{
                            identifier: 'openper',
                            rules: [
                                {
                                    type: 'empty',
                                    prompt: '请上传开户许可'
                                }
                            ]
                        },
                        companyart:{
                            identifier: 'companyart',
                            rules: [
                                {
                                    type: 'empty',
                                    prompt: '请上传公司章程'
                                }
                            ]
                        }
                    },
                    inline: true,
                    on: 'blur'
                })
                    ;
            })
            function handleFiles(files,filename) {
                var filesShow= files.parentNode.parentNode.firstChild.nextElementSibling;
                if (!filename) {
                }
                else{
                  filesShow.innerHTML = "<p>"+filename+"</p>"+ '<i class="file archive outline icon excel-inco">'+'</i>';
                }
            }
         
        </script>
<%@ include file="../footer.jsp"%>