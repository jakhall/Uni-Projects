package aurora.remote;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.JPanel;

import aurora.model.Document;
import aurora.model.Profile;
import aurora.model.ProfileModel;
import aurora.model.Session;
import aurora.network.Request;
import aurora.network.Response;
import aurora.view.BasePanel;
import aurora.view.DocumentPanel;
import aurora.view.HomePanel;
import aurora.view.LoginPanel;
import aurora.view.ProfilePanel;

/**
 * 
 * Observe Invoker - Handles tasks requested by the clients observe view.
 *
 */

public class ProfileInvoker extends BaseInvoker {
	
	private ProfileModel theModel;
	private Profile viewedUser;
	
	//Stores the session, the user being viewed and creates a new profile model.
	
	public ProfileInvoker(Session s, Profile u) {
		super(s);
		viewedUser = u;
		theSession = s;
		theModel = new ProfileModel(s);
	}
	
	public Profile getViewedUser() {
		return viewedUser;
	}
	
	//getProfileImage - Retrieves the image file in the users profile folder.
	
	public byte[] getProfileImage() {
		
		String s =  File.separator;
		
		File imageFile = new File(System.getenv("APPDATA") + s + "Aurora" + s + "profile_data" + s 
						+ viewedUser.getUsername() + s + "profile" + s + "icon.png");
	
		if(!imageFile.exists()) {
			String url = "/default_data/profile_image/default_" + viewedUser.getDefaultColor() + ".png";
			imageFile = new File(HomePanel.class.getResource(url).getFile());
		}
				
		try {
			return Files.readAllBytes(imageFile.toPath());
		} catch (IOException e) {
			return null;
		}
	}
	
	//isObserving - checks if the active user is current observing the user being viewed. 
	
	public boolean isObserving() {
			ArrayList<Profile> observing = theModel.getObserving();
			
			for(Profile user : observing) {
				if(user.getUserID() == viewedUser.getUserID()) {
					return true;
				} 
			}
			return false;
		}

	//manageObserving - changes the active invoker to the observe invoker.
	
	public void viewObserve() {
		ObserveInvoker invoker = new ObserveInvoker(theSession, viewedUser);
		invoker.setPreviousInvoker(this);
		theSocket.setInvoker(invoker);
	}
	
	//viewDocument - changes the active invoker to the document invoker for the selected document.
	
	public void viewDocument(int index) {
		Document doc = theModel.getDocument(index);
		theModel.addAuthor(doc);
		DocumentInvoker invoker = new DocumentInvoker(theSession, doc, !isAuthor(doc));
		invoker.setPreviousInvoker(this);
		theSession.getSocket().setInvoker(invoker);
	}
	
	
	//isAuthor - checks if the selected document was created by the active user.
	
	public boolean isAuthor(Document doc) {
		if(doc.getAuthor().getUserID() == theSession.getUser().getUserID()) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//viewObserver - changes the active invoker to the profile invoker for the selected user.
	
	public void viewObserver(int index) {
		if(index >= 0) {
			Profile profile = theModel.getObservers(viewedUser)[index];
			theModel.addUserLinks(profile);
			ProfileInvoker invoker = new ProfileInvoker(theSession, profile);
			invoker.setPreviousInvoker(this);
			theSession.getSocket().setInvoker(invoker);
		}
	}
	
	//viewObserving - changes the active invoker to the profile invoker for the selected user.
	public void viewObserving(int index) {
		if(index >= 0) {
			Profile profile = theModel.getObserving(viewedUser)[index];
			theModel.addUserLinks(profile);
			ProfileInvoker invoker = new ProfileInvoker(theSession, profile);
			invoker.setPreviousInvoker(this);
			theSession.getSocket().setInvoker(invoker);
		}
	}
	
	//removeObserving - removes the viewed user from the active users observing list. 
	
	public void removeObserving() {
		theModel.removeObserving(viewedUser);
	}
	
	//addObserving - adds the viewed user to the active users observing list. 
	
	public void addObserving() {
		theModel.addObserving(viewedUser);
	}
	
	//getViewedUsername - returns the username of the user being viewed.
	public String getViewedUsername() {
		return viewedUser.getUsername();
	}
	
	//viewObserving - changes the active invoker to the document invoker for the selected document.
	
	public void viewDocuments() {
		DocListInvoker invoker = new DocListInvoker(theSession, viewedUser);
		invoker.setPreviousInvoker(this);
		theSocket.setInvoker(invoker);
	}
	
	//viewDetails - changes the active invoker to the detail invoker.
	
	public void viewDetails() {
		DetailInvoker invoker = new DetailInvoker(theSession, viewedUser);
		invoker.setPreviousInvoker(this);
		theSocket.setInvoker(invoker);
	}
	
	
	//getDocuments - returns all documents created by the viewed user.
	
	public ArrayList<Document> getDocuments() {
		ArrayList<Document> docList = theModel.getDocList(viewedUser);
		for(Document doc : docList) {
			theModel.addAuthor(doc);
		}
		return docList;
	}
	
	
	//handleRequest - Dictates what method should be called for each incoming request, 
	//returns the corresponding response.
	
	public Response handleRequest(Request request) {
		
		Response response = super.handleRequest(request);
		int index = request.getMethod();
		
		switch(index) {
		
		case 3 :  response = new Response(getViewedUser());
				  break;
		case 4 :  response.setSwitch(isObserving());
				  break;
		case 5 :  addObserving();
				  break;
		case 6 :  viewDocument(request.getValue());
	 	 		  break;
		case 7 :  response = new Response(getProfileImage());
				  break;
		case 8 :  viewObserve();
				  break;
		case 9 :  response = new Response(getDocuments());
		  		  break;
		case 10 : viewDetails();
				  break;
		case 11 : removeObserving();
		  		  break;
		  
		}
		
		return response;
	}
	

}
