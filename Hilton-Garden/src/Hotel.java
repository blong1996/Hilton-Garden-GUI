import java.util.*;
import java.text.*;

/**
 *
 *  @author Brandon Long, Aaren Avery
 *  File: Hotel.java
 *  Project: Hilton-Garden
 *  Date: Oct-Nov 2016
 *  Class: COMP260 Programming Languages, Dr. Yu
 *  Assignment: Project 2
 *  File Description:
 *
 *  This class is used to represent the actual hotel it self. It takes in the number of rooms
 *  to be in the hotel and generates the rooms for it. Junior Suites are room numbers divisible
 *  by 5 Deluxe Rooms are divisible by 3 but not by 5, and all others are Standard Rooms.
 *
 */


public class Hotel {

    private int totalRooms, juniorRooms, deluxeRooms, standardRooms;
    private int reservations;


    private String roomChoiceError;
    private ArrayList<Room> hotel = new ArrayList<Room>();

    /**
     *
     * @param rooms number of rooms in hotel
     */

    public Hotel(int rooms){
        this.totalRooms = rooms;
        juniorRooms = 0;
        deluxeRooms = 0;
        standardRooms = 0;
        reservations = 0;

            // Add vacant rooms to hotel
        for (int x = 0; x < rooms; x++)  {
            if (x % 5 == 0) {
                hotel.add(new Room(false, 1000 + x, "Junior Suite", "", "", false, 180.00, null, null, 0, 0));
                juniorRooms++;
            }
            else if(x % 3 == 0) {
                hotel.add(new Room(false, 1000 + x, "Deluxe", "", "", false, 140.00, null, null, 0, 0));
                deluxeRooms++;
            }
            else {
                hotel.add(new Room(false, 1000 + x, "Standard", "", "", false, 120.00, null, null, 0, 0));
                standardRooms++;
            }

        }

    }

    /**
     *
     * @param firstName String for the First Name on the reservation
     * @param lastName String for the Last Name on the reservation
     * @param roomType this String value represents one of the three room types
     * @param safe true if the reservation includes a safe
     * @return  an integer representing the room number. if the room type is unavailable or other
     *          error occurs, return -1
     */

    public int reserve( String firstName, String lastName, String roomType,
                        boolean safe, String checkIn, String checkOut) {

        boolean roomFound = false; // boolean to see if room was found
        SimpleDateFormat myDateFormat = new SimpleDateFormat ("yyyy-MM-dd");
        System.out.println("User Check In String: "+ checkIn+"\nUser Check Out String: " +checkOut);
        Date checkInDate, checkOutDate;

            // Attempt to add reservation only if the inserted data is valid
        try {
            checkInDate = myDateFormat.parse(checkIn);
            checkOutDate = myDateFormat.parse(checkOut);

            System.out.println("Parsed Check In Date: "+ checkInDate+"\nParsed Check Out Date: " +checkOutDate);

            int days = (int)( (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24));
            System.out.println("Days of reservation: "+days);

            if (days <= 0)
                setRoomChoiceError("Check out date must be after check in date.");
            else {

                // Set Check In time to 3:00PM and Check Out time to 12:00PM
                checkInDate.setTime(checkInDate.getTime()+(1000*60*60*15));
                checkOutDate.setTime(checkOutDate.getTime()+(1000*60*60*12));
                System.out.println("Actual Check In Date: "+ checkInDate+"\nActual Check Out Date: " +checkOutDate);

                double total = 0.00;
                // if safe is requested, add it to the room total
                if (safe)
                    total += 20.00;

                // check to see if the room they desire is available. If so, reserve it
                for (Room room : hotel) {
                    if (room.getRoomType().equalsIgnoreCase(roomType)) {
                        if (roomType.equalsIgnoreCase("Junior Suite")) {

                            total += (180.00 * days);
                            reservations++;
                            juniorRooms--;
                            room.setRervation(true, firstName, safe, lastName, checkInDate,
                                    checkOutDate, total, reservations+10000, days);
                            return days;

                        } else if (roomType.equalsIgnoreCase("Deluxe")) {

                            total += (140.00 * days);
                            reservations++;
                            deluxeRooms--;
                            room.setRervation(true, firstName, safe, lastName, checkInDate,
                                    checkOutDate, total, reservations+10000, days);
                            return days;

                        } else if (roomType.equalsIgnoreCase("Standard")) {

                            total += (120.00 * days);
                            reservations++;
                            standardRooms--;
                            room.setRervation(true, firstName, safe, lastName, checkInDate,
                                    checkOutDate, total, reservations+10000, days);
                            return days;

                        } else {

                            setRoomChoiceError("No room type selected.");
                            return -1;
                        }

                    }

                }
                    // if room not found, inform user that hotel is out of this room type
                if(!roomFound)
                    setRoomChoiceError("Unfortunately, there are no more "+roomType+"'s available at this time.");

            }



        }catch (ParseException e) {
            // Catch if there is an issue parsing the users inputted dates

            System.out.println("User date input was unable to be parsed");
            setRoomChoiceError("Invalid date was entered. \nAccepted Date format: YYYY-MM-DD");

        }



        return -1;
    }

    public String getRoomChoiceError() {
        return roomChoiceError;
    }

    public void setRoomChoiceError(String roomChoiceError) {
        this.roomChoiceError = roomChoiceError;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public int getJuniorRooms() {
        return juniorRooms;
    }

    public int getDeluxeRooms() {
        return deluxeRooms;
    }

    public int getStandardRooms() {
        return standardRooms;
    }

    public int getReservations() {
        return reservations;
    }

    @Override
    public String toString() {
        String hotelStatus = "Current Reservations: \n\n";

        for (Room room : hotel) {
            if (room.isReserved())
                hotelStatus+= room.toString()+"\n";
        }

        hotelStatus+= "\nAvailable Rooms: \n"+
                "\nStandard: "+getStandardRooms()+
                "\nDeluxe: "+getDeluxeRooms()+
                "\nJunior Suite: "+getJuniorRooms()+
                "\n Total: "+getTotalRooms();

        return hotelStatus;
    }
}
