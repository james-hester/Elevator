import java.util.ArrayList;


public class GuestSpace {
	
	ArrayList<GuestSpace> adjacentSpace = new ArrayList<GuestSpace>();
	ArrayList<Guest> guests = new ArrayList<Guest>();
	
	public GuestSpace() {
		World.guestSpaces.add(this);
	}
	
	public ArrayList<Guest> getGuests() {
		return guests;
	}

}
