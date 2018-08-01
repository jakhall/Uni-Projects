package aurora.model;

import aurora.retrieval.VectorSpaceModel;

public class VSMHandler {
	
	private static VSMHandler handler;
	private static VectorSpaceModel vsm;
	
	VSMHandler(){
		vsm = new VectorSpaceModel(DBConnection.dbConnector());
	}
	
	static {
		handler = new VSMHandler();
	}
	
	public static VSMHandler getHandler() {
		return handler;
	}
	
	public VectorSpaceModel getVSM() {
		return vsm;
	}
	
}
