<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>
	<title>셔틀맵</title>

	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/shuttlemap/editAssociationForm.js"></script>
</head>
<body> 
	<div class="container">
		<h3 class="page-header">협회 정보</h3>
		<c:url value="/admin/saveAssociation" var="formUrl"/>
		
		<form id="associationForm" class="form-horizontal" role="form" enctype="multipart/form-data" method="post" action="${formUrl}">
			<input type=hidden id="associationId" name="associationId" value="${association.id}"/>
			
			<div class="info-section-top">
				<div class="opengarden-small-title">
					 기본정보
				</div>
				<div class="tab-content">
					<div class="tab-pane active opengarden-tab">
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 협회명</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	<input type="text" id="name" name="name" class="form-control input-sm" value="${association.name}" 
							     		placeholder="협회명을 입력하세요."
							     		data-counter="#nameCounter" data-text-length="50"
							     		/>
							    </div>
							    <div class="col-lg-2 height-sm">
							    	<code id="nameCounter">0/50</code>
							    </div>
							 </div>
						</div>
						
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 전화번호</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
						     	 	<input type="text" id="phone" name="phone" class="form-control input-sm" value="${association.phone}" 
						     			placeholder="협회 전화번호를 입력하세요."/>
						     	</div>
							    <div class="col-lg-5 height-sm">
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 등록번호</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	 <input type="text" id="regNo" name="regNo" class="form-control input-sm" value="${association.regNo}" 
							     		placeholder="등록번호를 입력하세요."/>
							    </div>
							    <div class="col-lg-5 height-sm">
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 대표자</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	 <input type="text" id="presidentName" name="presidentName" class="form-control input-sm" value="${association.presidentName}" 
							     		placeholder="대표자 이름을 입력하세요."
							     		data-counter="#contactPersonCounter" data-text-length="10"
							     		/>
							    </div>
							    <div class="col-lg-5 height-sm">
							    	<code id="contactPersonCounter">0/10</code>
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label">휴대폰번호</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	 <input type="text" id="mobilePhone" name="mobilePhone" class="form-control input-sm" value="${association.mobilePhone}" />
							    </div>
							    <div class="col-lg-5 height-sm">
							    	
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label">이메일</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	 <input type="text" id="email" name="email" class="form-control input-sm" value="${association.email}" />
							    </div>
							    <div class="col-lg-5 height-sm">
							    	
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label">Fax</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	 <input type="text" id="faxNo" name="faxNo" class="form-control input-sm" value="${association.faxNo}" />
							    </div>
							    <div class="col-lg-5 height-sm">
							    	
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label">협회 관리자 아이디</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
									<c:choose>
										<c:when test="${!empty companyUser}">
											<input type="text" id="userLoginId" name="userLoginId" class="form-control input-sm" value="${associationUser.loginId}" 
								     	 		readonly="readonly"/>
										</c:when>
										<c:otherwise>
											<input type="text" id="userLoginId" name="userLoginId" class="form-control input-sm" value="${associationUser.loginId}" 
								     	 		placeholder="셔틀맵 시스템 로그인 아이디를 입력하세요."/>
										</c:otherwise>
							     	 </c:choose>
							    </div>
							    <div class="col-lg-5 height-sm">
							    	
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label">협회 관리자 비밀번호</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
								<c:choose>
									<c:when test="${empty companyUser.loginId}">
							     		 <input type="password" id="userPassword" name="userPassword" class="form-control input-sm" value="" 
							     	 		placeholder="셔틀맵 시스템 비밀번호를 입력하세요."/>
							     	</c:when>
							     	<c:otherwise>
							     		
							     	</c:otherwise> 	
							     </c:choose>
							    </div>
							    <div class="col-lg-5 height-sm">
							    </div>
							 </div>
						</div>
					</div>
				</div>
			</div>
		</form>
		<div class="info-section">
			<div class="container align-center">
				<button type="button" id="btnSave" class="btn og-btn-primary" onclick="saveAssociation();">저 장</button>
				<c:if test="${!empty association.id}">
					<button  type="button" class="btn og-btn-warning" onclick="goCompanyList('${association.id}');">소속 기관 관리하기</button>
				</c:if>
			</div>
		</div>
		
	</div>
	
</body>
</html>