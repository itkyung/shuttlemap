package com.shuttlemap.web.identity.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring security의 default 파라미터인 j_username과 j_password를 다른이름으로 바꾸게한다.
 * @author itkyung
 *
 */
public class UserPassAuthFilterBeanPostProcessor implements BeanPostProcessor {

	private String usernameParameter;
	private String passwordParameter;
	
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		
		if(bean instanceof UsernamePasswordAuthenticationFilter){
			final UsernamePasswordAuthenticationFilter filter = (UsernamePasswordAuthenticationFilter)bean;
			filter.setUsernameParameter(usernameParameter);
			filter.setPasswordParameter(passwordParameter);
		}
		
		
		return bean;
	}

	public String getUsernameParameter() {
		return usernameParameter;
	}

	public void setUsernameParameter(String usernameParameter) {
		this.usernameParameter = usernameParameter;
	}

	public String getPasswordParameter() {
		return passwordParameter;
	}

	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}


}
