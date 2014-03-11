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
			+ "attending.fields(first_name,gender,interested_in,id,last_name),invited.fields(first_name,gender,id,last_name)&limit=5000";
	
	private String id; 
	private FacebookConnect fb;
	
	public EventHandler(String id, String accessToken) {
		this.id = id;
		fb = new FacebookConnect(accessToken);
	}
	
	@SuppressWarnings("unchecked")
	public Event getEvent(boolean full) {
		Map<String, Object> result = fb.get(id, EVENT_PARAMS);
		
		Event event = new Event();
		if (result.containsKey("cover")) {
			event.setCover( (String)((Map<String, Object>)result.get("cover")).get("source") );
		}
		event.setDescription((String)result.get("description"));
		
		String endTime = (String)result.get("end_time");
		if (endTime != null) {
			event.setEndTime(DateUtils.parseDate(endTime.replace("T", " "), "yyyy-MM-dd hh:mm:ss"));
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
		
		getUsers((Map<String, Object>)result.get(EventUser.STATUS_INVITED), EventUser.STATUS_INVITED, event.getId(), full);
		getUsers((Map<String, Object>)result.get(EventUser.STATUS_ATTENDING), EventUser.STATUS_ATTENDING, event.getId(), full);
		
		return event;
	}
	
	@SuppressWarnings("unchecked")
	private void getUsers(Map<String, Object> result, String status, long eventId, boolean full) {
		Map<String, Object> users = (Map<String, Object>)result.get("data");
		if (users != null) {
			List<User> invitedUsers = new ArrayList<User>();
			List<EventUser> invitedEventUsers = new ArrayList<EventUser>();
			
			for (Map.Entry<String, Object> entry : users.entrySet()) {
				Map<String, Object> userMap = (Map<String, Object>)entry.getValue();
				
				User user = new User();
				user.setFirstName((String)userMap.get("first_name"));
				user.setGender((String)userMap.get("gender"));
				user.setId(Long.parseLong((String)userMap.get("id")));
				user.setLastName((String)userMap.get("last_name"));
				
				invitedUsers.add(user);
				
				EventUser eventUser = new EventUser();
				eventUser.setEventId(eventId);
				eventUser.setUserId(user.getId());
				eventUser.setStatus(status);
				
				invitedEventUsers.add(eventUser);
			}
			
			DAOFactory.getInstance().getUserDAO().insert(invitedUsers);
			DAOFactory.getInstance().getEventUserDAO().insert(invitedEventUsers);
		}
		
		Map<String, Object> paging = (Map<String, Object>)((Map<String, Object>)result).get("paging");//paginacao
		if (full && paging != null && paging.containsKey("next")) {
			String next = (String)paging.get("next");
			
			Map<String, Object> pagination = fb.get(next);
			if (pagination != null) {
				getUsers(pagination, status, eventId, full);
			}
			
		}
	}
	
	public static void main(String[] args) {
		String accessToken = FacebookConnect.ACCESS_TOKEN;
		EventHandler h = new EventHandler("663473610379144", accessToken);
		h.getEvent(true);
	}
}