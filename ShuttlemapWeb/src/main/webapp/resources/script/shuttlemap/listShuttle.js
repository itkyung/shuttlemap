var searchTable;

$(document).ready(function(){
	
	/**
	 * 각종 버튼의 click이벤트 처리.
	 */
	$("#btnSearch").click(function(){
		doSearch();
	});
	
	var url = _requestPath + "/admin/searchShuttle";
	
	if (associationId != null) {
		url += "?assocationId=" + associationId;
	} else if (companyId != null) {
		url += "?companyId=" + companyId;
	}
	
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
		        { data : "companyName",width : "15%"},
		       { data : "shuttleName"},
		       { data : "carType",width : "15%"},
		       { data : "carNo",width : "10%"},
		       { data : "scheduleType",width:"10%"}
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
		            targets : 2,
		            render : function(data,type,row){
		            	return "<a href='javascript:viewShuttle(\"" + row.id + "\");'>" + data + "</a>";
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
	
	if(keyword.length > 0){
		params.keyword = keyword;
	}else{
		params.keyword = "";
	}
	
	return params;
};


doSearch = function(){
	searchTable.ajax.reload(function(json){
		
	});
};

viewShuttle = function(id){
	document.location.href = _requestPath + "/admin/editShuttleForm?id=" + id;
};

