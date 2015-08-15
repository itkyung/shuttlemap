var fv_codeType = 'IMPORTING_COUNTRY_KR';

$(document).ready(function(){
	
	var url = _requestPath + "/admin/getImporting_Countries_Kr";
	var params = makeParams();
/*	var params = {};
	params.codeType = $("#codeType").val();*/
	
	$("#codeSearchResultTable").jqGrid({ 
		url : url,
		datatype : 'json',
		postData:params,mtype:'POST',jsonReader:{repeatitems: false},
		colNames : ['id','분류','원산지명1','원산지명2','생성일','수정일','사용여부','수정/삭제'],
		colModel : [
		    {name:'id',index:'id', width:30, hidden:true, sortable : false, key:true},
		    {name : 'type', index : 'type', align:'center', width : 100,
		    	formatter: function (type, option){
		    		var text = "";
		    		
		    		if(type == "BRAND") {
   						text = "브랜드명";
   					}else if(type == "MANUFACTURE"){
   						text = "제조사명";
   					}else if(type == "IMPORTING_COUNTRY"){
   						text = "수입국가명";
   					}else if(type == "IMPORTING_COUNTRY_KR"){
   						text = "국내원산지명";
   					}
	   				return $.trim(text);
		    	}
		    },
		    {name : 'value1', index : 'value1', width : 200},
		    {name : 'value2', index : 'value2', width : 200},
		    {name : 'createDate', index : 'createDate', width : 100 , formatter : customDateFormat,align:'center'},
		    {name : 'updateDate', index : 'updateDate', width : 100 , formatter : customDateFormat,align:'center'},
		    {name : 'active', index : 'active',  align:'center',  width : 100,
		    	formatter: function (activeYn, option) {
	   				var text = "";
	   				
   					if(activeYn == "1") {
   						text = "사용중";
   					}else if(activeYn == "0"){
   						text = "사용중지";
   					}
	   				return $.trim(text);
				}
		    },
		    {name : 'act', index:'act', width:100, sortable:false, align:"center"}
		],
		gridComplete: function(){ 
	        var ids = $("#codeSearchResultTable").getDataIDs(); 
	        for(var i=0;i<ids.length;i++){
	            var cl = ids[i];
	            be = "<input style='height:22px;width:40px;' type='button' value='Edit' onclick=editRow('"+cl+"','1'); />"; 
	            se = "<input style='height:22px;width:40px;' type='button' value='Del' onclick=deleteRow('"+cl+"'); />"; 
	            $("#codeSearchResultTable").setRowData(ids[i],{act:be+"&nbsp;"+se});
	        }
	    },
	    onSelectRow:function(id) {
	    	var rowData = $(this).jqGrid('getRowData',id);
	    	var resultSubTable = $("#resultSubTable");
	    	resultSubTable.setCaption(rowData.value1);
	    	resultSubTable.clearGridData();
	    	var params = {};
			params.parentCode = id;
			params.codeType = fv_codeType;
			//질문 선택시 하위에 parentQuestionId설정.
			$('#resultSubTable').val(id);
			resultSubTable.setGridParam({postData:params,url:_requestPath+'/admin/getImporting_Countries_Kr'});
			subGridReLoad();
		},
	   	emptyrecords:"자료가 없습니다.",
		rowNum : 20,
		rowList : [20,40,60],
		pager : '#pager',
		sortname : 'type',sortorder : 'asc',
		caption : '국내원산지 관리',hidegrid : false,height : 300
	});
	
	$("#resultSubTable").jqGrid({ 
		datatype:'json',
		mtype:'POST',
		postData:{},
		height: 150,
		rowNum:-1,
		//width: 800,
		//shrinkToFit:false,
		jsonReader:{repeatitems: false},
		//autowidth:true,
		colNames : ['id','분류','세부원산지명1','세부원산지명2','생성일','수정일','사용여부','수정/삭제'],
		colModel : [
				    {name:'id',index:'id', width:30, hidden:true, sortable : false, key:true},
				    {name : 'type', index : 'type', align:'center', width : 100,
				    	formatter: function (type, option){
				    		var text = "";
				    		
				    		if(type == "BRAND") {
		   						text = "브랜드명";
		   					}else if(type == "MANUFACTURE"){
		   						text = "제조사명";
		   					}else if(type == "IMPORTING_COUNTRY"){
		   						text = "수입국가명";
		   					}else if(type == "IMPORTING_COUNTRY_KR"){
		   						text = "국내원산지명";
		   					}
			   				return $.trim(text);
				    	}
				    },
				    {name : 'value1', index : 'value1', width : 200},
				    {name : 'value2', index : 'value2', width : 200},
				    {name : 'createDate', index : 'createDate', width : 100 , formatter : customDateFormat,align:'center'},
				    {name : 'updateDate', index : 'updateDate', width : 100 , formatter : customDateFormat,align:'center'},
				    {name : 'active', index : 'active',  align:'center',  width : 100,
				    	formatter: function (activeYn, option) {
			   				var text = "";
			   				
		   					if(activeYn == "1") {
		   						text = "사용중";
		   					}else if(activeYn == "0"){
		   						text = "사용중지";
		   					}
			   				return $.trim(text);
						}
				    },
				    {name : 'act', index:'act', width:100, sortable:false, align:"center"}
				],
	   	gridComplete: function(){ 
	        var ids = $("#resultSubTable").getDataIDs(); 
	        for(var i=0;i<ids.length;i++){
	            var cl = ids[i];
	            be = "<input style='height:22px;width:40px;' type='button' value='Edit' onclick=editRow('"+cl+"','2'); />"; 
	            se = "<input style='height:22px;width:40px;' type='button' value='Del' onclick=deleteRow('"+cl+"'); />"; 
	            $("#resultSubTable").setRowData(ids[i],{act:be+"&nbsp;"+se});
	        }
	    },
	    rowNum : 20,
		rowList : [20,40,60],
		pager : '#pager',
	   	emptyrecords:"자료가 없습니다.",
	   	caption: "국내원산지 세부관리"
	});
	
	$( "#dialog-form" ).dialog({ 
	      autoOpen: false,
	      height: 590,
	      width: 680,
	      modal: true,
	      buttons: {
	        "저장": function() {
	        	$("#commCodeForm").submit();
	        },
	        "닫기": function() {
	          $( this ).dialog( "close" );
	        }
	      },
	      close: function() {
	    	  $("#commCodeForm").resetForm();
	      }
	});
	
	$( "#codeType" ).change(function() {
		gridReLoad();
	});
	
    $('#commCodeForm').ajaxForm({ 
    	dataType:'json',
    	success : function(result){
			var success = result.success;
			if(success){
				gridReLoad();
				$( "#dialog-form" ).dialog( "close" );
			}else{
				alert(result.msg);
			}
		},
		error : function(response, status, err){
			alert(err);
		}
    });
});

