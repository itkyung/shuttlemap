package com.shuttlemap.web.identity.impl;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * 인증 토큰을 통한 로그인에서 사용할 Token클래스 
 * loginId와 token을 받아들인다.
 * @author itkyung
 *
 */

public class SignedAuthenticationToken extends UsernamePasswordAuthenticationToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6823945669608261974L;
	
	private String signedToken;
	

	
	public SignedAuthenticationToken(String principal,String token){
		super(principal,token);
		this.signedToken = token;
	
	}

	public String getSignedToken() {
		return signedToken;
	}

	public void setSignedToken(String signedToken) {
		this.signedToken = signedToken;
	}

	
	
}
