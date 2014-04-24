import javax.swing.*;

/**
 * Main class for Elevator program.
 * Initializes as many Elevators and Floors as the user wants
 * and then sets up components of the program whose operation
 * depends on what these quantities are (ElevatorGUI and ElevatorController).
 * After doing so, serves as an access point for Elevators and Floors through
 * static accessor methods.
 * @author 
 * @version 1.2
 * 
 */
public class ElevatorSystem extends JFrame
{
	private static final long	serialVersionUID = 1L; //Compiler gives warning unless this is included. 
	
	private static final int	NUM_ELEVATORS = 6;
	private static final int	NUM_FLOORS = 6;
	/**
	 * Array storing each of the elevators used by the program.
	 * Initialized by the constructor of this class and accessible
	 * through getElevator(int) and getNumberOfElevators().
	 */
	private static Elevator[]	elevators;
	/**
	 * Array storing each of the floors used by the program.
	 * Likewise used by other classes through static accessor methods.
	 */
	private static Floor[]		floors;
	
	public ElevatorSystem(int numberOfFloors, int numberOfElevators)
	{
		//First, set up master arrays of elevators and floors.
		elevators = new Elevator[numberOfElevators];
		floors = new Floor[numberOfFloors];
		
		//Then, initialize each of the elevators and floors.

		for(int id = 0; id < numberOfElevators; id++)
		{
			elevators[id] = new Elevator(id);
		}
		
		for(int id = 0; id < numberOfFloors; id++)
		{
			floors[id] = new Floor(id);
		}
		
		ElevatorGUI theGUI = new ElevatorGUI();
		ElevatorController theController = new ElevatorController();
		
		setContentPane(theGUI);
		setSize(theGUI.getSize());
	}
	
	public static synchronized Elevator getElevator(int which)
	{
		return elevators[which];
	}
	
	public static synchronized Floor getFloor(int which)
	{
		return floors[which];
	}
	
	public static int getNumberOfFloors()
	{
		return floors.length;
	}
	
	public static int getNumberOfElevators()
	{
		return elevators.length;
	}
	
	public static void main(String[] args)
	{
		ElevatorSystem theApp = new ElevatorSystem( NUM_FLOORS, NUM_ELEVATORS );
		theApp.setResizable(false);
		theApp.setDefaultCloseOperation(EXIT_ON_CLOSE);
		theApp.setVisible(true);
	}

}


