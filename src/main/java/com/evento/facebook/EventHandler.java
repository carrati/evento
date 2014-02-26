package com.evento.facebook;

import java.util.Map;

public class EventHandler {
	
	//pegar de cara so que vier, depois o processo em background fazer paginacao e pegar todo mundo

	private static final String EVENT_PARAMS = "fields=cover,description,end_time,id,location,name,start_time,privacy,owner,"
			+ "attending.fields(first_name,gender,interested_in,id,last_name),invited.fields(first_name,gender,id,last_name)";
	
	public void getEvent(String id, String accessToken) {
		FacebookConnect fb = new FacebookConnect(accessToken);
		Map<String, Object> profile = fb.get(id, EVENT_PARAMS);
		for (Map.Entry<String, Object> entry : profile.entrySet()) {
			System.out.println(entry.getKey() + " = ");
			System.out.println(entry.getValue());
			System.out.println("-----------------------------------------------");
		}
	}
	
	public static void main(String[] args) {
		EventHandler h = new EventHandler();
		String accessToken = FacebookConnect.ACCESS_TOKEN;
		h.getEvent("732202180124415", accessToken);
	}
}