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
	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_MISS_DATE = 1;
	public static final int STATUS_MISS_TITLE = 2;
	public static final int STATUS_EDIT = 3;
	public static final int STATUS_DELETE = 4;
	public static final int STATUS_DELETE_FAIL = 5;
	
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
	private JLabel status = new JLabel(" ");
	private JLabel dateLabel = new JLabel("Date:");
	private JLabel titleLabel = new JLabel("Enter event name below:");
	private JLabel memoLabel = new JLabel("Enter additional information below:");
	JScrollPane memoArea = new JScrollPane(memo);
	private Entry current = new Entry("1/1/1");
	private JTextComponent lastSelected = null;
	
	private ControlPanel originalControl = this;
	private ActionHandler action = new ActionHandler();
	private Keyboard keys = new Keyboard();
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
		memo.setLineWrap(true);
		date.addKeyListener(keys);
		title.addKeyListener(keys);
		
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
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0,0,10,0);
		add(status, c);
		
		//add button
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
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
		c.gridy = 2;
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
		c.gridy = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(titleLabel, c);
		
		//title field
		c.gridy = 4;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(title, c);
		
		//memo label
		c.gridy = 5;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(memoLabel, c);
		
		//memo field
		c.gridy = 6;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		add(memoArea, c);
	}
	
	public void add(){
		//add information in the controlpanel fields as a new event
		//ignore if date or title is empty
		if( date.getText().compareTo("") == 0 ){
			setStatus(STATUS_MISS_DATE);
			System.out.println("Please Enter a date");
			return;
		}
		if( title.getText().replace(" ", "").compareTo("") == 0 ){
			setStatus(STATUS_MISS_TITLE);
			System.out.println("Please Enter a title");
			return;
		}
		list.add(date.getText(), title.getText(), memo.getText());	
		setStatus(STATUS_SUCCESS);
		updateControlPanel();
		filer.saveData();
	}

	public void delete(){
		//deletes the selected entry
		if(MainWindow.deleteDialog )
			new DialogWindow(mainWindow, new ControlPanel(this, 0), DialogWindow.DELETE_WINDOW);
		else
			deleteEntry();
	}
	public void deleteEntry(){
		//deletes the selected entry
		list.delete();
		clearControlPanel();
		setStatus(STATUS_DELETE);
		filer.saveData();
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
	public void setStatus(int i){
		switch (i){
		case STATUS_SUCCESS:
			status.setForeground(Color.GREEN);
			status.setText("Successfully added new event");
			break;
		case STATUS_MISS_DATE:
			status.setForeground(Color.RED);
			status.setText("Please enter a valid date");
			
			break;
		case STATUS_MISS_TITLE:
			status.setForeground(Color.RED);
			status.setText("Please enter a title");
			break;
		case STATUS_EDIT:
			status.setForeground(Color.GREEN);
			status.setText("Successfully updated event");
			
			break;
		case STATUS_DELETE:
			status.setForeground(Color.GREEN);
			status.setText("Successfully deleted event");
			
			break;
		case STATUS_DELETE_FAIL:
			status.setForeground(Color.RED);
			status.setText("Unable to delete selected event");
			break;
		default:
			status.setForeground(Color.RED);
			status.setText("Unknown message type");
			break;
		}
		
	}
	public void clearStatus(){
		status.setText(" ");
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
	
	private class Keyboard implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			if( e.getKeyCode() == KeyEvent.VK_ENTER ){
				if( e.getSource() == title ){
				add();
				filer.saveData();
				}
				else {
					title.requestFocus();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class Mouse implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {	
			clearStatus();
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
