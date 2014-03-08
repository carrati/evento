package com.evento.db.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.evento.bean.EventUser;
import com.evento.db.ConnectionManager;
import com.evento.db.interfaces.EventUserDAO;

public class MysqlEventUserDAO implements EventUserDAO {
	
	private static MysqlEventUserDAO instance;

	private static String SELECT_STATEMENT = "select event_id, user_id, status from event_users ";
	
	private static String INSERT_STATEMENT = "insert into event_users (event_id, user_id, status, created_at, updated_at) " +
			" values (?, ?, ?, now(),now() ) "
			+ "ON DUPLICATE KEY UPDATE status=values(status), updated_at=now()";
	
			
	private MysqlEventUserDAO() {
	}
	
	public static MysqlEventUserDAO getInstance() {
		if (instance == null) {
			instance = new MysqlEventUserDAO();
		}
		
		return instance;
	}

	public EventUser findById(long id) {
		return null;
	}
	
	public void insert(EventUser t) {
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(true);
			int pos = 0;

			st = conn.prepareStatement(INSERT_STATEMENT);
			
			st.setLong(++pos, t.getEventId());
			st.setLong(++pos, t.getUserId());
			st.setString(++pos, t.getStatus());
			
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

	public void insert(List<EventUser> list) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(true);

			pstmt = conn.prepareStatement(INSERT_STATEMENT);
			
			for (int i = 0; list != null && i < list.size(); i++) {
				EventUser t = list.get(i);
				
				int pos = 0;
				
				pstmt.setLong(++pos, t.getEventId());
				pstmt.setLong(++pos, t.getUserId());
				pstmt.setString(++pos, t.getStatus());
				
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

	public List<EventUser> listAll() {
		return null;
	}

	public EventUser findBy(Map<String, Object> values) {
		List<EventUser> list = listBy(SELECT_STATEMENT, values, null);
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}
	
	public List<EventUser> listBy(Map<String, Object> conditions, LinkedHashMap<String, String> orders) {
		return listBy(SELECT_STATEMENT, conditions, orders);
	}
	
	public List<EventUser> listBy(String query, Map<String, Object> conditions, LinkedHashMap<String, String> orders) {
		List<EventUser> list = new ArrayList<EventUser>();
		
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
				
				EventUser user = new EventUser();
				user.setEventId(rs.getInt(++pos));
				user.setUserId(rs.getInt(++pos));
				user.setStatus(rs.getString(++pos));
				
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

	public void update(EventUser t) {
		// TODO Auto-generated method stub
	}
}
