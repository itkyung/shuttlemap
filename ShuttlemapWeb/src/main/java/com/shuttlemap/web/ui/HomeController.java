package com.shuttlemap.web.ui;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.identity.ILogin;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired
	private ILogin login;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("/")
	public void defaultPage(HttpServletRequest request,HttpServletResponse response) throws IOException{
		User currentUser = login.getCurrentUser();
		String url;
		if(currentUser == null) {
			url = request.getContextPath() + "/login/loginForm";
		}else{
			url = request.getContextPath() + "/home";
		}
		response.sendRedirect(url);
	}
	
	@RequestMapping("/home")
	public String home() {
		
		return "home";
	}
}
