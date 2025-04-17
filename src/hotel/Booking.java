package hotel;

import hotel.room.Room;

import java.time.LocalDate;
import java.util.Random;

public class Booking implements Overlappable{
    private int bookingID;
    private Customer customer;
    private Room room;
    private double bookingCost;
    private int occupants;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate bookingDate;


    public Booking(Customer customer, Room selectedRoom) {
        this.bookingID = new Random().nextInt(99999);
    }
    public Booking(Customer customer, Room room, int occupants, LocalDate checkInDate, LocalDate checkOutDate) {
        bookingID = new Random().nextInt(99999);
        this.customer = customer;
        this.room = room;
        this.occupants = occupants;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        int nights = (int) (checkOutDate.toEpochDay() - checkInDate.toEpochDay()); // int because the number of nights must be a whole number
        this.bookingCost = room.getPricePerNight() * nights;;
        bookingDate = LocalDate.now();
    }
    public int getBookingID() {
        return bookingID;
    }
    public Customer getCustomer() {
        return customer;
    }
    public Room getRoom() {
        return room;
    }
    public double getBookingCost() {
        return bookingCost;
    }
    public int getOccupants() {
        return occupants;
    }
    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingID() {
        bookingID = new Random().nextInt(99999);
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    public void setBookingCost() { // Calculating booking cost
        // Using the number of days since 01/01/1970 to get the number of nights
        int nights = (int) (checkOutDate.toEpochDay() - checkInDate.toEpochDay()); // int because the number of nights must be a whole number
        this.bookingCost = room.getPricePerNight() * nights;;
    }
    public void setOccupants(int occupants) {
        this.occupants = occupants;
    }
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    // No booking date setter as this should remain unchanged

    @Override
    public boolean isOverlapping(LocalDate checkInDate, LocalDate checkOutDate) {
        if (this.checkInDate.isAfter(checkInDate) && this.checkInDate.isBefore(checkOutDate)) {
            return true;
        } else return this.checkOutDate.isAfter(checkInDate) && this.checkOutDate.isBefore(checkOutDate);
    }

    @Override
    public String toString() {
        System.out.println("Booking ID: " + bookingID);
        System.out.println("Customer: " + customer);
        System.out.println("Room: " + room.toString());
        System.out.println("Occupants: " + occupants);
        System.out.println("Check In Date: " + checkInDate);
        System.out.println("Check Out Date: " + checkOutDate);
        System.out.println("Booking Cost: " + bookingCost);
        System.out.println("Booking Date: " + bookingDate);
        return " ";
    }
}

