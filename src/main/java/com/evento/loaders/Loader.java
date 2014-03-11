package com.evento.loaders;

import java.util.List;

public interface Loader<K, O> {

	public boolean contains(K key);

	public O load(K key);
	
	public List<O> loadList(List<K> ids);
	
	public void invalidate(K key); 
	
}