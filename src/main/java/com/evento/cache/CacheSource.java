package com.evento.cache;


public abstract class CacheSource {
	
	protected static final long DEFAULT_TIME_TO_LIVE_IN_SECONDS = 30 * 60;

	protected long ttl;
	protected String nameSpace;
	
	public long getTimeToLive() {
		return ttl;
	}

	public void setTimeToLive(long ttl) {
		this.ttl = ttl;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public CacheSource() {
	}
	
	protected abstract void configure(String nameSpace, long ttlSeconds) throws Exception;
	
	public abstract void put(Object key, Object o);
	
	public abstract Object get(Object key);

	public abstract void remove(Object key);
	
	public abstract boolean isAvailable();
	
}