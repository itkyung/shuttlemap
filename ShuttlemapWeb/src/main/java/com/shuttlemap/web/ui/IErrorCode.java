package com.shuttlemap.web.ui;

public interface IErrorCode {
	public static final int SUCCESS = 0;
	
	// -1번은 알려지지 않은 에러.
	public static final int ERROR_UNKNOWN = -1;
	public static final int ERROR_NO_PERMISSION = 10;
	public static final int ERROR_DATA_NOT_FOUND = 20;
	
	//100번대는 로그인 에러
	public static final int ERROR_NOT_ACTIVE = 101;
	public static final int ERROR_USER_NOT_EXIST = 102;
	public static final int ERROR_PASSWORD = 103;
	
	
	// 200번대는 account에러.
	public static final int ERROR_DUPLICATE = 201;
	public static final int ERROR_MANDATORY = 202;
	
	// 300번대는 artwork에러
	public static final int ERROR_ALREADY_PROCESSED = 301;
	public static final int ERROR_YOUR_ARTWORK = 302;
	public static final int ERROR_CANT_REMOVE_COLLECT = 303;
	
	
	//400번대는 contest에러
	public static final int ERROR_NOT_ACTIVE_CONTEST = 401;
	
	//500번대는 story 
	public static final int ERROR_DOES_NOT_DRAFT = 501;
	
	//600번대는 class
	public static final int ERROR_ALREADY_RESERVE_PART = 601;
	
	
}
