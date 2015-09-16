
$(document).ready(function(){
	
	$(":text").maxlength();
	var shuttleId = $("#shuttleId").val();
	
	var url = _requestPath + "/admin/searchRoute?shuttleId=" + shuttleId;
	
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
		       { data : "idx", width : "10%"},
		       { data : "routeName"},
		       { data : "arrivedHour"},
		       { data : "arrivedMinute"},
		       { data : "latitude",width : "15%"},
		       { data : "longitude",width : "15%"}
		   ],
		   processing: true,
		   serverSide : true,
		   columnDefs : [
		       {
		            targets : 1,
		            render : function(data,type,row){
		            	return "<a href='javascript:editRoute(\"" + row.id + "\",\"" + row.routeName + "\",\"" + row.idx + "\",\"" + 
		            		row.arrivedHour + "\",\"" + row.arrivedMinute + "\",\"" +
		            		row.latitude + "\",\"" + row.longitude + "\");'>" + data + "</a>";
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
	var shuttleId = $("#shuttleId").val();
	
	params.shuttleId = shuttleId;
	
	return params;
};

editRoute = function(routeId,routeName,routeIdx,arrivedHour,arrivedMinute, latitude,longitude){
	$("#registRouteModal").on('show.bs.modal',function(){
		$("#routeId").val(routeId);
		$("#routeName").val(routeName);
		$("#routeIdx").val(routeIdx);
		$("#arrivedHour").val(arrivedHour);
		$("#arrivedMinute").val(arrivedMinute);
		$("#latitude").val(latitude);
		$("#longitude").val(longitude);
		
	}).modal('show');
};

registRoute = function() {
	var shuttleId = $("#shuttleId").val();
	if(shuttleId.length == 0){
		alert("저장버튼을 눌러서 노선정보를 먼저 생성하세요.");
		return;
	}
	
	$("#registRouteModal").on('show.bs.modal',function(){
		$("#routeId").val('');
		$("#routeName").val('');
		$("#routeIdx").val('');
		$("#latitude").val('');
		$("#longitude").val('');
		
	}).modal('show');
};

saveRouteAction = function() {
	var shuttleId = $("#shuttleId").val();
	var routeId = $("#routeId").val();
	var routeName = $("#routeName").val();
	var routeIdx = $("#routeIdx").val();
	var latitude = $("#latitude").val();
	var longitude = $("#longitude").val();
	var arrivedMinute = $("#arrivedMinute").val();
	var arrivedHour = $("#arrivedHour").val();
	
	var params = {
			"shuttleId":shuttleId,
			"routeId":routeId,
			"routeName":routeName,
			"routeIdx":routeIdx,
			"latitude":latitude,
			"longitude":longitude,
			"arrivedHour":arrivedHour,
			"arrivedMinute":arrivedMinute
			};
	
	$.ajax({
		dataType:  'json', 
		type : 'POST',
		url : _requestPath + "/admin/saveRoute",
		timeout : 10000,
		data : params,
		beforeSubmit : function(){
			
		},				
		success : function(results){
			var success = results.success;
			if(success){
				alert("등록되었습니다.");
				setTimeout(function(){
					document.location.href = _requestPath + "/admin/editShuttleForm?id=" + shuttleId; 
				},1000);
			}else{
				alert("Error");
			}
		},
		error : function(response, status, err){
			alert("ERROR [" + status + "][" + err + "]");
		}
	});	//Ajax로 호출한다.
};

searchDriver = function() {
	$("#addressModal").on('show.bs.modal',function(){
		$("#driverSearchPhone").val('');
		$("#driverTBody tr").remove();
		
		$("#driverSearchPhone").keypress(function(e){
			var code = (e.keyCode?e.keyCode:e.which);
			if(code == 13){
				searchDriverAction();
				e.preventDefault();
			}
		});
		
		
		$("#btnSearchDriver").click(function(){
			searchDriverAction();
		});
	}).modal('show');
};

searchShuttle = function() {
	$("#searchShuttleModal").on('show.bs.modal',function(){
		$("#searchName").val('');
		
		$("#shuttleResultTBody tr").remove();
		
		$("#searchName").keypress(function(e){
			var code = (e.keyCode?e.keyCode:e.which);
			if(code == 13){
				searchShuttleAction();
				e.preventDefault();
			}
		});
		
		$("#btnSearchShuttle").click(function(){
			btnSearchShuttle();
		});
		
	}).modal('show');
};

searchShuttleAction = function() {
	var keyword = $("#searchName").val();
	if(keyword.length == 0){
		alert("셔틀이름을 입력하세요.");
		return;
	}
	var companyId = $("#companyId").val();
	
	var params = {"searchName":keyword, "companyId":companyId};
	
	$.ajax({
		dataType:  'json', 
		type : 'POST',
		url : _requestPath + "/admin/searchShuttleForCopy",
		timeout : 10000,
		data : params,
		beforeSubmit : function(){
			
		},				
		success : function(results){
			$("#searchName").val('');
			$("#shuttleResultTBody tr").remove();
			$("#shuttleTemplate").tmpl(results).appendTo("#shuttleResultTBody");
		},
		error : function(response, status, err){
			alert("ERROR [" + status + "][" + err + "]");
		}
	});	//Ajax로 호출한다.
};

clickShuttle = function(id) {
	document.location.href = _requestPath + "/admin/copyShuttle?targetId=" + id;
};

function searchDriverAction(){
	var keyword = $("#driverSearchPhone").val();
	if(keyword.length == 0){
		alert("전화번호를 입력하세요.");
		return;
	}
	var params = {"phone":keyword};
	
	$.ajax({
		dataType:  'json', 
		type : 'POST',
		url : _requestPath + "/admin/searchDriver",
		timeout : 10000,
		data : params,
		beforeSubmit : function(){
			
		},				
		success : function(results){
			$("#driverSearchPhone").val('');
			$("#driverTBody tr").remove();
			$("#driverTemplate").tmpl(results).appendTo("#driverTBody");
		},
		error : function(response, status, err){
			alert("ERROR [" + status + "][" + err + "]");
		}
	});	//Ajax로 호출한다.
};

clickDriver = function(id,name,phone){
	
	$("#driverId").val(id);
	$("#driverPhone").val(phone);
	$("#driverName").val(name);
	
	$("#addressModal").modal("hide");
	
};

registDriver = function() {
	$("#registDriverModal").on('show.bs.modal',function(){
		$("#newDriverName").val('');
		$("#newDriverPhone").val('');
		$("#newDriverLoginId").val('');
		$("#newDriverPassword").val('');
		
	}).modal('show');
};

saveDriverAction = function() {
	var driverName = $("#newDriverName").val();
	var driverPhone = $("#newDriverPhone").val();
	var loginId = $("#newDriverLoginId").val();
	var password = $("#newDriverPassword").val();
	
	var params = {
			"driverName":driverName,
			"driverPhone":driverPhone,
			"loginId":loginId,
			"password":password
			};
	
	$.ajax({
		dataType:  'json', 
		type : 'POST',
		url : _requestPath + "/admin/saveDriver",
		timeout : 10000,
		data : params,
		beforeSubmit : function(){
			
		},				
		success : function(results){
			var success = results.success;
			if(success){
				alert("등록되었습니다.운전자를 검색해서 할당하세요.");
				$("#registDriverModal").modal("hide");
			}else{
				alert("이미 존재하는 로그인 아이디입니다.");
			}
		},
		error : function(response, status, err){
			alert("ERROR [" + status + "][" + err + "]");
		}
	});	//Ajax로 호출한다.
	
};

saveShuttle = function() {
	var name = $("#name").val();
	if(name.length == 0){
		alert("노선명을 입력하세요.");
		return;
	}
	
	var carNo = $("#carNo").val();
	if(carNo.length == 0){
		alert("차량번호를 입력하세요.");
		return;
	}
	
	var startHour = $("#startHour").val();
	if(startHour.length == 0){
		alert("출발시를 입력하세요.");
		return;
	}
	
	var endHour = $("#endHour").val();
	if(endHour.length == 0){
		alert("도착시를 입력하세요.");
		return;
	}
	
	var googleMapUrl = $("#googleMapUrl").val();
	if(googleMapUrl.length == 0) {
		alert("구글맵 URL을 입력하세요.");
		return;
	}
	
	var routeFile = $("#routeFile").val();
	if(routeFile.length == 0) {
		alert("구글맵 노선파일을 선택하세요.");
		return;
	}
	
	var driverPhone = $("#driverPhone").val();
	if(driverPhone.length == 0) {
		alert("운전자를 선택하세요.");
		return;
	}
	
	$("#shuttleForm").submit();
};

viewGoogleMap = function() {
	var googleMapUrl = $("#googleMapUrl").val();
	
	if(googleMapUrl.length > 0) {
		window.open(googleMapUrl,"노선도","width=950,height=650,resizable=yes,toolbar=no");
	}
};