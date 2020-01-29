<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>
	<h1>Hydra User Login</h1>
	
	<h2>Please sign in</h2>
	<c:if test="${error}">
		<div style="line-height:45px;color:red;">${message}</div>
	</c:if>
	<form action="${pageContext.request.contextPath}/login" method="post">
		<input type="hidden" name="loginChallenge" value="${loginChallenge}"/>
		<div>
			<label> Username: <input type="text" name="username" />
			</label>
		</div>
		<div>
			<label> Password: <input type="password" name="password" />
			</label>
		</div>
		<div>
			<label><input type="checkbox" name="remember" value="true"/> Remember Me</label>
		</div>
		<div>
			<input type="submit" value="Sign In" />
		</div>
	</form>
</body>
</html>