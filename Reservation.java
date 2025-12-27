/**
 * Represents a booking/reservation made by a passenger.
 * Stores passenger information and the flight(s) booked.
 * 
 * Source: Custom implementation based on M2 design requirements
 */
public class Reservation {
    private String reservationId;       // Unique reservation identifier
    private Flight flight;              // The flight booked (for direct flights)
    private String passengerName;       // Name of the passenger
    private int numberOfSeats;          // Number of seats booked
    private double totalCost;           // Total cost at time of booking
    
    /**
     * Constructor for single-flight reservation
     * @param reservationId Unique ID for this reservation
     * @param flight The flight being booked
     * @param passengerName Name of passenger
     * @param numberOfSeats Number of seats to book
     * @param totalCost Total price paid (locked at booking time)
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
     * String representation for debugging/testing.
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

