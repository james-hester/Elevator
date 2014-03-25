import javax.swing.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class ElevatorSystem extends JFrame
{
	private static final long	serialVersionUID = 1L;
	private static final int	NUM_ELEVATORS = 5;
	private static final int	NUM_FLOORS = 6;
	
	public static void main(String[] args)
	{
		ElevatorSystem theApp = new ElevatorSystem();
		ElevatorGUI theGUI = new ElevatorGUI( NUM_FLOORS, NUM_ELEVATORS );
		theApp.setContentPane(theGUI);
		theApp.setSize(theGUI.getSize());
		theApp.setResizable(false);
		theApp.setDefaultCloseOperation(EXIT_ON_CLOSE);
		theApp.setVisible(true);
	}

}

class ElevatorGUI extends Container
{
	private static final long serialVersionUID = 1L;
	/*
	 * The following are the sizes of screen elements in pixels.
	 * Changing these will not change the behavior of the program.
	 */
	private static final int ELEVATOR_HEIGHT = 160;
	private static final int ELEVATOR_WIDTH = 170;
	private static final int ELEVATOR_SHAFT_WIDTH = 180;
	private static final int FLOOR_DISPLAY_WIDTH = 200;
	private static final int BOTTOM_SCREEN_MARGIN = 24;
	/**
	 * Delay between calls of paint(), in milliseconds.
	 */
	public static final long REPAINT_DELAY = 5;
	
	/**
	 * Array storing each of the elevators used by the program.
	 * Initialized by the constructor of this class and passed as a reference
	 * to each of the elevators for their use. (An elevator can thereby directly 
	 * monitor the movement of other elevators to optimize its own performance.)
	 */
	private Elevator[]	elevators;
	/**
	 * Array storing each of the floors used by the program.
	 * Likewise passed as a reference to each of the elevators.
	 */
	private Floor[]		floors;
	
	public ElevatorGUI(int numberOfFloors, int numberOfElevators)
	{
		//First, set up master arrays of elevators and floors.
		elevators = new Elevator[numberOfElevators];
		floors = new Floor[numberOfFloors];
		
		//Then, initialize each of the elevators and floors.

		for(int id = 0; id < numberOfElevators; id++)
		{
			elevators[id] = new Elevator(id, elevators, floors);
		}
		
		for(int id = 0; id < numberOfFloors; id++)
		{
			floors[id] = new Floor(id, numberOfFloors);
		}
		
		//Finally, size of the window based on the number
		//and width of elevators and floors.
		int windowWidth, windowHeight;
		
		windowWidth = (ELEVATOR_SHAFT_WIDTH * numberOfElevators) + FLOOR_DISPLAY_WIDTH;
		windowHeight = (ELEVATOR_HEIGHT * numberOfFloors) + BOTTOM_SCREEN_MARGIN;
		
		this.setSize(windowWidth, windowHeight);
	}
	
	/**
	 * The main run loop for the program.
	 * paint(), in addition to drawing all of the graphics,
	 * calls elevate() on each elevator and generateGuests() on each floor
	 * each time it is called. When paint() is done, it waits for 
	 * REPAINT_DELAY milliseconds, and then calls repaint().
	 */
	@Override
	public void paint(Graphics g)
	{
		g.drawLine(FLOOR_DISPLAY_WIDTH, 0, FLOOR_DISPLAY_WIDTH, getHeight());
		
		for(int i = 0; i < this.getHeight(); i+=ELEVATOR_HEIGHT)
		{
			g.drawLine(0, i, FLOOR_DISPLAY_WIDTH, i);
		}
		
		//Redraw elevators...
		for(Elevator elevator : elevators)
		{
			elevator.elevate();
			int elevatorX = ( FLOOR_DISPLAY_WIDTH + (elevator.getID() * ELEVATOR_SHAFT_WIDTH) + ( (ELEVATOR_SHAFT_WIDTH - ELEVATOR_WIDTH) / 2 ) );
			int elevatorY = ( (floors.length - elevator.getFloor()) - 1) * ELEVATOR_HEIGHT;
			if(elevator.isGoingUp())
			{
				elevatorY -= (int)( (float)ELEVATOR_HEIGHT * elevator.getDisplacementPercent() );
			}
			else
			{
				elevatorY += (int)( (float)ELEVATOR_HEIGHT * elevator.getDisplacementPercent() );
			}	
			g.drawRect(elevatorX, elevatorY, ELEVATOR_WIDTH, ELEVATOR_HEIGHT);
			
			/*
			 * The following three lines draw the current floor number and lights
			 * (as a boolean array) onto the elevator. 
			 */
			g.drawString(Integer.toString(elevator.getFloor()), elevatorX+5, elevatorY+15);
			g.setFont(getFont().deriveFont(9.0f)); //Change font size to 9.0
			g.drawString(Arrays.toString(elevator.getLights()), elevatorX+5, elevatorY+30);
			g.setFont(getFont().deriveFont(12.0f)); //Change font size back to 12.0
		}
	
		//Redraw floors...
		for(int i = 0; i < floors.length; i++)
		{
			floors[i].generateGuests();
			g.drawString(Arrays.toString(floors[i].getGuests()), 10, getHeight() - (i * ELEVATOR_HEIGHT) - 10);
		}
		
		waitMillis(REPAINT_DELAY);
		
		repaint();
	}
	
	public void waitMillis(long millis)
	{
		try 
		{
			Thread.sleep(millis);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
}
