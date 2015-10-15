package com.soco.security.authentication;

import java.util.Date;

import com.soco.db.security.AuthenticationTokenController;
import com.soco.db.user.UserController;
import com.soco.log.Log;
import com.soco.security.AuthenticationToken;
import com.soco.security.encryption.AES;
import com.soco.user.User;

public class UserAuthentication {

	/*
	 * authentication user by email and password
	 * */
	public static User authentication(String email, String password){
		User dbUser = new User();
		UserController uc = new UserController();
		dbUser.setEmail(email);
		dbUser.setUserPlainPassword(password);
		dbUser = uc.hasByUIdOrEmail(dbUser);
	    if(dbUser != null ){
	    	if(!dbUser.getUserPlainPassword().equals(password)){
	    		dbUser = null;
	    	}
	    }
	    
		return dbUser;
	}
	
	/*
	 * authentication user by email and password
	 * */
	public static User authentication(User user){
		User dbUser = new User();
		UserController uc = new UserController();
		dbUser = uc.hasByUIdOrEmail(user);
	    if(dbUser != null ){
	    	if(!dbUser.getUserPlainPassword().equals(user.getUserPlainPassword())){
	    		dbUser = null;
	    	}
	    }
	    
		return dbUser;
	}
	
	public static boolean authentication(AuthenticationToken auToken){
		boolean ret = false;
		
		AuthenticationTokenController atController = new AuthenticationTokenController();
		AuthenticationToken dbAuToken = atController.hasTokenByUId(auToken);
		if(dbAuToken != null && dbAuToken.getToken().equals(auToken.getToken())){
			Date d = new Date();
			if(d.getTime() < dbAuToken.getValidity()){
				ret = true;
			} else {
				Log.warn("The token expired for user id: " + auToken.getUId());
			}
		} else {
			Log.warn("There is no token in database for user id: " + auToken.getUId());
		}
		
		return ret;
	}
	
	public static boolean authentication(Long userID, String token){
		AuthenticationToken auToken = new AuthenticationToken();
		auToken.setUId(userID);
		auToken.setToken(token);
		return UserAuthentication.authentication(auToken);
	}
	
	public static AuthenticationToken generateTokenForUser(User user){
		boolean ret = false;
		AuthenticationToken auToken = null;
		if(user.getId() > 0){
			long expired = (new Date()).getTime() + UserToken.ONE_MONTH_MILLIONSECOND;
			String key = AES.getRandomSecKey();
			String token = UserToken.getToken(key, user.getId(), expired);
			AuthenticationTokenController atc = new AuthenticationTokenController();
			auToken = new AuthenticationToken();
			auToken.setKey(key);
			auToken.setStartTime(new Date());
			auToken.setUId(user.getId());
			auToken.setToken(token);
			auToken.setValidity(expired);
			if(atc.hasTokenByUId(auToken) != null){
				// error
				Log.error("In register. When insert authentication token, the record is already existent.");
				Log.error("To update the authentication token record for user id: " + user.getId());
				ret = atc.updateAuthenticationToken(auToken);
			} else {
				ret = atc.createAuthenticationToken(auToken);
			}
			if(!ret) {
				//
				Log.error("There is error to create/update authentication token.");
				//TODO : refresh db for authentication token later.
			}
		}else{
			Log.error("The user is invalide because the id of user is 0.");
		}
		return auToken;
	}
}
