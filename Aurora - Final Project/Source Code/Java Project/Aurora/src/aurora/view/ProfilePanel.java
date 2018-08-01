package aurora.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import aurora.controller.BaseController;
import aurora.controller.HomeController;
import aurora.controller.LoginController;
import aurora.controller.ProfileController;
import aurora.model.Document;
import aurora.model.LoginModel;
import aurora.model.Profile;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.Dimension;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import javax.swing.GroupLayout.Alignment;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

public class ProfilePanel extends MainPanel {
	
	protected ProfileController theController;
	protected JPanel basePanel;
	
	
	protected JTextField fldUsername;
	protected JPanel profilePanel;
	protected JLabel lblProfile;
	protected JLabel lblUserAurora;
	protected JLabel lblObservers;
	protected JLabel lblObserving;
	protected JLabel lblUsername;
	protected JButton btnView;
	protected JScrollPane auroraScroll;
	protected JScrollPane observingScroll;
	protected JScrollPane observerScroll;
	protected JList documentList;
	protected JList observerList;
	protected JList observingList;
	private JLabel profileIcon;
	private JTextField fldFirstName;
	private JLabel lblFirstName;
	private JLabel lblAge;
	private JLabel lblTitle;
	private JTextField fldEmail;
	private JTextField fldTitle;
	private JTextField fldLastName;
	private JLabel lblEmail;
	private JTextField fldAge;
	private JButton btnObserve;
	private JButton btnRemove;
	
	public ProfilePanel(ProfileController profileController) {
		super(profileController);
		theController = profileController;
		setupPanel();
		setupData();
		setupListeners();
	}
	
	//setupPanel - adds all the visual elements, defines their properties.
	
	protected void setupPanel() {
		super.setupPanel();
		this.setPreferredSize(new Dimension(500, 600));
		SpringLayout springLayout = (SpringLayout) contentPanel.getLayout();
		
		profilePanel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, profilePanel, 46, SpringLayout.NORTH, contentPanel);
		springLayout.putConstraint(SpringLayout.WEST, profilePanel, 56, SpringLayout.WEST, contentPanel);
		springLayout.putConstraint(SpringLayout.EAST, profilePanel, -67, SpringLayout.EAST, contentPanel);
		profilePanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		contentPanel.add(profilePanel);
		SpringLayout sl_profilePanel = new SpringLayout();
		profilePanel.setLayout(sl_profilePanel);
		
		lblProfile = new JLabel("Profile");
		springLayout.putConstraint(SpringLayout.WEST, lblProfile, 0, SpringLayout.WEST, profilePanel);
		springLayout.putConstraint(SpringLayout.SOUTH, lblProfile, -6, SpringLayout.NORTH, profilePanel);
		contentPanel.add(lblProfile);
		
		lblUserAurora = new JLabel("Aurora");
		springLayout.putConstraint(SpringLayout.SOUTH, profilePanel, -18, SpringLayout.NORTH, lblUserAurora);
		springLayout.putConstraint(SpringLayout.WEST, lblUserAurora, 56, SpringLayout.WEST, contentPanel);
		contentPanel.add(lblUserAurora);
		
		lblObserving = new JLabel("Observing");
		springLayout.putConstraint(SpringLayout.NORTH, lblObserving, 0, SpringLayout.NORTH, lblUserAurora);
		contentPanel.add(lblObserving);
		
		lblObservers = new JLabel("Observers");
		springLayout.putConstraint(SpringLayout.WEST, lblObserving, 0, SpringLayout.WEST, lblObservers);
		contentPanel.add(lblObservers);
		
		btnView = new JButton("View");
		springLayout.putConstraint(SpringLayout.WEST, btnView, 56, SpringLayout.WEST, contentPanel);
		contentPanel.add(btnView);
		
		auroraScroll = new JScrollPane();
		springLayout.putConstraint(SpringLayout.WEST, lblObservers, 26, SpringLayout.EAST, auroraScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, lblUserAurora, -6, SpringLayout.NORTH, auroraScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, auroraScroll, -72, SpringLayout.SOUTH, contentPanel);
		springLayout.putConstraint(SpringLayout.EAST, auroraScroll, -275, SpringLayout.EAST, contentPanel);
		springLayout.putConstraint(SpringLayout.WEST, auroraScroll, 56, SpringLayout.WEST, contentPanel);
		springLayout.putConstraint(SpringLayout.NORTH, auroraScroll, 229, SpringLayout.NORTH, contentPanel);
		auroraScroll.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		contentPanel.add(auroraScroll);
		
