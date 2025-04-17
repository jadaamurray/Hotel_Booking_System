package hotel.room;

public class StandardRoom extends Room {
    private int windows;

    public StandardRoom(int roomNumber, int floorNumber, int occupantsMax, double pricePerNight, int windows) {
        super(roomNumber, floorNumber, occupantsMax, pricePerNight);
        this.windows = windows;
    }
    public int getWindows() {
        return windows;
    }
    public void setWindows(int windows) {
        this.windows = windows;
    }

    @Override
    public String getRoomType() {
        return "Standard";
    }

    @Override
    public String toString() {
        return "Room Type: " + getRoomType() + ", Room number: " + getRoomNumber() + ", Floor number: " + getFloorNumber() + ", Maximum number of occupants: " + getMaxOccupancy() + ", Price per night: " + getPricePerNight() + ", Number of windows: " + windows;
    }
}
