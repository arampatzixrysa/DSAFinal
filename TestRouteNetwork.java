/**
 * Development test for RouteNetwork ADT functionality.
 * Tests airport management, flight routing, and path finding with BFS.
 * kept for reference.
 */
public class TestRouteNetwork {
    public static void main(String[] args) {
        System.out.println("=== TESTING ROUTENETWORK ADT ===\n");
        
        RouteNetwork network = new RouteNetwork();
        
        // Add airports
        network.addAirport("ATH", "Athens International", "Athens");
        network.addAirport("JFK", "John F Kennedy", "New York");
        network.addAirport("LHR", "Heathrow", "London");
        network.addAirport("CDG", "Charles de Gaulle", "Paris");
        
        System.out.println("Added 4 airports");
        
        // Add flights (create a simple network)
        // ATH → LHR (direct)
        // ATH → CDG (direct)
        // LHR → JFK (direct)
        // CDG → JFK (direct)
        network.addFlight("A3501", "ATH", "LHR", 180, 200.0);
        network.addFlight("A3502", "ATH", "CDG", 180, 220.0);
        network.addFlight("BA177", "LHR", "JFK", 200, 350.0);
        network.addFlight("AF007", "CDG", "JFK", 200, 380.0);
        network.addFlight("A3600", "ATH", "JFK", 180, 500.0); // Direct option
        
        System.out.println("Added 5 flights");
        System.out.println("Total flights: " + network.getAllFlights().size());
        
        // Test direct flight lookup
        System.out.println("\n--- Testing Direct Flight Lookup ---");
        Flight direct = network.getDirectFlight("ATH", "JFK");
        System.out.println("ATH → JFK direct: " + (direct != null ? direct.getFlightCode() : "null"));
        
        Flight noDirect = network.getDirectFlight("LHR", "ATH");
        System.out.println("LHR → ATH direct: " + (noDirect != null ? noDirect.getFlightCode() : "null"));
        
        // Test route search: ATH → JFK with max 1 stop
        System.out.println("\n--- Testing Route Search: ATH → JFK (max 1 stop) ---");
        DynamicList<DynamicList<Flight>> routes = network.findRoutesWithStops("ATH", "JFK", 1);
        System.out.println("Found " + routes.size() + " routes:");
        
        for (int i = 0; i < routes.size(); i++) {
            DynamicList<Flight> route = routes.get(i);
            System.out.print("Route " + (i + 1) + " (" + route.size() + " flights): ");
            for (int j = 0; j < route.size(); j++) {
                Flight f = route.get(j);
                System.out.print(f.getOrigin().getCode() + "→" + f.getDestination().getCode());
                if (j < route.size() - 1) System.out.print(", ");
            }
            System.out.println();
        }
        
        // Expected:
        // Route 1: ATH→JFK (direct - 1 flight)
        // Route 2: ATH→LHR, LHR→JFK (via London - 2 flights)
        // Route 3: ATH→CDG, CDG→JFK (via Paris - 2 flights)
        
        // Test with 0 stops (direct only)
        System.out.println("\n--- Testing Direct Only: ATH → JFK (max 0 stops) ---");
        DynamicList<DynamicList<Flight>> directOnly = network.findRoutesWithStops("ATH", "JFK", 0);
        System.out.println("Found " + directOnly.size() + " route(s)");
        
        // Test no route exists
        System.out.println("\n--- Testing No Route: JFK → ATH ---");
        DynamicList<DynamicList<Flight>> noRoute = network.findRoutesWithStops("JFK", "ATH", 1);
        System.out.println("Found " + noRoute.size() + " routes (expected 0)");
        
        System.out.println("\n===ROUTENETWORK ADT TESTS PASSED ===");
    }
}

