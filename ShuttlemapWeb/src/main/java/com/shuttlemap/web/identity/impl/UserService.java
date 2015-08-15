package com.shuttlemap.web.identity.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.google.gson.annotations.Expose;
import com.shuttlemap.web.Role;
import com.shuttlemap.web.common.CommonUtils;
import com.shuttlemap.web.delegate.CompanyDelegate;
import com.shuttlemap.web.delegate.CompanySearchModel;
import com.shuttlemap.web.delegate.UserDelegate;
import com.shuttlemap.web.entity.Company;
import com.shuttlemap.web.entity.CurrentLocation;
import com.shuttlemap.web.entity.Friends;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.entity.UserRoles;
import com.shuttlemap.web.entity.UserType;
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
		if(SecurityContextHolder.getContext().getAuthentication() == null) return null;
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

	@Transactional
	@Override
	public void updateLastLoginDate(User user) {
		user.setLastLoginDate(new Date());
		dao.updateUser(user);
	}

	@Transactional
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
	public User loadUser(String id) {
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
	public boolean updateRegistrationId(String userId, String regId) {
		User user = dao.load(userId);
		
		if(user != null){
			user.setGcmId(regId);
			dao.updateUser(user);
			return true;
		}
		return false;
	}

	@Transactional
	@Override
	public User registUser(String loginId, String plainPassword, String name,
			String phone,UserType userType,String appVersion,OsType osType)  throws Exception{
		User user = dao.findByLoginId(loginId);
		if(user != null){
			throw new Exception("이미 존재하는 로그인 아이디입니다.");
		}
		
		user = new User();
		user.setLoginId(loginId);
		user.setName(name);
		user.setPassword(new String(CommonUtils.md5(plainPassword)));
		user.setPhone(phone);
		user.setActive(true);
		user.setUserType(userType);
		user.setAppVersion(appVersion);
		user.setOsType(osType);
		
		switch (userType) {
		case NORMAL:
			dao.addRoleToUser(user,Role.USER_ROLE);
			break;
		case DRIVER:
			dao.addRoleToUser(user,Role.USER_ROLE);
			break;
		default:
			break;
		}
		
		dao.createUser(user);
		
		return user;
	}

	
	//user가 requester또는 receiver인 친구관계중에서 승인난것만을 구한다.
	@Override
	public List<UserDelegate> getFriends(User user,boolean includeLocation,int start, int limits) {
		List<String> friendIds = new ArrayList<String>();
		List<UserDelegate> users = new ArrayList<UserDelegate>();
		List<Friends> results = dao.findFriends(user, true, start,limits);
		Map<String,CurrentLocation> locationMap = new HashMap<String, CurrentLocation>();
		if(includeLocation){
			//위치 정보를 얻어와서 update한다.
			List<CurrentLocation> locations = dao.listFriendsLocation(user);
			for(CurrentLocation location : locations){
				locationMap.put(location.getUser().getId(), location);
			}
		}
		
		for(Friends friend : results){
			User target = null;
			if(friend.getRequester().equals(user)){
				target = friend.getReceiver();
			}else{
				target = friend.getRequester();
			}
			friendIds.add(target.getId());
			UserDelegate delegate = new UserDelegate(target);
			delegate.setFriendId(friend.getId());
			delegate.setFriendFlag(true);
			CurrentLocation location = locationMap.get(target.getId());
			if(location != null){
				delegate.setLatitude(location.getLatitude().doubleValue());
				delegate.setLongitude(location.getLongitude().doubleValue());
				delegate.setLastLocationDate(location.getLastUpdateDate().getTime());
			}
			users.add(delegate);
		}
		
		return users;
	}

	@Override
	public List<UserDelegate> listFriends(User user, boolean receiverFlag,
			boolean approved, boolean rejected, int start, int limits) {
		User requester = receiverFlag ? null : user;
		User receiver = receiverFlag ? user : null;
		
		List<Friends> results = dao.findFriends(requester, receiver, approved, rejected, start, limits);
		
		List<UserDelegate> users = new ArrayList<UserDelegate>();
		
		for(Friends friend : results){
			if(receiverFlag){
				//requester들이 친구가 된다.
				UserDelegate delegate = new UserDelegate(friend.getRequester());
				delegate.setFriendFlag(approved);
				delegate.setFriendProgress(!approved);
				delegate.setFriendId(friend.getId());
				users.add(delegate);
			}else{
				UserDelegate delegate = new UserDelegate(friend.getReceiver());
				delegate.setFriendFlag(approved);
				delegate.setFriendProgress(!approved);
				delegate.setFriendId(friend.getId());
				users.add(delegate);
			}
		}
		
		
		
		return users;
	}

	/**
	 * 이미 친구이거나 또는 친구요청이 있는지를 확인한다.
	 */
	@Override
	public boolean isFriends(User user1, User user2) {
		int approvedCount = dao.countFriends(user1, user2, true);
		int waitCount = dao.countFriends(user1, user2, false);
		
		return (approvedCount+waitCount) == 0 ? false : true;
	}

	@Transactional
	@Override
	public boolean requestFriends(User requester, User receiver) {
		if(isFriends(requester, receiver)){
			return false;
		}
		
		Friends friend = new Friends();
		friend.setApproved(false);
		friend.setReject(false);
		friend.setRequester(requester);
		friend.setReceiver(receiver);
		dao.createFriends(friend);
		
		return true;
	}

	@Transactional
	@Override
	public boolean approveFriends(String friendsId) {
		Friends friend = dao.loadFriends(friendsId);
		friend.setApproved(true);
		friend.setApprovedDate(new Date());
		dao.updateFriends(friend);
		
		return true;
	}

	@Transactional
	@Override
	public boolean rejectFriends(String friendsId) {
		Friends friend = dao.loadFriends(friendsId);
		friend.setReject(true);
		friend.setRejectDate(new Date());
		return true;
	}

	@Transactional
	@Override
	public void updateLocation(User user, double latitude, double longitude) {
		CurrentLocation location = dao.getLocation(user);
		if(location == null){
			location = new CurrentLocation();
			location.setUser(user);
			location.setLatitude(new BigDecimal(latitude));
			location.setLongitude(new BigDecimal(longitude));
			location.setLastUpdateDate(new Date());
			dao.createLocation(location);
		}else{
			location.setUser(user);
			location.setLatitude(new BigDecimal(latitude));
			location.setLongitude(new BigDecimal(longitude));
			location.setLastUpdateDate(new Date());
			dao.updateLocation(location);
		}
		
	}

	/**
	 * 전화번호를 기반으로 사용자를 찾는다.
	 */
	@Override
	public List<UserDelegate> findUser(User user,String phoneNum) {
		List<UserDelegate> delegates = new ArrayList<UserDelegate>();
		List<User> results = dao.findUser(phoneNum);
		if(results.size() > 0){
			List<Friends> friends = dao.findFriends(user,null, 0, 50);
			Map<String,Friends> map = convertMap(friends, user);
			
			for(User target : results){
				UserDelegate deleate = new UserDelegate(target);
				if(map.containsKey(target.getId())){
					Friends f = map.get(target.getId());
					if(f.isApproved()){
						deleate.setFriendFlag(true);
					}else{
						deleate.setFriendProgress(true);
					}
				}else{
					deleate.setFriendFlag(false);
				}
				delegates.add(deleate);
			}
		}
		
		return delegates;
	}
	
	@Override
	public List<UserDelegate> findUserByPhone(String phoneNum, UserType userType) {
		List<UserDelegate> delegates = new ArrayList<UserDelegate>();
		List<User> results = dao.findUser(phoneNum);
		for (User user : results){
			if(user.getUserType().equals(userType)){
				UserDelegate delegate = new UserDelegate(user);
				delegates.add(delegate);
			}
		}
		
		return delegates;
	}

	private Map<String,Friends> convertMap(List<Friends> friends,User user){
		Map<String,Friends> map = new HashMap<String, Friends>();
		
		for(Friends friend : friends){
			User target = null;
			if(friend.getRequester().equals(user)){
				target = friend.getReceiver();
			}else{
				target = friend.getRequester();
			}
			if(!map.containsKey(target.getId())){
				map.put(target.getId(), friend);
			}
		}
		
		return map;
	}

	@Override
	public List<CompanyDelegate> findCompany(CompanySearchModel condition) {
		return dao.findCompany(condition);
	}

	@Override
	public int countCompany(CompanySearchModel condition) {
		return dao.countCompany(condition);
	}

	@Override
	public Company loadCompany(String id) {
		return dao.loadCompany(id);
	}

	@Transactional
	public void saveCompany(Company company) {
		if(company.getId() == null){
			dao.createCompany(company);
		}else{
			dao.updateCompany(company);
		}
	}

	@Override
	public List<User> findUser(Company company) {
		return dao.findUser(company);
	}

	@Transactional
	@Override
	public void saveCompanyAndUser(Company company,String loginId,String plainPassword) throws Exception {
		saveCompany(company);
		
		if(loginId != null && loginId.length() > 0){
			User adminUser = findByLoginId(loginId);
			if(adminUser == null){
				adminUser = new User();
				adminUser.setLoginId(loginId);	
				adminUser.setPassword(new String(CommonUtils.md5(plainPassword)));
				adminUser.setName(company.getName());
				adminUser.setActive(true);
				adminUser.setCreated(new Date());
				adminUser.setUpdated(new Date());
				adminUser.setCompany(company);
				dao.addRoleToUser(adminUser,Role.COMPANY_ROLE);
				dao.createUser(adminUser);
			}else{
				adminUser.setCompany(company);
				adminUser.setName(company.getName());
				adminUser.setPassword(new String(CommonUtils.md5(plainPassword)));
				adminUser.setUpdated(new Date());
				dao.updateUser(adminUser);
			}
			
		}
		
		
	}

	@Override
	public List<UserDelegate> findUserByName(String name, String userType,
			int start, int limits) {
		UserType uType = null;
		if(userType != null) uType = UserType.valueOf(userType);
		List<User> users = dao.findUserByName(name, uType, start, limits);
		List<UserDelegate> delegates = new ArrayList<UserDelegate>();
		for(User user : users){
			UserDelegate delegate = new UserDelegate(user);
			delegates.add(delegate);
		}
		
		return delegates;
	}

	@Override
	public int countUser(String name, String userType) {
		UserType uType = null;
		if(userType != null) uType = UserType.valueOf(userType);
		return dao.countUser(name, uType);
	}
	
	
}
