package aurora.controller;

import java.util.ArrayList;
import aurora.model.Document;
import aurora.model.Profile;
import aurora.model.ProfileModel;
import aurora.model.Session;
import aurora.view.BasePanel;
import aurora.view.DocumentPanel;
import aurora.view.HomePanel;
import aurora.view.ProfilePanel;


/**
 * 
 * Profile Controller - controls the profile view, allows the user to view a selected user.
 * 
 */

public class ProfileController extends BaseController {
	
	private ProfileModel theModel;
	private Profile viewedUser;
	
	
	//Stores the session and the user being viewed, creates a new profile model. 
	
	public ProfileController(Session s, Profile u) {
		viewedUser = u;
		theSession = s;
		theModel = new ProfileModel(s);
	}
	
	
	//getUserDocuments - returns a list of all documents created by the user being viewed.
	
	public Document[] getUserDocuments() {
		return theModel.getUserDocuments(viewedUser);
	}
	
	
	//getViewedUser - returns the profile object associated with the user being viewed.
	
	public Profile getViewedUser() {
		return viewedUser;
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
	
	
	
	//getUserDocsTitle - returns a list of document titles that were created by the user being viewed.
	
	public String[] getUserDocsTitle() {
		Document[] docArr = theModel.getUserDocuments(viewedUser);
		String[] strArr = new String[docArr.length];
		for(int i = 0; i < docArr.length; i++) {
			strArr[i] = docArr[i].getTitle();
		}
		return strArr;
	}
	
	//getObserversUsername - returns a list of username's of profiles that are observing the viewed user.
	
	public String[] getObserversUsername() {
		Profile[] profArr = theModel.getObservers(viewedUser);
		String[] strArr = new String[profArr.length];
		for(int i = 0; i < profArr.length; i++) {
			strArr[i] = profArr[i].getUsername();
		}
		return strArr;
	}
	
	//getObservingUsername - returns a list of username's of profiles that are being observed by the viewed user.
	
	public String[] getObservingUsername() {
		Profile[] profArr = theModel.getObserving(viewedUser);
		String[] strArr = new String[profArr.length];
		for(int i = 0; i < profArr.length; i++) {
			strArr[i] = profArr[i].getUsername();
		}
		return strArr;
	}

	
	//getNumOfDocuments - returns the total number of documents created by the viewed user. 
	
	public int getNumOfDocuments() {
		return theModel.getUserDocuments(viewedUser).length;
	}
	
	//getNumOfObservers - returns the total number of profiles that are observing the viewed user.
	
	public int getNumOfObservers() {
		return theModel.getObservers(viewedUser).length;
	}
	
	//getNumOfObserving - returns the total number of profiles that the viewed user is observing.
	
	public int getNumOfObserving() {
		return theModel.getObserving(viewedUser).length;
	}
	
	
	
	//viewDocument - gets the document at the document list index, assigns the author of that document,
	//creates a new document controller with the selected document, then adds the controller and panel to the 
	//active content pane. 
			
	
	public void viewDocument(int index) {
		Document doc = theModel.getUserDocuments(viewedUser)[index];
		theModel.addAuthor(doc);
		DocumentController dc = new DocumentController(theSession, doc, !isAuthor(doc));
		dc.setPreviousPanel((BasePanel) theFrame.getContentPane());
		dc.setFrame(theFrame);
		DocumentPanel dp = new DocumentPanel(dc);
		theFrame.setContentPane(dp);
		theFrame.pack();
	}
	
	
	//isAuthor - checks if the selected document was created by the active user.
	
	public boolean isAuthor(Document doc) {
		if(doc.getAuthor().getUserID() == theSession.getUser().getUserID()) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	//viewObserver - gets the profile at the observer list index, adds the users to the selected profile,
	//creates a new profile controller with that user and sets it to the active content pane. 
	
	public void viewObserver(int index) {
		if(index >= 0) {
			
			BaseController controller;
			BasePanel panel;
			
			Profile user = theModel.getObservers(viewedUser)[index];
			if(user.getUserID() == theSession.getUser().getUserID()) {
				controller = new HomeController(theSession);
				controller.setPreviousPanel((BasePanel) theFrame.getContentPane());
				controller.setFrame(theFrame);
				panel = new HomePanel((HomeController) controller);
			} else {
				theModel.addUserLinks(user);
				controller = new ProfileController(theSession, user);
				controller.setPreviousPanel((BasePanel) theFrame.getContentPane());
				controller.setFrame(theFrame);
				panel = new ProfilePanel((ProfileController) controller);
			}
			
			theFrame.setContentPane(panel);
			theFrame.pack();
		}
	}
	
	
	//viewObserving - gets the profile at the observing list index, adds the users to the selected profile,
	//creates a new profile controller with that user and sets it to the active content pane. 
	
	public void viewObserving(int index) {
		
		if(index >= 0) {
			
			BaseController controller;
			BasePanel panel;
			
			Profile user = theModel.getObserving(viewedUser)[index];
			if(user.getUserID() == theSession.getUser().getUserID()) {
				controller = new HomeController(theSession);
				controller.setPreviousPanel((BasePanel) theFrame.getContentPane());
				controller.setFrame(theFrame);
				panel = new HomePanel((HomeController) controller);
			} else {
				theModel.addUserLinks(user);
				controller = new ProfileController(theSession, user);
				controller.setPreviousPanel((BasePanel) theFrame.getContentPane());
				controller.setFrame(theFrame);
				panel = new ProfilePanel((ProfileController) controller);
			}
			
			theFrame.setContentPane(panel);
			theFrame.pack();
		}
	}
	
	//getIconColor - returns the default icon colour of the viewed user.
	
	public String getIconColor() {
		return viewedUser.getDefaultColor();
	}
	
	//getViewedUsername - returns the username of the user being viewed.
	
	public String getViewedUsername() {
		return viewedUser.getUsername();
	}
	
	//removeObserving - removes the viewed user from the active users observing list. 
	
	public void removeObserving() {
		theModel.removeObserving(viewedUser);
	}
	
	//addObserving - adds the viewed user to the active users observing list. 
	
	public void addObserving() {
		theModel.addObserving(viewedUser);
	}
	

}
