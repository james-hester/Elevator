import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;
import javax.swing.JFrame;
import javax.swing.Timer;

import java.awt.Font;
/**
 * Interprets the characteristics of each elevator and floor as graphical
 * data, and writes this to the screen. This method is the source of all of
 * the program's output.
 * @author
 * 
 */
class ElevatorGUI extends Container implements MouseListener
{
	private static final long	serialVersionUID = 1L; //Compiler gives warning unless this is included. 
	/**
	 * The delay, in milliseconds, between calls to repaint(). 
	 * Frames per second can be calculated by (1000/REPAINT_FREQUENCY).
	 */
	private static final int	REPAINT_FREQUENCY = 22;
	/*
	 * The following are the sizes of screen elements in pixels.
	 * Changing these will not change the behavior of the program.
	 */
	private static int	elevatorHeight;
	private static int	elevatorWidth;
	private static int	elevatorShaftWidth;
	
	private static final int	FLOOR_DISPLAY_WIDTH = 200;
	private static final int	BOTTOM_SCREEN_MARGIN = 24;
	//private static final int	PANEL_HORIZONTAL_BUTTON_SPACING = 4;
	private static final int	PANEL_VERTICAL_BUTTON_SPACING = 12;
	private static final int	LABEL_OFFSET_X = 12;
	private static final int	LABEL_OFFSET_Y = 20;
	
	/*
	 * Filenames of the image files used by the GUI.
	 */
	private static final String	DOORS_OPEN_IMG_NAME = "open.png";
	private static final String	DOORS_CLOSED_IMG_NAME = "closed.png";
	private static final String SHAFT_IMG_NAME = "shaft.png";
	private static final String	PANEL_IMG_NAME = "panel.png";
	private static final String	BUTTON_ON_IMG_NAME = "button_on.png";
	private static final String	BUTTON_OFF_IMG_NAME = "button_off.png";
	
	private static BufferedImage 	imgElevatorDoorsOpen;
	private static BufferedImage 	imgElevatorDoorsClosed;
	private static BufferedImage	imgElevatorShaft;
	private static BufferedImage	imgPanel;
	private static BufferedImage	imgButtonOn;
	private static BufferedImage	imgButtonOff;
	
