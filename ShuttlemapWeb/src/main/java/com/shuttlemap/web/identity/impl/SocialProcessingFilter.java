package com.shuttlemap.web.identity.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class SocialProcessingFilter extends AbstractAuthenticationProcessingFilter {

	public SocialProcessingFilter(){
		super("/loginBySocial");
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		String loginId = request.getParameter("loginId");
		String socialId = request.getParameter("socialId");
		String socialType = request.getParameter("socialType");
		
		SocialAuthenticationToken authRequest = new SocialAuthenticationToken(loginId,socialType,socialId);
		
		return this.getAuthenticationManager().authenticate(authRequest);
	}

}
