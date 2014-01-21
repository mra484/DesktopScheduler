/**Desktop Schedule
 * Copyright(C) 2013 Mark Andrews
 * 
 *   Desktop Scheduler is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Desktop Scheduler is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *   
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 * Class for creating dialog windows for editing or deleting entries
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogWindow extends JDialog{
	private static final long serialVersionUID = 1L;
	public static final int DELETE_WINDOW = 0;
	public static final int EDIT_WINDOW = 1;
	public static final int OPTION_WINDOW = 2;
	
		private ControlPanel control;
		private Actions actions = new Actions();
		private MainWindow mainWindow;
		
		//option dialog window components
		private JLabel[] optionNames = {new JLabel("Display empty dates:")};
		private JCheckBox[] optionBoxes;
		private JLabel dateFormatLabel = new JLabel("Display format:");
		private JComboBox<String> dateFormat = new JComboBox<String>();
		private JPanel optionPanel = new JPanel();
		private GridBagConstraints c = new GridBagConstraints();
		private Entry dateName = new Entry("1/1/1");
		
		//entry dialog window components
		private String questionString = "Are you sure you want to delete the selected event?";
		private JLabel question = new JLabel(String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 180, questionString));
		private JButton[] buttons = {new JButton("Yes"), new JButton("No")};
		private JPanel buttonPanel = new JPanel();
		private int option;
		
		//option window constructor
		public DialogWindow(MainWindow mainWindow){
			this.mainWindow = mainWindow;
			setSize(290, 130);
			setVisible(false);
			setLocation(new Point(mainWindow.getX()+50, mainWindow.getY()+50));
			createComboBox();
			option = OPTION_WINDOW;
			arrangeWindow();
		}
		
		//edit/delete window constructor
		public DialogWindow(MainWindow mainWindow, ControlPanel controlPanel, int option){
			this.option = option;
			control = controlPanel;
			setSize(230, 260);
			setVisible(true);
			setLocation(new Point(mainWindow.getX()+100, mainWindow.getY()+100));

			setModal(true);
			setModalityType(ModalityType.APPLICATION_MODAL);
			
			arrangeWindow();
		}
		
		public void arrangeWindow(){
			int i = 0;
			setLayout(new BorderLayout());
			buttonPanel.setLayout(new FlowLayout());
			
			//create check boxes
			if( option == OPTION_WINDOW){
				optionBoxes = new JCheckBox[optionNames.length];
				for(i = 0 ; i < optionNames.length ; i++ ){
					optionBoxes[i] = new JCheckBox();
					optionBoxes[i].addActionListener(actions);
				}
			}
			//adds action listener to buttons and adds buttons to panel
			for( i = 0 ; i < buttons.length ; i++ ){
				buttons[i].addActionListener(actions);
				buttonPanel.add(buttons[i]);
			}
			
			//Create window appearance depending on the situation
			switch (option) {
			case OPTION_WINDOW:
				setTitle("Options");
				buttons[1].setText("Close");
				buttonPanel.remove(buttons[0]);
				optionPanel.setLayout(new GridBagLayout());
				c.fill = GridBagConstraints.NONE;
				c.anchor = GridBagConstraints.NORTHWEST;
				
				//list each option
				for( i = 0 ; i < optionNames.length ; i++ ){
					c.gridx = i;
					c.gridy = i;
					optionPanel.add(optionNames[i], c);
					
					c.gridx = 1;
					optionPanel.add(optionBoxes[i], c);
				}
				c.gridx = 0;
				c.gridy = i+1;
				optionPanel.add(dateFormatLabel, c);
				
				c.gridx = 1;
				optionPanel.add(dateFormat, c);
				
				add(optionPanel, BorderLayout.WEST);
				add(buttonPanel, BorderLayout.SOUTH);
				break;
				
			case DELETE_WINDOW:
				setTitle("Confirm event delete");
				add(question, BorderLayout.NORTH);
				add(control, BorderLayout.CENTER);
				add(buttonPanel, BorderLayout.SOUTH);
				break;
				
			case EDIT_WINDOW:
				setTitle("Edit the current event");
				buttons[0].setText("Edit");
				buttons[1].setText("Cancel");
				add(control, BorderLayout.CENTER);
				add(buttonPanel, BorderLayout.SOUTH);
			}
		}
		private void createComboBox(){
			int temp = Entry.format;
			Entry dateName = new Entry("1/1/1");
			for(int i = 0 ; i < 6 ; i++ ){
				dateName.setFormat(i);
				dateFormat.addItem(dateName.getName());
			}
			dateName.setFormat(temp);
			dateFormat.addActionListener(actions);
		}
		private class Actions implements ActionListener{
			public void actionPerformed(ActionEvent e){
				Object source = e.getSource();
				
				//change in dateFormat
				if(source == dateFormat){
					dateName.setFormat(dateFormat.getSelectedIndex());
					mainWindow.updateDateNames();
					return;
					
				//change in other options
				} else if (source instanceof JCheckBox) {
					int i;
					for(i = 0 ; i < optionNames.length ; i++){
						if( source == optionBoxes[i])
							break;
					}
					switch(i){
					case 0:
						MainWindow.emptyDates = optionBoxes[i].isSelected();
						break;
					default:
						System.out.println("unformatted option selected");
					}
					return;
				}
				switch (option){
				
				case DELETE_WINDOW:
					//"Yes" selected from delete window, close on no
					if( e.getSource() == buttons[0] )
						control.delete();
					break;
					
				case EDIT_WINDOW:
					//"edit" selected from the edit window, close on cancel
					if( e.getSource() == buttons[0] ){
						control.delete();
						control.add();
					}
					break;
					
				case OPTION_WINDOW:
					//hide option window on close
					setVisible(false);
					return;
				default:
					break;
				}
				dispose();
			}
		}
		
	}
