package com.evento.db.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.evento.bean.Event;
import com.evento.db.ConnectionManager;
import com.evento.db.interfaces.EventDAO;

public class MysqlEventDAO implements EventDAO {
	
	private static MysqlEventDAO instance;

	private static String SELECT_STATEMENT = "select id, name, owner, description, pic_cover, location, privacy, start_time, end_time, "
			+ "pic_big, venue_latitude, venue_longitude, venue_city, venue_state, venue_country, venue_id, venue_street, venue_zip, all_members_count, "
			+ "attending_count, not_replied_count, declined_count, unsure_count from event ";
	
	private static String INSERT_STATEMENT = "insert into event (id, name, owner, description, pic_cover, location, privacy, start_time, end_time, "
			+ "pic_big, venue_latitude, venue_longitude, venue_city, venue_state, venue_country, venue_id, venue_street, venue_zip, all_members_count, "
			+ "attending_count, not_replied_count, declined_count, unsure_count, created_at, updated_at) " +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(),now() ) "
			+ "ON DUPLICATE KEY UPDATE name=values(name), owner=values(owner), description=values(description), pic_cover=values(pic_cover), location=values(location), privacy=values(privacy), start_time=values(start_time),	"
			+ "end_time=values(end_time), pic_big=values(pic_big), venue_latitude=values(venue_latitude), venue_longitude=values(venue_longitude), venue_city=values(venue_city), "
			+ "venue_state=values(venue_state), venue_country=values(venue_country), venue_id=values(venue_id), venue_street=values(venue_street), venue_zip=values(venue_zip), all_members_count=values(all_members_count), "
			+ "attending_count=values(attending_count), not_replied_count=values(not_replied_count), declined_count=values(declined_count), unsure_count=values(unsure_count), updated_at=now()";
	
			
	private MysqlEventDAO() {
	}
	
	public static MysqlEventDAO getInstance() {
		if (instance == null) {
			instance = new MysqlEventDAO();
		}
		
		return instance;
	}

	public Event findById(long id) {
		return findBy(Collections.<String, Object>singletonMap("id", id));
	}
	
	public void insert(Event t) {
		List<Event> list = new ArrayList<Event>();
		list.add(t);
		insert(list);
	}

	public void insert(List<Event> list) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(true);

			pstmt = conn.prepareStatement(INSERT_STATEMENT);
			
			for (int i = 0; list != null && i < list.size(); i++) {
				Event t = list.get(i);
				
				int pos = 0;
				
				pstmt.setLong(++pos, t.getId());
				pstmt.setString(++pos, t.getName());
				pstmt.setLong(++pos, t.getOwner());
				pstmt.setString(++pos, t.getDescription());
				pstmt.setString(++pos, t.getPicCover());
				pstmt.setString(++pos, t.getLocation());
				pstmt.setString(++pos, t.getPrivacy());
				if (t.getStartTime() != null) {
					pstmt.setTimestamp(++pos, new Timestamp(((Date)  t.getStartTime()).getTime()));
				} else {
					pstmt.setNull(++pos, Types.NULL);
				}
				if (t.getEndTime() != null) {
					pstmt.setTimestamp(++pos, new Timestamp(((Date)  t.getEndTime()).getTime()));
				} else {
					pstmt.setNull(++pos, Types.NULL);
				}
				pstmt.setString(++pos, t.getPicBig());
				pstmt.setDouble(++pos, t.getVenueLatitude());
				pstmt.setDouble(++pos, t.getVenueLongitude());
				pstmt.setString(++pos, t.getVenueCity());
				pstmt.setString(++pos, t.getVenueState());
				pstmt.setString(++pos, t.getVenueCountry());
				pstmt.setLong(++pos, t.getVenueId());
				pstmt.setString(++pos, t.getVenueStreet());
				pstmt.setString(++pos, t.getVenueZip());
				pstmt.setLong(++pos, t.getAllMembersCount());
				pstmt.setLong(++pos, t.getAttendingCount());
				pstmt.setLong(++pos, t.getNotRepliedCount());
				pstmt.setLong(++pos, t.getDeclinedCount());
				pstmt.setLong(++pos, t.getUnsureCount());
				pstmt.addBatch();
			}

			pstmt.executeBatch();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) conn.close();
				if (pstmt != null) pstmt.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}

	public List<Event> listAll() {
		
		LinkedHashMap<String, String> orders = new LinkedHashMap<String, String>();
		orders.put("id", "asc");
		
		List<Event> users = listBy(SELECT_STATEMENT, null, orders);
		
		return users;
	}

	public Event findByName(String name) {
		return findBy(Collections.<String, Object>singletonMap("username", name));
	}

	public Event findBy(Map<String, Object> values) {
		List<Event> list = listBy(SELECT_STATEMENT, values, null);
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}
	
	public List<Event> listBy(Map<String, Object> conditions, LinkedHashMap<String, String> orders) {
		return listBy(SELECT_STATEMENT, conditions, orders);
	}
	
	public List<Event> listBy(String query, Map<String, Object> conditions, LinkedHashMap<String, String> orders) {
		List<Event> list = new ArrayList<Event>();
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			conn = ConnectionManager.getConnection();
			
			StringBuilder str = new StringBuilder(query);
			
			if (conditions != null && !conditions.isEmpty()) {
				str.append(" where ");
				
				for (Map.Entry<String, Object> entry : conditions.entrySet()) {
					String key = entry.getKey();
					if (entry.getValue() == null) {
						str.append(key).append(" and ");
					} else {
						str.append(key).append(key.contains("like") ? " ? and " : " = ? and ");//isso aqui tem que ser algo dinamico pra aceitar >=, <, like...
					}
				}
				
				str.append(" 1=1 ");
			}
			
			if (orders != null && !orders.isEmpty()) {
				str.append(" order by ");
				
				for (Map.Entry<String, String> entry : orders.entrySet()) {
					str.append(entry.getKey()).append(" ").append(entry.getValue()).append(",");
				}
				
				str.delete(str.length()-1, str.length());
			}
			
			st = conn.prepareStatement(str.toString());
			int i = 1;
			if (conditions != null && !conditions.isEmpty()) {
				for (Object value : conditions.values()) {
					// bind dos parametros
					if (value instanceof String)
						st.setString(i++, (String) value);
					else if (value instanceof Integer)
						st.setInt(i++, (Integer) value);
					else if (value instanceof Long)
						st.setLong(i++, (Long) value);
					else if (value instanceof Date)
						st.setTimestamp(i++, new Timestamp(((Date) value).getTime()));
				}
			}
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				int pos = 0;
				
				Event event = new Event();
				event.setId(rs.getLong(++pos));
				event.setName(rs.getString(++pos));
				event.setOwner(rs.getLong(++pos));
				event.setDescription(rs.getString(++pos));
				event.setPicCover(rs.getString(++pos));
				event.setLocation(rs.getString(++pos));
				event.setPrivacy(rs.getString(++pos));
				event.setStartTime(rs.getTimestamp(++pos));
				event.setEndTime(rs.getTimestamp(++pos));
				event.setPicBig(rs.getString(++pos));
				event.setVenueLatitude(rs.getDouble(++pos));
				event.setVenueLongitude(rs.getDouble(++pos));
				event.setVenueCity(rs.getString(++pos));
				event.setVenueState(rs.getString(++pos));
				event.setVenueCountry(rs.getString(++pos));
				event.setVenueId(rs.getLong(++pos));
				event.setVenueStreet(rs.getString(++pos));
				event.setVenueZip(rs.getString(++pos));
				event.setAllMembersCount(rs.getLong(++pos));
				event.setAttendingCount(rs.getLong(++pos));
				event.setNotRepliedCount(rs.getLong(++pos));
				event.setDeclinedCount(rs.getLong(++pos));
				event.setUnsureCount(rs.getLong(++pos));
				
				list.add(event);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {	
			try {
				if (conn != null) conn.close();
				if (st != null) st.close();
				if (rs != null) rs.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
		
		return list;
	}

	public void update(Event t) {
		// TODO Auto-generated method stub
	}
}
