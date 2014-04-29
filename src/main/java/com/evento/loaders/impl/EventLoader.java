package com.evento.loaders.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evento.bean.Event;
import com.evento.cache.CacheLocator;
import com.evento.cache.CacheSource;
import com.evento.db.DAOFactory;
import com.evento.loaders.Loader;
import com.evento.loaders.impl.EventLoader.EventKey;
import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;

public class EventLoader implements Loader<EventKey, Event> {

	private static EventLoader instance = null;
	
	private static final long expires = 60 * 60;
	
	private EventLoader() {
	}
	
	public static EventLoader getInstance() {
		if (instance == null) {
			instance = new EventLoader();
		}
		return instance;
	}
	
	public boolean contains(EventKey key) {
		CacheSource cache = CacheLocator.getCache(Event.class, expires);
		return cache.get(key) != null;
	}

	public Event load(EventKey key) {
		CacheSource cache = CacheLocator.getCache(Event.class, expires);

		Event event = null;
		try {
			Object obj = cache.get(key);

			if (obj != null) {
				event =  (Event) obj;
			} else {
				//se tiver no banco com data de ultima atualizacao menor que x dias, recupera, senao vai no fb e salva novamente
				Map<String, Object> conditions = new HashMap<String, Object>();
				conditions.put("id", key.getId());
				conditions.put("date(updated_at) > DATE_ADD(date(now()), INTERVAL -1 DAY) ", null);
				List<Event> list = DAOFactory.getInstance().getEventDAO().listBy(conditions, null);
				event = list != null && !list.isEmpty() ? list.get(0) : null;
				if (event == null) {
					event = new Event(key.getAccessToken(), key.getId());
					DAOFactory.getInstance().getEventDAO().insert(event);
				} else {
					event.setAccessToken(key.getAccessToken());
				}
				
				if (event != null)
					cache.put(event.getId(), event);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (MySQLDataException e) {
			e.printStackTrace();
		}

		return event;
	}
	
	public List<Event> loadList(List<EventKey> ids) {
		List<Event> list = new ArrayList<Event>(ids.size());
		for (EventKey id : ids) {
			list.add(load(id));
		}
		return list;
	}

	public void invalidate(EventKey key) {
		CacheSource cache = CacheLocator.getCache(Event.class, expires);
		cache.remove(key);
	}
	
	public static class EventKey {
		private long eid;
		private String accessToken;
		
		public EventKey(long eid, String accessToken) {
			this.eid = eid;
			this.accessToken = accessToken;
		}
		
		public long getId() {
			return eid;
		}
		
		public String getAccessToken() {
			return accessToken;
		}
	}
}
