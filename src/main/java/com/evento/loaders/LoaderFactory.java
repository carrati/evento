package com.evento.loaders;

import com.evento.loaders.impl.EventLoader;



public class LoaderFactory {
	
	public static EventLoader getAccountLoader() {
		return EventLoader.getInstance();
	}
	
}