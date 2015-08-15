var searchTable;

$(document).ready(function(){
	
	/**
	 * 각종 버튼의 click이벤트 처리.
	 */
	$("#btnSearch").click(function(){
		doSearch();
	});
	
	var url = _requestPath + "/admin/searchUser";
	
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
		       { data : "id",width : "5%"},
		       { data : "name"},
		       { data : "loginId",width : "20%"},
		       { data : "phone",width : "10%"},
		       { data : "lastLoginDate",width:"15%"},
		       { data : "created",width:"15%"}
		   ],
		   processing: true,
		   serverSide : true,
		   columnDefs : [
			{
			    targets : 0,
			    render : function(data,type,row){
			    	return "";
			    }
			},{
				targets : 1,
				render : function(data,type,row){
					return "<a href='javascript:registLocation(\"" + row.loginId + "\");'>" + data + "</a>";
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
	var keyword = $("#searchKeyword").val();
	var userType = $("#userType").val();
	
	if(keyword.length > 0){
		params.keyword = keyword;
	}else{
		params.keyword = "";
	}
	params.userType = userType;
	
	return params;
};


doSearch = function(){
	searchTable.ajax.reload(function(json){
		
	});
};


registLocation = function(loginId) {
	$("#registRouteModal").on('show.bs.modal',function(){
		$("#latitude").val('');
		$("#longitude").val('');
		$("#loginId").val(loginId);
		
	}).modal('show');
};


saveLocationAction = function() {
	var loginId = $("#loginId").val();
	var latitude = $("#latitude").val();
	var longitude = $("#longitude").val();
	
	var params = {
			"loginId":loginId,
			"latitude":latitude,
			"longitude":longitude
			};
	
	$.ajax({
		dataType:  'json', 
		type : 'POST',
		url : _requestPath + "/mobile/updateLocation",
		timeout : 10000,
		data : params,
		beforeSubmit : function(){
			
		},				
		success : function(results){
			var success = results.success;
			if(success){
				alert("위치가 등록되었습니다.");
				$("#registRouteModal").modal('hide');
			}else{
				alert("Error");
			}
		},
		error : function(response, status, err){
			alert("ERROR [" + status + "][" + err + "]");
		}
	});	//Ajax로 호출한다.
};

