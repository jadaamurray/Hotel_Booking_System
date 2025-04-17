package hotel.room;

public abstract class Room {
    private int roomNumber;
    private int floorNumber;
    private int maxOccupancy;
    private double pricePerNight;

    public Room(int roomNumber, int floorNumber, int maxOccupancy, double pricePerNight) {
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.maxOccupancy = maxOccupancy;
        this.pricePerNight = pricePerNight;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
    public int getFloorNumber() {
        return floorNumber;
    }
    public int getMaxOccupancy() {
        return maxOccupancy;
    }
    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }
    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public abstract String getRoomType();

    @Override
    public String toString() {
        return "Room number: " + roomNumber + "Floor number: " + floorNumber + "Maximum number of occupants: " + maxOccupancy + "Price per night: " + pricePerNight;
    }
}
