import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class MainWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	private JList<String> list = new JList<String>();
	private Lister lister = new Lister(list);
	private FileHandler filer = new FileHandler(lister);
	private ControlPanel controls = new ControlPanel(lister, filer);
	private GridBagConstraints c = new GridBagConstraints();
	
	public MainWindow(){
		super("Desktop Scheduler");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(325, 700);

		setLayout(new GridBagLayout());
		arrange();
	}
	
	private void arrange(){
		setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = GridBagConstraints.RELATIVE;
		add(lister, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.gridy = 1;
		add(controls, c);		
	}
	
	public static void main(String[] args){
		new MainWindow();
	}
}
