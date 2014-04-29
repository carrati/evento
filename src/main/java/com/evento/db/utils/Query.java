package com.evento.db.utils;

import java.io.Serializable;
import java.util.List;

public class Query implements Serializable {
	
	private static final long serialVersionUID = -3281939276051064451L;
	
	private String statement;
	private List<Object> parameters;
	
	public Query(String statement, List<Object> parameters) {
		super();
		this.statement = statement;
		this.parameters = parameters;
	}

	public String getStatement() {
		return statement;
	}

	public List<Object> getParameters() {
		return parameters;
	}

	public String toString() {
		return "Query [query=" + statement + ", parameters=" + parameters + "]";
	}
}
