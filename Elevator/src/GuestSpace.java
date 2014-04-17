import java.util.ArrayList;


public class GuestSpace {
	
	ArrayList<GuestSpace> adjacentSpace;
	ArrayList<Guest> guests;
	
	public GuestSpace() {
		World.guestSpaces.add(this);
	}
	
	public ArrayList<Guest> getGuests() {
		return guests;
	}

}
