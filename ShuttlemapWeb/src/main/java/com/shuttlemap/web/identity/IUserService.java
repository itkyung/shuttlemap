package com.shuttlemap.web.identity;

import java.util.List;

import javax.persistence.ManyToOne;

import com.shuttlemap.web.Role;
import com.shuttlemap.web.delegate.CompanyDelegate;
import com.shuttlemap.web.delegate.CompanySearchModel;
import com.shuttlemap.web.delegate.UserDelegate;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.Friends;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.entity.UserType;


public interface IUserService {
	final static String ADMIN_ID = "admin";
	final static String ADMIN_PASSWORD = "admin1234";
		
	void initAdmin();
	void initUser();
	User findByLoginId(String loginId);
	User findBySocialId(SocialType type,String socialId);
	List<User> getUsers(String role);
	User loadUser(String id);
	
	boolean isValidToken(String loginId,String loginToken);
	boolean updateRegistrationId(String userId,String regId);
	
	User registUser(String loginId,String plainPassword,String name,String phone,UserType userType,String appVersion,OsType osType) throws Exception;
	List<UserDelegate> listFriends(User user,boolean recevier, boolean approved, boolean rejected, int start, int limits);
	List<UserDelegate> getFriends(User user,boolean includeLocation,int start, int limits);
	boolean requestFriends(User requester,User receiver);
	boolean approveFriends(String friendsId);
	boolean rejectFriends(String friendsId);
	
	void updateLocation(User user,double latitude,double longitude);
	boolean isFriends(User user1,User user2);
	List<UserDelegate> findUser(User user,String phoneNum);
	List<UserDelegate> findUserByPhone(String phoneNum, UserType userType);
	List<CompanyDelegate> findCompany(CompanySearchModel condition);
	int countCompany(CompanySearchModel condition);
	Company loadCompany(String id);
	List<User> findUser(Company company);
	void saveCompanyAndUser(Company company,String loginId,String plainPassword) throws Exception;
	
	List<UserDelegate> findUserByName(String name, String userType, int start, int limits);
	int countUser(String name, String userType);
}
