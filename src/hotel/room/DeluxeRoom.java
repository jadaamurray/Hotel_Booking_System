package hotel.room;

public class DeluxeRoom extends Room {
    private String sizeBalcony;
    private String view;

    public DeluxeRoom(int roomNumber, int floorNumber, int occupantsMax, double pricePerNight, String sizeBalcony, String view) {
        super(roomNumber, floorNumber, occupantsMax, pricePerNight);
        this.sizeBalcony = sizeBalcony;
        if (isViewValid(view)) {
            this.view = view;
        }
    }

    public String getSizeBalcony() {
        return sizeBalcony;
    }
    public String getView() {
        return view;
    }
    public void setSizeBalcony(String sizeBalcony) {
        this.sizeBalcony = sizeBalcony;
    }
    public void setView(String view) {
        this.view = view;
    }

    public boolean isViewValid(String view){
        return switch (view) {
            case "Sea View", "sea view", "seaview", "SeaView", "Sea", "sea", "Landmark View", "landmark view",
                 "landmarkview", "LandmarkView", "Landmark", "landmark", "Mountain View", "mountain view",
                 "mountainview", "MountainView", "mountain", "Mountain" -> true;
            default -> false;
        };
    }

    @Override
    public String getRoomType() {
        return "Deluxe";
    }

    @Override
    public String toString() {
        return "Room Type: " + getRoomType() + ", Room Number: " + getRoomNumber() + ", Floor Number: " + getFloorNumber() + ", Occupants Max: " + getMaxOccupancy() + ", PricePerNight: " + getPricePerNight() + ", Balcony Size: " + sizeBalcony + ", View: " + view;
    }
}
