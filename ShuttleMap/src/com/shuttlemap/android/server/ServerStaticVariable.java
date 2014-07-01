package com.shuttlemap.android.server;

public interface ServerStaticVariable {
	//public static final String BASE_URL = "http://121.78.131.152/shuttlemap";
	public static final String IMG_BASE_URL = "http://121.78.131.152/";

	//public static final String IMG_BASE_URL = "http://192.168.219.158/";
	public static final String BASE_URL = "http://192.168.219.158:8080/shuttlemap";
	public static final String BASE_MOBILE_URL = BASE_URL + "/mobile";
	public static final String ProfileURL   = IMG_BASE_URL + "/upload/profile/";
	public static final String ImageDownloadURL = BASE_URL + "/image_download";
	public static final String ImageURL     = IMG_BASE_URL + "/upload/picture/";
	public static final String ThumbURL     = IMG_BASE_URL + "/upload/thumbnail/";
	
	/**
	 * Login
	 */
	public static final String LoginURL = BASE_URL + "/loginAction";
	public static final String LoginByTokenURL = BASE_URL + "/loginByToken";
	public static final String LoginBySocialURL = BASE_URL + "/loginBySocial";
	public static final String LoginOutURL = BASE_URL + "/logout";
	public static final String CheckSnsAccountURL = BASE_URL + "/login/checkSnsAccount";
	
	public static final String GcmRegistURL = BASE_MOBILE_URL + "/updateGCMRegistrationId";
	

	public static final String SearchShuttleURL = BASE_MOBILE_URL + "/shuttle/searchShuttle";
	public static final String GetRoutesURL = BASE_MOBILE_URL + "/shuttle/listRoutes";
	public static final String AddBookmarkURL = BASE_MOBILE_URL + "/shuttle/addBookmark";
	public static final String ListBookmarkURL = BASE_MOBILE_URL + "/shuttle/listBookmark";
	public static final String RemoveBookmarkURL = BASE_MOBILE_URL + "/shuttle/removeBookmark";
	
	
	/**
	 * Account
	 */
	public static final String RegistAccountURL = BASE_MOBILE_URL + "/registAccount";
	
	public static final String ResendActivateURL = BASE_MOBILE_URL + "/account/resendActivateLink";
	public static final String ResetPasswordURL = BASE_MOBILE_URL + "/account/resetPassword";
	public static final String UpdateAccountURL = BASE_MOBILE_URL + "/account/updateAccount";
	public static final String ChangePassworkdURL = BASE_MOBILE_URL + "/account/changePasswd";
	
	
	/**
	 * Error Code
	 */
	public static final int SUCCESS = 0;
	public static final int LOGIN_COMPLETE = 1000;
	
	// -1번은 알려지지 않은 에러.
	public static final int ERROR_UNKNOWN = -1;
	
	//100번대는 로그인 에러
	public static final int ERROR_NOT_ACTIVE = 101;
	public static final int ERROR_USER_NOT_EXIST = 102;
	public static final int ERROR_PASSWORD = 103;
	
	
	// 200번대는 account에러.
	public static final int ERROR_DUPLICATE = 201;
	public static final int ERROR_MANDATORY = 202;
}
