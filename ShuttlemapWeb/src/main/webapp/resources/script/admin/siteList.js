

$(document).ready(function(){
	
	var url = _requestPath + "/admin/getSites";
	var params = {};
	
	$("#siteListTable").jqGrid({
		url : url,
		datatype : 'json',
		postData:params,mtype:'POST',jsonReader:{repeatitems: false},
		colNames : ['ApiKey','Site명','설명','MaxLimits','수정/삭제'],
		colModel : [
		    {name:'id',index:'id', width:250, sortable : false, key:true},
		    {name : 'siteName', index : 'siteName', width : 100},
		    {name : 'comment', index : 'comment', width : 400},
		  
		    {name : 'maxLimits', index : 'maxLimitPerDay',  align:'center',  width : 100},
		    {name : 'act', index:'act', width:100, sortable:false, align:"center"}
		],
	   	gridComplete: function(){ 
	        var ids = $("#siteListTable").getDataIDs(); 
	        for(var i=0;i<ids.length;i++){
	            var cl = ids[i];
	            be = "<input style='height:22px;width:40px;' type='button' value='Edit' onclick=editRow('"+cl+"','1'); />"; 
	            se = "<input style='height:22px;width:40px;' type='button' value='Del' onclick=deleteRow('"+cl+"'); />"; 
	            $("#siteListTable").setRowData(ids[i],{act:be+"&nbsp;"+se});
	        }
	    },
	    onSelectRow:function(id) {
	    	
		},
		rowNum : 20,
		rowList : [20,40,60],
		pager : '#pager',
		emptyrecords:"자료가 없습니다.",
		sortname : 'name',sortorder : 'asc',
		caption : 'Api Site 관리',hidegrid : false,height : 300
	});
	
	$( "#dialogForm" ).dialog({ 
	      autoOpen: false,
	      height: 590,
	      width: 680,
	      modal: true,
	      buttons: {
	        "저장": function() {
	        	$("#siteForm").submit();
	        },
	        "닫기": function() {
	          $( this ).dialog( "close" );
	        }
	      },
	      close: function() {
	    	  $("#siteForm").resetForm();
	      }
	});

	
	$('#siteForm').ajaxForm({
		dataType:'json',
		success : function(result){
			var success = result.success;
			if(success){
				gridReLoad();
				$( "#dialogForm" ).dialog( "close" );
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
	
	$("#siteListTable").jqGrid().trigger("reloadGrid");
};

function addRow(){
	var pop = $( "#dialogForm" ).dialog( "open" );
}

function editRow(id) {
	
	var url = _requestPath+ "/admin/getSiteForm";

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
				var pop = $( "#dialogForm" ).dialog( "open" );
				setSiteForm(pop, result.data);
			}else{
				alert(result.msg);
			}
		},
		error : function(response, status, err){
			alert(err);
			
		}
	});
}


function setSiteForm(obj, data, acc) { 
	
	$(obj).find("#siteId").val(data.id);
	$(obj).find("#siteName").val(data.siteName);
	$(obj).find("#comment").val(data.comment);
	$(obj).find("#value2").val(data.value2);
	$(obj).find("#active").val(data.active);
}

