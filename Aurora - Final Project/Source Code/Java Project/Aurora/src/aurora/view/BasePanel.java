package aurora.view;


import javax.swing.*;
import aurora.controller.BaseController;

/**
 * 
 * Base Panel - Defines the basic functionality of all panels. 
 *
 */

public class BasePanel extends JPanel{

		protected	BaseController theController;
		protected SpringLayout baseLayout;
		
		public BasePanel(BaseController baseController) {
			theController = baseController;
			baseLayout = new SpringLayout();
		}
		
		public BasePanel() {
			setLayout(new SpringLayout());
			baseLayout = new SpringLayout();
			
			setupPanel();
			setupData();
			setupListeners();
		}
		
		
		protected void setupPanel() {
		}
		
		public void setupData() {
		}
		
		
		protected void setupListeners() {
			
		}
}

