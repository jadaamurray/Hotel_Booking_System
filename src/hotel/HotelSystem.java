package hotel;

import hotel.room.DeluxeRoom;
import hotel.room.Room;
import hotel.room.StandardRoom;
import hotel.room.SuiteRoom;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class HotelSystem implements HotelCustomer, HotelManager {
    private static List<Room> rooms = new ArrayList<>(); // List of all registered rooms
    private static List<Booking> bookings = new ArrayList<>(); // List of all bookings
    private static HashMap<Integer, Room> roomsMap = new HashMap<>(); // Room number is the key, Room object is the value
    private static HashMap<Integer, Booking> bookingsMap = new HashMap<>(); //Booking ID is the key, booking object is the value

    public HotelSystem() { //Providing scalability by being able to create a new hotel system if needed.
        rooms = new ArrayList<>();
        bookings = new ArrayList<>();
        roomsMap = new HashMap<>();
        bookingsMap = new HashMap<>();
    }

    private static class RoomNumberComparator implements Comparator<Room> {
        @Override
        public int compare(Room r1, Room r2) {
            if (r1.getRoomNumber()<r2.getRoomNumber()) {
                return -1;
            } else if (r1.getRoomNumber()>r2.getRoomNumber()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static class RoomFloorNumberComparator implements Comparator<Room> {
        @Override
        public int compare(Room r1, Room r2) {
            if (r1.getFloorNumber()<r2.getFloorNumber()) {
                return 1;
            } else if (r1.getFloorNumber()>r2.getFloorNumber()) {
                return -1;
            } else {
                return 0;
            }
        }
    }


    private static class RoomPriceComparator implements Comparator<Room> {
        @Override
        public int compare(Room r1, Room r2) {
            if (r1.getPricePerNight()<r2.getPricePerNight()) {
                return -1;
            } else if (r1.getFloorNumber()>r2.getFloorNumber()) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    @Override
    public void availableRooms() {
        Scanner input = new Scanner(System.in);
        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;

        // Exiting the method if there are no rooms in the system
        if (rooms.isEmpty()) {
            System.out.println("There are no rooms available.");
            return;
        }

        // Validating check in and out dates
        do {
            checkInDate = getValidDateInput(input, "Please enter a check in date in the format yyyy-MM-dd: ");
            checkOutDate = getValidDateInput(input, "Please enter a check out date in the format yyyy-MM-dd: ");
        } while (!areDatesValid(checkInDate, checkOutDate));

        // Display rooms that are available within the timeframe
        System.out.println("Dates are valid. Details of Available Rooms:");
        if (bookings.isEmpty()) {
            listRooms();
        } else {
            boolean availability = false;
            for (Room r : rooms) { // Checking if a room has any bookings that overlap with the given check in and out dates
                if (isRoomAvailable(r, checkInDate, checkOutDate)) {
                    System.out.println(r.toString());
                    availability = true;
                }
            }
            if (!availability) {
                System.out.println("There are no rooms available.");
            }
        }
    }
    @Override
    public void availableRoomsByTypeOccupancy() {
        Scanner input = new Scanner(System.in);
        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;

        // Getting number of occupants and type of room desired
        int occupants = getValidOccupants(input);
        if (occupants == 0){ // 0 means there are no rooms in the system
            return;
        }
        input.nextLine();

        //Validating check in and out dates
        do {
            checkInDate = getValidDateInput(input, "Please enter a check in date in the format yyyy-MM-dd: ");
            checkOutDate = getValidDateInput(input, "Please enter a check out date in the format yyyy-MM-dd: ");
        } while (!areDatesValid(checkInDate, checkOutDate));

        // Listing rooms based that match a type and occupancy
        int roomType = getValidIntInput(input, "What type of room do you require? \n[1] Standard \n[2]Deluxe \n[3]Suite \nPlease choose 1, 2, or 3: ");
        boolean availability = false;
        switch (roomType) {
            case 1:
                for (Room r : rooms) {
                    if (r instanceof StandardRoom && isRoomAvailable(r, checkInDate, checkOutDate) && r.getMaxOccupancy() >= occupants) {
                        System.out.println(r.toString());
                        availability = true;
                    }
                }
                if (!availability) {
                    System.out.println("There are no rooms available.");
                }
                return;
            case 2:
                for (Room r : rooms) {
                    if (r instanceof DeluxeRoom && isRoomAvailable(r, checkInDate, checkOutDate) && r.getMaxOccupancy() >= occupants) {
                        System.out.println(r.toString());
                        availability = true;
                    }
                }
                if (!availability) {
                    System.out.println("There are no rooms available.");
                }
                return;
            case 3:
                for (Room r : rooms) {
                    if (r instanceof SuiteRoom && isRoomAvailable(r, checkInDate, checkOutDate) && r.getMaxOccupancy() >= occupants) {
                        System.out.println(r.toString());
                        availability = true;
                    }
                }
                if (!availability) {
                    System.out.println("There are no rooms available.");
                }
                return;
            default:
                System.out.println("Invalid room type. Please choose 1, 2, or 3");
        }
    }



    @Override
    public String makeBooking(int roomNumber) {
        Scanner input = new Scanner(System.in);
        Room selectedRoom = null;
        LocalDate checkInDate = null;
        LocalDate checkOutDate = null;

        // Validating if room number provided exists
        if (roomsMap.containsKey(roomNumber)){
            selectedRoom = roomsMap.get(roomNumber);
        } else { //exiting the method as the room number provided is invalid.
            return "Room not found.";
        }

        // Determining check in and out dates
        do {
            checkInDate = getValidDateInput(input, "Please enter a check in date in the format yyyy-MM-dd: ");
            checkOutDate = getValidDateInput(input, "Please enter a check out date in the format yyyy-MM-dd: ");
        } while (!areDatesValid(checkInDate, checkOutDate));

        // Determining if room holds the amount of people required and is available for the dates given
        int occupants = getValidIntInput(input, "How many people will be staying? ");
        if (selectedRoom.getMaxOccupancy() < occupants) {
            return "Room not suitable for group size.";
        }
        if (!isRoomAvailable(selectedRoom, checkInDate, checkOutDate)) {
            return "Room not available for dates chosen.";
        }

        // Registering customer information and booking
        Customer customer = addCustomer(input);
        Booking booking = new Booking(customer, selectedRoom, occupants, checkInDate, checkOutDate);
        bookings.add(booking);
        bookingsMap.put(booking.getBookingID(), booking);
        return "Room booked successfully. \nBooking ID: " + booking.getBookingID() + "\nBooking Cost: £" + booking.getBookingCost();
    }

    @Override
    public void deleteBooking() {
        Scanner input = new Scanner(System.in);
        while (true) {
            int bookingID = getValidIntInput(input, "Please enter your Booking ID: ");
            if (bookingsMap.containsKey(bookingID)) {
                bookings.remove(bookingsMap.get(bookingID));
                bookingsMap.remove(bookingID);
                System.out.println("Booking cancelled successfully.");
                return;
            }
            System.out.println("Booking ID not found.");
        }
    }

    // Method to check if a room is available
    public boolean isRoomAvailable(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        for (Booking b : bookings) { // Checking if a room has any bookings that overlap with the given check in and out dates
            if (b.getRoom().equals(room) && b.isOverlapping(checkInDate, checkOutDate)) {
                return false;
            }
        }
        return true;
    }
    // Method to collect customer details and create customer object
    public Customer addCustomer(Scanner input) {
            input.nextLine();
            System.out.print("What is your name? ");
            String name = input.nextLine();
            System.out.print("What is you email address? ");
            String email = input.nextLine();
            System.out.print("What is your phone number? ");
            String phone = input.nextLine();

            return new Customer(name, email, phone);
    }

    // Method to get a valid number of occupants that can stay in one room that exists within the system
    private int getValidOccupants (Scanner input) {
        int occupants = 0;
        if (rooms.isEmpty()) {
            System.out.println("There are no rooms available.");
            return 0;
        }
        while (true) {
            occupants = getValidIntInput(input, "How many people will be staying? ");
            if (occupants <= 0) {
                System.out.println("Invalid input. Must be at least 1 occupant.");
            } else {
                boolean canAccommodate = false;
                for (Room r : rooms) {
                    if (r.getMaxOccupancy() >= occupants) {
                        canAccommodate = true;
                        return occupants;
                    }
                }
                System.out.println("There isn't a room that can accommodate your group size. Please adjust the number of occupants or book multiple rooms.");
            }
        }
    }

    @Override
    public void addRoom() {
        System.out.println("Adding Room...");
        System.out.println("More information needed");
        Scanner input = new Scanner(System.in);

        // Determining room number
        int roomNumber = getValidIntInput(input, "Please enter the room number: ");
        if (roomsMap.containsKey(roomNumber)) { // Checking if the room number already exists in the system
            System.out.println("Room number already exists. Cannot add duplicate room.");
            return;
        }
        System.out.println("Room number confirmed: " + roomNumber); // If no duplicates, confirm the room number

        // Determining floor number
        int floorNumber = getValidIntInput(input, "What number floor is this room located on?");

        // Determining max occupants
        int occupantsMax = getValidIntInput(input, "What is the maximum number of occupants allowed in this room?");

        // Determining the price of the room per night
        double pricePerNight = getValidDoubleInput(input, "What is the price of the room per night?");

        // Determining what type of room is to be added.
        while (true) { // Loops until an input of either 1, 2, or 3 is given
            int choice = getValidIntInput(input, "Is this a Standard, Deluxe, or Suite Room?\n[1] Standard \n[2] Deluxe \n[3] Suite Room");
            switch (choice) {
                case 1: // Standard Room
                    System.out.println("Adding Standard Room");
                    int windows = getValidIntInput(input, "How many windows are in the room?");
                    StandardRoom roomStandard = new StandardRoom(roomNumber, floorNumber, occupantsMax, pricePerNight, windows);
                    rooms.add(roomStandard);
                    roomsMap.put(roomNumber, roomStandard);
                    System.out.println("Standard room added succesfully.");
                    return;
                case 2: // Deluxe Room
                    System.out.println("Adding Deluxe Room");
                    System.out.println("How big is the balcony in sqm? (Include units)");
                    String balconySize = input.nextLine();
                    System.out.println("What view does this room have? Sea View, Landmark View, or Mountain View?");
                    String view = input.nextLine(); // View
                    DeluxeRoom roomDeluxe = new DeluxeRoom(roomNumber, floorNumber, occupantsMax, pricePerNight, balconySize, view);
                    rooms.add(roomDeluxe);
                    roomsMap.put(roomNumber, roomDeluxe);
                    System.out.println("Deluxe room added succesfully.");
                    return;
                case 3: // Suite Room
                    System.out.println("Adding Suite Room");
                    String livingArea = "N/A";
                    System.out.println("How big is the living area in sqm? (Include units)");
                    livingArea = input.nextLine(); // Living Area
                    int bathrooms = getValidIntInput(input, "How many bathrooms are in the suite?"); // Bathrooms
                    boolean kitchenette = getValidBooleanInput(input, "Is there a kitchenette in the suite? (true/false"); // Kitchenette
                    SuiteRoom roomSuite = new SuiteRoom(roomNumber, floorNumber, occupantsMax, pricePerNight, livingArea, bathrooms, kitchenette);
                    rooms.add(roomSuite);
                    roomsMap.put(roomNumber, roomSuite);
                    System.out.println("Suite room added succesfully.");
                    return;
                default:
                    System.out.println("Invalid input. Please choose 1, 2, or 3");
            }
        }
    }

    @Override
    public boolean deleteRoom() {
        Scanner input = new Scanner(System.in);
        System.out.println("You have selected to delete a room.");
        System.out.println("Please choose what room you would like to delete:");
        listRooms(); //Printing all rooms in the hotel system.
        int roomNumber = getValidIntInput(input, "Write the room number: ");
        if (roomsMap.containsKey(roomNumber)) { //checking if the room number provided exists in the system
            rooms.remove(roomsMap.get(roomNumber));
            roomsMap.remove(roomNumber);
            return true;
        }
        System.out.println("Room " + roomNumber + " not found.");
        return false;
    }

    @Override
    public void listRooms() {
        if(rooms.isEmpty()) {
            System.out.println("There are no rooms registered in the system.");
        } else {
            for (Room r : rooms) {
                System.out.println(r.toString());
            }
        }
    }
    // List all registered rooms sorted by room number in ascending order
    @Override
    public void listRoomsByRoomNumber(List<Room> rooms) {
        rooms.sort(new RoomNumberComparator());
        listRooms();
    }
    // List all registered rooms by floor number in descending order
    @Override
    public void ListRoomsByFloorNumber(List<Room> rooms) {
        rooms.sort(new RoomFloorNumberComparator());
        listRooms();
    }

    @Override
    public void bookingReport(Scanner input, List<Booking> bookings){
        // Determine and validate time frame
        input.nextLine();
        LocalDate startDate = getValidDateInput(input, "Please enter start date in the format yyyy-MM-dd: ");
        LocalDate endDate = getValidDateInput(input, "Please enter end date in the format yyyy-MM-dd: ");
        if (!areDatesValid(startDate, endDate)) {
            return;
        }

        // Generate report
        try (FileWriter writer = new FileWriter("BookingReport.txt")) {
            writer.write("Booking Report\n");
            writer.write("Time Frame: " + startDate + " to " + endDate + "\n\n");

            boolean hasBookings = false;

            for (Booking booking : bookings) {
                if (!booking.getCheckOutDate().isBefore(startDate) && !booking.getCheckInDate().isAfter(endDate)) {
                    hasBookings = true;

                    Room room = booking.getRoom();
                    Customer customer = booking.getCustomer();

                    writer.write("Booking Date: " + booking.getBookingDate() + "\n");
                    writer.write("Room Number: " + room.getRoomNumber() + "\n");
                    writer.write("Room Type: " + room.getClass().getSimpleName() + "\n");
                    writer.write("Price per Night: £" + room.getPricePerNight() + "\n");
                    writer.write("Customer Name: " + customer.getName() + "\n");
                    writer.write("Customer Email: " + customer.getEmail() + "\n");
                    writer.write("Check-in Date: " + booking.getCheckInDate() + "\n");
                    writer.write("Check-out Date: " + booking.getCheckOutDate() + "\n");
                    writer.write("----------------------------\n");
                }
            }

            if (!hasBookings) { // If there are no bookings
                writer.write("No bookings found for the specified time frame.\n");
            }

            System.out.println("Booking report generated: BookingReport.txt");
        } catch (IOException e) { // If an error occurs while writing to file
            System.out.println("An error occurred while writing the report: " + e.getMessage());
        }
    }


    // Method to validate int input
    private int getValidIntInput(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " ");
                return input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            }
        }
    }

    // Method to validate double input
    private double getValidDoubleInput(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " ");
                return input.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number (e.g., 299.99).");
                input.nextLine();
            }
        }
    }

    // Method to validate boolean input
    private boolean getValidBooleanInput(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String response = input.next().trim().toLowerCase();
            if (response.equals("true") || response.equals("t")) return true;
            if (response.equals("false") || response.equals("f")) return false;
            System.out.println("Invalid input. Please enter 'true' or 'false'.");
        }
    }

    //Method to validate check in and out input
    private LocalDate getValidDateInput(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            try{
                String dateString = input.nextLine();
                return LocalDate.parse(dateString);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter valid check in dates in the format yyyy-MM-dd.");
            }
        }
    }

    //validating the check in dates input are valid
    public boolean areDatesValid(LocalDate checkInDate, LocalDate checkOutDate) {
        if(checkInDate.isBefore(LocalDate.now()) || checkInDate.isEqual(LocalDate.now())) {
            System.out.println("Please enter dates that after today.");
            return false;
        } else if(checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)) {
            System.out.println("Please enter a check in/start date that is at least one day before your check out/end date.");
            return false;
        }
        return true;
    }

    // Method to ask the user if the want to go to the main menu or exit the program
    public boolean menuOrExitPrompt(Scanner input) {
        while (true) {
            int choice = getValidIntInput(input, """
                    Back to main menu or exit?
                    [1] Main Menu
                    [2] Exit
                    """);
            if (choice == 1) {
                return true;
            }else if (choice == 2) {
                System.out.println("Exiting...");
                return false;
            } else {
                System.out.println("Invalid input. Please choose 1 or 2");
            }
        }
    }

    public static void main(String[] args) {
        HotelSystem hotelSystem = new HotelSystem();
        // Pre-populating the system
        StandardRoom room5 = new StandardRoom(205, 2, 2, 150,2);
        rooms.add(room5);
        roomsMap.put(room5.getRoomNumber(), room5);
        StandardRoom room1 = new StandardRoom(101, 1, 2, 100,2);
        rooms.add(room1);
        roomsMap.put(room1.getRoomNumber(), room1);
        StandardRoom room2 = new StandardRoom(102, 3, 4, 175,4);
        rooms.add(room2);
        roomsMap.put(room2.getRoomNumber(), room2);
        DeluxeRoom room3 = new DeluxeRoom(303, 3,4, 260, "2 sqm", "Sea View");
        rooms.add(room3);
        roomsMap.put(room3.getRoomNumber(), room3);
        Customer customer = new Customer("John Doe", "johndoe@gmail.com", "07123456789");
        Customer janeSmith = new Customer("Jane Smith", "janesmith@aol.com", "07987654321");
        Booking booking = new Booking(customer, room5, 2, LocalDate.parse("2025-05-29"), LocalDate.parse("2025-06-10"));
        bookings.add(booking);
        bookingsMap.put(booking.getBookingID(), booking);
        Booking booking1 = new Booking(janeSmith, room3, 2, LocalDate.parse("2025-05-29"), LocalDate.parse("2025-06-10"));
        bookings.add(booking1);
        bookingsMap.put(booking1.getBookingID(), booking1);
        System.out.println("Welcome to the Hotel Booking Management System");


        // Text Menu
        Scanner input = new Scanner(System.in);
        boolean isAdmin = false;
        boolean isAdmin2 = false;
        int choice = 0;
        int adminChoice = 0;
        // Text Menu for if either the admin or customer
        outer:
        while (true) {
            if (isAdmin) { // Admin menu
                adminChoice = hotelSystem.getValidIntInput(input, """
                        Hello Admin.
                        Please choose (a number) what you would like to do from the following option list:
                        [1] Add Room
                        [2] Delete Room
                        [3] List Rooms by Room Number
                        [4] List Rooms by Floor Number
                        [5] Generate Booking Report
                        [6] Return to Main Menu
                        """);
                isAdmin2 = true; // starts switch statement for determining admin input
            } else { // The customer menu shows by default
                choice = hotelSystem.getValidIntInput(input, """
                        Please choose (a number) what you would like to do from the following option list:
                        [1] List Available Rooms
                        [2] Make Booking
                        [3] Cancel Booking
                        [4] Admin
                        [5] Exit
                        """);
                if (choice == 4) {
                    isAdmin = true;
                } else if (choice == 1 || choice == 2 || choice == 3) {
                    // Moves to customer menu
                } else if (choice == 5) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid input. Please choose 1, 2, 3, 4 or 5");
                     // Could change to go back to beginning of loop
                }
            }

            // Admin menu selection output
            if (isAdmin2) {
                switch (adminChoice) {
                    case 1: // Add a room
                        System.out.println("You have selected to add a room.");
                        hotelSystem.addRoom();
                        // Prompt to go to menu or exit after action has completed
                        if (hotelSystem.menuOrExitPrompt(input)) {
                            isAdmin = false;
                            isAdmin2 = false;
                            break; // Back to menu
                        } else {
                            break outer; // Exit
                        }

                    case 2: // Delete a room
                        if (hotelSystem.deleteRoom()) {
                            System.out.println("Room deleted successfully.");
                            System.out.println("Registered rooms: ");
                            hotelSystem.listRooms();
                        } else {
                            System.out.println("Room could not be deleted.");
                        }
                        // Prompt to go to menu or exit after action has completed
                        if (hotelSystem.menuOrExitPrompt(input)) {
                            isAdmin = false;
                            isAdmin2 = false;
                            break; // Back to menu
                        } else {
                            break outer; // Exit
                        }
                    case 3: // List rooms by room number is ascending order
                        System.out.println("Listing by Room Number...");
                        hotelSystem.listRoomsByRoomNumber(rooms);
                        // Prompt to go to menu or exit after action has completed
                        if (hotelSystem.menuOrExitPrompt(input)) {
                            isAdmin = false;
                            isAdmin2 = false;
                            break; // Back to menu
                        } else {
                            break outer; // Exit
                        }
                    case 4: // List rooms by floor number with the top floor displayed first
                        System.out.println("Listing by Floor Number...");
                        hotelSystem.ListRoomsByFloorNumber(rooms);
                        // Prompt to go to menu or exit after action has completed
                        if (hotelSystem.menuOrExitPrompt(input)) {
                            isAdmin = false;
                            isAdmin2 = false;
                            break; // Back to menu
                        } else {
                            break outer; // Exit
                        }
                    case 5: // Booking Report
                        System.out.println("Generating Booking Report...");
                        hotelSystem.bookingReport(input, bookings);
                        // Prompt to go to menu or exit after action has completed
                        if (hotelSystem.menuOrExitPrompt(input)) {
                            isAdmin = false;
                            isAdmin2 = false;
                            break; // Back to menu
                        } else {
                            break outer; // Exit
                        }
                    case 6:
                        isAdmin = false;
                        isAdmin2 = false;
                        break;
                    default:
                        System.out.println("Invalid input. Please enter the number that corresponds with your choice. 1, 2, 3, 4, 5, or 6.");
                        break;
                }
            }

            //Customer menu selection output
            int listChoice = 0;
            boolean availableRoomChoice = false;
            if (!isAdmin){
                switch (choice) {
                    case 1: // List Available Rooms
                        listChoice = hotelSystem.getValidIntInput(input,"""
                            Would you like to list available rooms by price or would you like to see a certain type of room that can accommodate your group size?
                            [1] List Available Rooms by Price
                            [2] List Available Rooms that Match a Type and Occupancy""");
                        availableRoomChoice = true;
                        break;
                    case 2: // Make Booking
                        System.out.println("Thank you for choosing to book with us. Would you like to see what rooms match your check in and out dates first?");
                        int availChoice = hotelSystem.getValidIntInput(input, "[1] List Available Rooms \n[2] Go Straight to Booking \n");
                        if (availChoice == 1) {
                            hotelSystem.availableRooms();
                        } else if (availChoice == 2) {
                        } else {
                            System.out.println("Invalid input. Please choose 1 or 2");
                            break;
                        }
                        int roomNumber = hotelSystem.getValidIntInput(input, "Please provide the room number of the room you wish to book: ");
                        System.out.println(hotelSystem.makeBooking(roomNumber));
                        break;
                    case 3: // Delete Booking
                        System.out.println("We are sorry you have decided to cancel your booking with us.");
                        hotelSystem.deleteBooking();
                        break;
                }
                if (availableRoomChoice) { // Secondary menu selection output for list available rooms
                    switch (listChoice) {
                        case 1: // List available rooms by Price
                            System.out.println("More information needed to list available rooms...");
                            rooms.sort(new RoomPriceComparator());
                            hotelSystem.availableRooms();
                            // Prompt to go to menu or exit after action has completed
                            if (hotelSystem.menuOrExitPrompt(input)) {
                                break; // Back to menu
                            } else {
                                break outer; // Exit
                            }
                        case 2: // List available rooms that match a type and occupancy
                            System.out.println("More information needed to list available rooms...");
                            hotelSystem.availableRoomsByTypeOccupancy();
                            // Prompt to go to menu or exit after action has completed
                            if (hotelSystem.menuOrExitPrompt(input)) {
                                break; // Back to menu
                            } else {
                                break outer; // Exit
                            }
                            default:
                            System.out.println("Invalid input. Please choose 1 or 2.");
                            // Prompt to go to menu or exit after action has completed
                            if (hotelSystem.menuOrExitPrompt(input)) {
                                break; // Back to menu
                            } else {
                                break outer; // Exit
                            }
                    }
                }
            }
        }
    }
}
