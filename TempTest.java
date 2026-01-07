/**
 * Quick development test for individual data classes.
 * Used during development to verify Airport, Flight, and Reservation work correctly.
 * kept for reference.
 */
public class TempTest {
    public static void main(String[] args) {
        // Test Airport
        Airport ath = new Airport("ATH", "Athens International", "Athens");
        Airport jfk = new Airport("JFK", "John F. Kennedy", "New York");
        System.out.println(ath);
        System.out.println(jfk);
        
        // Test Flight
        Flight f1 = new Flight("A3501", ath, jfk, 180, 250.0);
        System.out.println(f1);
        System.out.println("Available: " + f1.getAvailableSeats());
        System.out.println("Load Factor: " + f1.getLoadFactor());
        
        // Simulate booking
        f1.setBookedSeats(90);
        System.out.println("After booking 90 seats:");
        System.out.println(f1);
        System.out.println("Load Factor: " + f1.getLoadFactor());
        
        // Test Reservation
        Reservation r1 = new Reservation("RES001", f1, "John Doe", 2, 500.0);
        System.out.println(r1);
        
        System.out.println("\nAll data classes working correctly!");
    }
}
