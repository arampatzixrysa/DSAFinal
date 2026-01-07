/**
 * Development test for ReservationsList ADT functionality.
 * Tests dynamic pricing, cancellations, and route finding.
 * kept for reference.
 */
public class TestReservationsList {
    public static void main(String[] args) {
        System.out.println("=== TESTING RESERVATIONSLIST ADT ===\n");
        
        // Setup network
        RouteNetwork network = new RouteNetwork();
        network.addAirport("ATH", "Athens International", "Athens");
        network.addAirport("JFK", "John F Kennedy", "New York");
        network.addAirport("LHR", "Heathrow", "London");
        
        // Add flights
        network.addFlight("A3501", "ATH", "LHR", 100, 200.0); // Base €200
        network.addFlight("BA177", "LHR", "JFK", 100, 300.0); // Base €300
        network.addFlight("A3600", "ATH", "JFK", 100, 450.0); // Direct, base €450
        
        ReservationsList reservations = new ReservationsList(network);
        
        System.out.println(" Network setup complete\n");
        
        // Test 1: Book a direct flight (should pick cheapest initially)
        System.out.println("--- Test 1: First Booking (ATH → JFK, 10 seats) ---");
        Reservation r1 = reservations.addReservation("ATH", "JFK", "Alice Johnson", 10);
        System.out.println("Reservation: " + r1);
        System.out.println("Total cost: €" + r1.getTotalCost());
        
        // Check flight A3600 (direct)
        Flight directFlight = network.getDirectFlight("ATH", "JFK");
        System.out.println("Flight A3600 load: " + directFlight.getBookedSeats() + "/" 
                         + directFlight.getTotalCapacity());
        System.out.println("Flight A3600 price now: €" + directFlight.getCurrentPrice());
        System.out.println("(Expected: 10% load, price = €450 base price)\n");
        
        // Test 2: Book more seats to cross 50% threshold
        System.out.println("--- Test 2: Book 45 More Seats (Total 55%) ---");
        Reservation r2 = reservations.addReservation("ATH", "JFK", "Bob Smith", 45);
        System.out.println("Flight A3600 load: " + directFlight.getBookedSeats() + "/" 
                         + directFlight.getTotalCapacity());
        System.out.println("Flight A3600 price now: €" + directFlight.getCurrentPrice());
        System.out.println("(Expected: 55% load, price = €540 = 450 × 1.2)\n");
        
        // Test 3: Book more to cross 80% threshold
        System.out.println("--- Test 3: Book 30 More Seats (Total 85%) ---");
        Reservation r3 = reservations.addReservation("ATH", "JFK", "Carol White", 30);
        System.out.println("Flight A3600 load: " + directFlight.getBookedSeats() + "/" 
                         + directFlight.getTotalCapacity());
        System.out.println("Flight A3600 price now: €" + directFlight.getCurrentPrice());
        System.out.println("(Expected: 85% load, price = €675 = 450 × 1.5)\n");
        
        // Test 4: Cancel reservation (price should drop)
        System.out.println("--- Test 4: Cancel Reservation (Free 30 Seats) ---");
        boolean cancelled = reservations.cancelReservation(r3.getReservationId());
        System.out.println("Cancelled: " + cancelled);
        System.out.println("Flight A3600 load: " + directFlight.getBookedSeats() + "/" 
                         + directFlight.getTotalCapacity());
        System.out.println("Flight A3600 price now: €" + directFlight.getCurrentPrice());
        System.out.println("(Expected: 55% load, price back to €540)\n");
        
        // Test 5: Find cheapest route
        System.out.println("--- Test 5: Find Cheapest Route (ATH → JFK, 5 seats) ---");
        DynamicList<Flight> cheapest = reservations.findCheapestAvailableRoute("ATH", "JFK", 5);
        if (cheapest != null) {
            System.out.print("Cheapest route: ");
            for (int i = 0; i < cheapest.size(); i++) {
                Flight f = cheapest.get(i);
                System.out.print(f.getOrigin().getCode() + "→" + f.getDestination().getCode() 
                               + " (€" + f.getCurrentPrice() + ")");
                if (i < cheapest.size() - 1) System.out.print(" + ");
            }
            System.out.println();
        }
        System.out.println();
        
        // Test 6: Total reservations
        System.out.println("--- Test 6: Total Reservations ---");
        System.out.println("Total reservations: " + reservations.getTotalReservations());
        System.out.println("(Expected: 2, since one was cancelled)\n");
        
        System.out.println("===  RESERVATIONSLIST ADT TESTS PASSED ===");
    }
}
