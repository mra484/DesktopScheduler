import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private Lister list;
	private FileHandler filer;
	private JButton add = new JButton("Create New Entry");
	private JButton delete = new JButton("Delete Selected Entry");
	private JTextField date = new JTextField();
	private JTextArea memo = new JTextArea(4, 4);
	private JLabel dateLabel = new JLabel("Date:");
	private JLabel memoLabel = new JLabel("Enter event information below:");
	JScrollPane memoArea = new JScrollPane(memo);
	
	private ActionHandler action = new ActionHandler();
	private GridBagConstraints c = new GridBagConstraints();
	
	public ControlPanel(Lister a, FileHandler b){
		list = a;
		filer = b;
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
		add(memoLabel, c);
		
		c.gridy = 3;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.BOTH;
		add(memoArea, c);
	}
	
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if( date.getText() == "" )
				System.out.println("Please Enter a date");
			if( e.getSource() == add ){
				list.add(date.getText(), memo.getText());
			}
			if( e.getSource() == delete) {
				list.delete();
			}
			filer.saveData();
		}
	}
}
