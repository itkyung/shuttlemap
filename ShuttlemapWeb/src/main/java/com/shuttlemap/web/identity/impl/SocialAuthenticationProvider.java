package com.shuttlemap.web.identity.impl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.identity.IUserService;



public class SocialAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	private UserDetailsService userDetailsService;
	private IUserService userService;
	
	@Override
	public boolean supports(Class<?> authentication) {
		return (SocialAuthenticationToken.class.isAssignableFrom(authentication));
	}
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		
		String loginId = userDetails.getUsername();
		
		
		SocialAuthenticationToken authToken = (SocialAuthenticationToken)authentication;
		
		String socialId = authToken.getSocialId();
		User user = userService.findByLoginId(loginId);
		
		boolean isValid = false;
		if(authToken.getSocialType().equals("FACEBOOK")){
			if(socialId.equals(user.getFacebook())){
				isValid = true;
			}
		}else{
			if(socialId.equals(user.getTwitter())){
				isValid = true;
			}
		}

		if(!isValid){
			throw new BadCredentialsException("Login Token is not valid");
		}
	}

	@Override
	protected UserDetails retrieveUser(String username,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		return this.userDetailsService.loadUserByUsername(username);
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
}
