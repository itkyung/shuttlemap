<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>
	<title>셔틀맵</title>

	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/shuttlemap/editCompanyForm.js"></script>
</head>
<body> 
	<div class="container">
		<h3 class="page-header">기관(협회)정보</h3>
		<c:url value="/admin/saveCompany" var="formUrl"/>
		<form id="companyForm" class="form-horizontal" role="form" enctype="multipart/form-data" method="post" action="${formUrl}">
			<input type=hidden id="companyId" name="companyId" value="${company.id}"/>
			
			<div class="info-section-top">
				<div class="opengarden-small-title">
					기관(협회)기본정보
				</div>
				<div class="tab-content">
					<div class="tab-pane active opengarden-tab">
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 기관명</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	<input type="text" id="name" name="name" class="form-control input-sm" value="${company.name}" 
							     		placeholder="기관명을 입력하세요."
							     		data-counter="#nameCounter" data-text-length="50"
							     		/>
							    </div>
							    <div class="col-lg-2 height-sm">
							    	<code id="nameCounter">0/50</code>
							    </div>
							 </div>
						</div>
					
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 기관종류</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	<select id="companyType" name="companyType" class="form-control input-sm">
										<option value="EDUCATION">보습학원</option>
										<option value="CHURCH">교회</option>
										<option value="DRIVE">대리운전</option>
									</select>
								</div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 전화번호</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
						     	 	<input type="text" id="phone" name="phone" class="form-control input-sm" value="${company.phone}" 
						     			placeholder="업체전화번호를 입력하세요."/>
						     	</div>
							    <div class="col-lg-5 height-sm">
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 사업자등록번호</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	 <input type="text" id="licenseNo" name="licenseNo" class="form-control input-sm" value="${company.licenseNo}" 
							     		placeholder="사업자등록번호를 입력하세요."/>
							    </div>
							    <div class="col-lg-5 height-sm">
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 담당자</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	 <input type="text" id="contactPerson" name="contactPerson" class="form-control input-sm" value="${company.contactPerson}" 
							     		placeholder="담당자이름을 입력하세요."
							     		data-counter="#contactPersonCounter" data-text-length="10"
							     		/>
							    </div>
							    <div class="col-lg-5 height-sm">
							    	<code id="contactPersonCounter">0/10</code>
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label">홈페이지</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	 <input type="text" id="homepage" name="homepage" class="form-control input-sm" value="${company.homepage}" />
							    </div>
							    <div class="col-lg-5 height-sm">
							    	
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label">기관관리자 아이디</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
									<c:choose>
										<c:when test="${!empty companyUser}">
											<input type="text" id="userLoginId" name="userLoginId" class="form-control input-sm" value="${companyUser.loginId}" 
								     	 		readonly="readonly"/>
										</c:when>
										<c:otherwise>
											<input type="text" id="userLoginId" name="userLoginId" class="form-control input-sm" value="${companyUser.loginId}" 
								     	 		placeholder="셔틀맵 시스템 로그인 아이디를 입력하세요."/>
										</c:otherwise>
							     	 </c:choose>
							    </div>
							    <div class="col-lg-5 height-sm">
							    	
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label">기관관리자 비밀번호</label>
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
				<button type="button" id="btnSave" class="btn og-btn-primary" onclick="saveCompany();">저 장</button>
			</div>
		</div>
		<form id="resultTable">
			<div class="search-result-title">
				<div class="pull-left">
					검색결과 : 총 <code id="totalCount"> 0</code> 개.
				</div>
				<div class="pull-right">
					<button  type="button" class="btn og-btn-warning" onclick="registShuttle();">셔틀 등록</button>
				</div>
			</div>
			<table id="searchResult" class="display table-bordered" cellspacing="0" width="100%">
				<thead>
		            <tr>
		                <th></th>
		                <th>노선명</th>
		                <th>차종</th>
		                <th>차량번호</th>
		                <th>운행타입</th>
		            </tr>
		        </thead>
		        
			</table>
			
		</form>
	</div>
	
</body>
</html>