	public ElevatorGUI()
	{	
		
		try 
		{
			imgElevatorDoorsOpen = ImageIO.read(new File(DOORS_OPEN_IMG_NAME));
			imgElevatorDoorsClosed = ImageIO.read(new File(DOORS_CLOSED_IMG_NAME));
			imgElevatorShaft = ImageIO.read(new File(SHAFT_IMG_NAME));
			imgPanel = ImageIO.read(new File(PANEL_IMG_NAME));
			imgButtonOn = ImageIO.read(new File(BUTTON_ON_IMG_NAME));
			imgButtonOff = ImageIO.read(new File(BUTTON_OFF_IMG_NAME));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		if (imgElevatorDoorsOpen.getHeight() != imgElevatorDoorsClosed.getHeight() 
				|| imgElevatorDoorsOpen.getWidth() != imgElevatorDoorsClosed.getWidth())
		{
			System.err.println("Error: images of closed elevator doors");
			System.err.println("and open elevator doors are not the same size.");
			System.exit(1);
		}
		
		elevatorWidth = imgElevatorDoorsOpen.getWidth();
		elevatorHeight = imgElevatorDoorsOpen.getHeight();
		elevatorShaftWidth = imgElevatorShaft.getWidth();

		//Resize the window based on the number
		//and width of elevators and floors.
		int windowWidth, windowHeight;
		
		windowWidth = (elevatorShaftWidth * ElevatorSystem.getNumberOfElevators()) + FLOOR_DISPLAY_WIDTH;
		windowHeight = (elevatorHeight * ElevatorSystem.getNumberOfFloors()) + BOTTOM_SCREEN_MARGIN;
		
		this.setSize(windowWidth, windowHeight);
		
		//Now, set up the Timer which will automatically redraw
		//the GUI every REPAINT_FREQUENCY milliseconds.
		
		new Timer(REPAINT_FREQUENCY, new RepaintActionHandler(this)).start();
	}
	
	@Override
	public void paint(Graphics g)
	{
		//Draw line separating floors from elevators.
		g.drawLine(FLOOR_DISPLAY_WIDTH, 0, FLOOR_DISPLAY_WIDTH, getHeight());
		
		for(int i = 0; i < ElevatorSystem.getNumberOfElevators(); i++)
		{
			for(int j = 0; j < ElevatorSystem.getNumberOfFloors(); j++)
			{
				g.drawImage(imgElevatorShaft, (i*elevatorShaftWidth)+FLOOR_DISPLAY_WIDTH, j*elevatorHeight, null);
			}
		}
		
		//Redraw elevators...
		for(int i = 0; i < ElevatorSystem.getNumberOfElevators(); i++)
		{
			Elevator elevatorBeingDrawn = ElevatorSystem.getElevator(i);
			
			int elevatorX = ( FLOOR_DISPLAY_WIDTH + (elevatorBeingDrawn.getID() * elevatorShaftWidth) + ( (elevatorShaftWidth - elevatorWidth) / 2 ) );
			int elevatorY = ( (ElevatorSystem.getNumberOfFloors() - elevatorBeingDrawn.getFloor()) - 1) * elevatorHeight;
			
			if(elevatorBeingDrawn.isGoingUp())
			{
				elevatorY -= (int)( (float)elevatorHeight * elevatorBeingDrawn.getDisplacementPercent() );
			}
			else
			{
				elevatorY += (int)( (float)elevatorHeight * elevatorBeingDrawn.getDisplacementPercent() );
			}	
			
			if (elevatorBeingDrawn.isStopped())
				g.drawImage(imgElevatorDoorsOpen, elevatorX, elevatorY, null);
			if (elevatorBeingDrawn.isBetweenFloors())
				g.drawImage(imgElevatorDoorsClosed, elevatorX, elevatorY, null);
		}
	
		//Redraw floors...
		for(int i = 0; i < ElevatorSystem.getNumberOfFloors(); i++)
		{
			Floor floorBeingDrawn = ElevatorSystem.getFloor(i);
			
			g.drawLine(0, i*elevatorHeight, FLOOR_DISPLAY_WIDTH, i*elevatorHeight);
			g.drawString(Arrays.toString(floorBeingDrawn.getGuests()), 10, getHeight() - (i * elevatorHeight) - 10);
		}
	}

	/**
	 * Provides a facility to respond to user mouse input.
	 * If an elevator is clicked--indeed, if any point in an
	 * elevator's shaft is clicked--a PanelView (a dialog window
	 * representing that elevator's panel) will be displayed.
	 */
	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		int mouseX = arg0.getX();
		
		mouseX -= FLOOR_DISPLAY_WIDTH;
		mouseX /= elevatorShaftWidth;

		if (mouseX >= 0 && mouseX < ElevatorSystem.getNumberOfElevators())
		{
			new PanelView(mouseX);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
	/**
	 * Class representing the window showing an Elevator's panel.
	 * Simply displays which floors the Elevator being inspected is traveling to.
	 */
	class PanelView extends JFrame
	{
		private static final long	serialVersionUID = 1L;
		private int	which;
		
		public PanelView(int whichElevator)
		{
			which = whichElevator;
			
			setTitle("Elevator "+(which+1));
			setSize(imgPanel.getWidth(), imgPanel.getHeight());
			setResizable(false);
			setVisible(true);
		}
		
		@Override
		public void paint(Graphics g)
		{
			//Draw the background image.
			g.drawImage(imgPanel, 0, 0, null);
			
			//Font used to draw the numerals. Font.SANS_SERIF is part of the JDK.
			g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));
			
			/*
			 * The buttons in the elevator are drawn one-by-one, from bottom to top:
			 * the first floor is at the bottom of the panel. Should one column be required,
			 * the panel is divided in two, and the buttons are drawn along the median.
			 * Should two columns be required, the panel is divided into thirds, with
			 * the buttons lying along the two medians, etc.
			 */
			
			//First, calculate the number of columns needed to fit in a button for each floor.
			int numberOfColumns = (ElevatorSystem.getNumberOfFloors() * (imgButtonOn.getHeight() + PANEL_VERTICAL_BUTTON_SPACING)) / imgPanel.getHeight();
			numberOfColumns++;
			
			//Then, calculate the maximum number of buttons that can fit in one of these columns.
			int buttonsPerColumn = imgPanel.getHeight() / (imgButtonOn.getHeight() + PANEL_VERTICAL_BUTTON_SPACING);
			
			int currentColumn = 1;
			for(int buttonToDraw = 0; buttonToDraw < ElevatorSystem.getNumberOfFloors(); buttonToDraw++)
			{
				if (buttonToDraw % buttonsPerColumn == 0 && buttonToDraw != 0)
					currentColumn++;
				
				int x = (imgPanel.getWidth() * currentColumn) / (numberOfColumns + 1);
				x -= imgButtonOn.getWidth() / 2; //adjust for width of button
				
				int y = (imgPanel.getHeight() - ( (buttonToDraw % buttonsPerColumn) + 1) * (imgButtonOn.getHeight() + PANEL_VERTICAL_BUTTON_SPACING));
				
				//Now we can finally draw the button.
				if (ElevatorSystem.getElevator(which).getLights()[buttonToDraw] == true)
				{
					g.drawImage(imgButtonOn, x, y, null);
					g.setColor(Color.RED);
					g.drawString(String.valueOf(buttonToDraw+1), x+LABEL_OFFSET_X, y+LABEL_OFFSET_Y);
				}
				else
				{
					g.drawImage(imgButtonOff, x, y, null);
					g.setColor(Color.BLACK);
					g.drawString(String.valueOf(buttonToDraw+1), x+LABEL_OFFSET_X, y+LABEL_OFFSET_Y);
				}
				
				repaint();
			} //for(...)

		}//paint(Graphics)
		
	}//PanelView class

}//ElevatorGUI class



/**
 * Simple event handler used by Timer class to repaint GUI.
 * @author 
 *
 */
class RepaintActionHandler implements ActionListener
{
	Container	theGUI;
	
	public RepaintActionHandler(Container _theGUI)
	{
		theGUI = _theGUI;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		theGUI.repaint();
	}
}