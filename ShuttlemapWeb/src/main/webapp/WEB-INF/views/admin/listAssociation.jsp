<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<html>
<head>
	<title>셔틀맵</title>	
</head>
<body> 
	<div class="container">
		<h3 class="page-header">협회 리스트</h3>
		
		<form id="searchForm" class="form-horizontal" role="form"  method="post">
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
					<button  type="button" class="btn og-btn-warning" onclick="registAssociation();">협회 등록</button>
				</div>
			</div>
			<table id="searchResult" class="display table-bordered" cellspacing="0" width="100%">
				<thead>
		            <tr>
		                <th></th>
		                <th>협회명</th>
		                <th>등록번호</th>
		                <th>대표자명</th>
		                <th>전화번호</th>
		                <th>이메일주소</th>
		            </tr>
		        </thead>
		        
			</table>
			
		</form>
	</div>
	

<script type="text/javascript">
	var searchTable;

	$(document).ready(function(){
		
		/**
		 * 각종 버튼의 click이벤트 처리.
		 */
		$("#btnSearch").click(function(){
			doSearch();
		});
		
		var url = _requestPath + "/admin/searchAssociation";
		
		searchTable = $('#searchResult').DataTable({
			  language: {
				  paginate: {
					  next: "다음",previous : "이전"
				  }
			   },
			   scrollY: 400,
			   searching: false,
			   paging:true,
			   displayLength : 10,
		       lengthChange : true,
		       info : false,
		       dom : '<"top"i>rt<"bottom"lp><"clear">',
			   ajax : {
				   url : url,
				   type : "POST"
			   },
			   columns : [
			       { data : "id"},
			       { data : "name"},
			       { data : "regNo"},
			       { data : "presidentName",width : "10%"},
			       { data : "phone",width:"10%"},
			       { data : "email",width:"15%"}
			   ],
			   processing: true,
			   serverSide : true,
			   columnDefs : [
			       {
			            targets : 0,
			            render : function(data,type,row){
			            	return "<input type='checkbox' class='inner-checkbox' name='rowCheck_" + data + "' value='" + data + "' status='" + row.status + "'/>";
			            }
			       },
			       {
			            targets : 1,
			            render : function(data,type,row){
			            	return "<a href='javascript:viewAssociation(\"" + row.id + "\");'>" + data + "</a>";
			            }
			       }
			   ]
		  }).on('preXhr.dt',function(e,settings,data){
			  //Ajax Call을 하기 전에 호출된다.
			  makeParams(data);
		  }).on("xhr.dt",function(e,settings,json){
			 $("#totalCount").html(json.recordsTotal);  
		  });
		
	});
	
	/**
	 * 검색조건을 만든다.
	 */
	makeParams = function(params){
		
		return params;
	};
	
	
	doSearch = function(){
		searchTable.ajax.reload(function(json){
			
		});
	};
	
	
	viewAssociation = function(id){
		document.location.href = _requestPath + "/admin/editAssociationForm?id=" + id;
	};
	
	registAssociation = function() {
		document.location.href = _requestPath + "/admin/editAssociationForm";
	};
	
	
	
</script>
</body>
</html>