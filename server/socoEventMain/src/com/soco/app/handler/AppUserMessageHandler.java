package com.soco.app.handler;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.soco.db.security.AuthenticationTokenController;
import com.soco.db.user.FacebookUserController;
import com.soco.db.user.RoleController;
import com.soco.db.user.UserController;
import com.soco.db.user.UserRoleController;
import com.soco.log.Log;
import com.soco.security.AuthenticationToken;
import com.soco.security.authentication.UserAuthentication;
import com.soco.security.authentication.UserToken;
import com.soco.security.encryption.AES;
import com.soco.security.encryption.MD5;
import com.soco.user.FacebookUser;
import com.soco.user.Role;
import com.soco.user.User;
import com.soco.user.UserRole;
import com.soco.algorithm.user.UserInfor;;


public class AppUserMessageHandler implements AppMessageHandler {

	private static final String[] USER_CMD_ARRAY = { "register", "login", "logout", "social_login" };
	
	private static final String FIELD_NAME = "name";
	private static final String FIELD_EMAIL = "email";
	private static final String FIELD_PHONE = "phone";
	private static final String FIELD_PASSWORD = "password";
	private static final String FIELD_HOMETOWN = "hometown";
	private static final String FIELD_LATITUDE = "latitude";
	private static final String FIELD_LONGITUDE = "longitude";
	
	private static final String FIELD_TYPE = "type";
	private static final String FIELD_TYPE_FB = "facebook";
	private static final String FIELD_TYPE_WECHAT = "wechat";
	
	private static final String FIELD_UID = "user_id";
	private static final String FIELD_ID = "id";
	private static final String FIELD_FIRST_NAME = "first_name";
	private static final String FIELD_LAST_NAME = "last_name";
	private static final String FIELD_AGE_RANGE = "age_range";
	private static final String FIELD_LINK = "link";
	private static final String FIELD_GENDER = "gender";
	private static final String FIELD_LOCALE = "locale";
	private static final String FIELD_TIMEZONE = "timezone";
	private static final String FIELD_VERIFIED = "verified";
	
	private static final String FIELD_TOKEN = "token";
	
	
	private static ArrayList<String> _cmdList = new ArrayList<String>();
	
	private HttpResponseStatus _http_status;
	private String _http_response_content;

