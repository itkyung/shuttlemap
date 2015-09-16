<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>
	<title>셔틀맵</title>

	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/script/shuttlemap/editShuttleForm.js"></script>
	<script id="driverTemplate" type="text/x-jquery-tmpl">
		<tr>
   			
   			<td><a href="javascript:clickDriver('\${id}','\${name}','\${phone}');"> \${name} </a></td>
			<td>\${phone}</td>
		</tr>
	</script>
	<script id="shuttleTemplate" type="text/x-jquery-tmpl">
		<tr>
   			
   			<td><a href="javascript:clickShuttle('\${id}');"> \${shuttleName} </a></td>
			<td>\${startHour} 시</td>
			<td>\${endHour} 시</td>
		</tr>
	</script>
	
</head>
<body> 
	<div class="container">
		<c:url value="/admin/editCompanyForm" var="companyUrl"/>
		
		<h3 class="page-header"> <a href="${companyUrl}?id=${companyId}">${companyName} </a> > 셔틀정보</h3>
		
		<c:url value="/admin/saveShuttle" var="formUrl"/>
		<form id="shuttleForm" class="form-horizontal" role="form" enctype="multipart/form-data" method="post" action="${formUrl}">
			<input type=hidden id="shuttleId" name="shuttleId" value="${shuttle.id}"/>
			<input type=hidden id="companyId" name="companyId" value="${companyId}"/>
			
			<div class="info-section-top">
				<div class="opengarden-small-title">
					셔틀 기본정보
				</div>
				<div class="tab-content">
					<div class="tab-pane active opengarden-tab">
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 셔틀명(노선명)</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	<input type="text" id="name" name="name" class="form-control input-sm" value="${shuttle.name}" 
							     		placeholder="노선명을 입력하세요."
							     		data-counter="#nameCounter" data-text-length="50"
							     		/>
							    </div>
							    <div class="col-lg-2 height-sm">
							    	<code id="nameCounter">0/50</code>
							    </div>
							 </div>
						</div>
					   <div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 운행상태</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	<select id="status" name="status" class="form-control input-sm">
										<option value="WAITING">운행중지</option>
										<option value="ING">운행중</option>
									</select>
								</div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 차종</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	<select id="carType" name="carType" class="form-control input-sm">
										<option value="BUS">버스</option>
										<option value="MINI_BUS">미니버스</option>
										<option value="VAN">승합차</option>
									</select>
								</div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">* 차량번호</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
						     	 	<input type="text" id="carNo" name="carNo" class="form-control input-sm" value="${shuttle.carNo}" 
						     			placeholder="차량번호를 입력하세요."/>
						     	</div>
							    <div class="col-lg-5 height-sm">
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">*출발지 출발시간</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-4">
							     	 <input type="text" id="startHour" name="startHour" class="form-control input-sm" value="${shuttle.startHour}" 
							     		placeholder="출발시를 입력하세요."/> 시(24시 기준) 
							    </div>
							    <div class="col-lg-5 height-sm">
							    	<input type="text" id="startMinute" name="startMinute" class="form-control input-sm" value="${shuttle.startMinute}" 
							     		placeholder="출발분을 입력하세요."/> 분 
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">*도착지 도착시간</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-4">
							     	 <input type="text" id="endHour" name="endHour" class="form-control input-sm" value="${shuttle.endHour}" 
							     		placeholder="도착시를 입력하세요."/> 시(24시 기준)
							    </div>
							    <div class="col-lg-5 height-sm">
							    	<input type="text" id="endMinute" name="endMinute" class="form-control input-sm" value="${shuttle.endMinute}" 
							     		placeholder="도착분을 입력하세요."/> 분
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">구글맵 노선도 업로드(kml)</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-4">
							     	 <input type="file" id="routeFile" name="routeFile" class="form-control input-sm" />
							    </div>
							    <div class="col-lg-5 height-sm">
									<a href="https://www.google.com/maps/d/" target="_blank">구글지도 만들기</a>
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">구글맵 URL(공유된)</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-4">
							     	 <input type="text" id="googleMapUrl" name="googleMapUrl" class="form-control input-sm" value="${shuttle.googleMapUrl}"
							     	 placeholder="공유한 구글지도 경로를 입력하세요."/>
							    </div>
							    <div class="col-lg-5 height-sm">
									<button type="button" id="viewGoogleMapBtn" class="btn og-btn-primary" onclick="viewGoogleMap();">노선도 보기</button>
							    </div>
							 </div>
						</div>
						
					</div>
				</div>
				<br/>
				<div class="opengarden-small-title">
					운전자 정보
				</div>
				<div class="tab-content">
					<div class="tab-pane active opengarden-tab">
						<input type="hidden" id="driverId" name="driverId" value="${shuttle.driver.id}"/>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">운전자 전화번호</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
							     	<input type="text" id="driverPhone" name="driverPhone" class="form-control input-sm" value="${shuttle.driver.phone}"
							     	readonly="readonly"/>
							    </div>
							    <div class="col-lg-5 height-sm">
							    	<button type="button" id="driverSearchBtn" class="btn og-btn-primary" onclick="searchDriver();">운전자 찾기</button>
							    	<button type="button" id="driverRegistBtn" class="btn" onclick="registDriver();">운전자 등록</button>
							    </div>
							 </div>
						</div>
						<div class="form-group border-bottom">
							<label class="col-lg-2 control-label required">운전자 이름</label>
							<div class="col-lg-10 og-form-input">
								<div class="col-lg-6">
									<input type="text" id="driverName" name="driverName" class="form-control input-sm" value="${shuttle.driver.name}"
							     		readonly="readonly"/>
							     	 
							    </div>
							    <div class="col-lg-5 height-sm">
							    </div>
							 </div>
						</div>
					</div>
				</div>
				<!-- 운전자 정보 끝 -->
			</div>
		</form>
		<div class="info-section">
			<div class="container align-center">
				<button type="button" id="btnSave" class="btn og-btn-primary" onclick="saveShuttle();">저 장</button>
				<button type="button" class="btn og-btn-primary" onclick="searchShuttle();">기존 셔틀 복사</button>
				
			</div>
		</div>
		<form id="resultTable">
			<div class="search-result-title">
				<div class="pull-left">
					검색결과 : 총 <code id="totalCount"> 0</code> 개.
				</div>
				<div class="pull-right">
					<button  type="button" class="btn og-btn-warning" onclick="registRoute();">정거장 등록</button>
				</div>
			</div>
			<table id="searchResult" class="display table-bordered" cellspacing="0" width="100%">
				<thead>
		            <tr>
		                <th>순서</th>
		                <th>정거장명</th>
		                <th>도착시</th>
		                <th>도착분</th>
		                <th>위도</th>
		                <th>경도</th>
		            </tr>
		        </thead>
		        
			</table>
			
		</form>
	</div>
	
	<!-- 운전자 검색 -->
	<div class="modal fade" id="addressModal" tabindex="-1" role="dialog" aria-labelledby="h4#driverModal" aria-hidden="true">
	  <div class="modal-dialog">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	        <h4 class="modal-title">운전자 찾기</h4>
	      </div>
	      <div class="modal-body">
	      	<form class="form-horizontal" role="form" name="driverSearchForm" id="driverSearchForm">
	      		<div class="form-group">
	      			<label class="col-lg-4 control-label">전화번호</label>
	      			<div class="col-lg-4">
	      				<input type="text" id="driverSearchPhone" name="driverSearchPhone" class="form-control input-sm"
	      				placeholder="뒷자리로 검색가능"/>
	      			</div>
	      		</div>
	      		<div class="form-group addressTable" id="addressResultTable">
	      			<table class="table table-striped table-bordered table-hover">
	      				<thead>
	      					<th>이름</th>
	      					<th>전화번호</th>
	      				</thead>
	      				<tbody id="driverTBody">
	      					
	      				</tbody>
	      			</table>
	      		</div>
	      		<div class="modal-footer">
			      	<button type="button" id="btnSearchDriver" class="btn og-btn-primary">확인</button>
			        <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
			     </div>
	      	</form>
	      </div>
	     
	    </div><!-- /.modal-content -->
	  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
	<!-- 운전자 등록 -->
	<div class="modal fade" id="registDriverModal" tabindex="-1" role="dialog" aria-labelledby="h4#registDriverModal" aria-hidden="true">
		<div class="modal-dialog">
		    <div class="modal-content">
		    	<div class="modal-header">
		        	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		        	<h4 class="modal-title">운전자 등록</h4>
		      	</div>
		      	<form id="registDriverForm" class="form-horizontal" role="form" method="POST">
		     		<div class="modal-body">
					  <div class="form-group">
					    <label for="exhibitionName" class="col-lg-2 control-label">이름</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="newDriverName" name="newDriverName" placeholder="이름">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="exhibitionDesc" class="col-lg-2 control-label">전화번호</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="newDriverPhone" name="newDriverPhone" placeholder="휴대폰번호">
					    </div>
					  </div>
					  
					  <div class="form-group">
					    <label for="desc" class="col-lg-2 control-label">로그인 아이디</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="newDriverLoginId" name="newDriverLoginId" placeholder="로그인아이디">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="desc" class="col-lg-2 control-label">로그인 비밀번호</label>
					    <div class="col-lg-10">
					      <input type="password" class="form-control" id="newDriverPassword" name="newDriverPassword" placeholder="비밀번호">
					    </div>
					  </div>
					</div>
					<div class="modal-footer">
				      	<button type="button" id="btnRegist" class="btn btn-primary" onclick="saveDriverAction();">저장하기</button>
				        <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					</div>
				</form>
				 </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->	
	
	<!-- 정거장 등록 -->
	<div class="modal fade" id="registRouteModal" tabindex="-1" role="dialog" aria-labelledby="h4#registRouteModal" aria-hidden="true">
		<div class="modal-dialog">
		    <div class="modal-content">
		    	<div class="modal-header">
		        	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		        	<h4 class="modal-title">정거장 등록(수정)</h4>
		      	</div>
		      	<form id="registRouteForm" class="form-horizontal" role="form" method="POST">
		      		<input type=hidden name="shuttleId" value="${shuttle.id}"/>
		      		<input type=hidden name="routeId" id="routeId"/>
		     		<div class="modal-body">
					  <div class="form-group">
					    <label for="exhibitionName" class="col-lg-2 control-label">정거장명</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="routeName" name="routeName" placeholder="정거장명">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="exhibitionDesc" class="col-lg-2 control-label">정거장 순서(1부터 시작)</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="routeIdx" name="routeIdx" placeholder="1부터 시작">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="exhibitionDesc" class="col-lg-2 control-label">도착시(24시기준)</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="arrivedHour" name="arrivedHour" placeholder="24시기준">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="exhibitionDesc" class="col-lg-2 control-label">도착분</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="arrivedMinute" name="arrivedMinute">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="desc" class="col-lg-2 control-label">위도</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="latitude" name="latitude" placeholder="지도상의 위도">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="desc" class="col-lg-2 control-label">경도</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="longitude" name="longitude" placeholder="지도상의 경도">
					    </div>
					  </div>
					</div>
					<div class="modal-footer">
				      	<button type="button" id="btnRegist" class="btn btn-primary" onclick="saveRouteAction();">저장하기</button>
				        <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					</div>
				</form>
				 </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->	
	
	<!-- 셔틀 검색 -->
	<div class="modal fade" id="searchShuttleModal" tabindex="-1" role="dialog" aria-labelledby="h4#searchShuttleModal" aria-hidden="true">
		<div class="modal-dialog">
		    <div class="modal-content">
		    	<div class="modal-header">
		        	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		        	<h4 class="modal-title">복사할 셔틀검색</h4>
		      	</div>
		      	<form id="searchSuttleForm" class="form-horizontal" role="form" method="POST">
		      		<input type=hidden name="companyId" value="${companyId}"/>
		      		
		     		<div class="modal-body">
					  <div class="form-group">
					    <label for="searchName" class="col-lg-2 control-label">셔틀 이름</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="searchName" name="searchName" placeholder="현재 기관에서만 검색가능">
					    </div>
					  </div>
					  <div class="form-group addressTable" id="shuttleResultTable">
		      			<table class="table table-striped table-bordered table-hover">
		      				<thead>
		      					<th>셔틀명</th>
		      					<th>출발시간</th>
		      					<th>도착시간</th>
		      				</thead>
		      				<tbody id="shuttleResultTBody">
		      					
		      				</tbody>
		      			</table>
		      		</div>
					</div>
					<div class="modal-footer">
				      	<button type="button" id="btnSearchShuttle" class="btn btn-primary" onclick="searchShuttleAction();">검색 하기</button>
				        <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					</div>
				</form>
				 </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
	</div><!-- /.modal -->
	
</body>
</html>