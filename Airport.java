/**
 * A simple airport object that stores basic info: IATA code, name, and city.
 * 
 * Source: Custom implementation based on M2 design requirements
 */
public class Airport {
    private String code;        // IATA code like "ATH" or "LAX"
    private String name;        // Full airport name
    private String city;        // City where it's located
    
    /**
     * Create a new airport with code, name, and city.
     * @param code 3-letter IATA code
     * @param name Airport's full name
     * @param city City location
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
     * Two airports are considered the same if they have the same IATA code.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Airport airport = (Airport) obj;
        return code.equals(airport.code);
    }
    
    /**
     * Hash code based on the IATA code so it works in HashMaps.
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }
    
    /**
     * Returns a readable string representation (e.g., "ATH - Athens International (Athens)").
     */
    @Override
    public String toString() {
        return code + " - " + name + " (" + city + ")";
    }
}
