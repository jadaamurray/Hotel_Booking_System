package hotel;

public interface HotelCustomer {
    void availableRooms();
    void availableRoomsByTypeOccupancy();
    String makeBooking(int roomNumber);
    void deleteBooking();
}
