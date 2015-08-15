$(document).ready(function(){
	
	var url = _requestPath + "/admin/getBrands";
	var params = makeParams();
/*	var params = {};
	params.codeType = $("#codeType").val();*/
	
	$("#codeSearchResultTable").jqGrid({
		url : url,
		datatype : 'json',
		postData:params,mtype:'POST',jsonReader:{repeatitems: false},
		colNames : ['id','분류','내용1','내용2','생성일','수정일','사용여부 '],
		colModel : [
		    {name:'id',index:'id', width:30, hidden:true, sortable : false, key:true},
		    {name : 'type', index : 'type', align:'center', width : 100},
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
		    }
		],
		rowNum : 20,
		rowList : [20,40,60],
		pager : '#pager',
		sortname : 'type',sortorder : 'asc',
		caption : '브랜드 관리',hidegrid : false,height : 300
	});
	
	$( "#codeType" ).change(function() {
		gridReLoad();
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
	
	var codeType = 'BRAND';
	params.codeType = codeType;
	
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


