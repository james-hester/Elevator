import java.util.ArrayList;


public abstract class LiftOperator implements Steppable {
	
	class ElevatorCall {
		Floor floor;
		Elevator.DIRECTION direction;
		
		public ElevatorCall(Floor floor, Elevator.DIRECTION direction) {
			this.floor = floor;
			this.direction = direction;
		}
		
		public Floor getFloor() {
			return floor;
		}
		
		public Elevator.DIRECTION getDirection() {
			return direction;
		}
	}
	
	ArrayList<ElevatorCall> elevatorCalls;
	
	public LiftOperator() {
		World.steppables.add(this);
		elevatorCalls = new ArrayList<ElevatorCall>();
	}
	
	public abstract void step();
	
	public double distanceFromFloor(Floor floor, Elevator elevator) {
		return Math.abs(floor.getHeight()-elevator.getLocation());
	}
	
	public Floor getFloor(Elevator elevator) {
		return World.getBuilding().getFloors().get((int)(elevator.getLocation()/World.getBuilding().getFloors().get(0).getHeight()));
	}
	
	public void setElevatorState(Elevator.DIRECTION direction, Elevator elevator) {
		elevator.setState(direction);
	}
	
	public void callElevator(Floor floor, Elevator.DIRECTION direction) {
		elevatorCalls.add(new ElevatorCall(floor, direction));
	}
	
}
