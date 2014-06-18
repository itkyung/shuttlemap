package com.shuttlemap.web.identity.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 *  /loginByToken 이라는 url을 감시해서 파라미터로 오는 loginToken을 이용해서 인증시도를 할수있게 한다.
 * @author itkyung
 *
 */
public class RequestHeaderProcessingFilter extends AbstractAuthenticationProcessingFilter {

	public RequestHeaderProcessingFilter(){
		super("/loginByToken");
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		
		String loginId = request.getParameter("loginId");
		String token = request.getParameter("loginToken");
		
		SignedAuthenticationToken authRequest = new SignedAuthenticationToken(loginId,token);
		
		return this.getAuthenticationManager().authenticate(authRequest);
	}

}
