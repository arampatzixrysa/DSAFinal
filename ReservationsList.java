/**
 * Handles all reservation bookings and manages dynamic pricing for flights.
 * Automatically adjusts flight prices based on how full they are.
 * 
 * Dynamic Pricing Formula:
 * - Load Factor >= 0.8 (80%): currentPrice = basePrice × 1.5
 * - Load Factor >= 0.5 (50%): currentPrice = basePrice × 1.2
 * - Load Factor < 0.5:        currentPrice = basePrice × 1.0
 * 
 * Source: Custom implementation with AI-assisted route selection logic
 */
public class ReservationsList implements ReservationsListInterface {
    
    private DynamicList<Reservation> reservations;
    private RouteNetwork routeNetwork;
    private int nextReservationId;
    
    public ReservationsList(RouteNetwork routeNetwork) {
        this.reservations = new DynamicList<>();
        this.routeNetwork = routeNetwork;
        this.nextReservationId = 1;
    }
    
    @Override
    public Reservation addReservation(String originCode, String destCode, 
                                      String passengerName, int seats) {
        // Check inputs make sense
        if (passengerName == null || passengerName.isEmpty()) {
            throw new IllegalArgumentException("Passenger name cannot be null or empty");
        }
        if (seats <= 0) {
            throw new IllegalArgumentException("Number of seats must be positive");
        }
        if (!routeNetwork.hasAirport(originCode) || !routeNetwork.hasAirport(destCode)) {
            throw new IllegalArgumentException("One or both airports do not exist");
        }
        
        // Find the cheapest available route (tries up to 1 stop)
        DynamicList<Flight> bestRoute = findCheapestAvailableRoute(originCode, destCode, seats);
        
        if (bestRoute == null || bestRoute.isEmpty()) {
            throw new IllegalArgumentException("No available route with " + seats + " seats from " 
                                             + originCode + " to " + destCode);
        }
        
        // Calculate total cost with current prices
        double totalCost = 0.0;
        for (int i = 0; i < bestRoute.size(); i++) {
            Flight flight = bestRoute.get(i);
            totalCost += flight.getCurrentPrice() * seats;
        }
        
        // Update the booked seats for each flight
        for (int i = 0; i < bestRoute.size(); i++) {
            Flight flight = bestRoute.get(i);
            flight.setBookedSeats(flight.getBookedSeats() + seats);
        }
        
        // Update dynamic pricing since occupancy changed
        for (int i = 0; i < bestRoute.size(); i++) {
            updateDynamicPrice(bestRoute.get(i));
        }
        
        // Create the reservation with a unique ID
        String reservationId = "RES" + String.format("%04d", nextReservationId++);
        
        // Store it (simplification: we save just the first flight)
        Reservation reservation = new Reservation(reservationId, bestRoute.get(0), 
                                                   passengerName, seats, totalCost);
        reservations.add(reservation);
        
        return reservation;
    }
    
    @Override
    public int getAvailableSeats(String originCode, String destCode) {
        Flight directFlight = routeNetwork.getDirectFlight(originCode, destCode);
        if (directFlight == null) {
            throw new IllegalArgumentException("No direct flight from " + originCode 
                                             + " to " + destCode);
        }
        return directFlight.getAvailableSeats();
    }
    
    @Override
    public boolean cancelReservation(String reservationId) {
        // Find the reservation to cancel
        Reservation toCancel = null;
        int index = -1;
        
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getReservationId().equals(reservationId)) {
                toCancel = reservations.get(i);
                index = i;
                break;
            }
        }
        
        if (toCancel == null) {
            return false; // Couldn't find it
        }
        
        // Give the seats back to the flight
        Flight flight = toCancel.getFlight();
        int seatsToFree = toCancel.getNumberOfSeats();
        flight.setBookedSeats(flight.getBookedSeats() - seatsToFree);
        
        // Adjust price since occupancy went down
        updateDynamicPrice(flight);
        
        // Remove the reservation from our list
        reservations.remove(index);
        
        return true;
    }
    
    @Override
    public DynamicList<Flight> findCheapestAvailableRoute(String originCode, String destCode, 
                                                           int requiredSeats) {
        // Get all possible routes with max 1 stop
        DynamicList<DynamicList<Flight>> allRoutes = 
            routeNetwork.findRoutesWithStops(originCode, destCode, 1);
        
        if (allRoutes.isEmpty()) {
            return null;
        }
        
        // Keep only the routes that have enough seats
        DynamicList<DynamicList<Flight>> validRoutes = new DynamicList<>();
        for (int i = 0; i < allRoutes.size(); i++) {
            DynamicList<Flight> route = allRoutes.get(i);
            if (routeHasCapacity(route, requiredSeats)) {
                validRoutes.add(route);
            }
        }
        
        if (validRoutes.isEmpty()) {
            return null;
        }
        
        // Pick the cheapest one
        DynamicList<Flight> cheapestRoute = validRoutes.get(0);
        double cheapestCost = calculateRouteCost(cheapestRoute, requiredSeats);
        
        for (int i = 1; i < validRoutes.size(); i++) {
            DynamicList<Flight> route = validRoutes.get(i);
            double cost = calculateRouteCost(route, requiredSeats);
            if (cost < cheapestCost) {
                cheapestCost = cost;
                cheapestRoute = route;
            }
        }
        
        return cheapestRoute;
    }
    
    @Override
    public int getTotalReservations() {
        return reservations.size();
    }
    
    @Override
    public Reservation getReservation(String reservationId) {
        for (int i = 0; i < reservations.size(); i++) {
            Reservation res = reservations.get(i);
            if (res.getReservationId().equals(reservationId)) {
                return res;
            }
        }
        return null;
    }
    
    // ========== PRIVATE HELPER METHODS ==========
    
    /**
     * Adjust flight price based on how full it is (DYNAMIC PRICING).
     * 
     * Pricing Formula:
     * - Load >= 80%: Price = Base × 1.5 (high demand)
     * - Load >= 50%: Price = Base × 1.2 (moderate demand)
     * - Load < 50%:  Price = Base × 1.0 (low demand)
     * 
     * Time: O(1)
     */
    private void updateDynamicPrice(Flight flight) {
        double loadFactor = flight.getLoadFactor();
        double basePrice = flight.getBasePrice();
        double newPrice;
        
        if (loadFactor >= 0.8) {
            newPrice = basePrice * 1.5;  // 80%+ full → 50% markup
        } else if (loadFactor >= 0.5) {
            newPrice = basePrice * 1.2;  // 50-79% full → 20% markup
        } else {
            newPrice = basePrice * 1.0;  // < 50% full → base price
        }
        
        flight.setCurrentPrice(newPrice);
    }
    
    /**
     * Check if all flights in a route have enough available seats.
     * Time: O(F) where F = flights in route
     */
    private boolean routeHasCapacity(DynamicList<Flight> route, int requiredSeats) {
        for (int i = 0; i < route.size(); i++) {
            Flight flight = route.get(i);
            if (flight.getAvailableSeats() < requiredSeats) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Calculate the total cost for booking a route with a specific number of seats.
     * Time: O(F) where F = flights in route
     */
    private double calculateRouteCost(DynamicList<Flight> route, int seats) {
        double total = 0.0;
        for (int i = 0; i < route.size(); i++) {
            total += route.get(i).getCurrentPrice() * seats;
        }
        return total;
    }
}
