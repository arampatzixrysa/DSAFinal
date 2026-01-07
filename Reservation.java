/**
 * Represents a passenger's booking with all the booking details.
 * Stores which flight they booked, how many seats, and what they paid.
 * 
 * Source: Custom implementation based on M2 design requirements
 */
public class Reservation {
    private String reservationId;       // Unique ID like "RES0001"
    private Flight flight;              // The flight that was booked
    private String passengerName;       // Who booked it
    private int numberOfSeats;          // How many seats they got
    private double totalCost;           // What they paid (locked in at booking)
    
    /**
     * Create a new reservation.
     * @param reservationId Unique reservation ID
     * @param flight The flight being booked
     * @param passengerName Name of the passenger
     * @param numberOfSeats Number of seats booked
     * @param totalCost Total price at booking time
     */
    public Reservation(String reservationId, Flight flight, String passengerName, 
                      int numberOfSeats, double totalCost) {
        this.reservationId = reservationId;
        this.flight = flight;
        this.passengerName = passengerName;
        this.numberOfSeats = numberOfSeats;
        this.totalCost = totalCost;
    }
    
    // Getters
    public String getReservationId() {
        return reservationId;
    }
    
    public Flight getFlight() {
        return flight;
    }
    
    public String getPassengerName() {
        return passengerName;
    }
    
    public int getNumberOfSeats() {
        return numberOfSeats;
    }
    
    public double getTotalCost() {
        return totalCost;
    }
    
    /**
     * Returns details like "Reservation RES0001: Alice booked 2 seat(s) on A3501 for €179.98".
     */
    @Override
    public String toString() {
        return String.format("Reservation %s: %s booked %d seat(s) on %s for €%.2f",
                           reservationId,
                           passengerName,
                           numberOfSeats,
                           flight.getFlightCode(),
                           totalCost);
    }
}

