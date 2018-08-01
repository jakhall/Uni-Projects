package aurora.network;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import aurora.controller.BaseController;
import aurora.controller.LoginController;
import aurora.model.Session;
import aurora.remote.BaseInvoker;
import aurora.remote.LoginInvoker;
import aurora.retrieval.VectorSpaceModel;

/**
 * 
 * Socket Controller - handles the socket for each client, directs incoming invoker requests.
 *
 */

public class SocketController extends Thread {
	
	    protected Socket clientSocket;
	    protected BaseInvoker remoteInvoker;
	    private boolean isConnected = false;
	    private Session theSession = null;
	    private VectorSpaceModel theVSM;

	    public SocketController(Socket socket, VectorSpaceModel vsm) {
	        this.clientSocket = socket;
	        isConnected = true;
	        theSession = new Session(this);
	        theSession.setVSM(vsm);
	        theVSM = vsm;
	    }

	    public Session getSession() {
			return theSession;
		}

		public void setSession(Session theSession) {
			this.theSession = theSession;
		}

		public BaseInvoker getInvoker() {
			return remoteInvoker;
		}

		public void setInvoker(BaseInvoker invoker) {
			this.remoteInvoker = invoker;
		}
		
		public VectorSpaceModel getVSM() {
			return theVSM;
		}

		public void setVSM(VectorSpaceModel theVSM) {
			this.theVSM = theVSM;
		}

		
		//Run - creates a new login invoker, begins to listen for client requests whilst the socket is open.
		//sends response back to client when needed.
		
		public void run() {
	    	
	    	Request request = null;
	    	Response response = null;
	        ObjectOutputStream objectOut = null;
            ObjectInputStream objectIn = null;
            
            remoteInvoker = new LoginInvoker(this);
            System.out.println("Client " + clientSocket.getInetAddress() + " connected.");
           
	        while (isConnected) {
	        	
				try {
					objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
					objectIn =  new ObjectInputStream(clientSocket.getInputStream());
		            	try {
							request = (Request)objectIn.readObject();
							response = remoteInvoker.handleRequest(request);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
	            	
	                if ((request == null)) {
	                    isConnected = false;
	                } else {
	                    objectOut.writeObject((response));
	                    objectOut.flush();
	                }
		           
	            } catch (IOException e) {
	            	 isConnected = false;
	            }
	        }
	        System.out.println("Client " + clientSocket.getInetAddress() + " disconnected.");
	        return;
				
	        }
	}