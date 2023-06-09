package com.shopme.security.oauth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOAuth2User implements OAuth2User {
	private String clientName;
	private String fullName;
	private OAuth2User oautho2User;
	
	
	public CustomerOAuth2User(OAuth2User user,String clientName) {
		this.oautho2User = user;
		this.clientName = clientName;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oautho2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return oautho2User.getAuthorities();
	}

	@Override
	public String getName() {
		return oautho2User.getAttribute("name");
	}
	
	public String getEmail() {
		return oautho2User.getAttribute("email");
	}
	

	public String getFullName() {
		return fullName != null ? fullName : oautho2User.getAttribute("name");
	}

	public String getClientName() {
		return clientName;
	}

	public void setFullName(String fullName) {
		 this.fullName = fullName;
	}

}
