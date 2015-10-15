package com.soco.algorithm.event;

import java.util.Date;

public class EventInfor {
	
	private static final long EID_UNIT = 100000000000000L;
	
	public static long getEID(long thread_id, long area_id){
		long eid = 0L;
		long eventTag = 2;
		long millionsecond = (new Date()).getTime();
		eid = millionsecond + ( eventTag * 10000 + thread_id * 1000 + area_id ) * EID_UNIT;
		
		return eid;
	}

}
