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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogWindow extends JDialog{
	private static final long serialVersionUID = 1L;
	public static final int DELETE_WINDOW = 0;
	public static final int EDIT_WINDOW = 1;
		private ControlPanel control;
		
		private Actions actions = new Actions();
		private String questionString = "Are you sure you want to delete the selected event?";
		private JLabel question = new JLabel(String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 180, questionString));
		private JButton[] buttons = {new JButton("Yes"), new JButton("No")};
		private JPanel buttonPanel = new JPanel();
		private int option;
		
		public DialogWindow(MainWindow mainWindow, ControlPanel controlPanel, int option){
			this.option = option;
			control = controlPanel;
			setSize(230, 260);
			setVisible(true);
			setLocation(new Point(mainWindow.getX()+100, mainWindow.getY()+100));

			setModal(true);
			setModalityType(ModalityType.APPLICATION_MODAL);
			setLayout(new BorderLayout());
			buttonPanel.setLayout(new FlowLayout());
			
			//adds action listener to buttons and adds buttons to panel
			for( int i = 0 ; i < buttons.length ; i++ ){
				buttons[i].addActionListener(actions);
				buttonPanel.add(buttons[i]);
			}
			
			//Create window appearance depending on the situation
			switch (option) {
			case DELETE_WINDOW:
				setTitle("Confirm event delete");
				add(question, BorderLayout.NORTH);
				add(controlPanel, BorderLayout.CENTER);
				add(buttonPanel, BorderLayout.SOUTH);
				break;
			case EDIT_WINDOW:
				setTitle("Edit the current event");
				buttons[0].setText("Edit");
				buttons[1].setText("Cancel");
				add(controlPanel, BorderLayout.CENTER);
				add(buttonPanel, BorderLayout.SOUTH);
			}
		}
		
		private class Actions implements ActionListener{
			public void actionPerformed(ActionEvent e){
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
					
				default:
					break;
				}
				dispose();
			}
		}
		
	}
