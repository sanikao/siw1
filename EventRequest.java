public class EventRequest {
    private final Event event;
    private final String submittedBy;
    private final int priority;
    private final long sequence;

    public EventRequest(Event event, String submittedBy, int priority, long sequence) {
        if (event == null) throw new IllegalArgumentException("Event cannot be null.");
        if (submittedBy == null || submittedBy.trim().isEmpty())
            throw new IllegalArgumentException("Organizer name cannot be empty.");
        this.event = event;
        this.submittedBy = submittedBy;
        this.priority = priority;
        this.sequence = sequence;
    }

    public Event getEvent() { return event; }
    public String getSubmittedBy() { return submittedBy; }
    public int getPriority() { return priority; }
    public long getSequence() { return sequence; }
}
