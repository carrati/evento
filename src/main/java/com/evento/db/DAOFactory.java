package com.evento.db;

import com.evento.db.impl.mysql.MysqlEventDAO;
import com.evento.db.impl.mysql.MysqlEventUserDAO;
import com.evento.db.impl.mysql.MysqlUserDAO;
import com.evento.db.interfaces.EventDAO;
import com.evento.db.interfaces.EventUserDAO;
import com.evento.db.interfaces.UserDAO;

public class DAOFactory {

	private static DAOFactory instance;
	
	private DAOFactory() {}
	
	public static DAOFactory getInstance() {	
		if (instance == null) {
			instance = new DAOFactory();
		}
		
		return instance;
	}
	
	public UserDAO getUserDAO() {
		return MysqlUserDAO.getInstance();
	}
	
	public EventDAO getEventDAO() {
		return MysqlEventDAO.getInstance();
	}
	
	public EventUserDAO getEventUserDAO() {
		return MysqlEventUserDAO.getInstance();
	}
}
