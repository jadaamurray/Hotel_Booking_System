package hotel.room;

public class SuiteRoom extends Room {
    private String livingArea;
    private int bathrooms;
    private boolean kitchenette;

    public SuiteRoom(int roomNumber, int floorNumber, int occupantsMax, double pricePerNight, String livingArea, int Bathrooms, boolean kitchenette) {
        super(roomNumber, floorNumber, occupantsMax, pricePerNight);
        this.livingArea = livingArea;
        this.bathrooms = Bathrooms;
        this.kitchenette = kitchenette;
    }

    public String getLivingArea() {
        return livingArea;
    }
    public int getBathrooms() {
        return bathrooms;
    }
    public boolean getKitchenette() {
        return kitchenette;
    }
    public void setLivingArea(String livingArea) {
        this.livingArea = livingArea;
    }
    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }
    public void setKitchenette(boolean kitchenette) {
        this.kitchenette = kitchenette;
    }

    @Override
    public String getRoomType() {
        return "Suite";
    }

    @Override
    public String toString() {
        return "Room Type: " + getRoomType() + ", Room number: " + getRoomNumber() + ", Floor number: " + getFloorNumber() + ", Maximum number of occupants: " + getMaxOccupancy() + ", Price per night: " + getPricePerNight() + ", Living area size: " + livingArea + ", Number of bathrooms: " + bathrooms + ", Kitchenette: " + kitchenette;
    }
}
