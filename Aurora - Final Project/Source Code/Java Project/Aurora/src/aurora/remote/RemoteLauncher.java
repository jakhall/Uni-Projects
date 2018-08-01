package aurora.remote;

import java.io.File;

public class RemoteLauncher {
	public static void main(String[] args) {
		createStructure();
		LoginInvoker loginInvoker = new LoginInvoker();
	}
	
	private static void createStructure() {
		formFiles();
		formDatabase();
	}
	
	
	private static void formFiles() {
		String dir = System.getenv("APPDATA");
		dir = dir + File.separator + "Aurora" + File.separator + "profile_data";
		File mainFolder = new File(dir);
		createDir(mainFolder);
	}
	
	private static void createDir(File f) {
		if (!f.exists()) {
			   f.mkdirs();
			   System.out.println("Directory created..");
			}
	}
	
	
	private static void formDatabase() {
		
	}
	
}
