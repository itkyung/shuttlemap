package com.shuttlemap.web.identity.impl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class SocialAuthenticationToken extends UsernamePasswordAuthenticationToken {
	private String socialType;
	private String socialId;
	
	public SocialAuthenticationToken(String principal,String socialType,String socialId){
		super(principal,socialId);
		this.socialType = socialType;
		this.socialId = socialId;
	}

	public String getSocialType() {
		return socialType;
	}

	public void setSocialType(String socialType) {
		this.socialType = socialType;
	}

	public String getSocialId() {
		return socialId;
	}

	public void setSocialId(String socialId) {
		this.socialId = socialId;
	}
	
	
}
