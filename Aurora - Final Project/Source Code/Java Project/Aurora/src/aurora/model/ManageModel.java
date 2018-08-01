package aurora.model;

import java.util.ArrayList;

/**
 * 
 * Manage Model - The model for the manage views of the system, interacts with the 
 * database and vector space model.
 *
 */


public class ManageModel extends BaseModel {
	

	ProfileDatabase profileDB;
	Document[] documents = null;
	Profile[] observers = null;
	Profile[] observing = null;
	
	//Stores the session object, gets the database connection.
	
	public ManageModel(Session s) {
		theSession = s;
		profileDB = new ProfileDatabase(s.getConnection());
	}

	public ProfileDatabase getProfileDatabase() {
		return profileDB;
	}


	
	public void removeObserver(Profile user) {
		int userId = theSession.getUser().getUserID();
		int observerId = user.getUserID();
		String sql = "DELETE FROM OBSERVER_Table WHERE ObserverID=" + observerId + " AND UserID=" + userId;
		profileDB.updateDatabase(sql);
	}
	
	
	//removeObserving - Deletes the connection between the active user observing the selected user.
	
	public void removeObserving(Profile user) {
		int observerId = theSession.getUser().getUserID();
		int userId = user.getUserID();
		String sql = "DELETE FROM OBSERVER_Table WHERE ObserverID=" + observerId + " AND UserID=" + userId;
		profileDB.updateDatabase(sql);
	}
	
	
	//getUser - Returns the user with the specified id if the user exists.
	
	public Profile getUser(int id) {
		return profileDB.getUser(id);
	}
	
	//removeDocument - Deletes the input document from the database.
	
	public void removeDocument(Document doc) {
		profileDB.removeDocument(doc.getID());
	}
	
	
	//getUserDocuments - returns an array of documents which are created by the active user.
	
	public Document[] getDocuments() {
		
		Profile user = theSession.getUser();
		
		user.setDocuments(profileDB.getUserDocuments(user));
		
		Document[] profArr = new Document[user.getDocuments().size()];
		
		for(int i = 0; i < profArr.length; i++) {
			profArr[i] = user.getDocuments().get(i);
		}
		
		return profArr;
	}

	//getObservers - returns all profiles that are observing the active user as an array.
	
	public Profile[] getObservers() {
		
		Profile user = theSession.getUser();
		
		user.setObservers(profileDB.getObservers(user));
		
		Profile[] profArr = new Profile[user.getObservers().size()];
		
		for(int i = 0; i < profArr.length; i++) {
			profArr[i] = user.getObservers().get(i);
		}
		
		return profArr;
	}

	//getObserving - returns all profiles the active user is observing as an array.
	
	public Profile[] getObserving() {
		
		Profile user = theSession.getUser();
	
		user.setObservingList(profileDB.getObserving(user));
		
		
		Profile[] profArr = new Profile[user.getObservingList().size()];
		
		for(int i = 0; i < profArr.length; i++) {
			profArr[i] = user.getObservingList().get(i);
		}
		
		return profArr;
	}
	
	//getUsers - returns a list of all profiles the active user is observing.
	
	public ArrayList<Profile> getUsers() {
		
		Profile user = theSession.getUser();
	
		user.setObservingList(profileDB.getObserving(user));
		
		return user.getObservingList();
		
	}
	
	
	//addUserLinks - gets all users associated with the input user, 
	//adds them to the profile object.	
	
	public Profile addUserLinks(Profile user) {
		return profileDB.addUserLinks(user);
	}
	
	//addAuthor - 	Retrieves the author for a specific document,
	//adds it to the document object.
	
	public void addAuthor(Document doc) {
		doc.setAuthor(profileDB.getAuthor(doc));
	}
	

}