/**
 * Adjacency list graph where airports are vertices and flights are edges.
 * Each airport maps to a list of outgoing flights.
 * 
 * Source: AI-generated based on standard adjacency list graph implementation
 */
public class FlightGraph {
    private SimpleHashMap<String, DynamicList<Flight>> adjacencyList;
    
    public FlightGraph() {
        this.adjacencyList = new SimpleHashMap<>();
    }
    
    /**
     * Add an airport to the graph. Does nothing if it already exists.
     * Time: O(1)
     */
    public void addAirport(String airportCode) {
        if (!adjacencyList.containsKey(airportCode)) {
            adjacencyList.put(airportCode, new DynamicList<>());
        }
    }
    
    /**
     * Add a flight from one airport to another. Automatically adds airports if needed.
     * Time: O(1)
     */
    public void addFlight(String originCode, String destCode, Flight flight) {
        // Ensure both airports exist
        addAirport(originCode);
        addAirport(destCode);
        
        // Add directed edge
        DynamicList<Flight> outgoingFlights = adjacencyList.get(originCode);
        outgoingFlights.add(flight);
    }
    
    /**
     * Get all flights leaving from a given airport.
     * Time: O(1)
     */
    public DynamicList<Flight> getOutgoingFlights(String airportCode) {
        DynamicList<Flight> flights = adjacencyList.get(airportCode);
        return flights != null ? flights : new DynamicList<>();
    }
    
    /**
     * Check if an airport is already in the graph.
     * Time: O(1)
     */
    public boolean hasAirport(String airportCode) {
        return adjacencyList.containsKey(airportCode);
    }
    
    /**
     * Find a direct flight between two airports if one exists.
     * Time: O(d) where d = number of outgoing flights from origin
     */
    public Flight getDirectFlight(String originCode, String destCode) {
        DynamicList<Flight> outgoing = getOutgoingFlights(originCode);
        for (int i = 0; i < outgoing.size(); i++) {
            Flight flight = outgoing.get(i);
            if (flight.getDestination().getCode().equals(destCode)) {
                return flight;
            }
        }
        return null;
    }
    
    /**
     * Return every single flight in the graph as one big list.
     * Time: O(V + E) where V = airports, E = flights
     */
    public DynamicList<Flight> getAllFlights() {
        DynamicList<Flight> allFlights = new DynamicList<>();
        DynamicList<DynamicList<Flight>> allAdjLists = adjacencyList.values();
        
        for (int i = 0; i < allAdjLists.size(); i++) {
            DynamicList<Flight> flights = allAdjLists.get(i);
            for (int j = 0; j < flights.size(); j++) {
                allFlights.add(flights.get(j));
            }
        }
        return allFlights;
    }
}
