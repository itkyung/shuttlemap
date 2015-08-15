package com.shuttlemap.web.ui;

import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shuttlemap.web.identity.IUserService;

@Controller
@RequestMapping(value="/login")
public class LoginController {
	@Autowired private IUserService userService;
	
	
	@RequestMapping(value="/loginForm",method=RequestMethod.GET)
	public String loginForm(Model model){
		
		return "/login";
	}
	
	@RequestMapping("/initUser")
	public String initUser(){
		userService.initAdmin();
		return "";
	}
}
