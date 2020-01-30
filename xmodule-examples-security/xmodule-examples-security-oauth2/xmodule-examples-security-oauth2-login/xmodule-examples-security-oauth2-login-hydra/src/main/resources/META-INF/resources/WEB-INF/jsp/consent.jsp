<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Consent</title>
</head>
<body>
	<h1>Hydra User Consent</h1>
	
	<h2>An application requests access to your data!</h2>
	
	<form action="${pageContext.request.contextPath}/consent" method="post">
		<input type="hidden" name="consentChallenge" value="${consentChallenge}"/>
		
		<c:if test="${oauth2Client.logoUri}">
			<img src="${oauth2Client.logoUri}">
		</c:if>
		
		<p>
			Hi ${oauth2Subject}, application <strong>${oauth2Client.clientId}</strong> wants access resources on your behalf and to:
		</p>
		<ul>
			<c:forEach items="${oauth2Scopes}" var="item">
				<li><label>${item} <input type="checkbox" name="grantScopes" value="${item}"/></label></li>
			</c:forEach>
		</ul>
		
		<br/>
		
		<p>
			Do you want to be asked next time when this application wants to access your data? The application will not be able to ask for more permissions without your consent.
		</p>
		<ul>
			<c:if test="${oauth2Client.policyUri}">
				<li><a href="${oauth2Client.policyUri}">Policy</a></li>
			</c:if>
			<c:if test="${oauth2Client.tosUri}">
				<li><a href="${oauth2Client.tosUri}">Terms of Service</a></li>
			</c:if>
		</ul>
		
		<div>
			<label><input type="checkbox" name="remember" value="true"/> Do not ask me again</label>
		</div>
		
		<br/>
		
		<p>
			Finally, do you agree to the above authorization?
		</p>
		<div>
			<ul>
				<li><label>Allow Access <input type="radio" name="consentAccess" value="1" checked="checked"/></label></li>
				<li><label>Deny  Access <input type="radio" name="consentAccess" value="0"/></label></li>
			</ul>
		</div>
	
		<div>
			<input type="submit" value="Authorize" />
		</div>
	</form>
</body>
</html>