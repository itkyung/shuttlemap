package com.shuttlemap.web.entity;

public enum CarType {
	BUS(){
		 public String getLabel(){
			 return "버스";
		 }
	}, //버스
	MINI_BUS(){
		 public String getLabel(){
			 return "미니 버스";
		 }
	}, //버스
	VAN(){
		//승합차 
		public String getLabel(){
			 return "승합차";
		 }
	};
	
	public String getLabel(){
		return null;
	}
}
