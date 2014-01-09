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

public class ControlPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private Lister list;
	private FileHandler filer;
	private JButton add = new JButton("Create New Entry");
	private JButton delete = new JButton("Delete Selected Entry");
	private JTextField date = new JTextField();
	private JTextField title = new JTextField();
	private JTextArea memo = new JTextArea(4, 4);
	private JLabel dateLabel = new JLabel("Date:");
	private JLabel titleLabel = new JLabel("Enter event name below:");
	private JLabel memoLabel = new JLabel("Enter additional information below:");
	JScrollPane memoArea = new JScrollPane(memo);
	private Entry current = new Entry("1/1/1");
	
	private ActionHandler action = new ActionHandler();
	private Mouse highlight = new Mouse();
//	private Keys keyAction = new Keys();
	private GridBagConstraints c = new GridBagConstraints();
	
	public ControlPanel(Lister a, FileHandler b){
		list = a;
		filer = b;
		
		date.addMouseListener(highlight);
		title.addMouseListener(highlight);
		memo.addMouseListener(highlight);
		
//		date.addKeyListener(keyAction);
//		title.addKeyListener(keyAction);
//		memo.addKeyListener(keyAction);
		
		setLayout(new GridBagLayout());
		arrange();
		add.addActionListener(action);
		delete.addActionListener(action);
	}
	
	public void arrange(){
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets(0,0,10,0);
		add(add, c);
		
		c.gridx = 1;
		add(delete, c);
		
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0,0,0,0);
		add(date, c);
		
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridwidth = GridBagConstraints.RELATIVE;
		add(dateLabel, c);
		
		c.gridy = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(titleLabel, c);
		
		c.gridy = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(title, c);
		
		c.gridy = 4;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(memoLabel, c);
		
		c.gridy = 5;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		add(memoArea, c);
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
				//ignore if date is empty
				if( date.getText().compareTo("") == 0 ){
					System.out.println("Please Enter a date");
					return;
				}
				list.add(date.getText(), title.getText(), memo.getText());
			} else if( source == delete) {
				list.delete();
			}

			filer.saveData();
		}
	}
	
	private class Mouse implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {	
			Object source = e.getSource();
		
			if( source == date || source == title || source == memo) {
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
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
//	private class Keys implements KeyListener{
//		public void keyPressed(KeyEvent e){
//		}
//		
//		public void keyReleased(KeyEvent e){
//		}
//		
//		public void keyTyped(KeyEvent e){
//		}
//	}
}
