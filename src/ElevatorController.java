import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author james
 *
 */
public class ElevatorController
{
	private static final int	ELEVATOR_EVENT_FREQUENCY = 10;
	private static final int	GUEST_GENERATION_FREQUENCY = 1500;
	
	public ElevatorController()
	{		
		new Timer().scheduleAtFixedRate(new ElevatorMotionLogic(), 1, ELEVATOR_EVENT_FREQUENCY);
		new Timer().scheduleAtFixedRate(new GuestGenerationLogic(), 5, GUEST_GENERATION_FREQUENCY);
	}
	
	public class GuestGenerationLogic extends TimerTask
	{
		@Override
		public void run()
		{
			for(int i = 0; i < ElevatorSystem.getNumberOfFloors(); i++)
				ElevatorSystem.getFloor(i).generateGuests();
		}
		
	}
	
	public class ElevatorMotionLogic extends TimerTask
	{
		@Override
		public void run()
		{
			for(int i = 0; i < ElevatorSystem.getNumberOfElevators(); i++)
			{
				moveOneElevator(ElevatorSystem.getElevator(i));
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
				if (e.getLights()[currentFloor] == true || (ElevatorSystem.getFloor(currentFloor).majorityDirectionPreference() == e.getDirection()) ) //If people want to get off at this floor
				{
					
					e.stop();

					e.getGuests()[currentFloor] = 0; //Let them off
					e.turnOffLight(currentFloor); //and then turn the light off

					if (e.isGoingUp())
					{
						ElevatorSystem.getFloor(currentFloor).takeGuestsGoingUp(e.getGuests());
					}
					else
					{
						ElevatorSystem.getFloor(currentFloor).takeGuestsGoingDown(e.getGuests());
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
						int[] weights = new int[ElevatorSystem.getNumberOfFloors()];
						int indexOfSmallestWeight, smallestWeight;
						for (int i = 0; i < ElevatorSystem.getNumberOfElevators(); i++)
						{
							for(int j = 0; j < weights.length; j++)
							{
								weights[j] += ElevatorSystem.getElevator(i).getLights()[j] ? 1 : 0;
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

						e.turnOnLight(indexOfSmallestWeight);

					}
				}
				e.keepGoing();
			}
			
		}//moveOneElevator(Elevator)
		
	} //ElevatorControllerEventHandler

}
