package aurora.view;

import javax.swing.JFrame;
import aurora.controller.LoginController;


/**
 * 
 *  Used to create a new JFrame and display the login view.
 *
 */

public class BaseFrame extends JFrame {
	
	private LoginPanel loginPanel;
	
	//Creates a new login controller, panel and sets up the frame.
	
	public BaseFrame(LoginController loginController) {
		loginPanel = new LoginPanel(loginController);
		setupFrame();
	}

	//setupFrame - defines the properties of the frame and adds the login panel to the frame.
	
	private void setupFrame() {
		this.setContentPane(loginPanel);
		this.setTitle("Aurora v1.0.0");
		this.setSize(500, 300);
		this.setVisible(true);
	}
}


