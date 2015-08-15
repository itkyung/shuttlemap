var currentPage = 1;
var perPage = 6;
var getDataProcessing = false;

var $masonryContainer = null;

$(document).ready(function(){
	
	$masonryContainer = $("#masonryContainer").masonry({
		columnWidth : '.sub-grid-sizer',
		itemSelector : '.sub-product-item'
	});
	initSubObject();
	
	$masonryContainer.masonry('on','layoutComplete',function(){
		initSubObject();
	});
	
	
	$(window).scroll(function(){
		  if  ($(window).scrollTop() == $(document).height() - $(window).height()){
			  if(getDataProcessing) return;
			  getDataProcessing = true;

			  getMoreProducts(currentPage + 1);
		  }
	});
	$(".sub-product-image").click(function(){
		var productId = $(this).attr("id");
		viewProduct(productId);
	});
});

initSubObject = function(){
	$(".sub-product-inner").hover(function(){
		var $infoObj = $(this).find(".sub-product-info");
		$infoObj.addClass("active");
		
	},function(){
		var $infoObj = $(this).find(".sub-product-info");
		$infoObj.removeClass("active");
	});
	
	$(".sub-product-image").click(function(){
		var productId = $(this).attr("id");
		viewProduct(productId);
	});
};

getMoreProducts = function(page){
	var params = {"page":page,"mallId":_currentMallId};
	
	$.ajax({
		dataType:  'json', 
		type : 'POST',
		url : _requestPath + "/searchProductByMall",
		timeout : 10000,
		data : params,
		beforeSubmit : function(){
			
		},				
		success : function(results){
			var elems = $("#subProductTemplate").tmpl(results);
			$masonryContainer.append(elems).masonry('appended',elems);
			
			initSubObject();
			
			currentPage = page;
		},
		error : function(response, status, err){
			alert("ERROR [" + status + "][" + err + "]");
		}
	});	//Ajax로 호출한다.
	
};

viewProduct = function(id){
	
	document.location.href = _requestPath + "/viewProduct/" + id;
};

addBookmark = function(mallId){
	bootbox.confirm("단골몰로 등록하시겠습니까?",function(result){
		if(result){
			var params = {"mallId":_currentMallId};
			
			$.ajax({
				dataType:  'json', 
				url : _requestPath + "/addMallBookmark",
				timeout : 10000,
				data : params,
				beforeSubmit : function(){
					
				},				
				success : function(results){
					bootbox.alert("성공적으로 등록되었습니다.",function(){
						$("#addMallBtnArea").remove();
					});
				},
				error : function(response, status, err){
					alert("ERROR [" + status + "][" + err + "]");
				}
			});	//Ajax로 호출한다.
		}
	});
};

