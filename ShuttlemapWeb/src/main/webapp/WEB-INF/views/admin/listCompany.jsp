<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>
	<title>셔틀맵</title>

	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/shuttlemap/listCompany.js"></script>
</head>
<body> 
	<div class="container">
		<h3 class="page-header">업체리스트</h3>
		
		<form id="searchForm" class="form-horizontal" role="form"  method="post">
			<div class="tab-content">
				<div class="tab-pane active opengarden-tab">
					<div class="form-group">
						<label class="col-lg-2 control-label">검색어</label>
						<div class="col-lg-10 og-form-input">
							<div class="col-lg-3">
						     	<input type="text" class="form-control input-sm" id="searchKeyword" name="searchKeyword" placeholder="업체명 입력"/>
						    </div>
					    </div>
					</div>
				</div>
			</div>
			<div class="info-section">
				<div class="container align-center">
					<button type="button" id="btnSearch" class="btn og-btn-primary">검 색</button>
				</div>
			</div>
		</form>
		
		<form id="resultTable">
			<div class="search-result-title">
				<div class="pull-left">
					검색결과 : 총 <code id="totalCount"> 0</code> 개.
				</div>
				<div class="pull-right">
					<button  type="button" class="btn og-btn-warning" onclick="registCompany();">업체등록</button>
				</div>
			</div>
			<table id="searchResult" class="display table-bordered" cellspacing="0" width="100%">
				<thead>
		            <tr>
		                <th></th>
		                <th>이미지</th>
		                <th>업체명</th>
		                <th>종류</th>
		                <th>담당자</th>
		                <th>전화번호</th>
		                <th>등록일</th>
		            </tr>
		        </thead>
		        
			</table>
			
		</form>
	</div>
	
</body>
</html>