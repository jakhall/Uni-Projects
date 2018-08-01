package aurora.network;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * Request - An object to store incoming requests from a client.
 *
 */

public class Request implements Serializable  {


	private static final long serialVersionUID = 1877756020448329072L;
	
	private int theMethod = -1;
	private int value;
	private Object theObject;
	private boolean switcher;
	
	public Request() {
		
	}
	
	public Request(int method) {
		this.theMethod = method;
	}
	
	public Request(Object obj) {
		theObject = obj;
	}
	
	public Request(int method, Object obj) {
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
