package com.evento.db.interfaces;

import java.util.List;

import com.evento.bean.User;

public interface UserDAO extends BasicDAO<User> {
	
	public User findByName(String name);
	
	public List<User> listByAccountId(int accountId);
}