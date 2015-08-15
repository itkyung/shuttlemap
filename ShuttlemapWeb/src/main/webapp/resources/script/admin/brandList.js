var fv_codeType = 'BRAND';

$(document).ready(function(){
	
	var url = _requestPath + "/admin/getBrands";
	var params = makeParams();
/*	var params = {};
	params.codeType = $("#codeType").val();*/
	
	$("#codeSearchResultTable").jqGrid({
		url : url,
		datatype : 'json',
		postData:params,mtype:'POST',jsonReader:{repeatitems: false},
		colNames : ['id','분류','브랜드명1','브랜드명2','생성일','수정일','사용여부','수정/삭제'],
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
	            be = "<input style='height:22px;width:40px;' type='button' value='Edit' onclick=editRow('"+cl+"'); />"; 
	            se = "<input style='height:22px;width:40px;' type='button' value='Del' onclick=deleteRow('"+cl+"'); />"; 
	            $("#codeSearchResultTable").setRowData(ids[i],{act:be+"&nbsp;"+se});
	        }
	    },
		rowNum : 20,
		rowList : [20,40,60],
		pager : '#pager',
		sortname : 'type',sortorder : 'asc',
		caption : '브랜드 관리',hidegrid : false,height : 300
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

doSearch = function(){
	gridReLoad();
};

function editRow(id, acc) {
	
	var url = _requestPath+ "/admin/getCommCodeForm";
	
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

function setCommCodeForm(obj, data, acc) { 
	
	$(obj).find("#ed_value1").html("브랜드명1");
	$(obj).find("#ed_value2").html("브랜드명2");
	
	$(obj).find("#getId").val(data.id);
	$(obj).find("#type").val(data.type);
	$(obj).find("#value1").val(data.value1);
	$(obj).find("#value2").val(data.value2);
	/*$(obj).find("#parentCode").val(data.parentCode);*/
	$(obj).find("#active").val(data.active);
}