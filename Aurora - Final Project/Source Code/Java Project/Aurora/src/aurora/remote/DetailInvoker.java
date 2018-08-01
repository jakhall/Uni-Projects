package aurora.remote;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import aurora.model.DetailModel;
import aurora.model.Document;
import aurora.model.ManageModel;
import aurora.model.Profile;
import aurora.model.SearchModel;
import aurora.model.Session;
import aurora.network.Request;
import aurora.network.Response;
import aurora.view.BasePanel;
import aurora.view.HomePanel;
import aurora.view.ProfilePanel;
import aurora.view.SearchPanel;

/**
 * 
 * Detail Invoker - Handles tasks requested by the clients detail view.
 *
 */


public class DetailInvoker extends BaseInvoker {

		private DetailModel theModel;
		private Profile theUser;
		private ArrayList<Profile> userList;
		private Image profileImage;
		
		
		//Stores the session, active user and creates a new detail model.
		
		public DetailInvoker(Session s) {
			super(s);
			theSession = s;
			theUser = s.getUser();
			theModel = new DetailModel(s);	
		}
		
		//Stores the session, viewed user and creates a new detail model.
		
		public DetailInvoker(Session s, Profile user) {
			super(s);
			theSession = s;
			theUser = user;
			theModel = new DetailModel(s);	
		}
		
		//setDetails - adds the inputed details to the user. 
		
		public void setDetails(Profile u) {
			theUser.setDetails(u.getTitle(), u.getDOB(), u.getFirstName(), u.getLastName());
			theUser.setEmail(u.getEmail());
			theUser.setPassword(u.getPassword());
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
		
	
		//getProfileImage - Retrieves the image file in the users profile folder.
		
		public byte[] getProfileImage() {
			
			String s =  File.separator;
			
			File imageFile = new File(System.getenv("APPDATA") + s + "Aurora" + s + "profile_data" + s 
							+ theUser.getUsername() + s + "profile" + s + "icon.png");
		
			if(!imageFile.exists()) {
				String url = "/default_data/profile_image/default_" + theUser.getDefaultColor() + ".png";
				imageFile = new File(HomePanel.class.getResource(url).getFile());
			}
					
			try {
				return Files.readAllBytes(imageFile.toPath());
			} catch (IOException e) {
				return null;
			}
		}
		
		//getViewedUser - returns the Profile object of the user currently being viewed.
		
		public Profile getViewedUser() {
			return theUser;
		}
		
		//updateUser - refreshes the users Profile object if the database has changed.
		
		public void updateUser(Profile user) {
			theSession.setUser(theModel.updateUser(user));
			
		}
		
		//setImage - sets the profileImage equal to an inputed image.
		
		public void setImage(Image img) {
			profileImage = img;
		}
		
		
		//isViewedUser - checks if the user being viewed is the active user.
		
		public boolean isViewedUser() {
			if(!theUser.equals(theSession.getUser())) {
				return true;
			} else {
				return false;
			}
		}
		
		//byteArraytoImage - converts an array of bytes into a java image object.
		
		private Image byteArraytoImage(byte[] bytes) {
			Image image = null;
			try {
				image = ImageIO.read(new ByteArrayInputStream(bytes));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return image;
		}
		
		
		//handleRequest - dictates what method should be called for each incoming request, 
		//returns the corresponding response.
		
		public Response handleRequest(Request request) {
			
			Response response = super.handleRequest(request);
			int index = request.getMethod();
			
			switch(index) {
			
			case 3 : response = new Response(getViewedUser());
					 break;
			case 4 : response = new Response(getProfileImage());
					 break;
			case 5 : Profile user = (Profile)request.getObject();
					 setDetails(user);
					 break;
			case 6 : user = (Profile)request.getObject();
					 updateUser(user);
					 break;
			case 7 : response.setSwitch(isViewedUser());
					 break;
			}
			
			return response;
		}
		
}