	@Override
	public boolean messageHandler(String version, String className, String httpMethod, String paramters, String content) {
		// TODO Auto-generated method stub
		boolean ret = false;
		Log.debug("In AppUserMessageHandler. The command " + httpMethod + " and message is: ");
		// format to json object
		try {
			this.setHttpResponseContent(content);
			this.setHttpStatus(OK);
			JSONObject jsonObj = new JSONObject(content);
			String methodName = httpMethod.toLowerCase() + "_" + className + "_" + version;
			Method method = this.getClass().getMethod(methodName, JSONObject.class, String.class);
			method.invoke(this, jsonObj, paramters);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public List<String> getCmdList() {
		// TODO Auto-generated method stub
		if(AppUserMessageHandler._cmdList != null){
			for(String cmd: AppUserMessageHandler.USER_CMD_ARRAY){
				_cmdList.add(cmd);
			}
		} else {
			Log.debug("In AppUserMessageHandler. The command list is null.");
		}
		
		return AppUserMessageHandler._cmdList;
	}
	
	/**
	 * Register by email and password
	 * @return
	 */
	public boolean post_register_v1 (JSONObject json, String param){
		boolean ret = false;
		Log.debug("In register.");
		String property = "";
		String message = "";
		int error_code = 0;
		int http_status = 400;
		//todo: thread id and area id
		long uid = UserInfor.getUID(1, 1);
		
		if(json != null){
			if(json.has(FIELD_EMAIL)){
				if(json.has(FIELD_PASSWORD)){
					try {
					    User user = new User();
						String name = "";
						String email = json.getString(FIELD_EMAIL);
						String password = json.getString(FIELD_PASSWORD);
						if(json.has(FIELD_NAME)){
						    name = json.getString(FIELD_NAME);
						} else {
                            Log.warn("There is no name.");
                        }
						String hometown = "";
						if(json.has(FIELD_HOMETOWN)){
						    hometown = json.getString(FIELD_HOMETOWN);
						} else {
                            Log.warn("There is no hometown.");
                        }
						String phone = "";
						if(json.has(FIELD_PHONE)){
							phone = json.getString(FIELD_PHONE);
						} else {
							Log.warn("There is no phone.");
						}
						Float latitude = (float) 0.0;
                        if(json.has(FIELD_LATITUDE)){
                            phone = json.getString(FIELD_LATITUDE);
                        } else {
                            Log.warn("There is no latitude.");
                        }
                        Float longitude = 0f;
                        if(json.has(FIELD_LONGITUDE)){
                            phone = json.getString(FIELD_LONGITUDE);
                        } else {
                            Log.warn("There is no longitude.");
                        }
						String encryptPassword = MD5.getMD5(password);
						////
						user.setId(uid);
						user.setUserName(name);
						user.setEmail(email);
						user.setUserEncryptPassword(encryptPassword);
						user.setUserPlainPassword(password);
						user.setMobilePhone(phone);
						user.setLatitude(latitude);
						user.setLongitude(longitude);
						user.setHometown(hometown);
						////
						UserController uc = new UserController();
						if(uc.hasByUIdOrEmail(user) != null){
							// the email existent, error
						} else {
							int rows = uc.createUser(user);
							if (rows > 0){
								/* it expired after one month */
								AuthenticationToken auToken = UserAuthentication.generateTokenForUser(user);
								// save user role
								UserRole ur = new UserRole();
								RoleController rc = new RoleController();
								Role role = new Role();
								role.setAuthority(Role.ROLE_USER);
								long rid = rc.has(role);
								if(rid >0 ){
									ur.setRoleID(rid);
								} else {
									Log.error("There is no ROLE_USER in db.");
									if(rc.createRole(role)){
										rid = rc.has(role);
										ur.setRoleID(rid);
									} else {
										Log.error("Can't create role ROLE_USER in DB.");
										///
									}
								}
								if( rid <= 0 ){
									//error
									Log.error("Can't creat the relationship between user:" + uid + " and USER_ROLE. Make a undo task.");
									////TODO: make an undo task.
								}else{
									////to create user role relationship
									ur.setUserID(uid);
									UserRoleController urc = new UserRoleController();
									if(!urc.has(ur)){
										urc.createUserRole(ur);
									} else {
										//
										Log.error("The user role relation already existent when create user. So do nothing, just keep it.");
									}
								}
								// set response
								String resp = AppResponseHandler.getRegisterSuccessResponse(200, uid, auToken.getToken());
								this.setHttpStatus(OK);
								this.setHttpResponseContent(resp);
								//send out the register code to the email
								ret = this.sendEmail(user.getEmail(), this.getEmailContent());
								if(!ret){
									//TODO: if failed, and then add to task queue to try again later
									Log.error("Send verification email failed. Server will try again later.");
									ret = true;
								}
							} else {
								// create user failed
								Log.error("Create user failed.");
								property = "";
								message = "Create user failed. Try again later.";
								error_code = 16;
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Log.error("There is no password field in request.");
					property = "password";
					message = "There is no password field in request.";
					error_code = 13;
				}
			} else {
				Log.error("There is no email field in request.");
				property = "email";
				message = "There is no email field in request.";
				error_code = 14;
			}
		} else {
			Log.error("This a invalide json request.");
			message = "This a invalide json request.";
			error_code = 10;
		}
		
		if(!ret){
			String resp = AppResponseHandler.getRegisterFailureResponse(http_status, error_code, property, message);
			this.setHttpStatus(HttpResponseStatus.BAD_REQUEST);
			this.setHttpResponseContent(resp);
		}
		
		return ret;
	}
	
	public boolean post_login_v1 (JSONObject json, String param){
		boolean ret = false;
		Log.debug("In login.");
		String property = "";
		String message = "";
		int error_code = 0;
		if(json != null){
			if(json.has(FIELD_EMAIL)){
				if(json.has(FIELD_PASSWORD)){
					try {
						String email = json.getString(FIELD_EMAIL);
						String password = json.getString(FIELD_PASSWORD);
					    User user = new User();
					    user.setEmail(email);
					    user.setUserPlainPassword(password);
					    if(UserAuthentication.authentication(user)){
					    	// email and password are correct
				    		// generate token for user
							AuthenticationToken auToken = UserAuthentication.generateTokenForUser(user);
							// set response
							String resp = AppResponseHandler.getLoginSuccessResponse(200, user.getId(), auToken.getToken(), user.getIsValidated().toString());
							this.setHttpStatus(OK);
							this.setHttpResponseContent(resp);
							ret = true;
					    } else {
				    		// password wrong
				    		Log.warn("Email or password is wrong.");
				    		property = "Email or password";
				    		message = "Email or password is wrong.";
				    		error_code = 11;
				    	} 
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Log.error("There is no password field in request.");
					property = "password";
					message = "There is no password field in request";
					error_code = 13;
				}
			} else {
				Log.error("There is no email field in request.");
				property = "email";
				message = "There is no email field in request.";
				error_code = 14;
			}
		} else {
			Log.error("This a invalide json request.");
			message = "This a invalide json request.";
			error_code = 10;
		}
		if(!ret){
			
			String resp = AppResponseHandler.getRegisterFailureResponse(400, error_code, property, message);
			this.setHttpStatus(HttpResponseStatus.BAD_REQUEST);
			this.setHttpResponseContent(resp);
		}
		
		return ret;
	}
	
	public boolean post_social_login_v1 (JSONObject json, String param){
		boolean ret = false;
		Log.debug("In social login to create social user.");
		
		this.socialLoginHandler(json, param, false);
		
		return ret;
	}
	
	public boolean put_social_login_v1 (JSONObject json, String param){
		boolean ret = false;
		Log.debug("In social login to update social user.");
		
		this.socialLoginHandler(json, param, true);
		
		return ret;
	}
	
	public boolean post_logout_v1 (JSONObject json, String param){
		boolean ret = false;
		Log.debug("In logout.");
		String property = "";
		String message = "";
		int error_code = 0;
		if(json != null){
			if(json.has(FIELD_UID)){
				if(json.has(FIELD_TOKEN)){
					try {
						long uid = json.getLong(FIELD_UID);
						String token = json.getString(FIELD_TOKEN);
					    User user = new User();
					    user.setId(uid);
					   
			    		AuthenticationTokenController atc = new AuthenticationTokenController();
			    		AuthenticationToken auToken = new AuthenticationToken();
			    		auToken.setUId(uid);
						auToken = atc.hasTokenByUId(auToken);
						if(auToken != null && auToken.getToken().equals(token)){
							ret = atc.deleteAuthenticationToken(auToken);
							// set OK response
							this.setHttpStatus(OK);
							this.setHttpResponseContent("{\"status\":200}");
						} else {
							// token error
							Log.warn("The token : " + token + " is not match for user id: " + uid);
					    	property = "token";
					    	message = "The token is incorrect.";
					    	error_code = 12;
						}
					 
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					Log.error("There is no token field in request.");
					property = "token";
					message = "There is no token field in request";
					error_code = 19;
				}
			} else {
				Log.error("There is no user id field in request.");
				property = "user_id";
				message = "There is no user id field in request.";
				error_code = 18;
			}
		} else {
			Log.error("This a invalide json request.");
			message = "This a invalide json request.";
			error_code = 10;
		}
		if(!ret){
			
			String resp = AppResponseHandler.getRegisterFailureResponse(400, error_code, property, message);
			this.setHttpStatus(HttpResponseStatus.BAD_REQUEST);
			this.setHttpResponseContent(resp);
		}
		
		return ret;
	}
	
	public void post_users_v1 (JSONObject json, String param) {
		
	}
	
	
	private boolean socialLoginHandler(JSONObject json, String param, boolean update){
		boolean ret = false;
		int httpStatus = 400;
		int error_code = 0;
		String property = "";
		String message = "";
		try {
			if (json.has(FIELD_TYPE)){
				String type;
				type = json.getString(FIELD_TYPE);
				
				if(type.equals(FIELD_TYPE_FB)){
					if(json.has(FIELD_ID)){
						long id = json.getLong(FIELD_ID);
						FacebookUser fbUser = this.parseFacebookUserFromJson(json);
						FacebookUserController fbuc = new FacebookUserController();
						FacebookUser fbUserFromDB = fbuc.hasById(id);
						if(update){
							////PUT Method
							if(null != fbUserFromDB){
								httpStatus = 400;
								error_code = 11;
								property = "id";
								message = "The user is not existent.";
							} else {
								fbUser.setUid(fbuc.getUId(id));
								ret = fbuc.updateFBUser(fbUser);
								if(ret){
									String resp = AppResponseHandler.getSocialLoginSuccessResponse(200, fbUser.getUid(), "");
									this.setHttpStatus(OK);
									this.setHttpResponseContent(resp);
								}
							}
						} else {
							////POST Method
							if(null != fbUserFromDB){
								////existent, and then log in
								ret = true;
								fbUser = fbUserFromDB;
							} else {
								fbUser.setUid(UserInfor.getUID(1, 1));
								ret = fbuc.createFBUser(fbUser);
							}
							if(ret){
								////generate a token for user
								// generate token for user
								User user = new User();
								user.setId(fbUser.getUid());
								AuthenticationToken auToken = UserAuthentication.generateTokenForUser(user);
								if(null != auToken){
									String token = auToken.getToken();
									String resp = AppResponseHandler.getSocialLoginSuccessResponse(200, fbUser.getUid(), token);
									this.setHttpStatus(OK);
									this.setHttpResponseContent(resp);
								} else {
									Log.error("Save token failed.");
									ret = false;
									property = "token";
									message = "Can't generate the token.";
									error_code = 17;
								}
							}
						}
					} else {
						Log.debug("There is no id field in json.");
						message = "This a invalide json request.";
						error_code = 10;
					}
				} else if(type.equals(FIELD_TYPE_WECHAT)){
					
				} else {
					Log.warn("The type value is incorrect.");
					property = "type";
					message = "The type value is incorrect.";
					error_code = 20;
				}
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(!ret){
			String resp = AppResponseHandler.getSocialLoginFailureResponse(httpStatus, error_code, property, message);
			this.setHttpStatus(HttpResponseStatus.valueOf(httpStatus));
			this.setHttpResponseContent(resp);
		}
		
		return ret;
	}
	
	
	private FacebookUser parseFacebookUserFromJson(JSONObject json){
		FacebookUser fbUser = new FacebookUser();
		try {
			if(json.has(FIELD_ID)){
				if(json.has(FIELD_NAME)){
					fbUser.setId(json.getLong(FIELD_ID));
					fbUser.setName(json.getString(FIELD_NAME));
					if(json.has(FIELD_EMAIL)) fbUser.setEmail(json.getString(FIELD_EMAIL));
					if(json.has(FIELD_FIRST_NAME)) fbUser.setFirstName(json.getString(FIELD_FIRST_NAME));
					if(json.has(FIELD_LAST_NAME)) fbUser.setLastName(json.getString(FIELD_LAST_NAME));
					if(json.has(FIELD_AGE_RANGE)) fbUser.setAgeRange(json.getString(FIELD_AGE_RANGE));
					if(json.has(FIELD_LINK)) fbUser.setLink(json.getString(FIELD_LINK));
					if(json.has(FIELD_GENDER)) fbUser.setGender(json.getInt(FIELD_GENDER));
					if(json.has(FIELD_LOCALE)) fbUser.setLocale(json.getString(FIELD_LOCALE));
					if(json.has(FIELD_TIMEZONE)) fbUser.setTimezone(json.getDouble(FIELD_TIMEZONE));
					if(json.has(FIELD_VERIFIED)) fbUser.setVerified(json.getBoolean(FIELD_VERIFIED));
				} else {
					
				}
			} else {
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fbUser;
	}
	
	private boolean sendEmail(String email, String content) {
		boolean ret = false;
		return ret;
	}
	
	private String getEmailContent(){
		String content = null;
		return content;
	}

	@Override
	public FullHttpResponse getResponse() {
		// TODO Auto-generated method stub
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, this.getHttpStatus(), Unpooled.wrappedBuffer(this.getHttpResponseContent().getBytes()));
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        response.headers().set(CONNECTION, Values.KEEP_ALIVE);
		return response;
	}

	public HttpResponseStatus getHttpStatus() {
		return _http_status;
	}

	public void setHttpStatus(HttpResponseStatus _http_status) {
		this._http_status = _http_status;
	}

	public String getHttpResponseContent() {
		return _http_response_content;
	}

	public void setHttpResponseContent(String _http_response_content) {
		this._http_response_content = _http_response_content;
	}

}
