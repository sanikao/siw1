import java.util.Objects;
import java.util.UUID;

// Aggregation: a Venue exists on its own, events only reference it (1 venue : N events).
public class Venue {
    private final String id;
    private String name;
    private int capacity;
    private double rentalPrice;

    public Venue(String name, int capacity, double rentalPrice) {
        this.id = UUID.randomUUID().toString();
        setName(name);
        setCapacity(capacity);
        setRentalPrice(rentalPrice);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public double getRentalPrice() { return rentalPrice; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Venue name cannot be empty.");
        this.name = name;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity must be positive.");
        this.capacity = capacity;
    }

    public void setRentalPrice(double rentalPrice) {
        if (rentalPrice < 0)
            throw new IllegalArgumentException("Rental price cannot be negative.");
        this.rentalPrice = rentalPrice;
    }

    public boolean canHost(int guests) {
        return guests <= capacity;
    }

    @Override
    public String toString() {
        return "Venue: " + name + " | Capacity: " + capacity + " | Rental: $" + rentalPrice;
    }

    // Business key: id
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        return id.equals(((Venue) object).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
