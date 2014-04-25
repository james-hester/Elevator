import java.util.ArrayList;


public class Building {
	
	private ArrayList<Elevator> elevators;
	private ArrayList<Floor> floors;
	private LiftOperator control;
	
	//Constructor for Building
	public Building(LiftOperator control) {
		elevators = new ArrayList<Elevator>();
		floors = new ArrayList<Floor>();
		this.control = control;
	}
	
	public ArrayList<Elevator> getElevators() {
		return elevators;
	}
	
	public ArrayList<Floor> getFloors() {
		return floors;
	}
	
	public LiftOperator getOperator() {
		return control;
	}
	
	public void addElevator() {
		elevators.add(new Elevator());
	}
	
	public void addFloor() {
		floors.add(new Floor());
	}
	
	public void addFloor(int height) {
		floors.add(new Floor(height));
	}
	
}
