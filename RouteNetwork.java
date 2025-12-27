/**
 * Implementation of RouteNetwork ADT.
 * Internally uses FlightGraph (adjacency list) and SimpleHashMap for airport storage.
 * 
 * Key Algorithm: BFS for finding routes with limited stops.
 * Source: Custom implementation with AI-assisted BFS algorithm
 */
public class RouteNetwork implements RouteNetworkInterface {
    
    private FlightGraph graph;                          // Internal graph structure
    private SimpleHashMap<String, Airport> airports;    // Airport lookup by code
    
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
            return new DynamicList<>(); // Return empty if airports don't exist
        }
        if (maxStops < 0) {
            throw new IllegalArgumentException("maxStops cannot be negative");
        }
        
        // Use BFS to find all paths with at most maxStops intermediate airports
        // maxStops = 0 → direct only (1 flight)
        // maxStops = 1 → up to 1 stop (up to 2 flights)
        int maxFlights = maxStops + 1;
        
        DynamicList<DynamicList<Flight>> allRoutes = new DynamicList<>();
        
        // BFS with path tracking
        // Each queue item stores: [current airport code, path of flights taken, number of flights]
        DynamicList<SearchNode> queue = new DynamicList<>();
        queue.add(new SearchNode(startCode, new DynamicList<>(), 0));
        
        while (!queue.isEmpty()) {
            // Dequeue (remove first element - manual since no Queue ADT exposed)
            SearchNode current = queue.get(0);
            queue.remove(0);
            
            String currentAirport = current.airportCode;
            DynamicList<Flight> currentPath = current.path;
            int flightCount = current.flightCount;
            
            // If we reached destination, save this route
            if (currentAirport.equals(endCode)) {
                allRoutes.add(currentPath);
                continue; // Don't expand further from destination
            }
            
            // If we've used all allowed flights, don't expand further
            if (flightCount >= maxFlights) {
                continue;
            }
            
            // Expand to all outgoing flights
            DynamicList<Flight> outgoingFlights = graph.getOutgoingFlights(currentAirport);
            for (int i = 0; i < outgoingFlights.size(); i++) {
                Flight flight = outgoingFlights.get(i);
                String nextAirport = flight.getDestination().getCode();
                
                // Avoid cycles: don't revisit airports already in path
                if (isAirportInPath(nextAirport, currentPath)) {
                    continue;
                }
                
                // Create new path with this flight added
                DynamicList<Flight> newPath = copyPath(currentPath);
                newPath.add(flight);
                
                // Enqueue
                queue.add(new SearchNode(nextAirport, newPath, flightCount + 1));
            }
        }
        
        // Sort routes by number of flights (fewest first)
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
    
    // ========== HELPER METHODS (PRIVATE) ==========
    
    /**
     * Check if an airport is already visited in the current path (cycle detection).
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
     * Create a copy of a flight path (needed for BFS branching).
     */
    private DynamicList<Flight> copyPath(DynamicList<Flight> original) {
        DynamicList<Flight> copy = new DynamicList<>();
        for (int i = 0; i < original.size(); i++) {
            copy.add(original.get(i));
        }
        return copy;
    }
    
    /**
     * Sort routes by number of flights (ascending - fewest flights first).
     * Uses simple bubble sort (sufficient for small number of routes).
     */
    private void sortRoutesByLength(DynamicList<DynamicList<Flight>> routes) {
        // Bubble sort - O(n^2) but routes list is typically small
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
     * Internal class for BFS search node.
     * Stores current airport, path taken so far, and number of flights used.
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

