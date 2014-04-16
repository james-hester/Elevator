import javax.swing.*;

public class ElevatorSystem extends JFrame
{
	private static final long	serialVersionUID = 1L;
	private static final int	NUM_ELEVATORS = 5;
	private static final int	NUM_FLOORS = 6;
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
	
	public ElevatorSystem(int numberOfFloors, int numberOfElevators)
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
		
		ElevatorGUI theGUI = new ElevatorGUI( elevators, floors );
		ElevatorController theController = new ElevatorController( elevators, floors );
		
		setContentPane(theGUI);
		setSize(theGUI.getSize());
	}
	
	public static void main(String[] args)
	{
		ElevatorSystem theApp = new ElevatorSystem( NUM_FLOORS, NUM_ELEVATORS );
		theApp.setResizable(false);
		theApp.setDefaultCloseOperation(EXIT_ON_CLOSE);
		theApp.setVisible(true);
	}

}


