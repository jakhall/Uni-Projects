package aurora.model;

import java.sql.Connection;


/**
 * 
 * Base  Model - Defines the basic functionality of all model objects, interacts with the
 * system database and vector space model.
 * 
 */


public class BaseModel {

	protected Session theSession = null;
	protected Connection conn = null;
	
	public BaseModel() {
		
	}
	
	//Stores the session and the database connection.
	
	public BaseModel(Session s) {
		theSession = s;
		conn = s.getConnection();
	}
	
	
	public Session getSession() {
		return theSession;
	}
	

	public void setTheSession(Session s) {
		theSession = s;
	}

	
	public Connection getConn() {
		return conn;
	}


	public void setConn(Connection c) {
		conn = c;
	}
	
	
}
