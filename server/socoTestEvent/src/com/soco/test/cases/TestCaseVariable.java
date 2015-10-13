package com.soco.test.cases;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.soco.log.Log;

public class TestCaseVariable {
	
	private Map<String, String> _string_map = new HashMap<String, String>();
	private Map<String, Long> _long_map = new HashMap<String, Long>();
	private Map<String, Float> _float_map = new HashMap<String, Float>();
	private Map<String, Integer> _int_map = new HashMap<String, Integer>();
	
	
	public void parseResponseVariables(JSONObject resp) throws JSONException{
		
		if(null == resp){
			return;
		}
		
		Iterator ite = resp.keys();
		while(ite.hasNext()){
			String key = (String) ite.next();
			if(this.getStringMap().containsKey(key)){
				Object obj = resp.get(key);
				if(obj instanceof String){
					this.getStringMap().put(key, (String) obj);
				}else if(obj instanceof Long){
					this.getLongMap().put(key, (Long) obj);
					this.getStringMap().remove(key);
				}else if(obj instanceof Float){
					this.getFloatMap().put(key, (Float) obj);
					this.getStringMap().remove(key);
				}else if(obj instanceof Integer){
					this.getIntMap().put(key, (Integer) obj);
					this.getStringMap().remove(key);
				}else {
					Log.error("Get unkown type.");
				}
				
			}else{
				if(resp.get(key).equals("?")){
					this.getStringMap().put(key, null);
				}
			}
		}
	}
	
	public JSONObject replaceVariables(JSONObject json) throws JSONException{
		
		if(null == json){
			return json;
		}
		
		Iterator ite = json.keys();
		while(ite.hasNext()){
			String key = (String) ite.next();
			if((String.valueOf(json.get(key)).equals("?"))){
				continue;
			}
			if(this.getStringMap().containsKey(key) 
			&& this.getStringMap().get(key) != null 
			&& json.getString(key).isEmpty()){
				json.put(key, this.getStringMap().get(key));
			} else if(this.getLongMap().containsKey(key) 
			&& this.getLongMap().get(key) != null 
			&& String.valueOf(json.get(key)).isEmpty()){
				json.put(key, this.getLongMap().get(key));
			} else if(this.getFloatMap().containsKey(key) 
			&& this.getFloatMap().get(key) != null 
			&& String.valueOf(json.get(key)).isEmpty() ){
				json.put(key, this.getFloatMap().get(key));
			} else if(this.getIntMap().containsKey(key) 
			&& this.getIntMap().get(key) != null 
			&& String.valueOf(json.get(key)).isEmpty()){
				json.put(key, this.getIntMap().get(key));
			} else {
				//Log.error("Not find matched map.");
			}
			
		}
		
		return json;
	}
	
	
	public String getResposneVariable(String variable){
		String value = null;
		if(this.getStringMap().containsKey(variable)){
			value = this.getStringMap().get(variable);
		}
		return value;
	}
	
	public Map<String, String> getStringMap() {
		return _string_map;
	}
	public void setStringMap(Map<String, String> _string_map) {
		this._string_map = _string_map;
	}
	public Map<String, Long> getLongMap() {
		return _long_map;
	}
	public void setLongMap(Map<String, Long> _long_map) {
		this._long_map = _long_map;
	}
	
	
	public void setKeyString(String key, String value){
		this._string_map.put(key, value);
	}
	
	public String getKeyString(String key){
		String value = "";
		value = this._string_map.get(key);
		return value;
	}
	
	public void setKeyLong(String key, Long value){
		this._long_map.put(key, value);
	}
	
	public Long getKeyLong(String key){
		Long value = 0L;
		value = this._long_map.get(key);
		return value;
	}
	public Map<String, Float> getFloatMap() {
		return _float_map;
	}
	public void setFloatMap(Map<String, Float> _float_map) {
		this._float_map = _float_map;
	}
	
	
	public String toString(){
		String str = "\n";
		for(String key: this.getStringMap().keySet()){
			String value = this.getStringMap().get(key);
			str += key + ":" + value + "\n";
		}
		for(String key: this.getLongMap().keySet()){
			Long value = this.getLongMap().get(key);
			str += key + ":" + value + "\n";
		}
		for(String key: this.getFloatMap().keySet()){
			Float value = this.getFloatMap().get(key);
			str += key + ":" + value + "\n";
		}
		for(String key: this.getIntMap().keySet()){
			Integer value = this.getIntMap().get(key);
			str += key + ":" + value + "\n";
		}
		return str;
	}


	public Map<String, Integer> getIntMap() {
		return _int_map;
	}


	public void setIntMap(Map<String, Integer> _int_map) {
		this._int_map = _int_map;
	}

}
