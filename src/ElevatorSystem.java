import javax.swing.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class ElevatorSystem extends JFrame
{
	private static final long	serialVersionUID = 1L;
	private static final int	NUM_ELEVATORS = 6;
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int ELEVATOR_HEIGHT = 150;
	private static int ELEVATOR_WIDTH = 90;
	private static int ELEVATOR_SHAFT_WIDTH = 100;
	private static int FLOOR_DISPLAY_WIDTH = 200;
	private static int BOTTOM_SCREEN_MARGIN = 30;
	
	private Elevator[]	elevators;
	private Floor[]		floors;
	
	public ElevatorGUI(int numberOfFloors, int numberOfElevators)
	{
		//First, set up master arrays of elevators and floors.
		elevators = new Elevator[numberOfElevators];
		floors = new Floor[numberOfFloors];
		
		//Then, initialize each of the elevators and floors.

		for(int id = 0; id < elevators.length; id++)
		{
			elevators[id] = new Elevator(id, elevators, floors);
		}
		
		for(int id = 0; id < floors.length; id++)
		{
			floors[id] = new Floor(id, elevators, floors);
		}
		
		//Finally, size of the window based on the number
		//and width of elevators and floors.
		int windowWidth, windowHeight;
		
		windowWidth = (ELEVATOR_SHAFT_WIDTH * numberOfElevators) + FLOOR_DISPLAY_WIDTH;
		windowHeight = (ELEVATOR_HEIGHT * numberOfFloors) + BOTTOM_SCREEN_MARGIN;
		
		this.setSize(windowWidth, windowHeight);
	}
	
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
			g.drawString(Integer.toString(elevator.getFloor()), elevatorX+5, elevatorY+15);
			g.setFont(getFont().deriveFont(9.0f)); //Change font size to 9.0
			g.drawString(Arrays.toString(elevator.getLights()), elevatorX+5, elevatorY+30);
		}
	
		//Redraw floors...
		for(int i = 0; i < floors.length; i++)
		{
			g.drawString(Arrays.toString(floors[i].getGuests()), 10, getHeight() - (i * ELEVATOR_HEIGHT) - 10);
			Random r = new Random();
			if (r.nextInt(100) % 51 == 0)
			{
				floors[i].generateGuests();
			}
		}
		
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		repaint();
	}
	
}
