import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Performance testing for the Most Significant Operation: addReservation
 * Tests with 3 different network sizes using REALISTIC CSV data.
 * 
 * CSV Format: airline_iata,airline_name,source_iata,source_city,destination_iata,
 *             destination_city,route,stops,aircraft_type,aircraft_capacity,price_usd
 * 
 * Theoretical Complexity: O(V + E) where V = airports, E = flights
 * 
 * Source: Custom implementation for M2 performance analysis requirement
 */
public class PerformanceTester {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   PERFORMANCE TESTING - addReservation Operation          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        System.out.println("Most Significant Operation: addReservation()");
        System.out.println("Theoretical Complexity: O(V + E)");
        System.out.println("  - V = number of airports (vertices)");
        System.out.println("  - E = number of flights (edges)");
        System.out.println("  - Dominated by BFS route search\n");
        
        System.out.println("Testing with 3 REALISTIC dataset sizes:\n");
        
        // Test 1: Small network (100 flights)
        testPerformance("SMALL", "SampleData100.csv");
        
        // Test 2: Medium network (1000 flights)
        testPerformance("MEDIUM", "SampleData1000.csv");
        
        // Test 3: Large network (10000 flights)
        testPerformance("LARGE", "SampleData10000.csv");
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   ANALYSIS                                                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        System.out.println("Expected behavior for O(V + E) complexity:");
        System.out.println("- When V and E increase linearly, time should increase linearly");
        System.out.println("- Small → Medium: ~10x increase in flights → ~10x time increase");
        System.out.println("- Medium → Large: ~10x increase in flights → ~10x time increase");
        System.out.println("\nActual results should show similar proportional increases,");
        System.out.println("confirming the theoretical O(V + E) complexity.");
    }
    
    /**
     * Test addReservation performance with a CSV file.
     * 
     * @param label Dataset size label (SMALL, MEDIUM, LARGE)
     * @param filename CSV file to load
     */
    private static void testPerformance(String label, String filename) {
        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println("  Test: " + label + " Network");
        System.out.println("══════════════════════════════════════════════════════════════");
        System.out.println("Loading data from: " + filename);
        
        // Build network from CSV
        long buildStartTime = System.nanoTime();
        RouteNetwork network = buildNetworkFromCSV(filename);
        
        if (network == null) {
            System.out.println(" Failed to load network from file\n");
            return;
        }
        
        ReservationsList reservations = new ReservationsList(network);
        long buildEndTime = System.nanoTime();
        
        // Count airports and flights
        DynamicList<Flight> allFlights = network.getAllFlights();
        int numFlights = allFlights.size();
        int numAirports = countUniqueAirports(allFlights);
        
        double buildTimeMs = (buildEndTime - buildStartTime) / 1_000_000.0;
        
        System.out.println("\nParameters:");
        System.out.println("  Airports (V): " + numAirports);
        System.out.println("  Flights (E):  " + numFlights);
        System.out.println("  V + E:        " + (numAirports + numFlights));
        System.out.println("  Build time:   " + String.format("%.3f", buildTimeMs) + " ms");
        System.out.println();
        
        // Warm-up run (JVM optimization)
        try {
            // Get first two different airports
            Flight f1 = allFlights.get(0);
            Flight f2 = allFlights.get(Math.min(1, allFlights.size() - 1));
            
            String origin = f1.getOrigin().getCode();
            String dest = f2.getDestination().getCode();
            
            if (!origin.equals(dest)) {
                reservations.addReservation(origin, dest, "Warm-up Passenger", 1);
            }
        } catch (Exception e) {
            // Ignore - route may not exist or no capacity
        }
        
        // Performance test: Multiple addReservation calls
        int numTests = 20; // Run 20 bookings
        long totalTime = 0;
        int successfulBookings = 0;
        
        System.out.println("Running " + numTests + " booking operations...");
        
        for (int i = 0; i < numTests; i++) {
            // Pick random flights for origin and destination
            int idx1 = (int)(Math.random() * allFlights.size());
            int idx2 = (int)(Math.random() * allFlights.size());
            
            while (idx2 == idx1) {
                idx2 = (int)(Math.random() * allFlights.size());
            }
            
            String origin = allFlights.get(idx1).getOrigin().getCode();
            String dest = allFlights.get(idx2).getDestination().getCode();
            
            // Ensure different airports
            if (origin.equals(dest)) {
                continue;
            }
            
            // Measure time for this booking
            long startTime = System.nanoTime();
            
            try {
                reservations.addReservation(origin, dest, "Passenger_" + i, 1);
                successfulBookings++;
            } catch (Exception e) {
                // Route doesn't exist or no capacity - skip
            }
            
            long endTime = System.nanoTime();
            totalTime += (endTime - startTime);
        }
        
        // Calculate statistics
        double avgTimeMs = (totalTime / (double)numTests) / 1_000_000.0;
        double totalTimeMs = totalTime / 1_000_000.0;
        
        System.out.println();
        System.out.println("Results:");
        System.out.println("  Successful bookings: " + successfulBookings + "/" + numTests);
        System.out.println("  Total time:          " + String.format("%.3f", totalTimeMs) + " ms");
        System.out.println("  Average time/op:     " + String.format("%.6f", avgTimeMs) + " ms");
        System.out.println("  Operations/second:   " + String.format("%.0f", 1000.0 / avgTimeMs));
        System.out.println();
    }
    
    /**
     * Build a network from CSV file.
     * CSV Format: airline_iata,airline_name,source_iata,source_city,destination_iata,
     *             destination_city,route,stops,aircraft_type,aircraft_capacity,price_usd
     * 
     * @param filename CSV file path
     * @return Populated RouteNetwork or null if file not found
     */
    private static RouteNetwork buildNetworkFromCSV(String filename) {
        RouteNetwork network = new RouteNetwork();
        SimpleHashMap<String, Boolean> addedAirports = new SimpleHashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isHeader = true;
            int flightCounter = 0;
            
            while ((line = br.readLine()) != null) {
                // Skip header
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                // Parse CSV line
                String[] parts = line.split(",");
                
                if (parts.length < 11) {
                    continue; // Skip malformed lines
                }
                
                String sourceIATA = parts[2].trim();
                String sourceCity = parts[3].trim();
                String destIATA = parts[4].trim();
                String destCity = parts[5].trim();
                String capacityStr = parts[9].trim();
                String priceStr = parts[10].trim();
                
                // Add source airport (if not already added)
                if (!addedAirports.containsKey(sourceIATA)) {
                    try {
                        network.addAirport(sourceIATA, sourceCity + " Airport", sourceCity);
                        addedAirports.put(sourceIATA, true);
                    } catch (Exception e) {
                        // Airport already exists - ignore
                    }
                }
                
                // Add destination airport (if not already added)
                if (!addedAirports.containsKey(destIATA)) {
                    try {
                        network.addAirport(destIATA, destCity + " Airport", destCity);
                        addedAirports.put(destIATA, true);
                    } catch (Exception e) {
                        // Airport already exists - ignore
                    }
                }
                
                // Parse capacity and price
                int capacity;
                double price;
                
                try {
                    capacity = Integer.parseInt(capacityStr);
                    price = Double.parseDouble(priceStr);
                } catch (NumberFormatException e) {
                    continue; // Skip invalid data
                }
                
                // Generate unique flight code
                String flightCode = "FL" + String.format("%05d", flightCounter++);
                
                // Add flight
                try {
                    network.addFlight(flightCode, sourceIATA, destIATA, capacity, price);
                } catch (Exception e) {
                    // Flight might be duplicate or invalid - skip
                }
            }
            
            return network;
            
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Count unique airports from flight list.
     * 
     * @param flights List of flights
     * @return Number of unique airports
     */
    private static int countUniqueAirports(DynamicList<Flight> flights) {
        SimpleHashMap<String, Boolean> uniqueAirports = new SimpleHashMap<>();
        
        for (int i = 0; i < flights.size(); i++) {
            Flight f = flights.get(i);
            uniqueAirports.put(f.getOrigin().getCode(), true);
            uniqueAirports.put(f.getDestination().getCode(), true);
        }
        
        return uniqueAirports.size();
    }
}

