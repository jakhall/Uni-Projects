package aurora.model;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.imageio.ImageIO;


/**
 * 
 * Register Model - The model for the register views of the system, interacts with the 
 * database and vector space model.
 *
 */


public class RegisterModel extends BaseModel {
	
	ProfileDatabase profileDB;
	Document[] documents = null;
	Profile[] observers = null;
	Profile[] observing = null;
	
	//Stores the connection to the database, passed from the login controller.
	
	public RegisterModel(Connection conn) {
		this.conn = conn;
		profileDB = new ProfileDatabase(conn);
	}

	public ProfileDatabase getProfileDatabase() {
		return profileDB;
	}
	
	
	//registerUser - Stores profile image in new profile folder, inserts new User object into USER_Table.
	
	public Profile registerUser(Profile user) {
		String location; 
		
		if(user.getImage() != null) {
			location="\\" + user.getUsername() + "\\profile\\icon.png";
			setProfileImage(user);
		} else {
			location= user.getDefaultColor();
		}
		
		String sql = "INSERT INTO USER_Table (Username, Password, DOB, FirstName, LastName, DefaultColor, Title, Email, ImageLocation)"
					+ " VALUES ('" + user.getUsername() + "', '" + user.getPassword() + "', '" 
					+ user.getDOB() + "', '" + user.getFirstName() + "', '" + user.getLastName() 
					+ "', '" + user.getDefaultColor() + "', '" + user.getTitle() +"', '" + user.getEmail() + "', '" + location +  "')";
		
		profileDB.updateDatabase(sql);
		
		formatFolders(user);
		
		return profileDB.getLogin(user.getUsername(), user.getPassword());
		
	}
	
	
	//setProfileImage - gets the folder specific to the users profile, saves the image stored in the user object.
	//updates the image location for the users database record. 
	
	public void setProfileImage(Profile user) {	
		String s = File.separator;
		String location = s + theSession.getUser().getUsername() + s + "profile" + s + "icon.png";
		String url = System.getenv("APPDATA") + s + "Aurora" + s + "profile_data" + location;
		Image image = byteArraytoImage(user.getImage());
		try {
			ImageIO.write((RenderedImage)image, "png", new File(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	
	public boolean usernameTaken(String username) {
		String sql = "SELECT * FROM USER_Table WHERE Username='" + username + "'";
		ResultSet rs = profileDB.queryDatabase(sql);
		try {
			while(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	//byteArraytoImage - converts a byte array stored in user object to an image object.
	
	private Image byteArraytoImage(byte[] bytes) {
		Image image = null;
		try {
			image = ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	//formatFolders - Creates file structure for the new users profile.
	
	
	private void formatFolders(Profile user) {
		String s = File.separator;
		ArrayList<String> dirs = new ArrayList<String>();
		String profileURL = System.getenv("APPDATA") + s + "Aurora" + s + "profile_data" + s + user.getUsername();
		dirs.add(profileURL);
		dirs.add(profileURL + s + "profile");
		profileURL = profileURL + s + "docs";
		dirs.add(profileURL);
		dirs.add(profileURL + s + "image");
		dirs.add(profileURL + s + "text");
		for(String dir : dirs) {
			createDir(dir);
		}
	}
	
	
	//createDir - Creates new file directory if the location does not already exist.
	
	private void createDir(String url){
		File f = new File(url);
		if (!f.exists()) {
				f.mkdirs();
			}
	}
	
	//createSession - Adds the associated users to the profile being logged in,
	//creates then returns a new session object. 
	
	public Session createSession(Profile user) {
			profileDB.addUserLinks(user);
			theSession = new Session(user, conn);
			return theSession;
	}


}