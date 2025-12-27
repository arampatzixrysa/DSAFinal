/**
 * Interface for RouteNetwork ADT.
 * Manages airports and flights in a network, provides route search functionality.
 * 
 * This is a business-level abstraction - internal graph details are hidden.
 * Source: Custom implementation based on M2 design requirements
 */
public interface RouteNetworkInterface {
    
    /**
     * Add an airport to the network.
     * 
     * Preconditions:
     * - code is not null and not empty
     * - Airport with this code does not already exist
     * 
     * Postconditions:
     * - New airport is registered in the network
     * - Airport can now be used in addFlight operations
     * 
     * Time Complexity: O(1)
     */
    void addAirport(String code, String name, String city);
    
    /**
     * Add a direct flight between two airports.
     * 
     * Preconditions:
     * - Both originCode and destCode exist in the network
     * - capacity > 0, basePrice > 0
     * - flightCode is unique and not null
     * 
     * Postconditions:
     * - New flight is added to the network
     * - Flight is available for search and booking
     * - Flight initially has 0 booked seats and currentPrice = basePrice
     * 
     * Time Complexity: O(1)
     */
    void addFlight(String flightCode, String originCode, String destCode, 
                   int capacity, double basePrice);
    
    /**
     * Find all possible routes from start to end with at most maxStops intermediate airports.
     * 
     * Preconditions:
     * - Both startCode and endCode exist in the network
     * - maxStops >= 0
     * 
     * Postconditions:
     * - No changes to network structure
     * - Returns list of routes (each route is a list of consecutive Flight objects)
     * - Routes are sorted by number of flights (fewest first)
     * - Returns empty list if no routes exist
     * - maxStops = 0 means direct flights only
     * - maxStops = 1 means direct or 1-stop flights
     * 
     * Time Complexity: O(V + E) where V = airports, E = flights (BFS traversal)
     */
    DynamicList<DynamicList<Flight>> findRoutesWithStops(String startCode, String endCode, int maxStops);
    
    /**
     * Get the direct flight between two airports (if exists).
     * 
     * Preconditions:
     * - Both originCode and destCode exist in the network
     * 
     * Postconditions:
     * - No changes to network
     * - Returns Flight object if direct flight exists, null otherwise
     * 
     * Time Complexity: O(d) where d = outdegree of origin (typically O(1))
     */
    Flight getDirectFlight(String originCode, String destCode);
    
    /**
     * Get all flights in the entire network.
     * 
     * Preconditions: None
     * 
     * Postconditions:
     * - No changes to network
     * - Returns list of all Flight objects
     * 
     * Time Complexity: O(E) where E = total flights
     */
    DynamicList<Flight> getAllFlights();
    
    /**
     * Check if an airport exists in the network.
     * 
     * Preconditions: None
     * 
     * Postconditions:
     * - No changes to network
     * - Returns true if airport exists, false otherwise
     * 
     * Time Complexity: O(1)
     */
    boolean hasAirport(String code);
    
    /**
     * Get an airport by its code.
     * 
     * Preconditions: None
     * 
     * Postconditions:
     * - No changes to network
     * - Returns Airport object if exists, null otherwise
     * 
     * Time Complexity: O(1)
     */
    Airport getAirport(String code);
}

