package aurora.controller;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import aurora.model.DetailModel;
import aurora.model.Document;
import aurora.model.ManageModel;
import aurora.model.Profile;
import aurora.model.SearchModel;
import aurora.model.Session;
import aurora.view.BasePanel;
import aurora.view.ProfilePanel;
import aurora.view.SearchPanel;

/**
 * Detail controller - controls the details view where users can view 
 * and input their personal details. 
 *
 */

public class DetailController extends BaseController {

		private DetailModel theModel;
		private Profile theUser;
		private ArrayList<Profile> userList;
		private Image profileImage;
		
		//Takes in the current session, stores the user associated with the session and creates the detail model. 
		
		public DetailController(Session s) {
			theSession = s;
			theUser = s.getUser();
			theModel = new DetailModel(s);	
		}
		
		
		//setDetails - adds the inputed details to the user. 
		
		public void setDetails(String t, String a, String f, String l, String e, String p) {
			theUser.setDetails(t, a, f, l);
			theUser.setEmail(e);
			theUser.setPassword(p);
		}
		
		
		//changeProfileIcon - finds the users profile folder, saves an inputed image to that location as a png. 
		
		public void changeProfileIcon(Image image) {	
			String s = File.separator;
			String url = System.getenv("APPDATA") + s + "Aurora" + s + "profile_data"
						 + s + theUser.getUsername() + s + "profile" + s + "icon.png";
			try {
				ImageIO.write((RenderedImage)image, "png", new File(url));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		//validatePassword - makes sure the password and confirmation password are the same. 
		
		public boolean validatePassword(String pass, String confirm) {
			if(pass.equals(confirm)) {
				return true;
			}
			
			System.out.println("Password Invalid");
			
			return false;
		}
		
		//updateUser - saves the changes made to the user as well as sets their profile image.
		
		public void updateUser(Image image) {
			theModel.updateUser(theUser);
			changeProfileIcon(image);
			back();
		}
		
		//setImage - independently sets the image, before the changes are saved. 
		
		public void setImage(Image img) {
			profileImage = img;
		}
		
		
		//getSession - returns the current sessions.
		
		public Session getSession() {
			return theSession;
		}
		
		
}
