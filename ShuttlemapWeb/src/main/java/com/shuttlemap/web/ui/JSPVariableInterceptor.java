package com.shuttlemap.web.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.shuttlemap.web.Role;
import com.shuttlemap.web.common.CommonUtils;
import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.identity.ILogin;






public class JSPVariableInterceptor extends HandlerInterceptorAdapter {
	@Autowired private ILogin login;
	
	private DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null) {
			ModelMap model = modelAndView.getModelMap();
			String uri = request.getRequestURI();
		    String contextPath = request.getContextPath();
		    String subUri = uri.substring(contextPath.length());
		    model.addAttribute("_isMobile",CommonUtils.isGalaxyNote(request));
		    
		    User currentUser = login.getCurrentUser();
		    model.addAttribute("_currentUser", currentUser);
		    
		    if (currentUser != null){
			    String roleName = Role.USER_ROLE;
				if (login.isInRole(currentUser, Role.COMPANY_ROLE)) {
					roleName = Role.COMPANY_ROLE;
				} else if(login.isInRole(currentUser, Role.ASSOCIATION_ROLE)) {
					roleName = Role.ASSOCIATION_ROLE;
				} else if(login.isInRole(currentUser, Role.ADMIN_ROLE)) {
					roleName = Role.ADMIN_ROLE;
				}
				model.addAttribute("_roleName", roleName);
		    }
		    model.addAttribute("_imageServerPath","http://" + request.getServerName());
		}
		
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		return super.preHandle(request, response, handler);
	}
	
}
