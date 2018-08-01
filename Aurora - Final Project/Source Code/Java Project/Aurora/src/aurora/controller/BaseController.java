package aurora.controller;

import javax.swing.JPanel;

import aurora.model.BaseModel;
import aurora.model.LoginModel;
import aurora.model.Profile;
import aurora.model.Session;
import aurora.network.Request;
import aurora.network.Response;
import aurora.view.BaseFrame;
import aurora.view.BasePanel;
import aurora.view.HomePanel;
import aurora.view.LoginPanel;
import aurora.view.SearchPanel;

/**
 * Base controller - Defines the basic functionality of all controllers. 
 *
 */

public class BaseController {
	
	protected BaseFrame theFrame;
	protected BaseModel theModel;
	protected Session theSession;
	protected BasePanel previousPanel;
	
	public BaseController() {
	}
	
	public BaseFrame getFrame() {
		return theFrame;
	}

	public void setFrame(BaseFrame f) {
		theFrame = f;
	}

	public BaseModel getModel() {
		return theModel;
	}

	public void setModel(BaseModel m) {
		theModel = m;
	}

	public Session getSession() {
		return theSession;
	}

	public void setSession(Session s) {
		theSession = s;
	}
	
	public Profile getUser() {
		return theSession.getUser();
	}

	public JPanel getPreviousPanel() {
		return previousPanel;
	}

	public void setPreviousPanel(BasePanel previousPanel) {
		this.previousPanel = previousPanel;
	}
	
	
	//logout - creates a new login panel, and sets it as the active content pane. 
	
	public void logout() {
		LoginController lc = new LoginController(theSession);
		lc.setFrame(theFrame);
		LoginPanel lp = new LoginPanel(lc);
		theFrame.setSize(500, 300);
		theFrame.setContentPane(lp);
	}
	
	//back - sets the active content pane to the previous panel. 
	
	public void back() {
		theFrame.setContentPane(previousPanel);
		previousPanel.setupData();
		theFrame.pack();
	}
	
	//search - creates new search controller and panel using input query, sets it as active content pane. 
	
	public void search(String query) {
		SearchController sc = new SearchController(theSession, query);
		sc.setFrame(theFrame);
		sc.setPreviousPanel((BasePanel) theFrame.getContentPane());
		SearchPanel sp = new SearchPanel(sc);
		theFrame.setContentPane(sp);
		theFrame.pack();
	}
	
	

}


























