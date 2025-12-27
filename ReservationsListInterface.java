/**
 * Interface for ReservationsList ADT.
 * Manages flight reservations and dynamic pricing based on seat availability.
 * 
 * This is a business-level abstraction - internal pricing mechanisms are hidden.
 * Source: Custom implementation based on M2 design requirements
 */
public interface ReservationsListInterface {
    
    /**
     * Book seats on the cheapest available route and automatically update pricing.
     * This is the MOST SIGNIFICANT OPERATION for performance analysis.
     * 
     * Preconditions:
     * - Both originCode and destCode exist in network
     * - seats > 0
     * - At least one route with sufficient available seats exists
     * - passengerName is not null or empty
     * 
     * Postconditions:
     * - Reservation object created and stored
     * - flight.bookedSeats increased by seats for each flight in route
     * - Dynamic pricing triggered: currentPrice updated for affected flights
     * - Reservation returned with total cost locked at booking time
     * 
     * Time Complexity: O(E log E) worst case
     * - Route search: O(V + E)
     * - Route evaluation: O(R × F) where R = routes, F = flights per route
     * - Price updates: O(F)
     * - Total dominated by potential heap operations if used
     */
    Reservation addReservation(String originCode, String destCode, 
                               String passengerName, int seats);
    
    /**
     * Check available seats on direct flight between two airports.
     * 
     * Preconditions:
     * - Both airports exist
     * - Direct flight exists between them
     * 
     * Postconditions:
     * - No changes to reservations or prices
     * - Returns number of available seats
     * 
     * Time Complexity: O(1)
     */
    int getAvailableSeats(String originCode, String destCode);
    
    /**
     * Cancel a reservation and free up seats, triggering price adjustment.
     * 
     * Preconditions:
     * - reservationId is not null
     * 
     * Postconditions:
     * - If reservation exists: removed from list, seats freed, prices updated
     * - If not exists: no changes
     * - Returns true if cancelled, false if not found
     * 
     * Time Complexity: O(n) where n = number of reservations
     */
    boolean cancelReservation(String reservationId);
    
    /**
     * Find the cheapest route with sufficient seats (without booking).
     * 
     * Preconditions:
     * - Both airports exist
     * - requiredSeats > 0
     * 
     * Postconditions:
     * - No changes to reservations or prices
     * - Returns list of Flight objects for cheapest route, or null if none available
     * 
     * Time Complexity: O(V + E)
     */
    DynamicList<Flight> findCheapestAvailableRoute(String originCode, String destCode, 
                                                     int requiredSeats);
    
    /**
     * Get total number of reservations in the system.
     * 
     * Preconditions: None
     * 
     * Postconditions:
     * - No changes
     * - Returns count of all reservations
     * 
     * Time Complexity: O(1)
     */
    int getTotalReservations();
    
    /**
     * Get a specific reservation by ID.
     * 
     * Preconditions: None
     * 
     * Postconditions:
     * - No changes
     * - Returns Reservation object if found, null otherwise
     * 
     * Time Complexity: O(n) where n = number of reservations
     */
    Reservation getReservation(String reservationId);
}

