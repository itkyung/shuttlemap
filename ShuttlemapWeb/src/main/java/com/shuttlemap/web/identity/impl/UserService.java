package com.shuttlemap.web.identity.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.identity.ILogin;
import com.shuttlemap.web.identity.OsType;

@Service("UserService")
public class UserService implements ILogin {

	@Override
	public User getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInRole(User user, String roleName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateLastLoginDate(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLoginData(User user, Date lastLoginDate, OsType osType,
			String osVersion, String appVersion) {
		// TODO Auto-generated method stub

	}

}
