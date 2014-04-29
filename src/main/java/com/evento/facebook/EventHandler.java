package com.evento.facebook;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.evento.bean.Event;
import com.evento.bean.EventUser;
import com.evento.bean.User;
import com.evento.db.DAOFactory;
import com.evento.db.utils.DBResult;
import com.evento.db.utils.MySQLCommands;
import com.evento.loaders.LoaderFactory;
import com.evento.loaders.impl.EventLoader.EventKey;
import com.evento.utils.DateUtils;
import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;

public class EventHandler {
	
	private static final String SELECT_INVITED_COUNT = "select status, count(*) cnt from event_users e where event_id = ? group by status";
	
	//usuarios de um evento
//	select u.*, e.status
//	from user u, event_users e
//	where u.id = e.user_id
//	and e.event_id = 663473610379144
//	order by field (status, 'attending', 'not_replied', 'unsure', 'declined');
	
	
	//% de homens e mulheres
//	select sum(t) total, sum(female) female, sum(male) male, (sum(female)*100)/sum(t) p_female, (sum(male)*100)/sum(t) p_male from (
//	select 1 t, IF(gender='female', 1, 0) female, IF(gender='male', 1, 0) male
//	from user u, event_users e
//	where u.id = e.user_id
//	and e.event_id = 663473610379144
//	) t
	
	private long id; 
	private FacebookConnect fb;
	
	public EventHandler(String id, String accessToken) {
		this.id = Long.parseLong(id);
		fb = new FacebookConnect(accessToken);
	}
	
	public Event getEvent() {
		EventKey key = new EventKey(id, fb.getAccessToken());
		Event event = LoaderFactory.getAccountLoader().load(key);
		
		long attendingCount = event.getAttendingCount();
		long notRepliedCount = event.getNotRepliedCount();
		long unsureCount = event.getUnsureCount();
		//esses counts no banco e com o fb estao dando diferenca
		
		try {
			List<DBResult> results = new MySQLCommands().setQuery(SELECT_INVITED_COUNT.replace("?", Long.toString(id))).list().getResults();
			
			boolean save = false;
			for (DBResult rs : results) {
				String status = rs.getString("status");
				long cnt = rs.getLong("cnt");
				
				//melhorar essa logica e talvez mudar o nome pra um evento result...
				if (status.equals(EventUser.STATUS_ATTENDING) && attendingCount != cnt) {
					save = true;
					break;
				} else if (status.equals(EventUser.STATUS_NOT_REPLIED) && notRepliedCount != cnt) {
					save = true;
					break;
				} else if (status.equals(EventUser.STATUS_UNSURE) && unsureCount != cnt) {
					save = true;
					break;
				}
			}
			
			if (save) {
				getUsersFromFacebook(EventUser.STATUS_ATTENDING, attendingCount, event);
				getUsersFromFacebook(EventUser.STATUS_NOT_REPLIED, notRepliedCount, event);
				getUsersFromFacebook(EventUser.STATUS_UNSURE, unsureCount, event);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return event;
	}
	
	private void getUsersFromFacebook(String status, long count, Event event) {
		for (int i = 0; i < count; i+=1000) {
			Map<String, Object> users = event.getUsers(i, status);
			System.out.println(users.size());
			
			getUsers(users, status, event.getId());
		} 
	}
	
	@SuppressWarnings("unchecked")
	private void getUsers(Map<String, Object> users, String status, long eventId) {
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
			
			try {
				DAOFactory.getInstance().getUserDAO().insert(invitedUsers);
				DAOFactory.getInstance().getEventUserDAO().insert(invitedEventUsers);
			} catch (MySQLDataException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		String accessToken = FacebookConnect.ACCESS_TOKEN;
		EventHandler h = new EventHandler("663473610379144", accessToken);
		h.getEvent();
	}
}