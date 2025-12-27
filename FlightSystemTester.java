/**
 * Main tester class for Dynamic Flight Pricing System.
 * Demonstrates ADT operations with a hardcoded realistic scenario.
 * 
 * This is NOT a UI application - it's a backend demonstration.
 * 
 * Source: Custom implementation demonstrating M2 requirements
 */
public class FlightSystemTester {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   DYNAMIC FLIGHT PRICING SYSTEM - M2 DEMONSTRATION        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Initialize system
        RouteNetwork network = new RouteNetwork();
        ReservationsList reservations = new ReservationsList(network);
        
        // Run demonstration
        System.out.println("========== PART 1: SYSTEM SETUP ==========\n");
        setupFlightNetwork(network);
        
        System.out.println("\n========== PART 2: FLIGHT SEARCH ==========\n");
        demonstrateFlightSearch(network, reservations);
        
        System.out.println("\n========== PART 3: BOOKING & DYNAMIC PRICING ==========\n");
        demonstrateDynamicPricing(network, reservations);
        
        System.out.println("\n========== PART 4: RESERVATION MANAGEMENT ==========\n");
        demonstrateReservationManagement(reservations);
        
        System.out.println("\n========== PART 5: SYSTEM SUMMARY ==========\n");
        printSystemSummary(network, reservations);
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     DEMONSTRATION COMPLETE                                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * Setup a realistic European-US flight network.
     * Network includes:
     * - 6 airports (Athens, London, Paris, Frankfurt, New York, Boston)
     * - Mix of direct and connecting flights
     * - Varying capacities and prices
     */
    private static void setupFlightNetwork(RouteNetwork network) {
    System.out.println("--- Adding Airports ---");
    
    // European airports
    network.addAirport("ATH", "Athens International", "Athens");
    network.addAirport("LHR", "London Heathrow", "London");
    network.addAirport("CDG", "Charles de Gaulle", "Paris");
    network.addAirport("FRA", "Frankfurt Airport", "Frankfurt");
    
    // US airports
    network.addAirport("JFK", "John F Kennedy", "New York");
    network.addAirport("BOS", "Boston Logan", "Boston");
    
    System.out.println(" Added 6 airports (4 Europe, 2 US)\n");
    
    System.out.println("--- Adding Flights ---");
    
    // Athens to Europe hubs
    network.addFlight("A3501", "ATH", "LHR", 180, 220.0);
    network.addFlight("A3502", "ATH", "CDG", 180, 240.0);
    network.addFlight("A3503", "ATH", "FRA", 160, 200.0);
    
    // Europe hubs to US
    network.addFlight("BA177", "LHR", "JFK", 250, 380.0);
    network.addFlight("BA178", "LHR", "BOS", 250, 360.0);
    network.addFlight("AF007", "CDG", "JFK", 280, 400.0);
    network.addFlight("AF008", "CDG", "BOS", 280, 390.0);
    network.addFlight("LH400", "FRA", "JFK", 300, 370.0);
    
    // Direct Athens to US (cheaper to make it attractive for demo)
    network.addFlight("A3600", "ATH", "JFK", 200, 450.0); // CHANGED from 650
    
    // US internal
    network.addFlight("AA100", "JFK", "BOS", 150, 120.0);
    
    System.out.println(" Added 10 flights");
    System.out.println("   - 3 ATH → Europe hub flights");
    System.out.println("   - 5 Europe hub → US flights");
    System.out.println("   - 1 Direct ATH → JFK flight (most convenient)");
    System.out.println("   - 1 US internal flight");
}

    
    /**
     * Demonstrate route search functionality.
     * Shows direct vs. connecting flight options.
     */
    private static void demonstrateFlightSearch(RouteNetwork network, 
                                                 ReservationsList reservations) {
        System.out.println("--- Searching: Athens (ATH) → New York (JFK) ---\n");
        
        // Find all routes with up to 1 stop
        DynamicList<DynamicList<Flight>> routes = network.findRoutesWithStops("ATH", "JFK", 1);
        
        System.out.println("Found " + routes.size() + " route(s):\n");
        
        for (int i = 0; i < routes.size(); i++) {
            DynamicList<Flight> route = routes.get(i);
            System.out.println("Route " + (i + 1) + ":");
            
            double totalPrice = 0.0;
            for (int j = 0; j < route.size(); j++) {
                Flight f = route.get(j);
                System.out.println("  Flight " + f.getFlightCode() + ": " 
                                 + f.getOrigin().getCode() + " → " + f.getDestination().getCode());
                System.out.println("    Capacity: " + f.getAvailableSeats() + "/" 
                                 + f.getTotalCapacity() + " seats available");
                System.out.println("    Price: €" + f.getCurrentPrice());
                totalPrice += f.getCurrentPrice();
            }
            System.out.println("  Total route price: €" + totalPrice);
            System.out.println();
        }
        
        // Find cheapest available route
        System.out.println("--- Finding Cheapest Route (5 passengers) ---\n");
        DynamicList<Flight> cheapest = reservations.findCheapestAvailableRoute("ATH", "JFK", 5);
        
        if (cheapest != null) {
            System.out.print("Cheapest: ");
            double total = 0.0;
            for (int i = 0; i < cheapest.size(); i++) {
                Flight f = cheapest.get(i);
                System.out.print(f.getOrigin().getCode() + "→" + f.getDestination().getCode());
                if (i < cheapest.size() - 1) System.out.print(" → ");
                total += f.getCurrentPrice();
            }
            System.out.println(" (€" + total + " per person)");
        }
    }
    
