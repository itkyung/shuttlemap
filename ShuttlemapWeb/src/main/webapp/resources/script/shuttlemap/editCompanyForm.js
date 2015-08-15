
$(document).ready(function(){
	
	$(":text").maxlength();
	var companyId = $("#companyId").val();
	
	var url = _requestPath + "/admin/searchShuttle?companyId=" + companyId;
	
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
		       },{
		            targets : 1,
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

makeParams = function(params){
	var companyId = $("#companyId").val();
	
	params.companyId = companyId;
	
	return params;
};

registShuttle = function() {
	var companyId = $("#companyId").val();
	if(companyId.length == 0){
		alert("저장버튼을 눌러서 업체정보를 먼저 생성하세요.");
		return;
	}
	
	document.location.href = _requestPath + "/admin/editShuttleForm?companyId=" + companyId;
};

viewShuttle = function(id){
	document.location.href = _requestPath + "/admin/editShuttleForm?id=" + id;
};

saveCompany = function() {
	var name = $("#name").val();
	if(name.length == 0){
		alert("업체명을 입력하세요.");
		return;
	}
	
	var phone = $("#phone").val();
	if(phone.length == 0){
		alert("업체 전화번호를 입력하세요.");
		return;
	}
	
	var licenseNo = $("#licenseNo").val();
	if(licenseNo.length == 0){
		alert("사업자 등록번호를 입력하세요.");
		return;
	}
	
	var contactPerson = $("#contactPerson").val();
	if(contactPerson.length == 0){
		alert("담당자를 입력하세요.");
		return;
	}
	
	$("#companyForm").submit();
};