package com.shuttlemap.web.identity;

import java.util.Date;

import com.shuttlemap.web.entity.User;






public interface ILogin {
	public User getCurrentUser();
	public boolean isInRole(User user,String roleName);
	public void updateLastLoginDate(User user);
	public void updateLoginData(User user,Date lastLoginDate,OsType osType,String osVersion,String appVersion);
}
