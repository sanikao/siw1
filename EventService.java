import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EventService {
    // Set: main registry. Uniqueness is controlled by Event.equals()/hashCode() (business key = id).
    // LinkedHashSet keeps insertion order.
    private final Set<Event> events = new LinkedHashSet<>();

    // Map: primary key index, O(1) lookup by ID.
    private final Map<String, Event> eventsById = new HashMap<>();

    // Map: categorization index, events grouped by concrete type.
    private final Map<String, List<Event>> eventsByType = new HashMap<>();

    // Queue: higher priority first, equal priority -> FIFO (by sequence number).
    private final Queue<EventRequest> requestQueue = new PriorityQueue<>(
            Comparator.comparingInt(EventRequest::getPriority).reversed()
                    .thenComparingLong(EventRequest::getSequence));

    private long requestSequence = 0;

    // Three custom comparators.
    public static final Comparator<Event> BY_NAME =
            Comparator.comparing(Event::getName, String.CASE_INSENSITIVE_ORDER);
    public static final Comparator<Event> BY_BUDGET =
            Comparator.comparingDouble(Event::getBudget);
    public static final Comparator<Event> BY_GUEST_COUNT =
            Comparator.comparingInt(Event::getGuestCount).reversed();

    // ---------- CRUD: every collection and index is changed together ----------

    public synchronized void addEvent(Event event) {
        if (event == null) throw new IllegalArgumentException("Event cannot be null.");
        if (!events.add(event))
            throw new IllegalArgumentException("An event with this ID already exists.");

        eventsById.put(event.getId(), event);
        eventsByType.computeIfAbsent(typeOf(event), key -> new ArrayList<>()).add(event);
    }

    public synchronized Optional<Event> findById(String id) {
        return Optional.ofNullable(eventsById.get(id));
    }

    // ID and type never change, so only the event itself needs updating.
    public synchronized boolean updateEvent(String id, Consumer<Event> changes) {
        if (changes == null) throw new IllegalArgumentException("Changes cannot be null.");
        Event event = eventsById.get(id);
        if (event == null) return false;
        changes.accept(event);
        return true;
    }

    public synchronized boolean removeEvent(String id) {
        Event event = eventsById.remove(id);
        if (event == null) return false;

        events.remove(event);
        List<Event> sameType = eventsByType.get(typeOf(event));
        sameType.remove(event);
        if (sameType.isEmpty()) eventsByType.remove(typeOf(event));
        return true;
    }

    private String typeOf(Event event) {
        return event.getClass().getSimpleName();
    }

    // ---------- Search, filter, sort ----------

    public synchronized List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }

    public synchronized List<Event> getEventsByType(String type) {
        return new ArrayList<>(eventsByType.getOrDefault(type, Collections.emptyList()));
    }

    public synchronized List<Event> getEventsByVenue(Venue venue) {
        return events.stream()
                .filter(event -> venue.equals(event.getVenue()))
                .collect(Collectors.toList());
    }

    public synchronized List<Event> searchByMinimumBudget(double minimumBudget) {
        return events.stream()
                .filter(event -> event.getBudget() >= minimumBudget)
                .collect(Collectors.toList());
    }

    public synchronized List<Event> getSortedEvents(Comparator<Event> comparator) {
        List<Event> result = new ArrayList<>(events);
        result.sort(comparator);
        return result;
    }

    // Natural order: Event.compareTo() -> date, then ID.
    public synchronized List<Event> getNaturallySortedEvents() {
        List<Event> result = new ArrayList<>(events);
        Collections.sort(result);
        return result;
    }

    // ---------- Queue ----------

    public synchronized void submitRequest(Event event, String organizer, int priority) {
        requestQueue.offer(new EventRequest(event, organizer, priority, requestSequence++));
    }

    // Returns null if the queue is empty (poll() behaviour).
    public synchronized EventRequest getNextRequest() {
        return requestQueue.poll();
    }

    public synchronized int getQueueSize() {
        return requestQueue.size();
    }

    // ---------- Analytics ----------

    public synchronized String buildReport() {
        double totalBudget = 0;
        int totalGuests = 0;
        for (Event event : events) {
            totalBudget += event.getBudget();
            totalGuests += event.getGuestCount();
        }

        StringBuilder report = new StringBuilder();
        report.append("Total events: ").append(events.size()).append('\n');
        report.append("Total planned budget: $").append(totalBudget).append('\n');
        report.append("Total guests: ").append(totalGuests).append('\n');
        report.append("Average guests per event: ")
                .append(events.isEmpty() ? 0 : (double) totalGuests / events.size()).append('\n');
        report.append("Events by type:\n");
        for (Map.Entry<String, List<Event>> entry : eventsByType.entrySet()) {
            report.append("  ").append(entry.getKey()).append(": ")
                    .append(entry.getValue().size()).append('\n');
        }
        return report.toString();
    }
}
