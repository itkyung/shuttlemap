package com.shuttlemap.web.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


import com.shuttlemap.web.IShuttleService;
import com.shuttlemap.web.common.CommonUtils;
import com.shuttlemap.web.delegate.UserDelegate;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.entity.UserType;
import com.shuttlemap.web.identity.ILogin;
import com.shuttlemap.web.identity.IUserService;
import com.shuttlemap.web.identity.OsType;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/mobile")
public class MobileController {
	@Autowired ILogin login;
	@Autowired IUserService userService;
	@Autowired IShuttleService shuttleService;
	
	private Log log = LogFactory.getLog(MobileController.class.getName());
	
	@RequestMapping(value="/registAccount",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String registAccount(@ModelAttribute("userDelegate") UserDelegate delegate,BindingResult result){
		if(userService.findByLoginId(delegate.getLoginId()) != null){
			//로그인 아이디 중복.
			return CommonUtils.toJsonResult(false, IErrorCode.ERROR_DUPLICATE, null);
		}
		
		try{
			User user = userService.registUser(delegate.getLoginId(), delegate.getPassword(), delegate.getName(), 
					delegate.getPhone(), UserType.NORMAL, delegate.getAppVersion(), OsType.valueOf(delegate.getOsType()));
			return CommonUtils.toJsonResult(true, 0, null);
		}catch(Exception e){
			log.error("Error ",e);
			return CommonUtils.toJsonResult(false, 0, null);
		}
	}
	
	@RequestMapping(value="/updateGCMRegistrationId",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String updateGCMRegistrationId(@RequestParam("userId") String userId,
			@RequestParam("regId") String regId){
		if(regId == null || "".equals(regId)){
			return CommonUtils.toJsonResult(true, IErrorCode.SUCCESS, null);
		}
		boolean result = userService.updateRegistrationId(userId, regId);
		if(result){
			return CommonUtils.toJsonResult(true, IErrorCode.SUCCESS, null);
		}else{
			return CommonUtils.toJsonResult(false, IErrorCode.ERROR_UNKNOWN, null);
		}
	}
	/**
	 * 현재 사용자의 현재 위치를 update한다.
	 * 만약 해당 사용자가 driver이면 driver의 시간과 위치에 따라서 셔틀의 현재 위치를 업데이트 해준다.
	 * 
	 */
	@RequestMapping(value="/updateLocation",produces = "application/json;charset=utf-8")
	@ResponseBody
	public String updateLocation(@RequestParam("loginId") String loginId,
			@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {
		User user = userService.findByLoginId(loginId);
		
		if(user.getUserType().equals(UserType.DRIVER)){
			shuttleService.updateDriverLocation(user, latitude, longitude);
			userService.updateLocation(user, latitude, longitude);
		}else{
			userService.updateLocation(user, latitude, longitude);
		}
		
		return CommonUtils.toJsonResult(true, IErrorCode.SUCCESS, null);
	}
	
	
}
