
package aurora.controller;

import java.util.ArrayList;

import aurora.model.Document;
import aurora.model.HomeModel;
import aurora.model.Profile;
import aurora.model.Session;
import aurora.remote.DocumentInvoker;
import aurora.view.BasePanel;
import aurora.view.DetailPanel;
import aurora.view.DocumentPanel;
import aurora.view.ManagePanel;
import aurora.view.ProfilePanel;
import aurora.view.UserDocumentPanel;

/**
 * Home controller - controls the home view where users users can view their profile
 * and navigate between sections.
 * 
 */

public class HomeController extends BaseController {
		
		private HomeModel theModel;
		private ArrayList<Document> suggestions;
		
		//Stores the current session and creates a new home model. 
		public HomeController(Session s) {
			theSession = s;
			theModel = new HomeModel(s);	
		}
	
		public HomeModel getModel() {
			return (HomeModel) theModel;
		}

		
		//getUserDocsTitle - returns a list of just the document titles creates by the current user.
		
		public String[] getUserDocsTitle() {
			Document[] docArr = theModel.getUserDocuments();
			String[] strArr = new String[docArr.length];
			for(int i = 0; i < docArr.length; i++) {
				strArr[i] = docArr[i].getTitle();
			}
			return strArr;
		}
		
		//getObserversUsername - returns a list of all the username's of profiles which are observing the current user.
		
		public String[] getObserversUsername() {
			Profile[] profArr = theModel.getObservers();
			String[] strArr = new String[profArr.length];
			for(int i = 0; i < profArr.length; i++) {
				strArr[i] = profArr[i].getUsername();
			}
			return strArr;
		}
		
		//getObservingUsername - returns a list of all the username's of profiles which the current user is observing.
		
		public String[] getObservingUsername() {
			Profile[] profArr = theModel.getObserving();
			String[] strArr = new String[profArr.length];
			for(int i = 0; i < profArr.length; i++) {
				strArr[i] = profArr[i].getUsername();
			}
			return strArr;
		}
		
		
		
		//getNumOfDocuments - returns the total number of documents created by the current user.
		
		public int getNumOfDocuments() {
			return theModel.getUserDocuments().length;
		}
		
		
		//getNumOfObservers - returns the total number of profiles that are observing the current user.
		
		public int getNumOfObservers() {
			return theModel.getObservers().length;
		}
		
		//getNumOfObserving - returns the total number of profiles that the current user is observing.
		
		public int getNumOfObserving() {
			return theModel.getObserving().length;
		}
		
		
		//removeDocument - gets the documents at a given list index, removes it from the database.
		
		public void removeDocument(int index) {
			Document doc = theModel.getUserDocuments()[index];
			theModel.removeDocument(doc);
		}
		
		//addDocument - creates a new blank document titled "new post" to the database.
		
		public void addDocument() {
			theModel.addUserDocument();
		}
		
		
		//viewDocument - gets the document at the document list index, assigns the author of that document,
		//creates a new document controller with the selected document, then adds the controller and panel to the 
		//active content pane. 
		
		public void viewDocument(int index) {
			Document doc = theModel.getUserDocuments()[index];
			theModel.addAuthor(doc);
			DocumentController dc = new DocumentController(theSession, doc, !isAuthor(doc));
			dc.setPreviousPanel((BasePanel) theFrame.getContentPane());
			dc.setFrame(theFrame);
			UserDocumentPanel dp = new UserDocumentPanel(dc);
			theFrame.setContentPane(dp);
			theFrame.pack();
		}
		
		//viewSuggestion - gets the suggestion at the suggestion index, assign the author of the document, 
		//creates a new document controller with the selected document, then adds the controller and panel
		//to the active content pane.
		
		public void viewSuggestion(int index) {
			if(index >= 0) {
				Document doc = suggestions.get(index);
				theModel.addAuthor(doc);
				DocumentController dc = new DocumentController(theSession, doc, !isAuthor(doc));
				dc.setPreviousPanel((BasePanel) theFrame.getContentPane());
				dc.setFrame(theFrame);
				DocumentPanel dp = new DocumentPanel(dc);
				theFrame.setContentPane(dp);
				theFrame.pack();
			}
		}
		
		//viewObserver - gets the profile at the observer list index, adds the users to the selected profile,
		//creates a new profile controller with that user and sets it to the active content pane. 
		
		public void viewObserver(int index) {
			if(index >= 0) {
				Profile profile = theModel.getObservers()[index];
				theModel.addUserLinks(profile);
				ProfileController pc = new ProfileController(theSession, profile);
				pc.setPreviousPanel((BasePanel) theFrame.getContentPane());
				pc.setFrame(theFrame);
				ProfilePanel pp = new ProfilePanel(pc);
				theFrame.setContentPane(pp);
				theFrame.pack();
			}
		}
		
		
		//getSuggestions - retrieves all documents suggested to the user based on similarity.
		
		public String[] getSuggestions(){
			ArrayList<Document> docList = theModel.getSuggestions(theSession.getUser(), 0, 15);
			String[] suggestionTitles = new String[docList.size()];
			for(int i = 0; i < docList.size(); i++) {
				suggestionTitles[i] = docList.get(i).getTitle();
			}
			suggestions = docList;
			
			return suggestionTitles;
		}
		
		
		
		//viewObserving - gets the profile at the observing list index, adds the users to the selected profile,
		//creates a new profile controller with that user and sets it to the active content pane. 
		
		public void viewObserving(int index) {
			if(index >= 0) {
				Profile profile = theModel.getObserving()[index];
				theModel.addUserLinks(profile);
				ProfileController pc = new ProfileController(theSession, profile);
				pc.setPreviousPanel((BasePanel) theFrame.getContentPane());
				pc.setFrame(theFrame);
				ProfilePanel pp = new ProfilePanel(pc);
				theFrame.setContentPane(pp);
				theFrame.pack();
			}
		}
		
		//manageObserving - creates a new manage controller, sets it as the active content pane. 
		
		public void manageObserving() {
				ManageController mc = new ManageController(theSession);
				mc.setPreviousPanel((BasePanel) theFrame.getContentPane());
				mc.setFrame(theFrame);
				ManagePanel mp = new ManagePanel(mc);
				theFrame.setContentPane(mp);
				theFrame.pack();
		}
		
		//editDetails - creates a new detail controller, sets it as the active content pane. 
		
		public void editDetails() {
			DetailController dc = new DetailController(theSession);
			dc.setPreviousPanel((BasePanel) theFrame.getContentPane());
			dc.setFrame(theFrame);
			DetailPanel dp = new DetailPanel(dc);
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
		
	
		
}


