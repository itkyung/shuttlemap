if (typeof String.prototype.startsWith != 'function') {
  String.prototype.startsWith = function (str){
    return this.slice(0, str.length) == str;
  };
}

if (typeof String.prototype.endsWith != 'function') {
	String.prototype.endsWith = function(suffix) {
	    return this.indexOf(suffix, this.length - suffix.length) !== -1;
	};
}

function _isMobile(){
	var isMobile = navigator.userAgent.match(/iPhone|iPad|Android/i);
	return isMobile;
}

function customDateFormat(cellValue, option) {
	if(cellValue){
		return $.datepicker.formatDate('yy-mm-dd', new Date(cellValue));
	}
	return "";
}

function numberFormat(cellValue,option){
	if(cellValue){
		return $.number(cellValue);
	}
	return "";
}

Map = function(){
	 this.map = new Object();
};

Map.prototype = {   
	    put : function(key, value){   
	        this.map[key] = value;
	    },   
	    get : function(key){   
	        return this.map[key];
	    },
	    containsKey : function(key){    
	     return key in this.map;
	    },
	    containsValue : function(value){    
	     for(var prop in this.map){
	      if(this.map[prop] == value) return true;
	     }
	     return false;
	    },
	    isEmpty : function(key){    
	     return (this.size() == 0);
	    },
	    clear : function(){   
	     for(var prop in this.map){
	      delete this.map[prop];
	     }
	    },
	    remove : function(key){    
	     delete this.map[key];
	    },
	    keys : function(){   
	        var keys = new Array();   
	        for(var prop in this.map){   
	            keys.push(prop);
	        }   
	        return keys;
	    },
	    values : function(){   
	     var values = new Array();   
	        for(var prop in this.map){   
	         values.push(this.map[prop]);
	        }   
	        return values;
	    },
	    size : function(){
	      var count = 0;
	      for (var prop in this.map) {
	        count++;
	      }
	      return count;
	    }
};

function showTotalMask(){
	var width = $(document).width();
	var height = $(document).height();
	$("#totaMask").css({width : width, height : height}).show();
	
}

function hideTotalMask(){
	$("#totaMask").hide();
}

$(document).ready(function(){
	$(window).resize(function(){
		var width = $(document).width();
		var height = $(document).height();
		$("#totaMask").css({width : width, height : height});
	});
	
	$('.js-loading-bar').modal({
	  backdrop: 'static',
	  show: false
	});
});

function showProgressModal(){
	var $modal = $('.js-loading-bar'),
    $bar = $modal.find('.progress-bar');

	$modal.modal('show');
	$bar.addClass('animate');

}

function removeComma(str){
	return parseInt(str.replace(/,/gi,""));
}



function hideProgressModal(){
	var $modal = $('.js-loading-bar'),
    $bar = $modal.find('.progress-bar');
  $bar.removeClass('animate');
  $modal.modal('hide');
}


(function($) {
	$.fn.maxlength = function() {
		function setCount(src, elem, limit) {
       	 var chars = src.value.length;
       	 if (chars > limit) {
       		 src.value = src.value.substr(0, limit);
       		 chars = limit;
       		 alert(limit + "자 이상 입력 할 수 없습니다.");
       	 }
       	 var count = limit-chars;
       	 elem.html( chars + " / " + limit );
       };
       
		return this.each(function() {
			
			var counterEl = $(this).attr("data-counter");
			var maxLength = $(this).attr("data-text-length");
			maxLength = Number(maxLength);
			if(maxLength > 0){
				$(this).on("keyup focus", function() {
					setCount(this, $(counterEl),maxLength);
		         });
		        
		         setCount($(this)[0], $(counterEl),maxLength);
				
			}
		});
			
			
			
	};
})(jQuery);
