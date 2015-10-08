package com.soco.security.authentication;

import com.soco.security.encryption.AES;

public class UserToken {
	
	
	public static final long ONE_MONTH_MILLIONSECOND = 2592000000L;
	public static final long ONE_DAY_MILLIONSECOND = 864000000L;
	
	/*
	 * 
	 * */
	public static String getToken(String key, long uid, long expired){
		String token = "";
		String plain = String.valueOf(uid) + String.valueOf(expired);
		
		try {
			token = AES.Encrypt(plain, key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return token;
	}

}
