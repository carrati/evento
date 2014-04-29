package com.evento.db.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DBResult extends LinkedHashMap<String,Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5383485844602400032L;
	
//	Map<String,Object> result;
//	
//	public DBResult() {
//		result = new LinkedHashMap<String, Object>();
//	}
	
	public Integer getInt(String key) {
		return getElement(key);
	}
	
	public Long getLong(String key){
		return getElement(key);
	}
	
	public String getString(String key) {
		return getElement(key);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getElement(final String key) {
		return (T) this.get(key);
	}
	
	public Set<String> getFields() {
		return this.keySet();
	}
	
	public Map<String, Object> toMap() {
		return new LinkedHashMap<String,Object>(this);
	}
}
