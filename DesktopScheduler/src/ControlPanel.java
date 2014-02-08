import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ControlPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private Lister list;
	private FileHandler filer;
	private MainWindow mainWindow;
	private DialogWindow optionWindow = null;
	
	private JButton add = new JButton("New Entry");
	private JButton delete = new JButton("Delete Selected Entry");
	private JButton option = new JButton("O");
	private JTextField date = new JTextField();
	private JTextField title = new JTextField();
	private JTextArea memo = new JTextArea(4, 4);
	private JLabel dateLabel = new JLabel("Date:");
	private JLabel titleLabel = new JLabel("Enter event name below:");
	private JLabel memoLabel = new JLabel("Enter additional information below:");
	JScrollPane memoArea = new JScrollPane(memo);
	private Entry current = new Entry("1/1/1");
	private JTextComponent lastSelected = null;
	
	private ControlPanel originalControl = this;
	private ActionHandler action = new ActionHandler();
	private Mouse highlight = new Mouse();
	private GridBagConstraints c = new GridBagConstraints();
	
	//constructor for edit/delete popup window
	public ControlPanel(ControlPanel a, int option){
		originalControl = a;
		list = a.getList();
		filer = a.getFiler();
		
		date.addMouseListener(highlight);
		title.addMouseListener(highlight);
		memo.addMouseListener(highlight);
		
		setLayout(new GridBagLayout());
		arrange();
		
		date.setText(a.getDate());
		title.setText(a.getTitle());
		memo.setText(a.getMemo());
		remove(add);
		remove(delete);
		remove(this.option);
		
	}
	
	//constructor for main window
	public ControlPanel(Lister a, FileHandler b, MainWindow c){
		list = a;
		filer = b;
		mainWindow = c;

		optionWindow = new DialogWindow(mainWindow);
		
		date.addMouseListener(highlight);
		title.addMouseListener(highlight);
		memo.addMouseListener(highlight);
		
		setLayout(new GridBagLayout());
		arrange();
		add.addActionListener(action);
		delete.addActionListener(action);
		option.addActionListener(action);
	}
	public Lister getList(){
		return list;
	}
	public FileHandler getFiler(){
		return filer;
	}
	public String getDate(){
		return date.getText();
	}
	public String getTitle(){
		return title.getText();
	}
	public String getMemo(){
		return memo.getText();
	}
	public void setDate(String a){
		date.setText(a);
	}
	public void setTitle(String a){
		title.setText(a);
	}
	public void setMemo(String a){
		memo.setText(a);
	}
	public void makeFinal(){
		date.setEditable(false);
		title.setEditable(false);
		memo.setEditable(false);
	}
	public void arrange(){
		c.fill = GridBagConstraints.HORIZONTAL;
		
		//add button
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0,0,10,0);
		add(add, c);
		
		//delete button
		c.gridx = 1;
		add(delete, c);
		
		//option button
		c.gridx = 2;
		add(option, c);
		
		//date label
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0,0,0,0);
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 1;
		add(dateLabel, c);
		
		//date field
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(date, c);
		
		//title label
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(titleLabel, c);
		
		//title field
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(title, c);
		
		//memo label
		c.gridy = 4;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(memoLabel, c);
		
		//memo field
		c.gridy = 5;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		add(memoArea, c);
	}
	
	public void add(){
		//add information in the controlpanel fields as a new event
		//ignore if date is empty
		if( date.getText().compareTo("") == 0 ){
			System.out.println("Please Enter a date");
			return;
		}
		list.add(date.getText(), title.getText(), memo.getText());	
		updateControlPanel();
	}
	
	public void delete(){
		//deletes the selected entry
		list.delete();
		clearControlPanel();
	}

	public void updateControlPanel(){
		//in the event of an edit being carried out, updates the original ControlPanel object
		originalControl.setDate(date.getText());
		originalControl.setTitle(title.getText());
		originalControl.setMemo(memo.getText());
	}
	public void clearControlPanel(){
		//clears the text fields
		originalControl.setDate("");
		originalControl.setTitle("");
		originalControl.setMemo("");
	}
	
	public void update(Entry currentDate, String currentTitle){
		short dateValue;
		
		//change field values to the values of the selected entry
		date.setText(currentDate.getName());
		title.setText(currentTitle);
		memo.setText(currentDate.getList().get(currentTitle).getMemo());
	}
	
	public void highlightField(JTextComponent field){
		field.setSelectionStart(0);
		field.setSelectionEnd(field.getText().length());
	}
	
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			Object source = e.getSource();
			
			if( source == add ){
				add();
			} else if( source == delete) {
				delete();
			} else if( source == option) {
				optionWindow.setVisible(true);
			} else {
				lastSelected = (JTextComponent) source;
			}

			filer.saveData();
		}
	}
	
	private class Mouse implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {	
			Object source = e.getSource();
			if( source == lastSelected)
				return;
			if( source == date || source == title || source == memo) {
				lastSelected = (JTextComponent) source;
				date.selectAll();
				title.selectAll();
				memo.selectAll();				
			} 
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
