package com.soco.app.exchange;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;

import java.util.HashMap;
import java.util.Map;

import com.soco.app.handler.AppMessageHandler;
import com.soco.app.handler.AppUserMessageHandler;
import com.soco.log.Log;

public class AppMessageDispatch {
	
	private static Map<String, AppMessageHandler> _map;

	
	public static void initHandler(){
		/* User handler */
		AppUserMessageHandler userMsgHandler = new AppUserMessageHandler();
		AppMessageDispatch.addHandler(userMsgHandler);
	}
	
	public static boolean addHandler(AppMessageHandler handler){
		boolean ret = true;
		try{
			if(AppMessageDispatch._map == null){
				AppMessageDispatch._map = new HashMap<String, AppMessageHandler>();
			}
			
			if(AppMessageDispatch._map != null){
				int size = handler.getCmdList().size();
				for(int i = 0; i < size; i++){
					String msgCmd = handler.getCmdList().get(i);
					if(!AppMessageDispatch._map.containsKey(msgCmd)){
						AppMessageDispatch._map.put(msgCmd, handler);
						Log.debug("Add command: " + msgCmd);
					} else {
						Log.debug("The command " + msgCmd + " is existent.");
					}
				}
				
			} else {
				Log.debug("The map is null !" );
			}
		} catch(Exception e){
			Log.error(e.getMessage());
			ret = false;
		}
		return ret;
	}
	
	public static void removeHandler(String key){
		try{
			if(AppMessageDispatch._map != null && AppMessageDispatch._map.containsKey(key)){
				AppMessageDispatch._map.remove(key);
				Log.debug("Remove command: " + key);
			} else {
				Log.debug("The map is null or it not contain "+key+" .");
			}
		}catch(Exception e){
			Log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static AppMessageHandler startDispatch(FullHttpRequest req){
		AppMessageHandler msgHandler = null;
		try {
			String method = req.getMethod().name();
			String url = req.getUri();
			String content = req.content().toString(CharsetUtil.UTF_8);
			
			UrlObject uObj = new AppMessageDispatch.UrlObject(url);
			//JSONObject jsonObj = new JSONObject(content);
			
			if(!uObj.get_class().isEmpty()){
				//String cmd = jsonObj.getString("cmd");
				//Object value = jsonObj.get("value");
				
				if(AppMessageDispatch._map.containsKey(uObj.get_class())){
					msgHandler = AppMessageDispatch._map.get(uObj.get_class());
					if( null != msgHandler){
						msgHandler.messageHandler(uObj.get_version(), uObj.get_class(), method, uObj.get_paramters(), content);
					} else {
						Log.error("The handler of " + uObj.get_class() + " is null!");
					} 
				} else {
					Log.error("There is no handler for " + uObj.get_class());
				}
			} else {
				Log.error("There are no cmd or value key in the received json object.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgHandler;
	}
	
	private static class UrlObject {
		private String _version;
		private String _class;
		private String _paramters;
		
		public UrlObject(String url) {
			this.parse(url);
		}
		
		private void parse(String url){
			Log.debug("In parser of UrlObject.");
			String[] listParameters = url.split("/");
			int index = 0;
			while(index < listParameters.length && listParameters[index].equals("")) index++;
			
			if(index < listParameters.length && !listParameters[index].equals("")) 
				this.set_version(listParameters[index++]);
			
			if(index < listParameters.length && !listParameters[index].equals("")) 
				this.set_class(listParameters[index++]);
			
			if(index < listParameters.length && !listParameters[index].equals("")) 
				this.set_paramters(listParameters[index++]);
			
			for(int i = 0; i < listParameters.length; i++){
				Log.debug(listParameters[i]);
			}
			
		}
		
		
		public String get_version() {
			return _version;
		}
		public void set_version(String _version) {
			this._version = _version;
		}
		
		
		public String get_class() {
			return _class;
		}

		public void set_class(String _class) {
			this._class = _class;
		}

		public String get_paramters() {
			return _paramters;
		}

		public void set_paramters(String _paramters) {
			this._paramters = _paramters;
		}
		
		
	}
	

}