    /**
     * Demonstrate dynamic pricing through multiple bookings.
     * Shows how prices increase as flights fill up.
     */
    private static void demonstrateDynamicPricing(RouteNetwork network, 
                                               ReservationsList reservations) {
    Flight directFlight = network.getDirectFlight("ATH", "JFK");
    
    System.out.println("--- Tracking Dynamic Pricing on Flight A3600 (ATH → JFK Direct) ---");
    System.out.println("Capacity: " + directFlight.getTotalCapacity() + " seats");
    System.out.println("Base Price: €" + directFlight.getBasePrice());
    System.out.println("Note: This is now the cheapest route, so bookings will use it.\n");
    
    // Booking 1: Low occupancy (10%)
    System.out.println(" Booking 1: 20 seats for Emily Davis");
    Reservation r1 = reservations.addReservation("ATH", "JFK", "Emily Davis", 20);
    printFlightStatus(directFlight);
    System.out.println("Reservation cost: €" + String.format("%.2f", r1.getTotalCost()) + " total");
    System.out.println("Price per seat: €" + directFlight.getCurrentPrice() + "\n");
    
    // Booking 2: Push to 60% occupancy
    System.out.println(" Booking 2: 100 seats for Mediterranean Tours Group");
    Reservation r2 = reservations.addReservation("ATH", "JFK", "Mediterranean Tours Group", 100);
    printFlightStatus(directFlight);
    System.out.println("Reservation cost: €" + String.format("%.2f", r2.getTotalCost()) + " total");
    System.out.println("Price per seat: €" + directFlight.getCurrentPrice());
    System.out.println(" Price increased by 20% (load factor >= 50%)\n");
    
    // Booking 3: Push to 90% occupancy
    System.out.println(" Booking 3: 60 seats for Tech Conference Attendees");
    Reservation r3 = reservations.addReservation("ATH", "JFK", "Tech Conference Attendees", 60);
    printFlightStatus(directFlight);
    System.out.println("Reservation cost: €" + String.format("%.2f", r3.getTotalCost()) + " total");
    System.out.println("Price per seat: €" + directFlight.getCurrentPrice());
    System.out.println(" Price increased by 50% (load factor >= 80%)\n");
    
    // Try booking beyond capacity
    System.out.println(" Attempting Booking 4: 50 seats (exceeds capacity)");
    try {
        reservations.addReservation("ATH", "JFK", "Late Booker", 50);
    } catch (IllegalArgumentException e) {
        System.out.println(" Booking failed: " + e.getMessage());
    }
}
    
    /**
     * Demonstrate reservation cancellation and price drop.
     */
    private static void demonstrateReservationManagement(ReservationsList reservations) {
        System.out.println("--- Current Reservations ---");
        System.out.println("Total reservations: " + reservations.getTotalReservations() + "\n");
        
        // Get a reservation
        Reservation res = reservations.getReservation("RES0002");
        if (res != null) {
            System.out.println("Retrieved: " + res);
            System.out.println();
            
            // Cancel it
            System.out.println("--- Cancelling Reservation RES0002 ---");
            boolean cancelled = reservations.cancelReservation("RES0002");
            
            if (cancelled) {
                Flight flight = res.getFlight();
                System.out.println(" Cancellation successful");
                System.out.println("Flight " + flight.getFlightCode() + " status after cancellation:");
                printFlightStatus(flight);
                System.out.println(" Price decreased due to lower occupancy\n");
            }
        }
        
        System.out.println("Total reservations after cancellation: " 
                         + reservations.getTotalReservations());
    }
    
    /**
     * Print overall system statistics.
     */
    private static void printSystemSummary(RouteNetwork network, 
                                           ReservationsList reservations) {
        DynamicList<Flight> allFlights = network.getAllFlights();
        
        System.out.println("--- System Statistics ---");
        System.out.println("Total flights in network: " + allFlights.size());
        System.out.println("Total reservations: " + reservations.getTotalReservations());
        
        // Calculate total revenue
        double totalRevenue = 0.0;
        for (int i = 1; i <= reservations.getTotalReservations(); i++) {
            Reservation res = reservations.getReservation("RES" + String.format("%04d", i));
            if (res != null) {
                totalRevenue += res.getTotalCost();
            }
        }
        System.out.println("Total revenue: €" + String.format("%.2f", totalRevenue));
        
        // Show most expensive flight (highest current price)
        Flight mostExpensive = allFlights.get(0);
        for (int i = 1; i < allFlights.size(); i++) {
            Flight f = allFlights.get(i);
            if (f.getCurrentPrice() > mostExpensive.getCurrentPrice()) {
                mostExpensive = f;
            }
        }
        System.out.println("\nMost expensive flight currently:");
        System.out.println("  " + mostExpensive.getFlightCode() + ": " 
                         + mostExpensive.getOrigin().getCode() + " → " 
                         + mostExpensive.getDestination().getCode() 
                         + " - €" + mostExpensive.getCurrentPrice());
        System.out.println("  (Load: " + (int)(mostExpensive.getLoadFactor() * 100) + "%)");
    }
    
    /**
     * Helper: Print current status of a flight.
     */
    private static void printFlightStatus(Flight flight) {
        int load = flight.getBookedSeats();
        int capacity = flight.getTotalCapacity();
        double loadFactor = flight.getLoadFactor();
        double price = flight.getCurrentPrice();
        
        System.out.println("  Current load: " + load + "/" + capacity + " seats (" 
                         + (int)(loadFactor * 100) + "%)");
        System.out.println("  Current price: €" + price);
    }
}

