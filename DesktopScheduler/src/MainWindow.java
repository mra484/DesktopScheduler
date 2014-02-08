import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;

public class MainWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	public static int windowHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50;
	public static int windowWidth = 325;
	public static int windowXPosition = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - windowWidth;
	public static boolean emptyDates = false;
	public static String today;
	public static int maxEntries = 20;
	
	private JList<String> list = new JList<String>();
	private Lister lister = new Lister(list, this);
	private FileHandler filer = new FileHandler(lister);
	private ControlPanel controls = new ControlPanel(lister, filer, this);
	private GridBagConstraints c = new GridBagConstraints();
	
	
	public MainWindow(){
		super("Desktop Scheduler");
		today = Entry.getToday();
		System.out.println(today);
		lister.setControl(controls);
		this.setLocation(windowXPosition, 0);
		setSize(windowWidth, windowHeight);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
	
	public ControlPanel getControlPanel(){
		return controls;
	}
	
	public void updateDateNames(){
		lister.updateDateNames();
		filer.saveData();
	}
	public static void main(String[] args){
		new MainWindow();
	}
}
