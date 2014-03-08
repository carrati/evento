package com.evento.db.impl.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.evento.bean.User;
import com.evento.db.ConnectionManager;
import com.evento.db.interfaces.UserDAO;

public class MysqlUserDAO implements UserDAO {
	
	private static MysqlUserDAO instance;

	private static String SELECT_STATEMENT = "select id, name, first_name, last_name, link, gender, username, email, location, location_id, birthday from user ";
	
	private static String INSERT_STATEMENT = "insert into user (id,	name, first_name, last_name, link, gender, username, email,	last_login_ip, location, location_id, birthday, last_login_date, created_at, updated_at) " +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now(),now() ) "
			+ "ON DUPLICATE KEY UPDATE name=values(name), first_name=values(first_name), last_name=values(last_name), link=values(link), gender=values(gender), username=values(username), email=values(email),	"
			+ "last_login_ip=values(last_login_ip), location=values(location), location_id=values(location_id), birthday=values(birthday), last_login_date=now(), updated_at=now()";
	
			
	private MysqlUserDAO() {
	}
	
	public static MysqlUserDAO getInstance() {
		if (instance == null) {
			instance = new MysqlUserDAO();
		}
		
		return instance;
	}

	public User findById(int id) {
		return findBy(Collections.<String, Object>singletonMap("id", id));
	}
	
	public List<User> listByAccountId(int accountId) {
		
		return listBy(SELECT_STATEMENT, Collections.<String, Object>singletonMap("account_id", accountId), null);
	}

	public void insert(User t) {
		Connection conn = null;
		PreparedStatement st = null;
		
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(true);
			int pos = 0;

			st = conn.prepareStatement(INSERT_STATEMENT);
			st.setLong(++pos, t.getId());
			st.setString(++pos, t.getName());
			st.setString(++pos, t.getFirstName());
			st.setString(++pos, t.getLastName());
			st.setString(++pos, t.getLink());
			st.setString(++pos, t.getGender());
			st.setString(++pos, t.getUsername());
			st.setString(++pos, t.getEmail());
			st.setString(++pos, t.getLastLoginIP());
			st.setString(++pos, t.getLocation());
			st.setLong(++pos, t.getLocationId());
			st.setDate(++pos, new java.sql.Date(t.getBirthday().getTime()));
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

	public void insert(List<User> list) {

	}

	public List<User> listAll() {
		
		LinkedHashMap<String, String> orders = new LinkedHashMap<String, String>();
		orders.put("id", "asc");
		
		List<User> users = listBy(SELECT_STATEMENT, null, orders);
		
		return users;
	}

	public User findByName(String name) {
		return findBy(Collections.<String, Object>singletonMap("username", name));
	}

	public User findBy(Map<String, Object> values) {
		List<User> list = listBy(SELECT_STATEMENT, values, null);
		return list != null && !list.isEmpty() ? list.get(0) : null;
	}
	
	public List<User> listBy(Map<String, Object> conditions, LinkedHashMap<String, String> orders) {
		return listBy(SELECT_STATEMENT, conditions, orders);
	}
	
	public List<User> listBy(String query, Map<String, Object> conditions, LinkedHashMap<String, String> orders) {
		List<User> list = new ArrayList<User>();
		
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
				
				User user = new User();
				user.setId(rs.getInt(++pos));
				user.setName(rs.getString(++pos));
				user.setFirstName(rs.getString(++pos));
				user.setlastName(rs.getString(++pos));
				user.setLink(rs.getString(++pos));
				user.setGender(rs.getString(++pos));
				user.setUsername(rs.getString(++pos));
				user.setEmail(rs.getString(++pos));
				user.setLocation(rs.getString(++pos));
				user.setLocationId(rs.getLong(++pos));
				user.setBirthday(rs.getDate(++pos));
				
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

	public void update(User t) {
		// TODO Auto-generated method stub
		
	}
}
