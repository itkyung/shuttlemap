package com.shuttlemap.android.server.entity;

public enum CarType {
	BUS(){
		 public String getLabel(){
			 return "버스";
		 }
	}, //버스
	VAN(){
		//승합차 
		public String getLabel(){
			 return "승합차";
		 }
	},
	UNKNOWN(){
		public String getLabel(){
			 return "확인안됨";
		 }
	};
	
	public String getLabel(){
		return null;
	}
}
