package com.shuttlemap.web.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuttlemap.web.common.CommonUtils;
import com.shuttlemap.web.delegate.UserDelegate;
import com.shuttlemap.web.entity.CurrentLocation;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.identity.ILogin;
import com.shuttlemap.web.identity.IUserService;

@Controller
@RequestMapping("/mobile/friends")
public class MobileFriendsController {
	@Autowired ILogin login;
	@Autowired IUserService userService;
	
	private final int PAGE_SIZE = 30;
	
	@RequestMapping(value="/findUser",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String findUser(@RequestParam("phone") String phone){
		User user = login.getCurrentUser();
		
		List<UserDelegate> results = userService.findUser(user, phone);
		return CommonUtils.toJsonResult(true, -1, results);
	}
	
	@RequestMapping(value="/getFriends",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String getFriends(@RequestParam("page") int page){
		User user = login.getCurrentUser();
		
		int start = (page-1) * PAGE_SIZE;
		int limit = PAGE_SIZE;
		
		List<UserDelegate> results = userService.getFriends(user, true, start, limit);
		
		return CommonUtils.toJsonResult(true, -1, results);
	}
	
	@RequestMapping(value="/requestFriends",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String requestFriends(@RequestParam("userId") String userId){
		User user = login.getCurrentUser();
		User receiver = userService.loadUser(userId);
		boolean result = userService.requestFriends(user, receiver);
		
		return CommonUtils.toJsonResult(result, -1, null);
	}
	
	
	@RequestMapping(value="/approveFriends",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String approveFriends(@RequestParam("friendId") String friendId){
		User user = login.getCurrentUser();
		
		boolean result = userService.approveFriends(friendId);
		
		return CommonUtils.toJsonResult(result, -1, null);
	}
	
	/**
	 * 나의 승인을 기다리는 리스트 
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/waitingForMeFriends",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String waitingForMeFriends(@RequestParam("page") int page){
		User user = login.getCurrentUser();
		
		int start = (page-1) * PAGE_SIZE;
		int limit = PAGE_SIZE;
		
		List<UserDelegate> results = userService.listFriends(user, true, false, false, start, limit);
		return CommonUtils.toJsonResult(true, -1, results);
	}
	
	/**
	 * 내가 요청한 리스트 
	 * @param page
	 * @return
	 */
	@RequestMapping(value="/waitingFriends",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String waitingFriends(@RequestParam("page") int page){
		User user = login.getCurrentUser();
		
		int start = (page-1) * PAGE_SIZE;
		int limit = PAGE_SIZE;
		
		List<UserDelegate> results = userService.listFriends(user, false, false, false, start, limit);
		return CommonUtils.toJsonResult(true, -1, results);
		
	}
	
}
