package com.soco.algorithm.user;

import java.util.Date;

public class UserInfor {
	
	private static final long UID_UNIT = 100000000000000L;
	
	public static long getUID(long thread_id, long area_id){
		long uid = 0l;
		long userTag = 1;
		long millionsecond = (new Date()).getTime();
		uid = millionsecond + ( userTag * 10000 + thread_id * 1000 + area_id ) * UID_UNIT;
		
		return uid;
	}

}
