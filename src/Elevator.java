import java.util.Arrays;

public class Elevator
{
	
	private static final int	MAX_DISPLACEMENT = 100;
	private static final int	STOP_TIME = 60;
	public enum 		Direction {UP, DOWN};
	
	private Elevator[]	elevators;
	private Floor[]		floors;
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
	
	public Elevator(int _id, Elevator[] _elevators, Floor[] _floors)
	{
		id = _id;
		elevators = _elevators;
		floors = _floors;
		
		guests = new int[floors.length];
		lights = new boolean[floors.length];
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
	
	public boolean isBetweenFloors()
	{
		return betweenFloors;
	}
	
	public boolean isGoingUp()
	{
		return (currentDirection == Direction.UP);
	}
	
	public void elevate()
	{
		if( betweenFloors )
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
		else if ( stopped )
		{
			stopTimer++;
			if (stopTimer == STOP_TIME)
			{
				stopTimer = 0;
				stopped = false;
			}			
		}
		else
		{
			if (lights[currentFloor] == true || (floors[currentFloor].majorityDirectionPreference() == currentDirection) ) //If people want to get off at this floor
			{
				{
					ding();
					guests[currentFloor] = 0; //Let them off
					lights[currentFloor] = false; //and then turn the light off
				}
				if (currentFloor == 0) //Are we at the bottom?
					currentDirection = Direction.UP; //Then we must go up
				if (currentFloor == (floors.length - 1)) //Are we at the top?
					currentDirection = Direction.DOWN; //Then we must go down

				if (currentDirection == Direction.UP)
				{
					//Take everyone onto the elevator from this floor who wants to go up
					System.out.println("About to call goingup with: "+Arrays.toString(guests));
					floors[currentFloor].takeGuestsGoingUp(guests);
					System.out.println("Out of goingup: "+Arrays.toString(guests));
					System.out.println(Arrays.toString(lights));
				}
				else
				{
					floors[currentFloor].takeGuestsGoingDown(guests);
				}

				//Now that guests are on we want to set the lights up
				for (int i = 0; i < floors.length; i++)
				{
					lights[i] = (guests[i] > 0); //if there is more than one guest wanting to go to floor i, turn on light i
				}

				/*
				 * But what if no one got on the elevator after all this? There must thus be passengers on other elevators.
				 * To figure out which floor to go to we must see how many guests are on each of the other elevators.
				 * The elevator will go to the floor that the fewest other elevators are going to, preferring a higher
				 * floor to a lower one.
				 */

				boolean[] falseArray = new boolean[lights.length];
				Arrays.fill(falseArray, false);
				if (Arrays.equals(lights, falseArray)) //If every value of lights is false
				{
					int[] weights = new int[floors.length];
					int indexOfSmallestWeight, smallestWeight;
					for (Elevator elevator : elevators)
					{
						for(int i = 0; i < weights.length; i++)
						{
							weights[i] += elevator.guests[i];
						}
					}
					smallestWeight = weights[weights.length-1];
					indexOfSmallestWeight = weights.length-1;
					for(int i = weights.length-1; i >= 0; i--)
					{
						if (weights[i] < smallestWeight)
						{
							smallestWeight = weights[i];
							indexOfSmallestWeight = i;
						}
					}

					lights[indexOfSmallestWeight] = true;

				}

				stopped = true;
				betweenFloors = true; //start traveling
			}
			else
			{
				betweenFloors = true;
				if (currentFloor == 0) //Are we at the bottom?
					currentDirection = Direction.UP; //Then we must go up
				if (currentFloor == (floors.length - 1)) //Are we at the top?
					currentDirection = Direction.DOWN; //Then we must go down
			}
		}
	}
	
	private void ding()
	{
		
	}
}
