package com.evento.cache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaCacheSource extends CacheSource {

	private Cache<Object, Object> cache;

	@SuppressWarnings("rawtypes")
	public GuavaCacheSource(Class nameSpace) throws Exception {
		configure(nameSpace.getName(), 0);
	}

	public GuavaCacheSource(String nameSpace, long ttlSeconds) throws Exception {
		configure(nameSpace, ttlSeconds);
	}

	public void configure(String nameSpace, long ttlSeconds) throws Exception {
		this.setNameSpace(nameSpace);
		this.setTimeToLive(ttlSeconds);
		if (ttlSeconds == 0) {
			ttlSeconds = CacheSource.DEFAULT_TIME_TO_LIVE_IN_SECONDS;
		}
		cache = CacheBuilder.newBuilder().expireAfterWrite(ttlSeconds, TimeUnit.SECONDS).build();
	}

	public Object get(Object key) {
		Object element = cache.getIfPresent(key);
		if (element != null)
			return element;
		return null;
	}

	public void put(Object key, Object o) {
		cache.put(key, o);
	}

	public void remove(Object key) {
		if (get(key) != null)
			cache.invalidate(key);
	}

	public boolean isAvailable() {
		return cache != null;
	}
}