package com.evento.facebook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.evento.bean.Event;
import com.evento.bean.EventUser;
import com.evento.bean.User;
import com.evento.db.DAOFactory;
import com.evento.utils.DateUtils;

public class EventHandler {
	
	//pegar de cara so que vier, depois o processo em background fazer paginacao e pegar todo mundo

	private static final String EVENT_PARAMS = "fields=cover,description,end_time,id,location,name,start_time,privacy,owner,"
			+ "attending.fields(first_name,gender,interested_in,id,last_name),invited.fields(first_name,gender,id,last_name)";
	
	@SuppressWarnings("unchecked")
	public void getEvent(String id, String accessToken) {
		FacebookConnect fb = new FacebookConnect(accessToken);
		Map<String, Object> result = fb.get(id, EVENT_PARAMS);
//		for (Map.Entry<String, Object> entry : result.entrySet()) {
//			System.out.println(entry.getKey() + " = ");
//			System.out.println(entry.getValue());
//			System.out.println("-----------------------------------------------");
//		}
		
		Event event = new Event();
		event.setCover( (String)((Map<String, Object>)result.get("cover")).get("source") );
		event.setDescription((String)result.get("description"));
		
		String endTime = (String)result.get("end_time");
		if (endTime != null) {
			event.setEndTime(DateUtils.parseDate(endTime, "yyyy-MM-ddThh:mm:ss"));
		}
		event.setId(Long.parseLong((String)result.get("id")));
		event.setLocation((String)result.get("location"));
		event.setName((String)result.get("name"));
		event.setOwner(Long.parseLong((String)((Map<String, Object>)result.get("owner")).get("id")));
		event.setPrivacy((String)result.get("privacy"));
		
		String startTime = (String)result.get("start_time");
		if (startTime != null) {
			event.setStartTime(DateUtils.parseDate(startTime.replace("T", " "), "yyyy-MM-dd hh:mm:ss"));
		}
		DAOFactory.getInstance().getEventDAO().insert(event);
		
		Map<String, Object> invited = result.get("invited") != null ? (Map<String, Object>)((Map<String, Object>)result.get("invited")).get("data") : null;
		if (invited != null) {
			List<User> invitedUsers = new ArrayList<User>();
			List<EventUser> invitedEventUsers = new ArrayList<EventUser>();
			
			for (Map.Entry<String, Object> entry : invited.entrySet()) {
				Map<String, Object> userMap = (Map<String, Object>)entry.getValue();
				
				User user = new User();
				user.setFirstName((String)userMap.get("first_name"));
				user.setGender((String)userMap.get("gender"));
				user.setId(Long.parseLong((String)userMap.get("id")));
				user.setLastName((String)userMap.get("last_name"));
				
				invitedUsers.add(user);
				
				EventUser eventUser = new EventUser();
				eventUser.setEventId(event.getId());
				eventUser.setUserId(user.getId());
				eventUser.setStatus("invited");
				
				invitedEventUsers.add(eventUser);
			}
			
			DAOFactory.getInstance().getUserDAO().insert(invitedUsers);
			DAOFactory.getInstance().getEventUserDAO().insert(invitedEventUsers);
		}
		
		result.get("paging.next");//paginacao
	}
	
	public static void main(String[] args) {
		EventHandler h = new EventHandler();
		String accessToken = FacebookConnect.ACCESS_TOKEN;
		h.getEvent("407054549409428", accessToken);
	}
}