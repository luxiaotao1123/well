<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE HTML>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>error-404</title>
<style type="text/css">
a:hover {
	font-family: 微软雅黑;
	font-size: 16px;
	color: #C00;
	text-decoration: none;
}

a:visited {
	font-family: 微软雅黑;
	font-size: 16px;
	color: #C00;
	text-decoration: none;
}

a:link {
	font-family: 微软雅黑;
	font-size: 16px;
	color: #C00;
	text-decoration: none;
}

a:active {
	font-family: 微软雅黑;
	font-size: 16px;
	color: #C00;
	text-decoration: none;
}
</style>
</head>
<body
	background="<c:url value="/resources/sys/images/error-404_05.jpg"/>">

	<table width="1000" border="0" align="center" cellpadding="0"
		cellspacing="0" height="139" background="">
		<tr>
			<td></td>
		</tr>
	</table>

	<table width="716" border="0" align="center" cellpadding="0"
		cellspacing="0" height="376"
		background="<c:url value="/resources/sys/images/error-404_09.jpg"/>"
		style="background-repeat: no-repeat;">
		<tr>
			<td width="254" height="130">&nbsp;</td>
			<td colspan="2">&nbsp;</td>
			<td width="62">&nbsp;</td>
		</tr>
		<tr>
			<td height="80">&nbsp;</td>
			<td width="329" colspan="2" rowspan="3" valign="top"
				style="font-family: 微软雅黑; font-size: 16px; color: #757575;">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="1" height="5"></td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td>1. 您在地址中可能存在键入错误。</td>
					</tr>
					<tr>
						<td height="10"></td>
						<td></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>2. 当您点击某个链接时，它可能已过期。</td>
					</tr>
					<tr>
						<td height="20"></td>
						<td></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td>您可以尝试以下操作：</td>
					</tr>
					<tr>
						<td height="10"></td>
						<td></td>
					</tr>
					<tr>
						<td></td>
						<td>
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="100"><a title="返回上一页"
										href="javascript:history.back(-1)">返回上一页</a></td>
									<td width="100"><a title="退 出"
										href="javascript:top.location.href='${pageContext.request.contextPath}'">退
											出</a></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td height="32">&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
	</table>
</body>
</html>