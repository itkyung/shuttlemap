package com.shuttlemap.android.server.entity;

public enum AccuracyType {
	HIGH(){
		public String getLabel(){
			return "높은 정확도";
		}
	},
	LOW(){
		public String getLabel(){
			return "낮은 정확도";
		}
	};
	
	
	public String getLabel(){
		return null;
	}
}
