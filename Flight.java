/**
 * Represents a flight between two airports.
 * Contains capacity, pricing (base + dynamic), and booking information.
 * 
 * Source: Custom implementation based on M2 design requirements
 */
public class Flight {
    private String flightCode;          // Unique flight identifier (e.g., "A3501")
    private Airport origin;             // Departure airport
    private Airport destination;        // Arrival airport
    private int totalCapacity;          // Total seats on aircraft
    private int bookedSeats;            // Number of seats already booked
    private double basePrice;           // Fixed base price (never changes)
    private double currentPrice;        // Dynamic price (updated based on load factor)
    
    /**
     * Constructor
     * @param flightCode Unique identifier for this flight
     * @param origin Departure airport
     * @param destination Arrival airport
     * @param totalCapacity Total number of seats
     * @param basePrice Base price before dynamic adjustments
     */
    public Flight(String flightCode, Airport origin, Airport destination, 
                  int totalCapacity, double basePrice) {
        this.flightCode = flightCode;
        this.origin = origin;
        this.destination = destination;
        this.totalCapacity = totalCapacity;
        this.bookedSeats = 0;                    // Initially no bookings
        this.basePrice = basePrice;
        this.currentPrice = basePrice;           // Initially same as base price
    }
    
    // Getters
    public String getFlightCode() {
        return flightCode;
    }
    
    public Airport getOrigin() {
        return origin;
    }
    
    public Airport getDestination() {
        return destination;
    }
    
    public int getTotalCapacity() {
        return totalCapacity;
    }
    
    public int getBookedSeats() {
        return bookedSeats;
    }
    
    public double getBasePrice() {
        return basePrice;
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }
    
    // Setters
    public void setBookedSeats(int bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
    
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    /**
     * Calculate available seats on this flight.
     * @return Number of seats not yet booked
     */
    public int getAvailableSeats() {
        return totalCapacity - bookedSeats;
    }
    
    /**
     * Calculate load factor (occupancy ratio) for dynamic pricing.
     * @return Ratio of booked seats to total capacity (0.0 to 1.0)
     */
    public double getLoadFactor() {
        if (totalCapacity == 0) return 0.0;
        return (double) bookedSeats / totalCapacity;
    }
    
    /**
     * Check if this flight has enough available seats.
     * @param requiredSeats Number of seats needed
     * @return true if enough seats available, false otherwise
     */
    public boolean hasAvailableSeats(int requiredSeats) {
        return getAvailableSeats() >= requiredSeats;
    }
    
    /**
     * String representation for debugging/testing.
     */
    @Override
    public String toString() {
        return String.format("%s: %s → %s [%d/%d seats, €%.2f]", 
                           flightCode, 
                           origin.getCode(), 
                           destination.getCode(),
                           bookedSeats,
                           totalCapacity,
                           currentPrice);
    }
}