		lblUsername = new JLabel("Username:");
		profilePanel.add(lblUsername);
		
		fldUsername = new JTextField();
		sl_profilePanel.putConstraint(SpringLayout.EAST, lblUsername, -7, SpringLayout.WEST, fldUsername);
		sl_profilePanel.putConstraint(SpringLayout.WEST, fldUsername, 179, SpringLayout.WEST, profilePanel);
		sl_profilePanel.putConstraint(SpringLayout.EAST, fldUsername, -10, SpringLayout.EAST, profilePanel);
		profilePanel.add(fldUsername);
		fldUsername.setColumns(10);
		
		observingScroll = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, observingScroll, 6, SpringLayout.SOUTH, lblObserving);
		springLayout.putConstraint(SpringLayout.WEST, observingScroll, 26, SpringLayout.EAST, auroraScroll);
		springLayout.putConstraint(SpringLayout.SOUTH, observingScroll, -22, SpringLayout.NORTH, lblObservers);
		springLayout.putConstraint(SpringLayout.EAST, observingScroll, -67, SpringLayout.EAST, contentPanel);
		observingScroll.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		
		documentList = new JList();
		auroraScroll.setViewportView(documentList);
		contentPanel.add(observingScroll);
		
		observingList = new JList();
		observingScroll.setViewportView(observingList);
		
		JScrollPane observerScroll = new JScrollPane();
		springLayout.putConstraint(SpringLayout.SOUTH, btnView, 0, SpringLayout.SOUTH, observerScroll);
		springLayout.putConstraint(SpringLayout.EAST, btnView, -26, SpringLayout.WEST, observerScroll);
		springLayout.putConstraint(SpringLayout.NORTH, observerScroll, 346, SpringLayout.NORTH, contentPanel);
		springLayout.putConstraint(SpringLayout.WEST, observerScroll, 251, SpringLayout.WEST, contentPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, observerScroll, -33, SpringLayout.SOUTH, contentPanel);
		springLayout.putConstraint(SpringLayout.EAST, observerScroll, -67, SpringLayout.EAST, contentPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, lblObservers, -6, SpringLayout.NORTH, observerScroll);
		observerScroll.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		contentPanel.add(observerScroll);
		
		observerList = new JList();
		observerScroll.setViewportView(observerList);
		
		
		profileIcon = new JLabel("");
		sl_profilePanel.putConstraint(SpringLayout.NORTH, profileIcon, 15, SpringLayout.NORTH, profilePanel);
		sl_profilePanel.putConstraint(SpringLayout.WEST, profileIcon, 13, SpringLayout.WEST, profilePanel);
		sl_profilePanel.putConstraint(SpringLayout.SOUTH, profileIcon, -36, SpringLayout.SOUTH, profilePanel);
		sl_profilePanel.putConstraint(SpringLayout.EAST, profileIcon, -6, SpringLayout.WEST, lblUsername);
		profileIcon.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		profilePanel.add(profileIcon);
		
		
		fldFirstName = new JTextField();
		sl_profilePanel.putConstraint(SpringLayout.NORTH, fldUsername, 10, SpringLayout.SOUTH, fldFirstName);
		sl_profilePanel.putConstraint(SpringLayout.SOUTH, fldUsername, 30, SpringLayout.SOUTH, fldFirstName);
		sl_profilePanel.putConstraint(SpringLayout.NORTH, fldFirstName, 15, SpringLayout.NORTH, profilePanel);
		sl_profilePanel.putConstraint(SpringLayout.SOUTH, fldFirstName, 35, SpringLayout.NORTH, profilePanel);
		profilePanel.add(fldFirstName);
		fldFirstName.setColumns(10);
		
		lblFirstName = new JLabel("Name:");
		sl_profilePanel.putConstraint(SpringLayout.NORTH, lblUsername, 15, SpringLayout.SOUTH, lblFirstName);
		sl_profilePanel.putConstraint(SpringLayout.WEST, fldFirstName, 7, SpringLayout.EAST, lblFirstName);
		sl_profilePanel.putConstraint(SpringLayout.NORTH, lblFirstName, 14, SpringLayout.NORTH, profilePanel);
		sl_profilePanel.putConstraint(SpringLayout.EAST, lblFirstName, -202, SpringLayout.EAST, profilePanel);
		profilePanel.add(lblFirstName);
		
		lblAge = new JLabel("Age:");
		sl_profilePanel.putConstraint(SpringLayout.NORTH, lblAge, 13, SpringLayout.SOUTH, lblUsername);
		sl_profilePanel.putConstraint(SpringLayout.EAST, lblAge, 0, SpringLayout.EAST, lblUsername);
		profilePanel.add(lblAge);
		
		lblTitle = new JLabel("Title:");
		sl_profilePanel.putConstraint(SpringLayout.NORTH, lblTitle, 0, SpringLayout.NORTH, lblAge);
		lblTitle.setHorizontalAlignment(SwingConstants.TRAILING);
		profilePanel.add(lblTitle);
		
		fldEmail = new JTextField();
		sl_profilePanel.putConstraint(SpringLayout.SOUTH, fldEmail, -18, SpringLayout.SOUTH, profilePanel);
		sl_profilePanel.putConstraint(SpringLayout.EAST, fldEmail, -10, SpringLayout.EAST, profilePanel);
		profilePanel.add(fldEmail);
		fldEmail.setColumns(10);
		
		fldTitle = new JTextField();
		sl_profilePanel.putConstraint(SpringLayout.EAST, lblTitle, -6, SpringLayout.WEST, fldTitle);
		sl_profilePanel.putConstraint(SpringLayout.NORTH, fldTitle, 10, SpringLayout.SOUTH, fldUsername);
		sl_profilePanel.putConstraint(SpringLayout.SOUTH, fldTitle, -48, SpringLayout.SOUTH, profilePanel);
		fldTitle.setColumns(10);
		profilePanel.add(fldTitle);
		
		fldLastName = new JTextField();
		sl_profilePanel.putConstraint(SpringLayout.WEST, fldTitle, 35, SpringLayout.WEST, fldLastName);
		sl_profilePanel.putConstraint(SpringLayout.EAST, fldTitle, 0, SpringLayout.EAST, fldLastName);
		sl_profilePanel.putConstraint(SpringLayout.NORTH, fldLastName, 0, SpringLayout.NORTH, fldFirstName);
		sl_profilePanel.putConstraint(SpringLayout.SOUTH, fldLastName, 0, SpringLayout.SOUTH, fldFirstName);
		sl_profilePanel.putConstraint(SpringLayout.EAST, fldFirstName, -10, SpringLayout.WEST, fldLastName);
		sl_profilePanel.putConstraint(SpringLayout.WEST, fldLastName, -100, SpringLayout.EAST, profilePanel);
		sl_profilePanel.putConstraint(SpringLayout.EAST, fldLastName, -9, SpringLayout.EAST, profilePanel);
		fldLastName.setColumns(10);
		profilePanel.add(fldLastName);
		
		lblEmail = new JLabel("Email:");
		sl_profilePanel.putConstraint(SpringLayout.WEST, fldEmail, 7, SpringLayout.EAST, lblEmail);
		sl_profilePanel.putConstraint(SpringLayout.NORTH, lblEmail, 1, SpringLayout.NORTH, fldEmail);
		sl_profilePanel.putConstraint(SpringLayout.EAST, lblEmail, 0, SpringLayout.EAST, lblUsername);
		profilePanel.add(lblEmail);
		
		fldAge = new JTextField();
		sl_profilePanel.putConstraint(SpringLayout.NORTH, fldEmail, 7, SpringLayout.SOUTH, fldAge);
		sl_profilePanel.putConstraint(SpringLayout.WEST, lblTitle, 8, SpringLayout.EAST, fldAge);
		sl_profilePanel.putConstraint(SpringLayout.EAST, fldAge, 0, SpringLayout.EAST, fldFirstName);
		sl_profilePanel.putConstraint(SpringLayout.WEST, fldAge, 6, SpringLayout.EAST, lblAge);
		sl_profilePanel.putConstraint(SpringLayout.NORTH, fldAge, 10, SpringLayout.SOUTH, fldUsername);
		sl_profilePanel.putConstraint(SpringLayout.SOUTH, fldAge, 30, SpringLayout.SOUTH, fldUsername);
		profilePanel.add(fldAge);
		fldAge.setColumns(10);
		
		btnRemove = new JButton("Remove");
		springLayout.putConstraint(SpringLayout.NORTH, btnRemove, -4, SpringLayout.NORTH, lblProfile);
		springLayout.putConstraint(SpringLayout.EAST, btnRemove, 0, SpringLayout.EAST, profilePanel);
		contentPanel.add(btnRemove);
		
		btnObserve = new JButton("Observe");
		springLayout.putConstraint(SpringLayout.NORTH, btnObserve, -4, SpringLayout.NORTH, lblProfile);
		springLayout.putConstraint(SpringLayout.EAST, btnObserve, 0, SpringLayout.EAST, profilePanel);
		contentPanel.add(btnObserve);
		
	}
	
	
	//setupData - adds all the necessary data retrieved from the controller.
	
	public void setupData() {
		super.setupData();
		
		updateDetails();
	    updateDocuments();
	    updateObservers();
	    updateObserving();
	    updateObserveButtons();
	    
	    try {
			updateProfileIcon();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	//updateDetails - update the profile fields with the user details.
	
	private void updateDetails() {
		Profile user = theController.getViewedUser();
		fldUsername.setText(user.getUsername());
		fldFirstName.setText(user.getFirstName());
		fldLastName.setText(user.getLastName());
		fldTitle.setText(user.getTitle());
		fldEmail.setText(user.getEmail());
		fldAge.setText(user.getDOB());
	}
	
	//updateObserveButton - check if the remove or observe button should be visible.
	
	private void updateObserveButtons(){
		if(theController.isObserving()) {
			btnObserve.setVisible(false);
			btnRemove.setVisible(true);
		} else {
			btnObserve.setVisible(true);
			btnRemove.setVisible(false);
		}
	}
	

	//updateProfileIcon - gets the users profile image from their profile folder adds it to the view.
	
	private void updateProfileIcon() throws IOException {
		
		String s =  File.separator;
		
		String dir = System.getenv("APPDATA") + s + "Aurora" + s + "profile_data" + s 
					+ theController.getViewedUsername() + s + "profile" + s + "icon.png";
		
		File imageFile = new File(dir);
		
		BufferedImage icon;
		
		if(imageFile.exists()) {
			icon = ImageIO.read(imageFile);
		} else {
			String url = "/default_data/profile_image/default_" + theController.getViewedUser().getDefaultColor() + ".png";
			icon = ImageIO.read(HomePanel.class.getResource(url));
		}
		profileIcon.setIcon(new ImageIcon(icon.getScaledInstance(90, 90, Image.SCALE_DEFAULT)));

	}
	
	//updateDocuments - get all the documents created by the user and updates the view.
	
	private void updateDocuments() {
	    String[] docTitles = theController.getUserDocsTitle();
	    documentList.setListData(docTitles);
	    lblUserAurora.setText("Documents: (" + theController.getNumOfDocuments() + ")");
	}
	
	//updateObserving - retrieves the list of profiles the user is observing and updates the view.
	
	private void updateObserving() {
		 String[] observing = theController.getObservingUsername();
		 observingList.setListData(observing);
		 lblObserving.setText("Observing: (" + theController.getNumOfObserving() + ")");
	}
	
	//updateObservers - retrieves the list of profiles that are observing the user and updates the view.
	
	private void updateObservers() {
		   String[] observers = theController.getObserversUsername();
		    observerList.setListData(observers);
		    lblObservers.setText("Observers: (" + theController.getNumOfObservers() + ")");
	}
	
	//setupListeners - adds listeners to all of the necessary fields and buttons.
	
	protected void setupListeners() {
		super.setupListeners();
		btnView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				theController.viewDocument(documentList.getSelectedIndex());
			}
		});
		
		btnObserve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				theController.addObserving();
				updateObserveButtons();
				updateObservers();
			}
		});
		
	    observerList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent click) {
				theController.viewObserver(observerList.getSelectedIndex());
			}
	      });
	    
	    observingList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent click) {
				theController.viewObserving(observingList.getSelectedIndex());
			}
	      });
	}
}