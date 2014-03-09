package com.evento.loaders.impl;

import java.util.ArrayList;
import java.util.List;

import com.evento.bean.Event;
import com.evento.cache.CacheLocator;
import com.evento.cache.CacheSource;
import com.evento.db.DAOFactory;
import com.evento.loaders.Loader;

public class EventLoader implements Loader<Long, Event> {

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
	
	public boolean contains(Long key) {
		CacheSource cache = CacheLocator.getCache(Event.class, expires);
		return cache.get(key) != null;
	}

	public Event load(Long key) {
		CacheSource cache = CacheLocator.getCache(Event.class, expires);

		//TODO checar se esta tanto tempo no banco sem atualizacao, e se for o caso consulta o fb pra atualizar
		Event event = null;
		try {
			Object obj = cache.get(key);

			if (obj != null) {
				event =  (Event) obj;
			} else {
				event = DAOFactory.getInstance().getEventDAO().findById(key);
				if (event != null) {
					cache.put(event.getId(), event);
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

		return event;
	}
	
	public List<Event> loadList(List<Long> ids) {
		List<Event> list = new ArrayList<Event>(ids.size());
		for (Long id : ids) {
			list.add(load(id));
		}
		return list;
	}

	public void invalidate(Long key) {
		CacheSource cache = CacheLocator.getCache(Event.class, expires);
		cache.remove(key);
	}
}
