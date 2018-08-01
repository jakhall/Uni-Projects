package aurora.controller;

import aurora.model.Document;
import aurora.model.DocumentModel;
import aurora.model.Profile;
import aurora.model.Session;
import aurora.remote.BaseInvoker;
import aurora.remote.HomeInvoker;
import aurora.remote.ProfileInvoker;
import aurora.view.BasePanel;
import aurora.view.HomePanel;
import aurora.view.ProfilePanel;


/**
 * Document controller - controls the document view where users can view 
 * a selected document.
 * 
 */

public class DocumentController extends BaseController {

		private DocumentModel theModel;
		private Document theDocument;
		private boolean isViewed = false;
	
		//Stores the session and the inputed document to be viewed. Creates a new document model. 
		
		public DocumentController(Session s, Document d) {
			theSession = s;
			theDocument = d;
			theModel = new DocumentModel(s);	
		}
		
		public DocumentController(Session s, Document d, boolean v) {
			theSession = s;
			theDocument = d;
			theModel = new DocumentModel(s);	
			if(v && !d.getText().equals("")) {
				theModel.viewDocument(d);
			}
			isViewed = v;
		}
		
		public Document getDocument() {
			return theDocument;
		}
		
	
		public void setDocument(Document doc) {
			theDocument = doc;
		}
		
		
		//getSession - gets the current session object.
		
		public Session getSession() {
			return theSession;
		}
		
		//getUser - gets the user associated with the current session. 
		
		public Profile getUser() {
			return theSession.getUser();
		}
		
		//getModel - returns the created document model assigned to this controller. 
		
		public DocumentModel getModel() {
			return (DocumentModel) theModel;
		}
	
		//updateDocument - refreshes the document object saved in this class.
		
		public void updateDocument() {
			theDocument = theModel.updateDocument(theDocument);
		}
		

		//saveDocument - saves any changes to the document to the database. 
		
		public void saveDocument(String title, String text) {
			theDocument.setText(text);
			theDocument.setTitle(title);
			theModel.saveDocument(theDocument);
			updateDocument();
		}
	
		//viewUser - changes the active invoker to the profile invoker of the selected user. 
		
		public void viewUser(int id) {
			
			BaseController controller;
			BasePanel panel;
			if(id == theSession.getUser().getUserID()) {
				controller = new HomeController(theSession);
				controller.setPreviousPanel((BasePanel) theFrame.getContentPane());
				controller.setFrame(theFrame);
				panel = new HomePanel((HomeController) controller);
			} else {
				Profile user = theModel.getUser(id);
				theModel.addUserLinks(user);
				controller = new ProfileController(theSession, user);
				controller.setPreviousPanel((BasePanel) theFrame.getContentPane());
				controller.setFrame(theFrame);
				panel = new ProfilePanel((ProfileController) controller);
			}
			

			theFrame.setContentPane(panel);
			theFrame.pack();
		}
		
		//getAuthor - returns the author of the current document.
		
		public Profile getAuthor() {
			return theDocument.getAuthor();
		}
		
		//isViewed - returns whether the document is created by a different author than the active user.
		
		public boolean isViewed() {
			return isViewed;
		}
		
		
}
