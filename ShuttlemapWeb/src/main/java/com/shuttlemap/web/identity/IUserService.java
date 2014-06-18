package com.shuttlemap.web.identity;

import java.util.List;

import com.shuttlemap.web.entity.User;


public interface IUserService {
	final static String ADMIN_ID = "admin";
	final static String ADMIN_PASSWORD = "admin1234";
		
	void initAdmin();
	void initUser();
	User findByLoginId(String loginId);
	User findBySocialId(SocialType type,String socialId);
	List<User> getUsers(String role);
	User loadUser(Long id);
	
	boolean isValidToken(String loginId,String loginToken);
	boolean updateRegistrationId(Long userId,String regId);
	
	
}
