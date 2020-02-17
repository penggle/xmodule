<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
</head>
<body>
	<h1>首页</h1>
	<div>
		<a href="${pageContext.request.contextPath}/logout" style="float:right;">退出</a>
	</div>
	<div>
		<h1>Welcome ${userInfo}!</h1>
	</div>
</body>
</html>