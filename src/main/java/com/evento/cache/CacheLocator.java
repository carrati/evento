package com.evento.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheLocator {
	
	private static Map<String, CacheSource> map = new HashMap<String, CacheSource>();
	
	@SuppressWarnings("rawtypes")
	public static CacheSource getCache(Class nameSpace, long ttlSeconds) {
		return getCache(nameSpace.getName(), ttlSeconds);
	}
	
	public static CacheSource getCache(String nameSpace, long ttlSeconds) {

		CacheSource cache = null;

		try {
			cache = map.get(nameSpace);

			// apenas ehcached
			if (cache == null || (cache != null && !cache.isAvailable())) {
				cache = new GuavaCacheSource(nameSpace, ttlSeconds);
				
				map.put(nameSpace, cache);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cache;
	}
}