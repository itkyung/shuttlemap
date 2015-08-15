package com.shuttlemap.web.delegate;

import com.shuttlemap.web.entity.UserType;




public class CompanySearchModel extends DataTableSearchModel{
	private String keyword;
	private String companyType;
	private String userType;
	
	private boolean desc=true;
	
	private String key;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public boolean isDesc() {
		return desc;
	}
	public void setDesc(boolean desc) {
		this.desc = desc;
	}
	public String getCompanyType() {
		return companyType;
	}
	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
	public String getUserType() {
		return userType == null ? UserType.NORMAL.name() : userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
	
}
