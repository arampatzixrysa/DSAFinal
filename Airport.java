/**
 * Represents an airport in the flight network.
 * Simple data class with IATA code, name, and city.
 * 
 * Source: Custom implementation based on M2 design requirements
 */
public class Airport {
    private String code;        // IATA code (e.g., "ATH", "JFK")
    private String name;        // Full airport name
    private String city;        // City location
    
    /**
     * Constructor
     * @param code IATA airport code (3 letters)
     * @param name Full name of the airport
     * @param city City where airport is located
     */
    public Airport(String code, String name, String city) {
        this.code = code;
        this.name = name;
        this.city = city;
    }
    
    // Getters
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCity() {
        return city;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    /**
     * Two airports are equal if they have the same IATA code.
     * This allows airports to be used in HashMaps and Sets correctly.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Airport airport = (Airport) obj;
        return code.equals(airport.code);
    }
    
    /**
     * Hash code based on airport code for HashMap usage.
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }
    
    /**
     * String representation for debugging/testing.
     */
    @Override
    public String toString() {
        return code + " - " + name + " (" + city + ")";
    }
}
