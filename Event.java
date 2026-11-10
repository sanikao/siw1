import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

// Abstract parent. Natural ordering: by date, then by ID.
// Business key for equals/hashCode: id.
public abstract class Event implements Comparable<Event> {
    private final String id;
    private String name;
    private LocalDate date;
    private int guestCount;
    private double budget;
    private Venue venue; // aggregation, may be null

    protected Event(String name, LocalDate date, int guestCount, double budget) {
        this(UUID.randomUUID().toString(), name, date, guestCount, budget);
    }

    // Explicit ID is useful when importing existing data.
    protected Event(String id, String name, LocalDate date, int guestCount, double budget) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Event ID cannot be empty.");
        this.id = id;
        setName(name);
        setDate(date);
        setGuestCount(guestCount);
        setBudget(budget);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public LocalDate getDate() { return date; }
    public int getGuestCount() { return guestCount; }
    public double getBudget() { return budget; }
    public Venue getVenue() { return venue; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Event name cannot be empty.");
        this.name = name;
    }

    public void setDate(LocalDate date) {
        if (date == null)
            throw new IllegalArgumentException("Event date cannot be null.");
        this.date = date;
    }

    public void setGuestCount(int guestCount) {
        if (guestCount <= 0)
            throw new IllegalArgumentException("Guest count must be positive.");
        if (venue != null && !venue.canHost(guestCount))
            throw new IllegalArgumentException("Venue " + venue.getName() + " cannot host "
                    + guestCount + " guests.");
        this.guestCount = guestCount;
    }

    public void setBudget(double budget) {
        if (budget < 0)
            throw new IllegalArgumentException("Budget cannot be negative.");
        this.budget = budget;
    }

    public void assignVenue(Venue venue) {
        if (venue == null)
            throw new IllegalArgumentException("Venue cannot be null.");
        if (!venue.canHost(guestCount))
            throw new IllegalArgumentException("Venue " + venue.getName()
                    + " is too small for " + guestCount + " guests.");
        this.venue = venue;
    }

    public void displayInfo() {
        System.out.println("ID: " + id + " | Event: " + name + " | Date: " + date
                + " | Guests: " + guestCount + " | Budget: $" + budget
                + " | Venue: " + (venue == null ? "not assigned" : venue.getName()));
    }

    @Override
    public int compareTo(Event other) {
        int dateResult = date.compareTo(other.date);
        if (dateResult != 0) return dateResult;
        return id.compareTo(other.id);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        return id.equals(((Event) object).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
