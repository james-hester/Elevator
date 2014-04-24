import java.util.Arrays;

public class Elevator
{
	
	private static final int	MAX_DISPLACEMENT = 100;
	private static final int	STOP_TIME = 130;
	public enum 		Direction {UP, DOWN};
	
	private int			id;
	private int[]		guests;
	private int			currentFloor;
	private boolean		betweenFloors;
	private boolean[]	lights;
	private boolean		stopped;
	private int			stopTimer;
	private Direction	currentDirection;
	/**
	* "displacement" refers to how far between floors the elevator is while traveling.
	* It is positive whether the elevator is going up or down.
	* The elevator will travel as long as displacement is under MAX_DISPLACEMENT--the elevator
	* will be redrawn MAX_DISPLACEMENT times while traveling, regardless of its size in pixels.
	*/
	private int			displacement;
	
	public Elevator(int _id)
	{
		id = _id;
		
		guests = new int[ElevatorSystem.getNumberOfFloors()];
		lights = new boolean[ElevatorSystem.getNumberOfFloors()];
		Arrays.fill(lights, false);
		
		betweenFloors = false;
		currentDirection = Direction.UP;
		displacement = 0;
		
		currentFloor = 0;
	}
	
	public int getID()
	{
		return id;
	}
	
	public int getFloor()
	{
		return currentFloor;
	}
	
	public double getDisplacementPercent()
	{
		return (double)displacement / (double)MAX_DISPLACEMENT;
	}
	
	public boolean[] getLights()
	{
		return lights;
	}
	
	public void turnOnLight(int which)
	{
		lights[which] = true;
	}
	
	public void turnOffLight(int which)
	{
		lights[which] = false;
	}
	
	public boolean isBetweenFloors()
	{
		return betweenFloors;
	}
	
	public boolean isGoingUp()
	{
		return (currentDirection == Direction.UP);
	}
	
	public void takeStep()
	{
		displacement++;
		if (displacement == MAX_DISPLACEMENT)
		{
			
			displacement = 0;
			betweenFloors = false;
			
			if (currentDirection == Direction.UP)
				currentFloor++;
			else
				currentFloor--;
		}		
	}
	
	public void waitAtFloor()
	{
		stopTimer++;
		if (stopTimer == STOP_TIME)
		{
			stopTimer = 0;
			stopped = false;
		}	
	}
	
	public boolean isStopped()
	{
		return stopped;
	}
	
	public void stop()
	{
		stopped = true;
		checkFlipDirection();
	}
	
	public Direction getDirection()
	{
		return currentDirection;
	}
	
	public void updateLights()
	{
		for (int i = 0; i < ElevatorSystem.getNumberOfFloors(); i++)
		{
			lights[i] = (guests[i] > 0); //if there is more than one guest wanting to go to floor i, turn on light i
		}
	}
	
	/**
	 * 
	 */
	public void keepGoing()
	{
		betweenFloors = true;
		checkFlipDirection();
	}
	
	public int[] getGuests()
	{
		return guests;
	}
	
	/**
	 * Checks whether elevator is at the top or the bottom. If so, its direction is reversed.
	 */
	private void checkFlipDirection()
	{
		if (currentFloor == 0) //Are we at the bottom?
			currentDirection = Direction.UP; //Then we must go up
		if (currentFloor == (ElevatorSystem.getNumberOfFloors() - 1)) //Are we at the top?
			currentDirection = Direction.DOWN; //Then we must go down
	}
	
}
