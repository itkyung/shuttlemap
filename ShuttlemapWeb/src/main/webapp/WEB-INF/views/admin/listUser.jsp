<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>
	<title>셔틀맵</title>

	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/shuttlemap/listUser.js"></script>
</head>
<body> 
	<div class="container">
		<h3 class="page-header">회원 리스트</h3>
		
		<form id="searchForm" class="form-horizontal" role="form"  method="post">
			<div class="tab-content">
				<div class="tab-pane active opengarden-tab">
					<div class="form-group border-bottom">
						<label class="col-lg-2 control-label">검색어</label>
						<div class="col-lg-10 og-form-input">
							<div class="col-lg-3">
						     	<input type="text" class="form-control input-sm" id="searchKeyword" name="searchKeyword" placeholder="회원명 입력"/>
						    </div>
					    </div>
					</div>
					<div class="form-group">
						<label class="col-lg-2 control-label">회원 타입</label>
						<div class="col-lg-10 og-form-input">
							<div class="col-lg-3">
						     	<select class="form-control" id="userType" name="userType">
						     		<option value="NORMAL">일반</option>
						     		<option value="DRIVER">운전수</option>
						     	</select>
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
			</div>
			<table id="searchResult" class="display table-bordered" cellspacing="0" width="100%">
				<thead>
		            <tr>
		                <th></th>
		                <th>이름</th>
		                <th>로그인아이디</th>
		                <th>전화번호</th>
		                <th>최근 로그인일</th>
		                <th>등록일</th>
		            </tr>
		        </thead>
		        
			</table>
			
		</form>
	</div>
	
	
	<!-- 임시 위치 지정 -->
	<div class="modal fade" id="registRouteModal" tabindex="-1" role="dialog" aria-labelledby="h4#registRouteModal" aria-hidden="true">
		<div class="modal-dialog">
		    <div class="modal-content">
		    	<div class="modal-header">
		        	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		        	<h4 class="modal-title">임시 위치 지정</h4>
		      	</div>
		      	<form id="registRouteForm" class="form-horizontal" role="form" method="POST">
		      		<input type=hidden name="loginId" id="loginId"/>
		     		<div class="modal-body">
					  <div class="form-group">
					    <label for="desc" class="col-lg-2 control-label">위도</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="latitude" name="latitude" placeholder="지도상의 위도(ex:37.5172)">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="desc" class="col-lg-2 control-label">경도</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="longitude" name="longitude" placeholder="지도상의 경도(ex:127.0201)">
					    </div>
					  </div>
					</div>
					<div class="modal-footer">
				      	<button type="button" id="btnRegist" class="btn btn-primary" onclick="saveLocationAction();">저장하기</button>
				        <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					</div>
				</form>
				 </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->	
</body>
</html>