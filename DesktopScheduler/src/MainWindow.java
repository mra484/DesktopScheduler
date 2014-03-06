import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class MainWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	public static final int RIGHT_WINDOW = 0;
	public static final int LEFT_WINDOW = 1;
	public static final int CUSTOM_WINDOW = 2;
	
	public static int windowHeight = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 50;
	public static int windowWidth = 325;
	public static int windowXPosition = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - windowWidth;
	public static boolean emptyDates = false;
	public static String today;
	public static int positionPref = RIGHT_WINDOW;
	public static int maxEntries = 31;
	public static boolean deleteDialog = true;
	public static int dateSize = 14;
	public static int titleSize = 10;
	
	private JList<String> list = new JList<String>();
	private Lister lister = new Lister(list, this);
	private FileHandler filer = new FileHandler(lister, this);
	private ControlPanel controls = new ControlPanel(lister, filer, this);
	private GridBagConstraints c = new GridBagConstraints();
	
	
	public MainWindow(){
		super("Desktop Scheduler");
		today = Entry.getToday();
		System.out.println(today);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//add ControlPanel reference to Lister
		lister.setControl(controls);
		
		//set position, size, and arrange components
		setWindow();
		arrange();
		
		//set positionPref to custom  if window is moved
		addComponentListener(new ComponentListener() {
			public void componentMoved(ComponentEvent e) {
				if( getY() != 0 || (getX() != 0 && getX() != windowXPosition)){
					positionPref = CUSTOM_WINDOW;
					controls.updateOptions();
				}
			}

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//display window
		setVisible(true);

	}
	
	public void setWindow(){
		
		switch(positionPref){
		
		case RIGHT_WINDOW:
			//position on right side of screen
			this.setLocation(windowXPosition, 0);
			setSize(windowWidth, windowHeight);
			break;
			
		case LEFT_WINDOW:
			//position on left side of screen
			this.setLocation(0,0);
			setSize(windowWidth, windowHeight);
			break;
			
		case CUSTOM_WINDOW:
			//position and size read from file
			break;
		}
		
	}
	
	private void arrange(){
		setLayout(new GridBagLayout());
		Insets top = new Insets(10,0,0,0);
		Insets bottom = new Insets(0,0,10,0);
		
		//entry list component
		c.insets = top;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 99;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = GridBagConstraints.RELATIVE;
		add(lister, c);
		
		//control panel component
		c.insets = bottom;
		c.fill = GridBagConstraints.NONE;
		c.gridheight = GridBagConstraints.REMAINDER;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 1;
		add(controls, c);		
		
	}
	
	public ControlPanel getControlPanel(){
		return controls;
	}
	
	public void setDateSize(int size){
		dateSize = size;
		lister.setDateSize(size);
	}
	public void setTitleSize(int size){
		titleSize = size;
		lister.setTitleSize(size);
	}
	
	public void updateDateNames(){
		lister.updateDateNames();
		filer.saveData();
	}
	
	public static void main(String[] args){
		new MainWindow();
	}
}