makeParams = function(){
	var params = {};
	var keyword = $("#keyword").val();
	
	if(keyword.trim().length > 0){
		params.keyword = keyword;
	}else{
		params.keyword = "";
	}
	
	params.codeType = fv_codeType;
	
	return params;
};



function gridReLoad() {
	var params = makeParams();
	
	$('#codeSearchResultTable').setGridParam({postData:params});
	$("#codeSearchResultTable").jqGrid().trigger("reloadGrid");
};

function subGridReLoad() {
	$("#resultSubTable").jqGrid().trigger("reloadGrid");
}

doSearch = function(){
	gridReLoad();
};



function editRow(id, acc) {
	
	var url = _requestPath+ "/admin/getCommCodeForm";
	
//	if(acc == "1")	url += "/admin/getHighForm";
//	else			url += "/admin/getLowForm";

	$.ajax({
		dataType:  'json', 
		type : 'POST',
		url : url,
		//timeout : 5000,
		data : {id:id},
		beforeSubmit : function(){
			
		},				
		success : function(result){
			var success = result.success;
			if(success){
				var pop = $( "#dialog-form" ).dialog( "open" );
				setCommCodeForm(pop, result.commCode, acc);
			}else{
				alert(result.msg);
			}
		},
		error : function(response, status, err){
			alert(err);
			
		}
	});
}

//function editHighRow(highId) {
//
//	$.ajax({
//		dataType:  'json', 
//		type : 'POST',
//		url : _requestPath + '/admin/getHighForm',
//		//timeout : 5000,
//		data : {id:highId},
//		beforeSubmit : function(){
//			
//		},				
//		success : function(result){
//			var success = result.success;
//			if(success){
//				var pop = $( "#high-form" ).dialog( "open" );
//				setHighForm(pop, result.commCode, "1");
//			}else{
//				alert(result.msg);
//			}
//		},
//		error : function(response, status, err){
//			alert(err);
//			
//		}
//	});
//}
//
//function editLowRow(lowId) {
//
//	$.ajax({
//		dataType:  'json', 
//		type : 'POST',
//		url : _requestPath + '/admin/getLowForm',
//		//timeout : 5000,
//		data : {id:lowId},
//		beforeSubmit : function(){
//			
//		},				
//		success : function(result){
//			var success = result.success;
//			if(success){
//				alert("1");
//				var pop = $( "#aaa" ).dialog( "open" );
//				alert("2");
//				setLowForm(pop, result.commCode, "2");
//			}else{
//				alert(result.msg);
//			}
//		},
//		error : function(response, status, err){
//			alert(err);
//			
//		}
//	});
//}

function setCommCodeForm(obj, data, acc) { // 값 셋팅하는거
	
	if(acc == "1"){
		$(obj).find("#ed_value1").html("원산지명1");
		$(obj).find("#ed_value2").html("원산지명2");
	}else{
		$(obj).find("#ed_value1").html("세부원산지명1");
		$(obj).find("#ed_value2").html("세부원산지명2");
	}
	
	$(obj).find("#getId").val(data.id);
	$(obj).find("#type").val(data.type);
	$(obj).find("#value1").val(data.value1);
	$(obj).find("#value2").val(data.value2);
	$(obj).find("#active").val(data.active);
}
//
//function setLowForm(obj, data) { // 값 셋팅하는거
//	$(obj).find("#lowId").val(data.id);
//	$(obj).find("#type").val(data.type);
//	$(obj).find("#value1").val(data.value1);
//	$(obj).find("#value2").val(data.value2);
//	$(obj).find("#active").val(data.active);
//}
