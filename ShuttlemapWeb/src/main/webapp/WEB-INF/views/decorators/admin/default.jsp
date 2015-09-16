<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.springframework.context.*" %>
<%@ page import="org.springframework.web.context.support.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="Content-Script-Type" content="text/javascript" />
	<meta http-equiv="Content-Style-Type" content="text/css" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>셔틀맵</title>
	<script>
		
		var _requestPath = "${pageContext.request.contextPath}";
		var _imageServerPath = "${_imageServerPath}";
		
		
	</script>	

	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/jquery/smoothness/jquery-ui-1.9.1.custom.min.css"/>	
	<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.0/css/jquery.dataTables.css"/>	
	<link rel="stylesheet" type="text/css" href="//cdn.datatables.net/plug-ins/e9421181788/integration/bootstrap/3/dataTables.bootstrap.css"/>	
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/script/jquery/fancybox/jquery.fancybox.css"/>	
	
	<link rel="stylesheet" type="text/css" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/bootstrap/datepicker.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/common.css"/>	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/front.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/mygarden.css"/>
	
	
	<script type="text/javascript" src="//code.jquery.com/jquery-1.10.2.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/jquery/jquery-ui-1.9.1.custom.min.js"></script>
	<!-- script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/jquery/fancybox/jquery.fancybox.pack.js"></script -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/jquery/jquery.masonry.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/jquery/jquery.imagesloaded.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/jquery/jquery.number.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/jquery/jquery.tmpl.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/jquery/jqGrid-4.4.1/js/i18n/grid.locale-kr.js"></script>
	<script type="text/javascript" src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="//cdn.datatables.net/1.10.0/js/jquery.dataTables.js"></script>
	<script type="text/javascript" src="//cdn.datatables.net/plug-ins/e9421181788/integration/bootstrap/3/dataTables.bootstrap.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/bootstrap/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/bootstrap/bootstrap-datepicker.kr.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/bootstrap/bootbox.min.js"></script>	
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/common.js"></script>
	
	
	
	<decorator:head />
</head>


<c:url value="/myGarden/mydesk/home" var="myDeskHomeUrl"/>
<c:url value="/myGarden/mydesk/myInfo" var="myInfoUrl"/>
<c:url value="/logout" var="logoutUrl"/>
<c:url value="/login/loginForm" var="loginUrl"/>
<c:url value="/myGarden/sharing/orderList" var="myPageUrl"/>
<body>

	<div id="main-container">
		<div id="main-header">
			<div class="container main-header-inner">
				
				<a href="${pageContext.request.contextPath}/admin/home" class="header-logo"> </a>
				
				<form id="searchForm">
				<div id="searchArea">
					<input type=text id="topSearchKeyword" name="topSearchKeyword" placeholder="노선을 검색해보세요." class="auto-hint"/>
					<a href="javascript:searchProduct();" class="search-btn"></a>
				</div>
				</form>
				
				<div class="logout-area">
					<img src="${pageContext.request.contextPath}/resources/images/1.5/icon_menu_1.png" class="textmiddle"/><span class="span-margin-left"><a href="${logoutUrl}">로그아웃</a></span>
				</div>
				
				<div class="top-menu-mobile">
					<img src="${pageContext.request.contextPath}/resources/images/1.5/icon_menu_2.png" class="textmiddle"/><span class="span-margin-left"><a href="#">모바일 앱</a></span>
				</div>
				
			
			</div>
		</div>
		<div id="main-body-wrapper">
			<div class="menu-bg">
				<nav class="navbar navbar-default container opengarden-nav" role="navigation">
				  <div class="container-fluid">
				    <!-- Brand and toggle get grouped for better mobile display -->
				    <div class="navbar-header">
				      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
				        <span class="sr-only">Toggle navigation</span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				      </button>
				     
				    </div>
				
				    <!-- Collect the nav links, forms, and other content for toggling -->
				    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
				      <ul class="nav navbar-nav">
				      	<c:if test="${_roleName eq 'ROLE_ADMIN'}">
				      		<li class="dropdown">
					        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">협회 관리</a>
					        	<ul class="dropdown-menu">
						            <li><a href="${pageContext.request.contextPath}/admin/listAssociation");">협회 리스트</a></li>
						            <li><a href="${pageContext.request.contextPath}/admin/editAssociationForm");">협회 등록</a></li>
						        </ul>
					        </li>
					        <li class="dropdown">
					        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">기관 관리</a>
					        	<ul class="dropdown-menu">
						            <li><a href="${pageContext.request.contextPath}/admin/listCompany");">기관 리스트</a></li>
						        </ul>
					        </li>
					         <li class="dropdown">
					        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">셔틀 관리</a>
					        	<ul class="dropdown-menu">
						            <li><a href="${pageContext.request.contextPath}/admin/listShuttle");">셔틀 리스트</a></li>
						        </ul>
					        </li>
					      	<li class="dropdown">
					        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">회원관리</a>
					        	<ul class="dropdown-menu">
						            <li><a href="${pageContext.request.contextPath}/admin/listUser");">등록된 회원</a></li>
						         </ul>
					        </li>
					        <li class="dropdown">
					        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">정산/통계</a>
					        	<ul class="dropdown-menu">
						            <li><a href="javascript:goCategoryMain('${secondCategory.id}');">사용자 통계</a></li>
						            <li><a href="javascript:goCategoryMain('${secondCategory.id}');">기관정산</a></li>
						         </ul>
					        </li>
				        </c:if>
				        <c:if test="${_roleName eq 'ROLE_ASSOCIATION_USER'}">
				        	<li class="dropdown">
					        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">협회 관리</a>
					        	<ul class="dropdown-menu">
					        		<li><a href="${pageContext.request.contextPath}/admin/editAssociationFormProxy");">협회 상세 정보</a></li>
						        </ul>
					        </li>
					        <li class="dropdown">
					        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">기관 관리</a>
					        	<ul class="dropdown-menu">
						            <li><a href="${pageContext.request.contextPath}/admin/listCompanyProxy");">기관 리스트</a></li>
						            <li><a href="${pageContext.request.contextPath}/admin/editCompanyFormProxy");">기관 등록</a></li>
						        </ul>
					        </li>
					        <li class="dropdown">
					        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">셔틀 관리</a>
					        	<ul class="dropdown-menu">
						            <li><a href="${pageContext.request.contextPath}/admin/listShuttleProxy");">셔틀 리스트</a></li>
						        </ul>
					        </li>
				        </c:if>
				        <c:if test="${_roleName eq 'ROLE_COMPANY_USER'}">
				        	<li class="dropdown">
					        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">기관 관리</a>
					        	<ul class="dropdown-menu">
					        		<li><a href="${pageContext.request.contextPath}/admin/editCompanyFormProxy");">기관 상세 정보</a></li>
						        </ul>
					        </li>
					        <li class="dropdown">
					        	<a href="#" class="dropdown-toggle" data-toggle="dropdown">셔틀 관리</a>
					        	<ul class="dropdown-menu">
						            <li><a href="${pageContext.request.contextPath}/admin/listShuttleProxy");">셔틀 리스트</a></li>
						        </ul>
					        </li>
				        </c:if>
				      </ul>
				     
				    </div><!-- /.navbar-collapse -->
				  </div><!-- /.container-fluid -->
				</nav>
			</div>
				<decorator:body />
			
			
		</div>
		<div id="main-footer">
			
				<%@ include file="/WEB-INF/views/decorators/footer.jspf" %>
			
		</div>
		
		<div id="totaMask"></div>
		
	</div>
</body>

</html>