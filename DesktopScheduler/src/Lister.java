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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.*;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

public class Lister extends JScrollPane{
	
	private static final long serialVersionUID = 1L;
	private HashMap<Short, Entry> dateList = new HashMap<Short, Entry>(); 
	private JList<String> list;
	private HashMap<Integer, String> dateIndex = new HashMap<Integer, String>();
	private HashSet<Integer> spaceIndex = new HashSet<Integer>();
	private Entry current = new Entry("1/1/1");
	
	public Lister(JList<String> jlist){
		super(jlist);
		list = jlist;
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectionModel(new DisableSelectionModel());
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(290, 500));
	}	
	
	public boolean add(String date, String data){
		Entry current = new Entry(date);
		
		//create new date entry if not present
		if( !dateList.containsKey(current.getDate()) ){
			dateList.put(current.getDate(), current);
		}else 
			current = dateList.get(current.getDate());
		
		//retrieve date entry object and add the current information to it
		current.add(data);
		updateList();
		return true;		
	}
	
	public boolean delete(){
		String data = list.getSelectedValue();
		int index = list.getSelectedIndex();
		if( data == "")
			return false;
	
		//move back selection until a non date-label entry is found - not needed?
		while(!dateIndex.containsKey(index))
			index--;
		
		current = dateList.get(current.parseDate(dateIndex.get(index), 1));
		current.getList().remove(data);
		if( current.getList().size() == 0)
			dateList.remove(current.parseDate(dateIndex.get(index), 1));
		
		updateList();
		return true;		
		
	}
	
	public HashMap<Short, Entry> getList(){
		return dateList;
	}
	
	public void updateList(){
		DefaultListModel<String> m = new DefaultListModel<String>();
		int i = 0;
		for(Entry a: dateList.values()){
			dateIndex.put(i, a.getName());
			m.add(i++, a.getName());
			for(DateEntry b: a.getList().values())
				m.add(i++, b.toString());
			spaceIndex.add(i);
			m.add(i++, " ");
		}
		list.setModel(m);
		
	}
	
	//disables selection of empty space and date labels
	public class DisableSelectionModel extends DefaultListSelectionModel{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void setSelectionInterval(int index0, int index1){
			if( !dateIndex.containsKey(index0) && !spaceIndex.contains(index0)){
				super.setSelectionInterval(index0, index0);
				
			}
		}
		
	}
}
