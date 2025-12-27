/**
 * Implementation of ReservationsList ADT.
 * Manages reservations with automatic dynamic pricing based on load factor.
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
        // Validate inputs
        if (passengerName == null || passengerName.isEmpty()) {
            throw new IllegalArgumentException("Passenger name cannot be null or empty");
        }
        if (seats <= 0) {
            throw new IllegalArgumentException("Number of seats must be positive");
        }
        if (!routeNetwork.hasAirport(originCode) || !routeNetwork.hasAirport(destCode)) {
            throw new IllegalArgumentException("One or both airports do not exist");
        }
        
        // Find cheapest route with available seats (up to 1 stop)
        DynamicList<Flight> bestRoute = findCheapestAvailableRoute(originCode, destCode, seats);
        
        if (bestRoute == null || bestRoute.isEmpty()) {
            throw new IllegalArgumentException("No available route with " + seats + " seats from " 
                                             + originCode + " to " + destCode);
        }
        
        // Calculate total cost at current prices
        double totalCost = 0.0;
        for (int i = 0; i < bestRoute.size(); i++) {
            Flight flight = bestRoute.get(i);
            totalCost += flight.getCurrentPrice() * seats;
        }
        
        // Update booked seats for each flight in route
        for (int i = 0; i < bestRoute.size(); i++) {
            Flight flight = bestRoute.get(i);
            flight.setBookedSeats(flight.getBookedSeats() + seats);
        }
        
        // Trigger dynamic pricing update for affected flights
        for (int i = 0; i < bestRoute.size(); i++) {
            updateDynamicPrice(bestRoute.get(i));
        }
        
        // Create reservation (simplified: store only first flight for single/multi-leg)
        String reservationId = "RES" + String.format("%04d", nextReservationId++);
        
        // For simplicity: create one reservation per flight segment
        // In real system, might have one reservation object with list of flights
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
        // Find reservation
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
            return false; // Reservation not found
        }
        
        // Free up seats on the flight
        Flight flight = toCancel.getFlight();
        int seatsToFree = toCancel.getNumberOfSeats();
        flight.setBookedSeats(flight.getBookedSeats() - seatsToFree);
        
        // Update price (should decrease as occupancy drops)
        updateDynamicPrice(flight);
        
        // Remove reservation
        reservations.remove(index);
        
        return true;
    }
    
    @Override
    public DynamicList<Flight> findCheapestAvailableRoute(String originCode, String destCode, 
                                                           int requiredSeats) {
        // Get all routes with up to 1 stop
        DynamicList<DynamicList<Flight>> allRoutes = 
            routeNetwork.findRoutesWithStops(originCode, destCode, 1);
        
        if (allRoutes.isEmpty()) {
            return null;
        }
        
        // Filter routes that have sufficient capacity
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
        
        // Find cheapest valid route
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
     * Update flight price based on load factor (DYNAMIC PRICING LOGIC).
     * This is a private method - not exposed in the ADT interface.
     * 
     * Pricing Formula:
     * - Load >= 80%: Price = Base × 1.5
     * - Load >= 50%: Price = Base × 1.2
     * - Load < 50%:  Price = Base × 1.0
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
     * Check if a route has sufficient capacity on all flights.
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
     * Calculate total cost of a route for given number of seats.
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
