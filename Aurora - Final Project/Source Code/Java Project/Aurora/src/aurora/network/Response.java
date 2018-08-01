package aurora.network;

import java.io.Serializable;

/**
 * 
 * Response - An object to store outgoing responses to the client.
 *
 */

public class Response implements Serializable {
	

	private static final long serialVersionUID = 8357910191635249876L;
	
	private int theMethod = 0;
	private int value;
	private Object theObject;
	private boolean switcher;
	
	public Response() {
		
	}
	
	public Response(Object obj) {
		theObject = obj;
	}
	
	public Response(int method, Object obj) {
		theMethod = method;
		theObject = obj;
	}
	
	public int getMethod() {
		return theMethod;
	}

	public void setMethod(int theMethod) {
		this.theMethod = theMethod;
	}

	public Object getObject() {
		return theObject;
	}

	public void setObject(Object theObject) {
		this.theObject = theObject;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean getSwitch() {
		return switcher;
	}

	public void setSwitch(boolean switcher) {
		this.switcher = switcher;
	}

}
