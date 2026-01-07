/**
 * Manages the flight network with airports and flights. Uses a graph internally for route finding.
 * Also maintains a map for quick airport lookups by code.
 * 
 * Key Algorithm: BFS to find routes with a limit on stops.
 * Source: Custom implementation with AI-assisted BFS algorithm
 */
public class RouteNetwork implements RouteNetworkInterface {
    
    private FlightGraph graph;                          // Stores the flight network structure
    private SimpleHashMap<String, Airport> airports;    // Quick lookup for airports by code
    
    public RouteNetwork() {
        this.graph = new FlightGraph();
        this.airports = new SimpleHashMap<>();
    }
    
    @Override
    public void addAirport(String code, String name, String city) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Airport code cannot be null or empty");
        }
        if (airports.containsKey(code)) {
            throw new IllegalArgumentException("Airport " + code + " already exists");
        }
        
        Airport airport = new Airport(code, name, city);
        airports.put(code, airport);
        graph.addAirport(code);
    }
    
    @Override
    public void addFlight(String flightCode, String originCode, String destCode, 
                         int capacity, double basePrice) {
        if (!airports.containsKey(originCode)) {
            throw new IllegalArgumentException("Origin airport " + originCode + " does not exist");
        }
        if (!airports.containsKey(destCode)) {
            throw new IllegalArgumentException("Destination airport " + destCode + " does not exist");
        }
        if (capacity <= 0 || basePrice <= 0) {
            throw new IllegalArgumentException("Capacity and price must be positive");
        }
        
        Airport origin = airports.get(originCode);
        Airport destination = airports.get(destCode);
        
        Flight flight = new Flight(flightCode, origin, destination, capacity, basePrice);
        graph.addFlight(originCode, destCode, flight);
    }
    
    @Override
    public DynamicList<DynamicList<Flight>> findRoutesWithStops(String startCode, String endCode, int maxStops) {
    if (!airports.containsKey(startCode) || !airports.containsKey(endCode)) {
        return new DynamicList<>();
    }
    if (maxStops < 0) {
        throw new IllegalArgumentException("maxStops cannot be negative");
    }
    
    int maxFlights = maxStops + 1;
    DynamicList<DynamicList<Flight>> allRoutes = new DynamicList<>();
    
    // Use efficient circular queue instead of DynamicList
    SimpleQueue<SearchNode> queue = new SimpleQueue<>(1000);
    queue.enqueue(new SearchNode(startCode, new DynamicList<>(), 0));
    
    while (!queue.isEmpty()) {
        SearchNode current = queue.dequeue(); // NOW O(1) instead of O(n)!
        
        String currentAirport = current.airportCode;
        DynamicList<Flight> currentPath = current.path;
        int flightCount = current.flightCount;
        
        if (currentAirport.equals(endCode)) {
            allRoutes.add(currentPath);
            continue;
        }
        
        if (flightCount >= maxFlights) {
            continue;
        }
        
        DynamicList<Flight> outgoingFlights = graph.getOutgoingFlights(currentAirport);
        for (int i = 0; i < outgoingFlights.size(); i++) {
            Flight flight = outgoingFlights.get(i);
            String nextAirport = flight.getDestination().getCode();
            
            if (isAirportInPath(nextAirport, currentPath)) {
                continue;
            }
            
            DynamicList<Flight> newPath = copyPath(currentPath);
            newPath.add(flight);
            
            queue.enqueue(new SearchNode(nextAirport, newPath, flightCount + 1));
        }
    }
    
    sortRoutesByLength(allRoutes);
    return allRoutes;
}

    
    @Override
    public Flight getDirectFlight(String originCode, String destCode) {
        return graph.getDirectFlight(originCode, destCode);
    }
    
    @Override
    public DynamicList<Flight> getAllFlights() {
        return graph.getAllFlights();
    }
    
    @Override
    public boolean hasAirport(String code) {
        return airports.containsKey(code);
    }
    
    @Override
    public Airport getAirport(String code) {
        return airports.get(code);
    }
    
    // ========== HELPER METHODS ==========
    
    /**
     * Check if an airport code already appears in the current path (prevents loops).
     */
    private boolean isAirportInPath(String airportCode, DynamicList<Flight> path) {
        for (int i = 0; i < path.size(); i++) {
            Flight flight = path.get(i);
            // Check both origin and destination to catch cycles
            if (flight.getOrigin().getCode().equals(airportCode) ||
                flight.getDestination().getCode().equals(airportCode)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Make a copy of a flight path so we can branch off without affecting the original.
     */
    private DynamicList<Flight> copyPath(DynamicList<Flight> original) {
        DynamicList<Flight> copy = new DynamicList<>();
        for (int i = 0; i < original.size(); i++) {
            copy.add(original.get(i));
        }
        return copy;
    }
    
    /**
     * Sort routes by length so shortest ones come first. Uses bubble sort since the list is small.
     */
    private void sortRoutesByLength(DynamicList<DynamicList<Flight>> routes) {
        // Bubble sort - simple and fine for small lists
        for (int i = 0; i < routes.size() - 1; i++) {
            for (int j = 0; j < routes.size() - i - 1; j++) {
                if (routes.get(j).size() > routes.get(j + 1).size()) {
                    // Swap
                    DynamicList<Flight> temp = routes.get(j);
                    routes.set(j, routes.get(j + 1));
                    routes.set(j + 1, temp);
                }
            }
        }
    }
    
    /**
     * Helper class for BFS. Tracks the current airport, the path taken, and how many flights we've used.
     */
    private static class SearchNode {
        String airportCode;
        DynamicList<Flight> path;
        int flightCount;
        
        SearchNode(String airportCode, DynamicList<Flight> path, int flightCount) {
            this.airportCode = airportCode;
            this.path = path;
            this.flightCount = flightCount;
        }
    }
}

