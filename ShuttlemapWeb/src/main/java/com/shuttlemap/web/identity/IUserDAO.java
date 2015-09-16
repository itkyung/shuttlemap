package com.shuttlemap.web.identity;

import java.util.List;

import com.shuttlemap.web.delegate.CompanyDelegate;
import com.shuttlemap.web.delegate.CompanySearchModel;
import com.shuttlemap.web.entity.Association;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.CurrentLocation;
import com.shuttlemap.web.entity.Friends;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.entity.UserType;



public interface IUserDAO {
	User load(String id);
	User findByLoginId(String loginId);	
	List<User> findByName(String name);
	User findBySocialId(SocialType type,String socialId);
	void createUser(User user);
	void addRoleToUser(User user, String roleName);
	void replaceRoleToUser(User user,String oldRole,String newRole);
	void updateUser(User user);
	User loadUser(String userId);
	
	List<User> getUsers(String role);
	List<User> searchUsers(String keyword,int start,int limits);
	List<User> findUser(String phoneNum);
	
	void createFriends(Friends friend);
	void updateFriends(Friends friend);
	Friends loadFriends(String id);
	/**
	 * 승인된 친구를 찾는다.
	 * @param user	//내가 requester이거나 receiver인 것..
	 * @param start
	 * @param limits
	 * @return
	 */
	List<Friends> findFriends(User user,Boolean approved,int start,int limits);
	List<Friends> findFriends(User requester,User receiver,boolean approved,boolean rejected,int start,int limits);
	int countFriends(User user1,User user2,boolean approved);
	
	void createLocation(CurrentLocation location);
	void updateLocation(CurrentLocation location);
	CurrentLocation getLocation(User user);
	List<CurrentLocation> listFriendsLocation(User user);
	List<CompanyDelegate> findCompany(CompanySearchModel condition);
	int countCompany(CompanySearchModel condition);
	
	Company loadCompany(String id);
	void createCompany(Company company);
	void updateCompany(Company company);
	List<User> findUser(Company company);
	List<User> findUser(Association association);
	
	List<User> findUserByName(String name,UserType userType,int start,int limits);
	int countUser(String name,UserType userType);
	List<Company> findCompany(Association association);
}
