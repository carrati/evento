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

	private static String SELECT_STATEMENT = "select id, name, owner, description, cover, location, privacy, start_time, end_time from event ";
	
	
//	id, name, owner, description, cover, location, privacy, start_time, end_time, created_at, updated_at
	
	private static String INSERT_STATEMENT = "insert into event (id, name, owner, description, cover, location, privacy, start_time, end_time, created_at, updated_at) " +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, now(),now() ) "
			+ "ON DUPLICATE KEY UPDATE name=values(name), owner=values(owner), description=values(description), cover=values(cover), location=values(location), privacy=values(privacy), start_time=values(start_time),	"
			+ "end_time=values(end_time), updated_at=now()";
	
			
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
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(true);
			int pos = 0;

			st = conn.prepareStatement(INSERT_STATEMENT);
			
			st.setLong(++pos, t.getId());
			st.setString(++pos, t.getName());
			st.setLong(++pos, t.getOwner());
			st.setString(++pos, t.getDescription());
			st.setString(++pos, t.getCover());
			st.setString(++pos, t.getLocation());
			st.setString(++pos, t.getPrivacy());
			if (t.getStartTime() != null) {
				st.setTimestamp(++pos, new Timestamp(((Date)  t.getStartTime()).getTime()));
			} else {
				st.setNull(++pos, Types.NULL);
			}
			if (t.getEndTime() != null) {
				st.setTimestamp(++pos, new Timestamp(((Date)  t.getEndTime()).getTime()));
			} else {
				st.setNull(++pos, Types.NULL);
			}
			st.execute();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) conn.close();
				if (st != null) st.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}

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
				pstmt.setString(++pos, t.getCover());
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
				
				for (String key : conditions.keySet()) {
					str.append(key).append(key.contains("like") ? " ? and " : " = ? and ");
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
				
				Event user = new Event();
				user.setId(rs.getInt(++pos));
				user.setName(rs.getString(++pos));
				user.setOwner(rs.getLong(++pos));
				user.setDescription(rs.getString(++pos));
				user.setCover(rs.getString(++pos));
				user.setLocation(rs.getString(++pos));
				user.setPrivacy(rs.getString(++pos));
				user.setStartTime(rs.getTimestamp(++pos));
				user.setEndTime(rs.getTimestamp(++pos));
				
				list.add(user);
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
