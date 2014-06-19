package com.shuttlemap.web.identity.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.shuttlemap.web.Role;
import com.shuttlemap.web.common.CommonUtils;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.entity.UserRoles;
import com.shuttlemap.web.identity.ILogin;
import com.shuttlemap.web.identity.IUserDAO;
import com.shuttlemap.web.identity.IUserService;
import com.shuttlemap.web.identity.OsType;
import com.shuttlemap.web.identity.SocialType;

@Service("UserService")
public class UserService implements ILogin,IUserService {
	@Autowired IUserDAO dao;
	
	Logger log = Logger.getLogger(UserService.class);
	
	@Transactional(readOnly=true)
	@Override
	public User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal != null){
			if(principal instanceof String){
				return null; //익명의 사용자이다.
			}
			org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User)principal;
			User user = dao.findByLoginId(springUser.getUsername());
			return user;
		}
		return null;
	}

	@Transactional(readOnly=true)
	@Override
	public boolean isInRole(User user, String roleName) {
		for(UserRoles ur : user.getRoles()){
			if(ur.getRoleName().equals(roleName))
				return true;
		}
		return false;
	}

	@Override
	public void updateLastLoginDate(User user) {
		user.setLastLoginDate(new Date());
		dao.updateUser(user);
	}

	@Override
	public void updateLoginData(User user, Date lastLoginDate, OsType osType,
			String osVersion, String appVersion) {
		user.setLastLoginDate(lastLoginDate);
		user.setAppVersion(appVersion);
		user.setOsVersion(osVersion);
		user.setOsType(osType);

		//여기에서 loginToken도 새로 발행해준다.
		user.setLoginToken(makeLoginToken(user));
		Calendar cur = Calendar.getInstance();
		cur.add(Calendar.MONTH, 1);	//우선 한달뒤로 설정한다. 
		user.setTokenExpireDate(cur.getTime());
		
		dao.updateUser(user);
	}
	
	private String makeLoginToken(User user){
		Date curr = new Date();
		return user.getId() + "-" + curr.getTime();
	}
	
	@Transactional
	@Override
	public void initAdmin() {
		try{
			User adminUser = dao.findByLoginId(IUserService.ADMIN_ID);
			if(adminUser == null){
				adminUser = new User();
				adminUser.setLoginId(IUserService.ADMIN_ID);	
				adminUser.setPassword(new String(CommonUtils.md5(IUserService.ADMIN_PASSWORD)));
				adminUser.setName("admin");
				adminUser.setActive(true);
				adminUser.setCreated(new Date());
				
				dao.addRoleToUser(adminUser,Role.ADMIN_ROLE);
				dao.createUser(adminUser);
			}
		}catch(Exception e){
			log.error("Init admin error : " + e);
		}
		
	}

	@Override
	public void initUser() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User findByLoginId(String loginId) {
		return dao.findByLoginId(loginId);
	}

	@Override
	public User findBySocialId(SocialType type, String socialId) {
		return dao.findBySocialId(type, socialId);
	}

	@Override
	public List<User> getUsers(String role) {
		return dao.getUsers(role);
	}

	@Override
	public User loadUser(Long id) {
		return dao.load(id);
	}

	@Override
	public boolean isValidToken(String loginId, String loginToken) {
		User user = findByLoginId(loginId);
		if(user != null){
			Date currDate = new Date();
			if(loginToken.equals(user.getLoginToken()) && user.getTokenExpireDate() != null 
					&& user.getTokenExpireDate().after(currDate)){
				return true;
			}
		}
		
		return false;
	}

	@Transactional
	@Override
	public boolean updateRegistrationId(Long userId, String regId) {
		User user = dao.load(userId);
		
		if(user != null){
			user.setGcmId(regId);
			dao.updateUser(user);
			return true;
		}
		return false;
	}

}
