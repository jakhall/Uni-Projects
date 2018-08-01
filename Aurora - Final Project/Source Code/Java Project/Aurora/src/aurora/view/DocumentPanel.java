package aurora.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import aurora.controller.BaseController;
import aurora.controller.DocumentController;
import aurora.controller.HomeController;
import aurora.controller.LoginController;
import aurora.model.Document;
import aurora.model.LoginModel;
import aurora.model.Profile;
import aurora.model.Session;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.MatteBorder;
import java.awt.Insets;

public class DocumentPanel extends MainPanel {
	
	private DocumentController theController;
	
	private JTextArea documentText;
	private JLabel postedByLbl;
	private JScrollPane textScroll;
	private JLabel lblTitle;
	private JLabel lblPostedBy;
	private JButton btnProfile;
	
	public DocumentPanel(DocumentController documentController) {
		super(documentController);
		theController = documentController;
		
		setupPanel();
		setupData();
		setupListeners();
	}
	
	//setupPanel - adds all the visual elements, defines their properties.
	
	protected void setupPanel() {
		super.setupPanel();
		this.setPreferredSize(new Dimension(500, 600));
		SpringLayout springLayout = (SpringLayout) contentPanel.getLayout();
		
		textScroll = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, textScroll, 83, SpringLayout.NORTH, contentPanel);
		springLayout.putConstraint(SpringLayout.WEST, textScroll, 52, SpringLayout.WEST, contentPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, textScroll, 413, SpringLayout.NORTH, contentPanel);
		springLayout.putConstraint(SpringLayout.EAST, textScroll, -48, SpringLayout.EAST, contentPanel);
		contentPanel.add(textScroll);
		
		documentText = new JTextArea();
		documentText.setWrapStyleWord(true);
		documentText.setLineWrap(true);
		documentText.setEditable(false);
		textScroll.setViewportView(documentText);
		
		lblTitle = new JLabel("Title");
		springLayout.putConstraint(SpringLayout.WEST, lblTitle, 52, SpringLayout.WEST, contentPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, lblTitle, -6, SpringLayout.NORTH, textScroll);
		contentPanel.add(lblTitle);
		
		lblPostedBy = new JLabel("Post By:");
		springLayout.putConstraint(SpringLayout.WEST, lblPostedBy, 171, SpringLayout.EAST, lblTitle);
		springLayout.putConstraint(SpringLayout.SOUTH, lblPostedBy, -6, SpringLayout.NORTH, textScroll);
		springLayout.putConstraint(SpringLayout.EAST, lblPostedBy, -48, SpringLayout.EAST, contentPanel);
		lblPostedBy.setHorizontalAlignment(SwingConstants.TRAILING);
		contentPanel.add(lblPostedBy);
		
		btnProfile = new JButton("Profile");
		springLayout.putConstraint(SpringLayout.SOUTH, btnProfile, -6, SpringLayout.NORTH, lblPostedBy);
		springLayout.putConstraint(SpringLayout.EAST, btnProfile, 0, SpringLayout.EAST, textScroll);
		contentPanel.add(btnProfile);
		
		
	}
	
	//setupData - adds all the necessary data retrieved from the controller.
	
	public void setupData() {
		super.setupData();
	    Document doc = theController.getDocument();
	    lblTitle.setText(doc.getTitle());
	    documentText.setText(doc.getText());
	    lblPostedBy.setText("Posted By: " + doc.getAuthor().getUsername());
	}
	
	
	//setupListeners - adds listeners to all of the necessary fields and buttons.
	protected void setupListeners() {
		super.setupListeners();
		
		btnProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				theController.viewUser(theController.getAuthor().getUserID());
			}
		});
		

	}
}