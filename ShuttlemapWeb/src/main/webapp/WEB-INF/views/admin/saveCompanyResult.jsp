<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>
	<title>셔틀맵</title>
	
	<script>
		
		setTimeout(function(){
			document.location.href = _requestPath + "/admin/editCompanyForm?id=${companyId}"; 
		},1000);
	
	</script>
</head>
<body>
	<div class="container">
		<c:choose>
			<c:when test="${success}">
				<h3>성공적으로 저장되었습니다.</h3>
			</c:when>
			<c:otherwise>
				<h3>${errorMsg}</h3>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>