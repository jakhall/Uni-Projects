package aurora.controller;

import java.io.File;
import java.io.IOException;

/**
 * 
 * Login Launcher - Creates a new desktop GUI window, can be used to interact with the system.
 *
 */


public class LoginLauncher {
	
	public static void main(String[] args) {
		System.out.println("Connected to Aurora..");
		createStructure();
		LoginController loginController = new LoginController();
		loginController.start();
	}
	
	
	private static void createStructure() {
		formFiles();
		formDatabase();
	}
	
	//createStructure - creates the file structure necessary to store user data (text and images).
	private static void formFiles() {
		String dir = System.getenv("APPDATA");
		dir = dir + File.separator + "Aurora" + File.separator + "profile_data";
		File mainFolder = new File(dir);
		createDir(mainFolder);
	}
	
	//createDir - construct the necessary directory if it does not already exist. 
	private static void createDir(File f) {
		if (!f.exists()) {
			   f.mkdirs();
			   System.out.println("Directory created..");
			}
	}
	
	
	private static void formDatabase() {
		
	}
	
}
