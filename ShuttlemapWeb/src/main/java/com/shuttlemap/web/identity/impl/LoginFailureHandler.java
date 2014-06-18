package com.shuttlemap.web.identity.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.shuttlemap.web.common.CommonUtils;
import com.shuttlemap.web.ui.IErrorCode;





public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException error)
			throws IOException, ServletException {
		boolean isMobile = null == request.getParameter("osType") ? false : true;
		
		if(isMobile || "true".equals(request.getHeader("mobile-call")) || "true".equals(request.getHeader("ajax-call"))){
			int errorCode =  IErrorCode.SUCCESS;
			if(error instanceof BadCredentialsException){
				errorCode = IErrorCode.ERROR_PASSWORD;
			}else if(error instanceof UsernameNotFoundException){
				errorCode = IErrorCode.ERROR_USER_NOT_EXIST;
			}else{
				errorCode = IErrorCode.ERROR_UNKNOWN;
			}
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(CommonUtils.toJsonResult(false,errorCode,null));
			response.getWriter().flush();
			return;
		}
	}

}
