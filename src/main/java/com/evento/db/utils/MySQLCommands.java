package com.evento.db.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.evento.db.ConnectionManager;
import com.evento.db.utils.DBResult;
import com.evento.utils.StringNormalizer;

// colocar os comandos de insert delete update . 

public class MySQLCommands {
	
	StringBuilder query = new StringBuilder();
	
	private static MySQLCommands instance;
	
	private List<DBResult> results = new ArrayList<DBResult>();
	
	private List<Object> params = new ArrayList<Object>();
	
	public static MySQLCommands getInstance() {
		if (instance == null) {
			instance = new MySQLCommands();
		}
		
		return instance;
	}
	
	public MySQLCommands setQuery(String query) {
		this.query.append(query);
		
		return this;
	}
	
	public MySQLCommands setQuery(String query, Object... params) {
		this.query.append(query);
		
		if (params.length > 0) {
			this.params.addAll(Arrays.asList(params));
		}
		
		return this;
	}

	public MySQLCommands select(String param){
		query.append("select ").append(param);
		return this;
	}
	
	public MySQLCommands from(String param){
		query.append(" from ").append(param);
		return this;
	}
	
	public MySQLCommands where(String query, Object... params){
		this.query.append(" where ").append(query);
		
		if (params.length > 0) {
			this.params.addAll(Arrays.asList(params));
		}
		
		return this;
	}
	
	public MySQLCommands leftJoin(String table, String aliasTableFrom, String keyTable, String keyTableFrom ){
		query.append(" left join ").append(table).append(" on ").append( keyTable  + " = ").append(keyTableFrom);
		return this;
	}
	
	public MySQLCommands rightJoin(String table, String tableFrom, String keyTable, String keyTableFrom ){
		query.append(" right join ").append(table).append(" on ").append( keyTable  + " = ").append(keyTableFrom);
		return this;
	}
	
	public MySQLCommands innerJoin(String table, String tableFrom, String keyTable, String keyTableFrom ){
		query.append(" inner join ").append(table).append(" on ").append( keyTable  + " = ").append(keyTableFrom);
		return this;
	}
	
	public MySQLCommands outerJoin(String table, String tableFrom, String keyTable, String keyTableFrom ){
		query.append(" outer join ").append(table).append(" on ").append( keyTable  + " = ").append(keyTableFrom);
		return this;
	}
	
	public MySQLCommands groupBy(String param){
		query.append(" group by ").append(param);
		return this;
	}
	
	public MySQLCommands orderBy(String key, String order){
		query.append(" order by ").append(key).append(" "+ order);
		return this;
	}
	
	public MySQLCommands having(String param){
		query.append(" having ").append(param);
		return this;
	}
	
	public MySQLCommands limit(int rowCount){
		query.append(" limit ").append(rowCount);
		return this;
	}
	
	public MySQLCommands offset(int offset){
		query.append(" offset ").append(offset);
		return this;
	}
	
	public MySQLCommands list() throws SQLException{
		String query = this.query.toString();
		this.query.delete(0, this.query.length());
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			st = conn.prepareStatement(query);
			
			int index = 1;
			if (params != null && !params.isEmpty()) {
				for (Object value : params) {
					// bind dos parametros
					if (value instanceof String)
						st.setString(index++, (String) value);
					else if (value instanceof Integer)
						st.setInt(index++, (Integer) value);
					else if (value instanceof Long)
						st.setLong(index++, (Long) value);
					else if (value instanceof Date)
						st.setTimestamp(index++, new Timestamp(
								((Date) value).getTime()));
					else if (value == null)
						st.setNull(index++, Types.NULL);
				}
			}
			
			rs = st.executeQuery();

			results = new ArrayList<DBResult>();
			params.clear();
			while(rs.next()){
				DBResult queryResult = new DBResult();

				//verificar como ver se a instancia da classe passada pelo metodo as é igual a um dos parametros abaixo
				for(int i = 1; i <= st.getMetaData().getColumnCount(); i++){
					String field = st.getMetaData().getColumnLabel(i);
					Object value = rs.getObject(field);
					
					if (value instanceof Long) {
						queryResult.put(field, (Long) value);
					} else if (value instanceof Integer) {
						queryResult.put(field, (Integer) value);
					} else if (value instanceof Float) {
						queryResult.put(field, (Float) value);
					} else if (value instanceof Boolean) {
						queryResult.put(field, (Boolean) value);
					} else if (value instanceof String) {
						queryResult.put(field, (String) value);
					} else if (value instanceof Timestamp) {
						queryResult.put(field, new Date(((Timestamp) value).getTime()));
					} else {
						queryResult.put(field, value);
					}
				} 
				
				results.add(queryResult);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) conn.close();
			if (st != null) st.close();	
			if (rs != null) rs.close();
		}
		return this;
	}
	
	public int count() throws SQLException{
		String query = this.query.toString();
		this.query.delete(0, this.query.length());
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			st = conn.prepareStatement(query);
			
			int index = 1;
			if (params != null && !params.isEmpty()) {
				for (Object value : params) {
					// bind dos parametros
					if (value instanceof String)
						st.setString(index++, (String) value);
					else if (value instanceof Integer)
						st.setInt(index++, (Integer) value);
					else if (value instanceof Long)
						st.setLong(index++, (Long) value);
					else if (value instanceof Date)
						st.setTimestamp(index++, new Timestamp(
								((Date) value).getTime()));
					else if (value == null)
						st.setNull(index++, Types.NULL);
				}
			}
			
			rs = st.executeQuery();

			results = new ArrayList<DBResult>();
			params.clear();
			if (rs.next()){
				return rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) conn.close();
			if (st != null) st.close();	
			if (rs != null) rs.close();
		}
		return 0;
	}
	
	public List<DBResult> getResults() {
		return results;
	}
	
	public <T> List<T> as(Class<T> clazz){
		
		// passar um map com o resultado da query para esse método
		
		List<T> list = new ArrayList<T>();
		try {
			for (DBResult result: results) {
				T obj = clazz.newInstance();
				
				Set<String> fields = result.getFields();
				
				for (String fieldName : fields) {
					String fieldNameCamelCase = StringNormalizer.toCamelCase(fieldName);
					
					String methodName = (fieldNameCamelCase.charAt(0) + "").toUpperCase() + fieldNameCamelCase.substring(1);
					methodName = "set" + methodName;
					
					Object value = result.get(fieldName);
					
					if (value != null) {
						Class<?> type = value.getClass();

						if (value.getClass().equals(Integer.class)) {
							type = Integer.TYPE;
						} else if (value.getClass().equals(Boolean.class)) {
							type = Boolean.TYPE;
						}

						Method method = obj.getClass().getDeclaredMethod(methodName, type);

						method.setAccessible(true);
						method.invoke(obj, value);
					}
				}
				list.add(obj);
			}
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return list;
	}	
}
