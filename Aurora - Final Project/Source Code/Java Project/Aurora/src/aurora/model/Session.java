package aurora.model;

import java.sql.Connection;
import aurora.network.SocketController;
import aurora.retrieval.VectorSpaceModel;

/**
 * 
 * Session - Stores information about a clients current session.
 *
 */


public class Session {
	
	private Profile theUser;
	private Connection theConnection;
	private SocketController clientSocket;
	private VectorSpaceModel vsm;
	
	public Session(Profile user, Connection conn) {
		this.theUser = user;
		this.theConnection = conn;
	}

	
	public Session(SocketController socket) {
		clientSocket = socket;
	}
	

	public Profile getUser() {
		return theUser;
	}

	public void setUser(Profile theUser) {
		this.theUser = theUser;
	}

	public Connection getConnection() {
		return theConnection;
	}

	public void setConnection(Connection conn) {
		this.theConnection = conn;
	}

	public SocketController getSocket() {
		return clientSocket;
	}

	public void setSocket(SocketController clientSocket) {
		this.clientSocket = clientSocket;
	}


	public VectorSpaceModel getVSM() {
		return vsm;
	}


	public void setVSM(VectorSpaceModel vsm) {
		this.vsm = vsm;
	}
	
	
	

}
