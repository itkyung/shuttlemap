
$(document).ready(function(){
	
	$(":text").maxlength();
	
});

goCompanyList = function(associationId) {
	
	document.location.href = _requestPath + "/admin/listCompany?associationId=" + associationId;
};


saveAssociation = function() {
	var name = $("#name").val();
	if(name.length == 0){
		alert("협회명을 입력하세요.");
		return;
	}
	
	var phone = $("#phone").val();
	if(phone.length == 0){
		alert("전화번호를 입력하세요.");
		return;
	}
	
	var licenseNo = $("#regNo").val();
	if(licenseNo.length == 0){
		alert("등록번호를 입력하세요.");
		return;
	}
	
	var presidentName = $("#presidentName").val();
	if(presidentName.length == 0){
		alert("대표자명 입력하세요.");
		return;
	}
	
	$("#associationForm").submit();
};