package com.evento.db.interfaces;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface BasicDAO<T> {
	
	public void insert(T t);
	
	public void insert(List<T> list);
	
	public void update(T t);
	
	public List<T> listAll();
	
	public List<T> listBy(Map<String, Object> conditions, LinkedHashMap<String, String> orders);
	
	public List<T> listBy(String query, Map<String, Object> conditions, LinkedHashMap<String, String> orders);
	
	public T findById(long id);
	
	public T findBy(Map<String, Object> values);
	
}