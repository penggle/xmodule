<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>login</title>
</head>
<body>
	<h1>User Login</h1>
	
	<h2>Please sign in</h2>
	<c:if test="${requestScope.error}">
		<div style="line-height:45px;color:red;">${requestScope.message}</div>
	</c:if>
	<form action="${pageContext.request.contextPath}/login" method="post">
		<div>
			<label> Username: <input type="text" name="username" />
			</label>
		</div>
		<div>
			<label> Password: <input type="password" name="password" />
			</label>
		</div>
		<div>
			<input type="submit" value="Sign In" />
		</div>
	</form>
	<br/>
	<h2>Login with OAuth 2.0</h2>
	<ul>
		<c:forEach items="${oauth2LoginLinks}" var="item">
			<li><a href="${item.key}"/>${item.value}</li>
		</c:forEach>
	</ul>
</body>
</html>