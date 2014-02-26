package com.evento.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class ConnectionManager {
	
	private static BasicDataSource ds;
	
	static {
		Properties props = new Properties(); 
		try {
			props.load(ConnectionManager.class.getResourceAsStream("/db.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//forma convenvional
		ds = new BasicDataSource();
		
		String userName = System.getProperty("PARAM1");
		String password = System.getProperty("PARAM2");
		String url = System.getProperty("JDBC_CONNECTION_STRING");
		
		if (url == null) {
			userName = props.getProperty("user", "root");
			password = props.getProperty("password", "");
			url = props.getProperty("url", "JDBC:mysql://127.0.0.1:3306/adwordsmatic");
		}
		
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername(userName);
		ds.setPassword(password);
		ds.setUrl(url);
		
//		ds.setInitialSize(5);
		ds.setMaxActive(15);
		ds.setMaxIdle(5);
		ds.setNumTestsPerEvictionRun(3);
		ds.setMaxWait(1000 * 10);  //10segs de espera ate receber uma conexao
		ds.setDefaultAutoCommit(false);
	
		ds.setTestOnBorrow(true); //testa tudo antes de passar pro solicitante
		ds.setTestOnReturn(true); //testa tudo que volta pro pool
		ds.setTimeBetweenEvictionRunsMillis(1000 * 60);
		ds.setMinEvictableIdleTimeMillis(1000 * 60);
		
	}
	
	public static DataSource getDataSource() {
		return ds;
	}
	
	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}
	 
	public static String getDataSourceStats(DataSource ds) throws SQLException {
        BasicDataSource bds = (BasicDataSource) ds;
        StringBuffer sb = new StringBuffer(100);
        
		sb.append("Driver....: "); sb.append(bds.getDriverClassName());
		sb.append("\nMaxActive.: "); sb.append(bds.getMaxActive());
		sb.append("\nMaxIdle...: "); sb.append(bds.getMaxIdle());
		sb.append("\nNumActive.: "); sb.append(bds.getNumActive());
		sb.append("\nNumIdle...: "); sb.append(bds.getNumIdle());
        
        return sb.toString();
    }

    public static void shutdownDataSource(DataSource ds) throws SQLException {
        BasicDataSource bds = (BasicDataSource) ds;
        bds.close();
    }

}
