<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script>
	function _login() {
		var loginId = $("#loginId").val();
		if(loginId.length == 0){
			alert("아이디를 입력하세요.");
			return;
		}
		var password = $("#pwd").val();
		if(password.length == 0){
			alert("비밀번호를 입력하세요.");
			return;
		}
		$("#loginForm").submit();
	}

	function registAction() {
		
		alert("직접 회원가입은 아직 작업중입니다.");
		
	}
	
	function _regist() {
		$("#registModal").on('show.bs.modal',function(){
			
		}).modal('show');
	}
	
</script>


	<c:url value="/loginAction" var="loginUrl"/>
	<div class="container">
		<h3>Shuttlemap 로그인</h3>
		<form id="loginForm" role="form" action="${loginUrl}" method="POST">
		  <div class="form-group">
		    <label for="loginId">아이디(이메일 주소)</label>
		    <input type="email" class="form-control" id="loginId" name="loginId" value="" placeholder="로그인아이디">
		  </div>
		  <div class="form-group">
		    <label for="pwd">비밀번호</label>
		    <input type="password" class="form-control" id="pwd" name="pwd" placeholder="비밀번호" value="">
		  </div>
		  <div class="checkbox">
		    <label>
		      <input type="checkbox"> 비밀번호 저장하기
		    </label>
		  </div>
		  <p>
		  	 <a href="javascript:_login();" class="btn btn-primary" role="button">로그인</a>
		  	 <a href="javascript:_regist();" class="btn btn-default" role="button">회원가입</a>
		  </p>
		  <p>
		  	 슈퍼관리자 (admin / admin1234) 본사협회 관리자(asso1 / 1234),  본사기관 관리자(company1 / 1234)
		  </p>
		</form>
		
	</div>
	<div class="modal fade" id="registModal" tabindex="-1" role="dialog" aria-labelledby="h4#registModal" aria-hidden="true">
		
		<div class="modal-dialog">
		    <div class="modal-content">
		    	<div class="modal-header">
		        	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
		        	<h4 class="modal-title">셔틀맵 회원가입</h4>
		      	</div>
		      	<form id="registForm" class="form-horizontal" role="form" action="${registActionUrl}" method="POST">
		     		<div class="modal-body">
					
					  <div class="form-group">
					    <label for="registLoginId" class="col-lg-2 control-label">이메일</label>
					    <div class="col-lg-10">
					      <input type="email" class="form-control" id="registLoginId" name="registLoginId" placeholder="Email">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="registPassword" class="col-lg-2 control-label">비밀번호</label>
					    <div class="col-lg-10">
					      <input type="password" class="form-control" id="registPassword" name="registPassword" placeholder="Password">
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="registPasswordConfirm" class="col-lg-2 control-label">비밀번호 확인</label>
					    <div class="col-lg-10">
					      <input type="password" class="form-control" id="registPasswordConfirm" name="registPasswordConfirm" placeholder="Confirm Password">
					    </div>
					  </div>
					 <div class="form-group">
					    <label for="registName" class="col-lg-2 control-label">업체명</label>
					    <div class="col-lg-10">
					      <input type="text" class="form-control" id="registName" name="registName" placeholder="이름">
					    </div>
					  </div>
					</div>
					<div class="modal-footer">
				      	<button type="button" id="btnRegist" class="btn btn-primary" onclick="registAction();">가입하기</button>
				      
				        <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
					</div>
				</form>
 			 </div><!-- /.modal-content -->
		  </div><!-- /.modal-dialog -->
		</div><!-- /.modal -->		