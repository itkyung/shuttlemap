package com.shuttlemap.web.ui;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.shuttlemap.web.entity.User;
import com.shuttlemap.web.identity.ILogin;





public class JSPTagUtil {
	private static ApplicationContext applicationContext;
	private static ILogin login;
	
	public static void init(ServletContext context){
		if(applicationContext == null){
			applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
			login = (ILogin)applicationContext.getBean("UserService");
		}
	}
	
	public static User getCurrentUser(){
		return login.getCurrentUser();
	}
	
	public static String getCurrentUserName(){
		User user = login.getCurrentUser();
		if(user == null){
			return "익명";
		}else{
			return user.getName();
		}
	}
	
	
    public static String getSubstring(String source,int length){
    	if(source != null){
    		if(source.length() > length){
    			return source.substring(0, length-1) + "..";
    		}else{
    			return source;
    		}
    	}
    	return null;
    }
    
    public static boolean isAm(String time){
    	if(time == null || "".equals(time)) return true;
    	int timeInt = Integer.parseInt(time);
    	if(timeInt >= 0 && timeInt <= 11){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 24시 기준을 오전,오후 값으로 바꾼다.
     * 00 ~ 23까지만 존재한다.
     * @param hour
     * @return
     */
    public static String parseToAmPm(String hour){
    	if(hour == null || "".endsWith(hour)) return "";
    	int timeInt = Integer.parseInt(hour);
    	if(isAm(hour)){
    		return hour;
    	}else{
    		if(timeInt == 12){
    			return "12";
    		}
    		if(timeInt > 23){
    			return "23";	//이 경우는 존재하면 안된다.
    		}
    		return "" + (timeInt - 12);
    	}
    	
    }
    
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public static String formatDate(Date date){
    	if(date == null) return "";
    	try{
    		return format.format(date);
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
    
    public static String formatNumber(double num){
    	NumberFormat nf = NumberFormat.getNumberInstance();
    	return nf.format((int)num);
    }
    
    public static String formatInt(double num){
    	int iNum = (int)num;
    	return iNum + "";
    }
    
    private static DateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String formatDateTime(Date date){
    	if(date == null) return "";
    	try{
    		return formatTime.format(date);
    	}catch(Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
    
}
