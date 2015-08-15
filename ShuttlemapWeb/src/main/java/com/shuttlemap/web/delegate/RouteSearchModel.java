package com.shuttlemap.web.delegate;

public class RouteSearchModel extends DataTableSearchModel {
	private String shuttleId;
	
	private String keyword;

	

	public String getShuttleId() {
		return shuttleId;
	}

	public void setShuttleId(String shuttleId) {
		this.shuttleId = shuttleId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
