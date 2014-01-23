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
 * Class for managing the list of dates and related data
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Dialog.ModalityType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class Lister extends JScrollPane{
	
	private static final long serialVersionUID = 1L;
	
	//contains a list of dates with events
	private TreeMap<Integer, Entry> dateList = new TreeMap<Integer, Entry>(); 
//	private HashMap<Integer, Entry> dateList = new HashMap<Integer, Entry>(); 
	private JList<String> list;
	
	//contains a list of indexes containing a date or a space
	private HashMap<Integer, String> dateIndex = new HashMap<Integer, String>();
	private HashSet<Integer> spaceIndex = new HashSet<Integer>();
	private Entry current = new Entry("1/1/1");
	
	private NewCellRenderer cr = new NewCellRenderer();
	private KeyHandler key = new KeyHandler();
	
	private ControlPanel control;
	private MainWindow main;
	
	public Lister(JList<String> jlist, MainWindow mainWindow){
		super(jlist);
		main = mainWindow;
		list = jlist;
		list.addKeyListener(key);
		list.setCellRenderer(cr);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectionModel(new DisableSelectionModel());
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(290, MainWindow.windowHeight - 235));
	}	
	
	public void setControl(ControlPanel a){
		control = a;
	}
	
	public boolean add(String date, String data, String memo){
		Entry current = new Entry(date);
		
		//create new date entry if not present
		if( !dateList.containsKey(current.getDate()) ){
			dateList.put(current.getDate(), current);
		}else 
			current = dateList.get(current.getDate());
		
		//retrieve date entry object and add the current information to it
		current.add(data, memo);
		updateList();
		return true;		
	}
	
	public boolean delete(){
		
		//retrieve the the title and index of the selected cell, ignore if nothing is selected
		int index = list.getSelectedIndex();
		if( index == -1 )
			return false;
		String data = list.getSelectedValue();
//		if( data == "")
//			return false;
	
		//move back selection to find the date index
		while(!dateIndex.containsKey(index))
			index--;
		
		//using the date index that was just found, retrieve the date name then the date object
		current = dateList.get(current.parseDate(dateIndex.get(index), 1));
		
		//remove from the event list of the date and delete date object if empty
		current.getList().remove(data);
		if( current.getList().size() == 0)
			dateList.remove(current.parseDate(dateIndex.get(index), 1));
		
		updateList();
		return true;		
		
	}
	
	public TreeMap<Integer, Entry> getList(){
		return dateList;
	}
	
	public void updateList(){
		DefaultListModel<String> m = new DefaultListModel<String>();
		int i = 0;
		
		//retrieve and add date to jlist, record the index of the date
		for(Entry a: dateList.values()){
			dateIndex.put(i, a.getName());
			m.add(i++, a.getName());
			
			//retrieve date's event list and add to jlist
			for(DateEntry b: a.getList().values()){
				m.add(i++, b.getTitle());
				spaceIndex.remove(i);
				dateIndex.remove(i);
			}
			
			//add space at the end of a date and record the index
			spaceIndex.add(i);
			m.add(i++, " ");
		}
		list.setModel(m);
		
	}
	
	public void updateDateNames(){
		for(Entry a: dateList.values()){
			a.makeName();
		}
		updateList();
	}
	
	public class NewCellRenderer extends DefaultListCellRenderer{
		private static final long serialVersionUID = 1L;

		public NewCellRenderer(){
		}

		@Override
		public Component getListCellRendererComponent(
				JList<? extends Object> list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			String indent = "   \u25cf ";
			String textValue = value.toString();
			Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			setText(indent + textValue);
			
			if( !dateIndex.containsKey(index) && !spaceIndex.contains(index) ){
				setFont(new Font("small", Font.PLAIN, 11));
				setText(indent + textValue);
			} else
				setText(textValue);
			
			if( isSelected ){
				comp.setBackground(Color.CYAN);
			}
			return this;
		}

	}
	
	//disables selection of empty space and date labels
	public class DisableSelectionModel extends DefaultListSelectionModel{
		private static final long serialVersionUID = 1L;
		int dateValue;
		String date;
		
		@Override
		public void setSelectionInterval(int index0, int index1){
			
			//ignore selection if it is an empty space or a date
			if( dateIndex.containsKey(index0) || spaceIndex.contains(index0))
				return;
			
			super.setSelectionInterval(index0, index0);
			
			//move back selection to find the date index
			while(!dateIndex.containsKey(index0))
				index0--;
			
			//update the display information for the current selection
			date = list.getModel().getElementAt(index0);
			dateValue = current.parseDate(date, 1);
			control.update(dateList.get(dateValue), list.getSelectedValue());			
		}
	}
	
	private class KeyHandler implements KeyListener{
		public void keyTyped(KeyEvent e){
			
		}
		public void keyPressed(KeyEvent e){
			
			//add behavior for delete and enter
			switch (e.getKeyCode()){
			case KeyEvent.VK_DELETE:
				new DialogWindow(main, new ControlPanel(control, 0), DialogWindow.DELETE_WINDOW);
				break;
			case KeyEvent.VK_ENTER:
				new DialogWindow(main, new ControlPanel(control, 0), DialogWindow.EDIT_WINDOW);
				break;
			}
		}
		
		public void keyReleased(KeyEvent e){
			
		}
	}
	

}
