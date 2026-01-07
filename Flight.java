/**
 * Represents a single flight with departure, arrival, capacity, and pricing info.
 * Keeps track of booked seats and supports dynamic pricing.
 * 
 * Source: Custom implementation based on M2 design requirements
 */
public class Flight {
    private String flightCode;          // Unique flight ID like "A3501"
    private Airport origin;             // Where it departs from
    private Airport destination;        // Where it arrives
    private int totalCapacity;          // Total seats available
    private int bookedSeats;            // How many seats are already booked
    private double basePrice;           // Original price (never changes)
    private double currentPrice;        // Price that may go up/down based on demand
    
    /**
     * Create a new flight with origin, destination, capacity, and base price.
     * @param flightCode Unique identifier
     * @param origin Departure airport
     * @param destination Arrival airport
     * @param totalCapacity Total number of seats
     * @param basePrice Original price (before any adjustments)
     */
    public Flight(String flightCode, Airport origin, Airport destination, 
                  int totalCapacity, double basePrice) {
        this.flightCode = flightCode;
        this.origin = origin;
        this.destination = destination;
        this.totalCapacity = totalCapacity;
        this.bookedSeats = 0;                    // Starts empty
        this.basePrice = basePrice;
        this.currentPrice = basePrice;           // Price starts at base
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
     * Calculate how many empty seats are left.
     * @return Number of unbooked seats
     */
    public int getAvailableSeats() {
        return totalCapacity - bookedSeats;
    }
    
    /**
     * Calculate what percentage of seats are booked (0.0 to 1.0).
     * Used to determine dynamic pricing.
     * @return The load factor as a decimal
     */
    public double getLoadFactor() {
        if (totalCapacity == 0) return 0.0;
        return (double) bookedSeats / totalCapacity;
    }
    
    /**
     * Check if this flight has enough seats available for a booking.
     * @param requiredSeats Number of seats needed
     * @return true if we have enough, false otherwise
     */
    public boolean hasAvailableSeats(int requiredSeats) {
        return getAvailableSeats() >= requiredSeats;
    }
    
    /**
     * Returns a readable string like "A3501: ATH → JFK [45/150 seats, €89.99]".
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

