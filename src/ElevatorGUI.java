import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import javax.swing.Timer;

/**
 * Interprets the characteristics of each elevator and floor as graphical
 * data, and writes this to the screen. This method is the source of all of
 * the program's output.
 * @author
 * 
 */
class ElevatorGUI extends Container
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
	private static int	elevatorHeight = 160;
	private static int	elevatorWidth = 170;
	private static int	elevatorShaftWidth = 180;
	
	private static final int	FLOOR_DISPLAY_WIDTH = 200;
	private static final int	BOTTOM_SCREEN_MARGIN = 24;
	/*
	 * Filenames of the image files used by the GUI.
	 */
	private static final String	DOORS_OPEN_IMG_NAME = "open.png";
	private static final String	DOORS_CLOSED_IMG_NAME = "closed.png";
	
	private static BufferedImage imgElevatorDoorsOpen;
	private static BufferedImage imgElevatorDoorsClosed;

	
	public ElevatorGUI()
	{	
		
		try 
		{
			imgElevatorDoorsOpen = ImageIO.read(new File(DOORS_OPEN_IMG_NAME));
			imgElevatorDoorsClosed = ImageIO.read(new File(DOORS_CLOSED_IMG_NAME));
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
		elevatorShaftWidth = elevatorWidth + 10;

		
		
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
		g.drawLine(FLOOR_DISPLAY_WIDTH, 0, FLOOR_DISPLAY_WIDTH, getHeight());
		
		for(int i = 0; i < this.getHeight(); i+=elevatorHeight)
		{
			g.drawLine(0, i, FLOOR_DISPLAY_WIDTH, i);
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

			
			/*
			 * The following four lines draw the current floor number and lights
			 * (as a boolean array) onto the elevator. 
			 */
			g.drawString(Integer.toString(elevatorBeingDrawn.getFloor()), elevatorX+5, elevatorY+15);
			g.setFont(getFont().deriveFont(9.0f)); //Change font size to 9.0
			g.drawString(Arrays.toString(elevatorBeingDrawn.getLights()), elevatorX+5, elevatorY+30);
			g.setFont(getFont().deriveFont(12.0f)); //Change font size back to 12.0
		}
	
		//Redraw floors...
		for(int i = 0; i < ElevatorSystem.getNumberOfFloors(); i++)
		{
			Floor floorBeingDrawn = ElevatorSystem.getFloor(i);
			g.drawString(Arrays.toString(floorBeingDrawn.getGuests()), 10, getHeight() - (i * elevatorHeight) - 10);
		}
	}
		
}

/**
 * Simple event handler used by Timer class to repaint GUI.
 * @author 
 *
 */
class RepaintActionHandler implements ActionListener
{
	Container theGUI;
	
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