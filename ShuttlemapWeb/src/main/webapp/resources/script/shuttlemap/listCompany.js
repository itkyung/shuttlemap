var searchTable;

$(document).ready(function(){
	
	/**
	 * 각종 버튼의 click이벤트 처리.
	 */
	$("#btnSearch").click(function(){
		doSearch();
	});
	
	var url = _requestPath + "/admin/searchCompany";
	if(associationId.length > 0){
		url += "?associationId=" + associationId;
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
			   type : "GET"
		   },
		   columns : [
		       { data : "id"},
		       { data : "companyLogo",width : "15%"},
		       { data : "associationName", width : "15%"},
		       { data : "name"},
		       { data : "companyType",width : "10%"},
		       { data : "contactPerson",width:"10%"},
		       { data : "phone",width:"10%"},
		       { data : "createdStr",width:"10%"}
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
		            	return "<img src='" + _requestPath + data + "'/>";
		            }
		       },
		       {
		            targets : 3,
		            render : function(data,type,row){
		            	return "<a href='javascript:viewCompany(\"" + row.id + "\");'>" + data + "</a>";
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


viewCompany = function(id){
	document.location.href = _requestPath + "/admin/editCompanyForm?id=" + id;
};

registCompany = function(associationId) {
	document.location.href = _requestPath + "/admin/editCompanyForm?associationId=" + associationId;
};