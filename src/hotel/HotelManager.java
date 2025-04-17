package hotel;

import hotel.room.Room;

import java.util.List;
import java.util.Scanner;

public interface HotelManager {
    void addRoom();
    boolean deleteRoom();
    void listRooms();
    void listRoomsByRoomNumber(List<Room> rooms);
    void ListRoomsByFloorNumber(List<Room> rooms);
    void bookingReport(Scanner input, List<Booking> bookings);
}
