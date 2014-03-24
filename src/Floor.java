import java.util.Random;


public class Floor
{
	private static final int	MAX_PER_DESTINATION = 6;
	
	private Floor[]		floors;
	private int			id;
	private int[]		guests;
	
	public Floor(int _id, Elevator[] _elevators, Floor[] _floors)
	{
		id = _id;
		floors = _floors;
		
		guests = new int[floors.length];
		generateGuests();

		System.out.println();
	}
	
	public int[] getGuests()
	{
		return guests;
	}
	
	/**
	 * Puts all guests from this floor who want to go up into the array provided.
	 * Note: the
	 * @param guestsGoingUp reference to array 
	 */
	public void takeGuestsGoingUp(int[] guestsGoingUp)
	{
		for(int i = id; i < floors.length; i++)
		{
			guestsGoingUp[i] += guests[i];
			guests[i] = 0;
		}
		System.out.println("TakeGuestsGoingUp");
	}
	
	public void takeGuestsGoingDown(int[] guestsGoingDown)
	{
		for(int i = 0; i <= id; i++)
		{
			guestsGoingDown[i] += guests[i];
			guests[i] = 0;
		}
	}
	
	/**
	 * Randomly adds between 0 and (MAX_PER_DESTINATION * numberOfFloors) guests to this floor.
	 * Each guest's preference of floor is random, except no guest will want to travel to the floor
	 * it is currently on.
	 */
	public void generateGuests()
	{
		Random	r = new Random();
		for(int i = 0; i < floors.length; i++)
		{
			if (i != id)
				{ guests[i] += r.nextInt(MAX_PER_DESTINATION); }
			else
				{ guests[i] = 0; }
		}		
	}
	
	public Elevator.Direction majorityDirectionPreference()
	{
		int	up = 0, down = 0;
		for(int i = 0; i < id; i++)
			down += guests[i];
		for(int i = id; i < floors.length; i++)
			up += guests[i];
		
		//NOTE: the following will return Elevator.Direction.DOWN if an
		//equal number of guests want to go up and down
		Elevator.Direction result = (up > down ? Elevator.Direction.UP : Elevator.Direction.DOWN);
		//System.out.println("DirectionPreference: "+result);
		return result;
	}
	
	
	
}
