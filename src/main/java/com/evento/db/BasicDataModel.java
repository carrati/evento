package com.evento.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evento.utils.StringNormalizer;

public class BasicDataModel {
	
	protected List<String> fieldNames = new ArrayList<String>();
	private static final Map<String, String> tableNames = new HashMap<String, String>();
	
	private Map<String, Object> fields = new HashMap<String, Object>();
	
	public BasicDataModel() {
		getTableName();
		initializeFields();
	}
	
	public String getTableName() {
		return getTableName(this.getClass());
	}
	
	@SuppressWarnings("rawtypes")
	public static String getTableName(Class classObj) {
		String className = classObj.getSimpleName();
		
		if (!tableNames.containsKey(className)) {
			tableNames.put(className, StringNormalizer.plurarize(StringNormalizer.toUnderscore(className)));
		}
		
		return tableNames.get(className);
	}
	
	private void initializeFields() {
		Field[] fields = this.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			String fieldName = StringNormalizer.toUnderscore(field.getName());
			
			if (!this.fieldNames.contains(fieldName)) {
				this.fieldNames.add(fieldName);
			}
		}
		
		this.fieldNames = Collections.unmodifiableList(this.fieldNames);
	}
	
	public List<String> getFields() {
		return fieldNames;
	}
	
	public void setFieldValue(String name, Object value) throws IllegalArgumentException, IllegalAccessException, SecurityException, NoSuchFieldException {
			System.out.println(StringNormalizer.toCamelCase(name));
			Field field = this.getClass().getDeclaredField(StringNormalizer.toCamelCase(name));
			
			field.set(this, value);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T extends BasicDataModel> List<T> select(Class classObj, String query, Object... values) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			StringBuilder str = new StringBuilder("select * from " + getTableName(classObj) + " where ");
			
			if (!query.isEmpty()) {
				str.append(query);
			}
			
//			System.out.println("ClassName: " + Thread.currentThread().getStackTrace()[1].getClassName());
			
			st = conn.prepareStatement(str.toString());
			int i = 1;
			if (values != null && values.length > 0) {
				for (Object value : values) {
					// bind dos parametros
					if (value instanceof String)
						st.setString(i++, (String) value);
					else if (value instanceof Integer)
						st.setInt(i++, (Integer) value);
					else if (value instanceof Long)
						st.setLong(i++, (Long) value);
					else if (value instanceof Date)
						st.setTimestamp(i++, new Timestamp(
								((Date) value).getTime()));
				}
			}
			
			
			rs = st.executeQuery();
			
			List<T> objs = new ArrayList<T>();
			
			while (rs.next()) {
				T t = (T) classObj.newInstance();
				
				List<String> fields = t.getFields();
				
				for (String fieldName : fields) {
					Field field = t.getClass().getDeclaredField(StringNormalizer.toCamelCase(fieldName));
				
					field.set(t, rs.getObject(fieldName));
				}
				
				objs.add(t);
			}
			
			return objs;
			
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
		
		return null;
	}
	
	public void save() {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(true);
			
			StringBuilder sb = new StringBuilder("INSERT INTO " + getTableName(this.getClass()) + " ");
			String collumns = "(";
			String values = "(";
			int count = 0;
			for (String fieldName : fieldNames) {
				
				if (!fieldName.equals("id")) {
					collumns += fieldName + (count < fields.size() - 1 ? ", " : "");
					values += "?" + (count < fields.size() - 1 ? ", " : "");
				}
				
				count++;
			}
			collumns += ")";
			values += ")";
			
			sb.append(collumns).append(" VALUES ").append(values);
			
			System.out.println(sb.toString());
			
			st = conn.prepareStatement(sb.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
			List<String> fields = this.getFields();
			int i = 1;
			if (fields != null && fields.size() > 0) {
				for (String fieldName : fields) {
					if (!fieldName.equals("id")) {
						Field field = this.getClass().getDeclaredField(StringNormalizer.toCamelCase(fieldName));
						// bind dos parametros
						Object value = field.get(this);
						if (value instanceof String)
							st.setString(i++, (String) value);
						else if (value instanceof Integer)
							st.setInt(i++, (Integer) value);
						else if (value instanceof Long)
							st.setLong(i++, (Long) value);
						else if (value instanceof Date)
							st.setTimestamp(i++, new Timestamp(
									((Date) value).getTime()));
					}
				}
			}
			st.execute();
			
			//recupera a pk
			rs = st.getGeneratedKeys();
			
			
		    if (rs.next()) {
		    	Field field = this.getClass().getDeclaredField("id");
				field.setInt(this, rs.getInt(1));
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
	}
	
	public String getString(String fieldName) {
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(new BasicDataModel().getTableName());
	}
}
