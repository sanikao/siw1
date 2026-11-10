// Role: submits event requests into the queue.
public class EventOrganizer {
    private final String name;
    private final EventService service;

    public EventOrganizer(String name, EventService service) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Organizer name cannot be empty.");
        this.name = name;
        this.service = service;
    }

    public void submitEvent(Event event, int priority) {
        service.submitRequest(event, name, priority);
        System.out.println(name + " submitted \"" + event.getName() + "\" with priority " + priority);
    }
}
