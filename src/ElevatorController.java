import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ElevatorController
{
	private Elevator[]	elevators;
	private Floor[]		floors;
	private static final int	ELEVATOR_EVENT_FREQUENCY = 10;
	private static final int	GUEST_GENERATION_FREQUENCY = 1500;
	
	public ElevatorController(Elevator[] e, Floor[] f)
	{
		elevators = e;
		floors = f;
		
		new Timer().scheduleAtFixedRate(new ElevatorMotionLogic(), 1, ELEVATOR_EVENT_FREQUENCY);
		new Timer().scheduleAtFixedRate(new GuestGenerationLogic(), 5, GUEST_GENERATION_FREQUENCY);
	}
	
	public class GuestGenerationLogic extends TimerTask
	{
		@Override
		public void run()
		{
			synchronized (floors)
			{
				for(int i = 0; i < floors.length; i++)
					floors[i].generateGuests();
			}
		}
		
	}
	
	public class ElevatorMotionLogic extends TimerTask
	{
		@Override
		public void run()
		{
			for(int i = 0; i < elevators.length; i++)
			{
				moveOneElevator(elevators[i]);
			}
		}
		
		public void moveOneElevator(Elevator e)
		{
			int currentFloor = e.getFloor();
			
			if( e.isBetweenFloors() )
			{
				e.takeStep();
			}
			else if ( e.isStopped() )
			{
				e.waitAtFloor();			
			}
			else
			{
				if (e.getLights()[currentFloor] == true || (floors[currentFloor].majorityDirectionPreference() == e.getDirection()) ) //If people want to get off at this floor
				{
					
					e.stop();
					
					{
						e.getGuests()[currentFloor] = 0; //Let them off
						e.getLights()[currentFloor] = false; //and then turn the light off
					}
					e.checkFlipDirection(); //Are we at the top or bottom of the building? If so, reverse the direction.

					if (e.isGoingUp())
					{
						floors[currentFloor].takeGuestsGoingUp(e.getGuests());
					}
					else
					{
						floors[currentFloor].takeGuestsGoingDown(e.getGuests());
					}

					//Now that guests are on we want to set the lights up
					e.updateLights();

					/*
					 * But what if no one got on the elevator after all this? There must thus be passengers on other elevators.
					 * To figure out which floor to go to we must see how many guests are on each of the other elevators.
					 * The elevator will go to the floor that the fewest other elevators are going to, preferring a higher
					 * floor to a lower one.
					 */

					boolean[] falseArray = new boolean[e.getLights().length];
					Arrays.fill(falseArray, false);
					if (Arrays.equals(e.getLights(), falseArray)) //If every value of lights is false
					{
						int[] weights = new int[floors.length];
						int indexOfSmallestWeight, smallestWeight;
						for (Elevator elevator : elevators)
						{
							for(int i = 0; i < weights.length; i++)
							{
								weights[i] += elevator.getGuests()[i];
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

						e.getLights()[indexOfSmallestWeight] = true;

					}

					//e.stop();
					e.keepGoing(); //start traveling
				}
				else
				{
					e.keepGoing();
					e.checkFlipDirection();
				}
			}
			
		}//moveOneElevator(Elevator)
		
	} //ElevatorControllerEventHandler

}
