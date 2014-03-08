package com.evento.facebook;

import java.util.Map;

import com.evento.bean.Event;

public class EventHandler {
	
	//pegar de cara so que vier, depois o processo em background fazer paginacao e pegar todo mundo

	private static final String EVENT_PARAMS = "fields=cover,description,end_time,id,location,name,start_time,privacy,owner,"
			+ "attending.fields(first_name,gender,interested_in,id,last_name),invited.fields(first_name,gender,id,last_name)";
	
	@SuppressWarnings("unchecked")
	public void getEvent(String id, String accessToken) {
		FacebookConnect fb = new FacebookConnect(accessToken);
		Map<String, Object> result = fb.get(id, EVENT_PARAMS);
		for (Map.Entry<String, Object> entry : result.entrySet()) {
			System.out.println(entry.getKey() + " = ");
			System.out.println(entry.getValue());
			System.out.println("-----------------------------------------------");
		}
		
		Event event = new Event();
		event.setCover( (String)((Map<String, Object>)result.get("cover")).get("source") );
		event.setDescription((String)result.get("description"));
//		event.setEndTime(endTime);
		event.setId(Long.parseLong((String)result.get("id")));
		event.setLocation((String)result.get("location"));
		event.setName((String)result.get("name"));
		event.setOwner(Long.parseLong((String)((Map<String, Object>)result.get("owner")).get("id")));
		event.setPrivacy((String)result.get("privacy"));
//		event.setStartTime(startTime);
	}
	
	public static void main(String[] args) {
		EventHandler h = new EventHandler();
		String accessToken = FacebookConnect.ACCESS_TOKEN;
		h.getEvent("407054549409428", accessToken);
	}
}