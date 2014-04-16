import java.awt.Container;
import java.awt.Graphics;
import java.util.Arrays;

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
	
	private Elevator[] elevators;
	private Floor[] floors;
	
	public ElevatorGUI(Elevator[] _elevators, Floor[] _floors)
	{	
		elevators = _elevators;
		floors = _floors;
		
		//Resize the window based on the number
		//and width of elevators and floors.
		int windowWidth, windowHeight;
		
		windowWidth = (ELEVATOR_SHAFT_WIDTH * elevators.length) + FLOOR_DISPLAY_WIDTH;
		windowHeight = (ELEVATOR_HEIGHT * floors.length) + BOTTOM_SCREEN_MARGIN;
		
		this.setSize(windowWidth, windowHeight);
	}
	
	/**
	 * Interprets the characteristics of each elevator and floor as graphical
	 * data, and writes this to the screen. This method is the source of all of
	 * the program's output.
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
			 * The following four lines draw the current floor number and lights
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
			g.drawString(Arrays.toString(floors[i].getGuests()), 10, getHeight() - (i * ELEVATOR_HEIGHT) - 10);
		}
		
		repaint();
	}
		
}