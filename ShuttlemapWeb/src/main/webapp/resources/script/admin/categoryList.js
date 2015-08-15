

$(document).ready(function(){
	
	var url = _requestPath + "/admin/getCategories";
	var params = makeParams();
	
	$("#firstCategoryListTable").jqGrid({
		url : url,
		datatype : 'json',
		postData:params,mtype:'POST',jsonReader:{repeatitems: false},
		colNames : ['id','분류명','생성일','설명','레벨','사용여부','수정/삭제'],
		colModel : [
		    {name:'id',index:'id', width:30, hidden:true, sortable : false, key:true},
		    {name : 'name', index : 'name', width : 300},
		    
		    {name : 'createDate', index : 'createDate', width : 100, formatter : customDateFormat,align:'center'},
		    {name : 'description', index : 'description', width : 100},
		    {name : 'level', index : 'level', width : 100},
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
	        var ids = $("#firstCategoryListTable").getDataIDs(); 
	        for(var i=0;i<ids.length;i++){
	            var cl = ids[i];
	            be = "<input style='height:22px;width:40px;' type='button' value='Edit' onclick=editRow('"+cl+"','1'); />"; 
	            se = "<input style='height:22px;width:40px;' type='button' value='Del' onclick=deleteRow('"+cl+"'); />"; 
	            $("#firstCategoryListTable").setRowData(ids[i],{act:be+"&nbsp;"+se});
	        }
	    },
	    onSelectRow:function(id) {
	    	var rowData = $(this).jqGrid('getRowData',id);
	    	var secondCategoryListTable = $("#secondCategoryListTable");
	    	secondCategoryListTable.setCaption(rowData.name);
	    	secondCategoryListTable.clearGridData();
	    	var params = {};
			params.parentCode = id;
			//질문 선택시 하위에 parentQuestionId설정.
			$('#secondCategoryListTable').val(id);
			secondCategoryListTable.setGridParam({postData:params,url:url});
			subGridReLoad();
		},
		rowNum : 20,
		rowList : [20,40,60],
		pager : '#pager',
		emptyrecords:"자료가 없습니다.",
		sortname : 'name',sortorder : 'asc',
		caption : 'First 카테고리 관리',hidegrid : false,height : 300
	});
	
	$("#secondCategoryListTable").jqGrid({
		datatype:'json',
		mtype:'POST',
		postData:{},
		height: 150,
		rowNum:-1,
		jsonReader:{repeatitems: false},
		colNames : ['id','분류명','생성일','설명','레벨','사용여부','수정/삭제'],
		colModel : [
		    {name:'id',index:'id', width:30, hidden:true, sortable : false, key:true},
		    {name : 'name', index : 'name', width : 300},
		    {name : 'createDate', index : 'createDate', width : 100, formatter : customDateFormat,align:'center'},
		    {name : 'description', index : 'description', width : 100},
		    {name : 'level', index : 'level', width : 100},
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
	        var ids = $("#secondCategoryListTable").getDataIDs(); 
	        for(var i=0;i<ids.length;i++){
	            var cl = ids[i];
	            be = "<input style='height:22px;width:40px;' type='button' value='Edit' onclick=editRow('"+cl+"','2'); />"; 
	            se = "<input style='height:22px;width:40px;' type='button' value='Del' onclick=deleteRow('"+cl+"'); />"; 
	            $("#secondCategoryListTable").setRowData(ids[i],{act:be+"&nbsp;"+se});
	        }
	    },
	    onSelectRow:function(id) {
	    	var rowData = $(this).jqGrid('getRowData',id);
	    	var thirdCategoryListTable = $("#thirdCategoryListTable");
	    	thirdCategoryListTable.setCaption(rowData.name);
	    	thirdCategoryListTable.clearGridData();
	    	var params = {};
			params.parentCode = id;
			//질문 선택시 하위에 parentQuestionId설정.
			$('#thirdCategoryListTable').val(id);
			thirdCategoryListTable.setGridParam({postData:params,url:url});
			sub2GridReLoad();
		},
		rowNum : 20,
		rowList : [20,40,60],
		pager : '#pager',
		emptyrecords:"자료가 없습니다.",
		sortname : 'name',sortorder : 'asc',
		caption : 'Second 카테고리 관리',hidegrid : false,height : 300
	});
	
	$("#thirdCategoryListTable").jqGrid({
		datatype:'json',
		mtype:'POST',
		postData:{},
		height: 150,
		rowNum:-1,
		jsonReader:{repeatitems: false},
		colNames : ['id','분류명','생성일','설명','레벨','사용여부','수정/삭제'],
		colModel : [
		    {name:'id',index:'id', width:30, hidden:true, sortable : false, key:true},
		    {name : 'name', index : 'name', width : 300},
		   
		    {name : 'createDate', index : 'createDate', width : 100, formatter : customDateFormat,align:'center'},
		    {name : 'description', index : 'description', width : 100},
		    {name : 'level', index : 'level', width : 100},
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
	        var ids = $("#thirdCategoryListTable").getDataIDs(); 
	        for(var i=0;i<ids.length;i++){
	            var cl = ids[i];
	            be = "<input style='height:22px;width:40px;' type='button' value='Edit' onclick=editRow('"+cl+"','3'); />"; 
	            se = "<input style='height:22px;width:40px;' type='button' value='Del' onclick=deleteRow('"+cl+"'); />"; 
	            $("#thirdCategoryListTable").setRowData(ids[i],{act:be+"&nbsp;"+se});
	        }
	    },
		rowNum : 20,
		rowList : [20,40,60],
		pager : '#pager',
		emptyrecords:"자료가 없습니다.",
		sortname : 'name',sortorder : 'asc',
		caption : 'Third 카테고리 관리',hidegrid : false,height : 300
	});
	
	$( "#dialog-form" ).dialog({ 
	      autoOpen: false,
	      height: 590,
	      width: 680,
	      modal: true,
	      buttons: {
	        "저장": function() {
	        	$("#categoryForm").submit();
	        },
	        "닫기": function() {
	          $( this ).dialog( "close" );
	        }
	      },
	      close: function() {
	    	  $("#categoryForm").resetForm();
	      }
	});
	
/*	$( "#codeType" ).change(function() {
		gridReLoad();
	});*/
	
	$('#categoryForm').ajaxForm({
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
	
	params.level = $("#level").val();
	
	return params;
};


function gridReLoad() {
	var params = makeParams();
	
	$('#firstCategoryListTable').setGridParam({postData:params});
	$("#firstCategoryListTable").jqGrid().trigger("reloadGrid");
};

function subGridReLoad() {
//	$('#secondCategoryListTable').setGridParam({postData:params});
	$("#secondCategoryListTable").jqGrid().trigger("reloadGrid");
};

function sub2GridReLoad(){
	$("#thirdCategoryListTable").jqGrid().trigger("reloadGrid");
};

doSearch = function(){
	gridReLoad();
};

function editRow(id) {
	
	var url = _requestPath+ "/admin/getCategoryForm";

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
				setCategoryForm(pop, result.category);
			}else{
				alert(result.msg);
			}
		},
		error : function(response, status, err){
			alert(err);
			
		}
	});
}


