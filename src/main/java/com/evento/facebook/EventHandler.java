package com.evento.facebook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.evento.bean.Event;
import com.evento.bean.EventUser;
import com.evento.bean.User;
import com.evento.db.DAOFactory;
import com.evento.loaders.LoaderFactory;
import com.evento.loaders.impl.EventLoader.EventKey;
import com.evento.utils.DateUtils;

public class EventHandler {
	
	private long id; 
	private FacebookConnect fb;
	
	public EventHandler(String id, String accessToken) {
		this.id = Long.parseLong(id);
		fb = new FacebookConnect(accessToken);
	}
	
	public Event getEvent(boolean full) {
		EventKey key = new EventKey(id, fb.getAccessToken());
		Event event = LoaderFactory.getAccountLoader().load(key);
		
		long attendingCount = event.getAttendingCount();
		long notRepliedCount = event.getNotRepliedCount();
		long unsureCount = event.getUnsureCount();
		
//		for (int i = 0; i < count; i+=1000) {
//			Map<String, Object> users = event.getUsers(i);
//			System.out.println(users.size());
//			
//			getUsers(users, EventUser.STATUS_INVITED, event.getId(), full);
//			getUsers(users, EventUser.STATUS_ATTENDING, event.getId(), full);
//		} 
		
		getUsers(EventUser.STATUS_ATTENDING, attendingCount, event, full);
		getUsers(EventUser.STATUS_NOT_REPLIED, notRepliedCount, event, full);
		getUsers(EventUser.STATUS_UNSURE, unsureCount, event, full);
		
		return event;
	}
	
	private void getUsers(String status, long count, Event event, boolean full) {
		for (int i = 0; i < count; i+=1000) {
			Map<String, Object> users = event.getUsers(i, status);
			System.out.println(users.size());
			
			getUsers(users, status, event.getId(), full);
		} 
	}
	
	@SuppressWarnings("unchecked")
	private void getUsers(Map<String, Object> users, String status, long eventId, boolean full) {
//		Map<String, Object> users = (Map<String, Object>)result.get("data");
		if (users != null) {
			List<User> invitedUsers = new ArrayList<User>();
			List<EventUser> invitedEventUsers = new ArrayList<EventUser>();
			
			for (Map.Entry<String, Object> entry : users.entrySet()) {
				Map<String, Object> userMap = (Map<String, Object>)entry.getValue();
				
				User user = new User();
				user.setFirstName((String)userMap.get("first_name"));
				user.setGender((String)userMap.get("sex"));
				user.setId((Long)userMap.get("uid"));
				user.setLastName((String)userMap.get("last_name"));
				user.setName((String)userMap.get("name"));
				if (userMap.get("birthday") != null) {
					user.setBirthday(DateUtils.parseDate((String)userMap.get("birthday"), "MM/dd/yyyy"));
				}
				user.setEmail((String)userMap.get("email"));
				user.setUsername((String)userMap.get("username"));
				user.setLink((String)userMap.get("profile_url"));
				
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
		
//		Map<String, Object> paging = (Map<String, Object>)((Map<String, Object>)result).get("paging");//paginacao
//		if (full && paging != null && paging.containsKey("next")) {
//			String next = (String)paging.get("next");
//			
//			Map<String, Object> pagination = fb.get(next);
//			if (pagination != null) {
//				getUsers(pagination, status, eventId, full);
//			}
//			
//		}
	}
	
	public static void main(String[] args) {
		String accessToken = FacebookConnect.ACCESS_TOKEN;
		EventHandler h = new EventHandler("663473610379144", accessToken);
		h.getEvent(true);
	}
}