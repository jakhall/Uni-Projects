	package aurora.view;

	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import javax.swing.*;
	import aurora.controller.BaseController;
	import aurora.model.Profile;
	import java.awt.Dimension;
	import javax.swing.border.MatteBorder;

	import java.awt.Color;
	import java.awt.Component;
import java.awt.Image;
import java.awt.Font;


public class MainPanel extends BasePanel{

		protected JLabel passwordLabel;
		protected JButton logoutBtn;
		protected JPanel headerPanel;
		protected JLabel sessionLabel;
		protected JLabel lblAurora;
		protected JPanel footerPanel;
		protected JButton backBtn;
		protected JPanel contentPanel;
		protected SpringLayout sl_footerPanel;
		protected SpringLayout sl_headerPanel;
		protected JTextField searchField;
		protected JButton btnSearch;
		
		public MainPanel(BaseController baseController) {
			super(baseController);
		}
		
		public MainPanel() {
			super();
		}
		
		//setupPanel - adds all the visual elements, defines their properties.
		
		protected void setupPanel() {
			
			this.setPreferredSize(new Dimension(500, 600));
			
			this.setLayout(baseLayout);
			
			headerPanel = new JPanel();
			headerPanel.setLocation(0, 0);
			baseLayout.putConstraint(SpringLayout.SOUTH, headerPanel, -539, SpringLayout.SOUTH, this);
			headerPanel.setBackground(new Color(169, 169, 169));
			headerPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			baseLayout.putConstraint(SpringLayout.EAST, headerPanel, 0, SpringLayout.EAST, this);
			baseLayout.putConstraint(SpringLayout.NORTH, headerPanel, 0, SpringLayout.NORTH, this);
			baseLayout.putConstraint(SpringLayout.WEST, headerPanel, 0, SpringLayout.WEST, this);
			headerPanel.setSize(new Dimension(500, 75));
			headerPanel.setAlignmentY(Component.TOP_ALIGNMENT);
			headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
			add(headerPanel);
			sl_headerPanel = new SpringLayout();
			headerPanel.setLayout(sl_headerPanel);
			
			sessionLabel = new JLabel("Logged in as:");
			sl_headerPanel.putConstraint(SpringLayout.NORTH, sessionLabel, 20, SpringLayout.NORTH, headerPanel);
			sl_headerPanel.putConstraint(SpringLayout.EAST, sessionLabel, -24, SpringLayout.EAST, headerPanel);
			sessionLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			headerPanel.add(sessionLabel);
			
			lblAurora = new JLabel("Aurora");
			sl_headerPanel.putConstraint(SpringLayout.WEST, lblAurora, 10, SpringLayout.WEST, headerPanel);
			sl_headerPanel.putConstraint(SpringLayout.SOUTH, lblAurora, -9, SpringLayout.SOUTH, headerPanel);
			ImageIcon logo = new ImageIcon(new ImageIcon(LoginPanel.class.getResource("/default_data/profile_image/aurora_shadow.png")).getImage().getScaledInstance(50, 43, Image.SCALE_DEFAULT));
			lblAurora.setIcon(logo);
			headerPanel.add(lblAurora);
			
			footerPanel = new JPanel();
			footerPanel.setLocation(0, 535);
			footerPanel.setSize(500, 65);
			baseLayout.putConstraint(SpringLayout.WEST, footerPanel, 0, SpringLayout.WEST, this);
			baseLayout.putConstraint(SpringLayout.SOUTH, footerPanel, 0, SpringLayout.SOUTH, this);
			baseLayout.putConstraint(SpringLayout.EAST, footerPanel, 0, SpringLayout.EAST, this);
			footerPanel.setBackground(new Color(169, 169, 169));
			footerPanel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			add(footerPanel);
			sl_footerPanel = new SpringLayout();
			footerPanel.setLayout(sl_footerPanel);
			
			logoutBtn = new JButton("Log Out");
			sl_footerPanel.putConstraint(SpringLayout.SOUTH, logoutBtn, -22, SpringLayout.SOUTH, footerPanel);
			sl_footerPanel.putConstraint(SpringLayout.EAST, logoutBtn, -31, SpringLayout.EAST, footerPanel);
			footerPanel.add(logoutBtn);
			
			backBtn = new JButton("Back");
			sl_footerPanel.putConstraint(SpringLayout.NORTH, backBtn, 0, SpringLayout.NORTH, logoutBtn);
			sl_footerPanel.putConstraint(SpringLayout.WEST, backBtn, 30, SpringLayout.WEST, footerPanel);
			sl_footerPanel.putConstraint(SpringLayout.EAST, backBtn, 101, SpringLayout.WEST, footerPanel);
			footerPanel.add(backBtn);
			
			contentPanel = new JPanel();
			contentPanel.setLocation(0, 73);
			contentPanel.setSize(500, 464);
			baseLayout.putConstraint(SpringLayout.NORTH, footerPanel, 0, SpringLayout.SOUTH, contentPanel);
			baseLayout.putConstraint(SpringLayout.NORTH, contentPanel, 61, SpringLayout.NORTH, this);
			baseLayout.putConstraint(SpringLayout.SOUTH, contentPanel, -61, SpringLayout.SOUTH, this);
			

			
			searchField = new JTextField();
			sl_headerPanel.putConstraint(SpringLayout.NORTH, lblAurora, -10, SpringLayout.NORTH, searchField);
			sl_headerPanel.putConstraint(SpringLayout.EAST, lblAurora, -6, SpringLayout.WEST, searchField);
			sl_headerPanel.putConstraint(SpringLayout.NORTH, searchField, 18, SpringLayout.NORTH, headerPanel);
			sl_headerPanel.putConstraint(SpringLayout.WEST, searchField, 132, SpringLayout.WEST, headerPanel);
			sl_headerPanel.putConstraint(SpringLayout.SOUTH, searchField, -18, SpringLayout.SOUTH, headerPanel);
			headerPanel.add(searchField);
			searchField.setColumns(10);
			
			btnSearch = new JButton("Search");
			sl_headerPanel.putConstraint(SpringLayout.EAST, searchField, -5, SpringLayout.WEST, btnSearch);
			sl_headerPanel.putConstraint(SpringLayout.WEST, btnSearch, 263, SpringLayout.WEST, headerPanel);
			sl_headerPanel.putConstraint(SpringLayout.WEST, sessionLabel, 21, SpringLayout.EAST, btnSearch);
			sl_headerPanel.putConstraint(SpringLayout.NORTH, btnSearch, 18, SpringLayout.NORTH, headerPanel);
			sl_headerPanel.putConstraint(SpringLayout.SOUTH, btnSearch, -18, SpringLayout.SOUTH, headerPanel);
			sl_headerPanel.putConstraint(SpringLayout.EAST, btnSearch, -173, SpringLayout.EAST, headerPanel);
			btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 9));
			headerPanel.add(btnSearch);
			ImageIcon logoImage = new ImageIcon(new ImageIcon("C:\\Users\\Jakha\\Desktop\\Project_Files_5\\Aurora\\resources\\Aurora_Shadow.png").getImage().getScaledInstance(45, 38, Image.SCALE_DEFAULT));
			
			baseLayout.putConstraint(SpringLayout.WEST, contentPanel, 0, SpringLayout.WEST, this);
			baseLayout.putConstraint(SpringLayout.EAST, contentPanel, 0, SpringLayout.EAST, this);
			add(contentPanel);
			contentPanel.setLayout(new SpringLayout());
			
		}
		
		//setupData - adds all the necessary data retrieved from the controller.
		
		public void setupData() {
			Profile user = theController.getUser();
		    sessionLabel.setText("Logged in as:  " + user.getFirstName());
		}
		
		//setupListeners - adds listeners to all of the necessary fields and buttons.
		
		protected void setupListeners() {
			logoutBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent click) {
					theController.logout();
				}
			});
			
			backBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent click) {
					theController.back();
				}
			});
			
			btnSearch.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent click) {
					theController.search(searchField.getText());
				}
			});
		}
}